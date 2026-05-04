/**
 * WhCraft - A Minecraft Mod about Warhammer 40,000.
 * Copyright (C) 2026 ShuShu, 42
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.whcraft.whcraft.Entities.ServoSkull;

import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.block.BlockState;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.control.FlightMoveControl;
import net.minecraft.entity.ai.pathing.BirdNavigation;
import net.minecraft.entity.ai.pathing.EntityNavigation;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.math.*;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;
import net.minecraft.world.explosion.ExplosionBehavior;
import org.jetbrains.annotations.Nullable;
import org.whcraft.whcraft.Items.ServoSkull.BrokenServoSkullItem;
import org.whcraft.whcraft.Items.ServoSkull.ServoSkullItem;
import org.whcraft.whcraft.Items.ServoSkull.ServoSkullRetrofit;
import org.whcraft.whcraft.WhcraftGameRules;
import org.whcraft.whcraft.WhcraftItems;

import java.util.*;

public class ServoSkullEntity extends MobEntity {

    private boolean detonated = false;

    private static final TrackedData<Optional<UUID>> OWNER_UUID =
            DataTracker.registerData(ServoSkullEntity.class, TrackedDataHandlerRegistry.OPTIONAL_UUID);
    private static final TrackedData<String> RETROFITS_STRING =
            DataTracker.registerData(ServoSkullEntity.class, TrackedDataHandlerRegistry.STRING);

    private final ServoSkullInventory inventory = new ServoSkullInventory(this, 54);
    private PlayerEntity cachedOwner;

    private int stuckTicks = 0;
    private double lastDistanceSq = Double.MAX_VALUE;

    private int weaponCooldown = 0;
    private int healthcareCooldown = 0;
    private final Deque<LivingEntity> shootTargets = new ArrayDeque<>();

    public ServoSkullEntity(EntityType<? extends ServoSkullEntity> entityType, World world) {
        super(entityType, world);
        this.moveControl = new FlightMoveControl(this, 10, true);
        this.setNoGravity(true);
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(OWNER_UUID, Optional.empty());
        this.dataTracker.startTracking(RETROFITS_STRING, "");
    }

    @Override
    public EntityDimensions getDimensions(EntityPose pose) {
        return EntityDimensions.fixed(0.8f, 0.8f);
    }

    public static DefaultAttributeContainer.Builder createServoSkullAttributes() {
        return MobEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 40.0)
                .add(EntityAttributes.GENERIC_ARMOR, 10.0)
                .add(EntityAttributes.GENERIC_FLYING_SPEED, 0.3)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.3)
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 32.0);
    }

    @Override
    protected EntityNavigation createNavigation(World world) {
        BirdNavigation navigation = new BirdNavigation(this, world);
        navigation.setCanPathThroughDoors(false);
        navigation.setCanSwim(false);
        navigation.setCanEnterOpenDoors(true);
        return navigation;
    }

    public Inventory getInventory() {
        return this.inventory;
    }

    public void setOwner(PlayerEntity player) {
        this.dataTracker.set(OWNER_UUID, Optional.of(player.getUuid()));
        this.cachedOwner = player;
    }

    @Nullable
    public UUID getOwnerUuid() {
        return this.dataTracker.get(OWNER_UUID).orElse(null);
    }

    @Nullable
    public PlayerEntity getOwner() {
        UUID uuid = getOwnerUuid();
        if (uuid == null) return null;
        if (cachedOwner != null && cachedOwner.getUuid().equals(uuid) && !cachedOwner.isRemoved()) {
            return cachedOwner;
        }
        PlayerEntity owner = this.getWorld().getPlayerByUuid(uuid);
        cachedOwner = owner;
        return owner;
    }

    private boolean isPlayerOnline(UUID uuid) {
        return this.getWorld().getServer() != null &&
                this.getWorld().getServer().getPlayerManager().getPlayer(uuid) != null;
    }

    private void teleportToOwner(PlayerEntity owner) {
        if (owner.getWorld().isClient) return;
        Vec3d target = owner.getPos().add(0, owner.getStandingEyeHeight() + 1.0, 0);
        ServerWorld targetWorld = (ServerWorld) owner.getWorld();
        boolean success = this.teleport(targetWorld, target.x, target.y, target.z, Set.of(), this.getYaw(), this.getPitch());
        if (success) {
            this.getNavigation().stop();
            this.stuckTicks = 0;
        } else {
            this.recallToOwnerInventory(owner);
        }
    }

    public void detonate() {
        if (this.getWorld().isClient) return;

        ExplosionBehavior noBlockDamage = new ExplosionBehavior() {
            @Override
            public boolean canDestroyBlock(Explosion explosion, BlockView world, BlockPos pos, BlockState state, float power) {
                return false;
            }
        };

        float power = (float) this.getWorld().getGameRules().getInt(WhcraftGameRules.SERVO_SKULL_SELF_DESTRUCT_POWER);

        this.getWorld().createExplosion(this, null, noBlockDamage,
                this.getX(), this.getY(), this.getZ(),
                power, false, World.ExplosionSourceType.MOB);

        this.detonated = true;

        for (int i = 0; i < inventory.size(); i++) {
            inventory.setStack(i, ItemStack.EMPTY);
        }
        inventory.markDirty();

        this.discard();
    }

    @Override
    public void onDeath(DamageSource damageSource) {
        if (!this.getWorld().isClient) {
            if (detonated) {
                super.onDeath(damageSource);
                return;
            }
            for (int i = 0; i < inventory.size(); i++) {
                ItemStack stack = inventory.getStack(i);
                if (!stack.isEmpty()) this.dropStack(stack);
            }
            inventory.clear();
            PlayerEntity owner = getOwner();
            ItemStack brokenStack = new ItemStack(WhcraftItems.BROKEN_SERVO_SKULL);
            if (this.hasCustomName()) {
                BrokenServoSkullItem.writeName(brokenStack, this.getCustomName());
            }
            BrokenServoSkullItem.writeRetrofits(brokenStack, this.getRetrofits());
            if (owner != null && owner.isAlive()) {
                if (!owner.getInventory().insertStack(brokenStack)) {
                    owner.dropItem(brokenStack, false);
                }
            } else {
                this.dropStack(brokenStack);
            }
        }
        super.onDeath(damageSource);
    }

    @Override
    public void tick() {
        super.tick();
        if (this.getWorld().isClient) return;

        PlayerEntity owner = getOwner();
        if (owner == null) {
            UUID uuid = getOwnerUuid();
            if (uuid != null && !isPlayerOnline(uuid)) {
                this.getNavigation().stop();
                this.stuckTicks = 0;
                return;
            } else {
                recallToOwnerInventory();
                return;
            }
        }
        if (owner.getWorld() != this.getWorld()) {
            teleportToOwner(owner);
            return;
        }
        if (!owner.isAlive()) {
            this.discard();
            return;
        }
        double distanceSq = this.squaredDistanceTo(owner);
        if (distanceSq > 225.0) {
            teleportToOwner(owner);
            return;
        }
        if (distanceSq > 9.0) {
            Vec3d targetPos = owner.getPos().add(0, owner.getStandingEyeHeight() + 1.0, 0);
            this.getNavigation().startMovingTo(targetPos.x, targetPos.y, targetPos.z, 1.0);
            if (this.getNavigation().isIdle() || distanceSq >= lastDistanceSq) {
                stuckTicks++;
            } else {
                stuckTicks = 0;
            }
            lastDistanceSq = distanceSq;
            if (stuckTicks > 100) {
                teleportToOwner(owner);
                stuckTicks = 0;
            }
        } else {
            this.getNavigation().stop();
            stuckTicks = 0;
        }
        this.setNoGravity(true);

        if (getRetrofits().contains("weaponry")) {
            if (weaponCooldown > 0) {
                weaponCooldown--;
            } else {
                shootTargets.removeIf(target -> !target.isAlive() || target.isRemoved());
                LivingEntity target = shootTargets.stream()
                        .filter(t -> t.isAlive() && !t.isRemoved() && t.squaredDistanceTo(this) < 144.0)
                        .findFirst().orElse(null);
                if (target != null) {
                    this.getMoveControl().moveTo(this.getX(), target.getEyeY(), this.getZ(), 1.0);
                    this.getLookControl().lookAt(target, 30, 30);
                    shoot(target);
                } else {
                    weaponCooldown--;
                }
            }
        }

        if (getRetrofits().contains("healthcare")) {
            owner = getOwner();
            if (owner != null && owner.isAlive() && owner.getWorld() == this.getWorld()) {
                if (healthcareCooldown <= 0) {
                    float maxHealth = owner.getMaxHealth();
                    if (owner.getHealth() < maxHealth * 0.3f) {
                        float healAmount = maxHealth * 0.1f + 5.0f;
                        owner.heal(healAmount);
                    } else {
                        owner.addStatusEffect(new StatusEffectInstance(
                                StatusEffects.REGENERATION,
                                20 * 20,
                                1,
                                false, false, true
                        ));
                    }
                    healthcareCooldown = this.getWorld().getGameRules().getInt(WhcraftGameRules.SERVO_SKULL_HEALTHCARE_COOLDOWN);
                } else {
                    healthcareCooldown--;
                }
            } else {
                if (healthcareCooldown > 0) {
                    healthcareCooldown--;
                }
            }
        }
    }

    public void addShootTarget(LivingEntity attacker) {
        if (attacker == null || attacker == this || attacker == getOwner()) return;
        shootTargets.remove(attacker);
        shootTargets.addFirst(attacker);
        while (shootTargets.size() > 10) {
            shootTargets.removeLast();
        }
    }

    private void shoot(LivingEntity target) {
        weaponCooldown = this.getWorld().getGameRules().getInt(WhcraftGameRules.SERVO_SKULL_ATTACK_COOLDOWN);
        if (this.getWorld().isClient) return;

        ServoSkullBullet bullet = new ServoSkullBullet(this.getWorld(), this);
        Vec3d muzzleLocal = new Vec3d(0.0, 0.5, 1.0);
        double yawRad = -this.getYaw() * MathHelper.RADIANS_PER_DEGREE;
        Vec3d muzzleWorld = this.getPos().add(muzzleLocal.rotateY((float) yawRad));
        bullet.setPosition(muzzleWorld);

        Vec3d dir = target.getEyePos().subtract(muzzleWorld).normalize();
        bullet.setVelocity(
                dir.x * 1.5 + (this.random.nextGaussian() * 0.0075 * 0.5),
                dir.y * 1.5 + (this.random.nextGaussian() * 0.0075 * 0.5),
                dir.z * 1.5 + (this.random.nextGaussian() * 0.0075 * 0.5)
        );

        this.getWorld().spawnEntity(bullet);
    }

    public void recallToOwnerInventory(PlayerEntity owner) {
        ItemStack skullStack = new ItemStack(WhcraftItems.SERVO_SKULL);
        ServoSkullItem item = (ServoSkullItem) WhcraftItems.SERVO_SKULL;
        item.createFromEntity(skullStack, this);
        if (!owner.getInventory().insertStack(skullStack)) {
            owner.dropItem(skullStack, false);
        }
        this.discard();
    }

    public void recallToOwnerInventory() {
        PlayerEntity owner = getOwner();
        if (owner != null && owner.isAlive()) {
            recallToOwnerInventory(owner);
        } else {
            ItemStack skullStack = new ItemStack(WhcraftItems.SERVO_SKULL);
            ServoSkullItem item = (ServoSkullItem) WhcraftItems.SERVO_SKULL;
            item.createFromEntity(skullStack, this);
            this.dropStack(skullStack);
            this.discard();
        }
    }

    @Override
    protected ActionResult interactMob(PlayerEntity player, Hand hand) {
        if (player.getWorld().isClient) return ActionResult.SUCCESS;
        if (!player.getUuid().equals(getOwnerUuid())) return ActionResult.FAIL;
        ItemStack heldStack = player.getStackInHand(hand);
        if (heldStack.getItem() instanceof ServoSkullRetrofit) {
            String retrofitId = ServoSkullRetrofit.getServoSkullRetrofit(heldStack);
            if ("null".equals(retrofitId)) {
                player.sendMessage(
                        Text.translatable("message.whcraft.servo_skull.retrofit_null")
                                .formatted(Formatting.RED),
                        true
                );
                return ActionResult.FAIL;
            }
            boolean added = this.addRetrofit(retrofitId);
            if (added) {
                if (!player.getAbilities().creativeMode) {
                    heldStack.decrement(1);
                }
                Text retrofitName = Text.translatable("servoskull.retrofit." + retrofitId);
                player.sendMessage(
                        Text.translatable("message.whcraft.servo_skull.retrofit_success", retrofitName)
                                .formatted(Formatting.GREEN),
                        true
                );
                return ActionResult.SUCCESS;
            } else {
                Text retrofitName = Text.translatable("servoskull.retrofit." + retrofitId);
                player.sendMessage(
                        Text.translatable("message.whcraft.servo_skull.retrofit_already_present", retrofitName)
                                .formatted(Formatting.YELLOW),
                        true
                );
                return ActionResult.FAIL;
            }
        }
        if (player.isSneaking()) {
            recallToOwnerInventory(player);
            return ActionResult.SUCCESS;
        } else {
            openInventory(player);
            return ActionResult.SUCCESS;
        }
    }

    private void openInventory(PlayerEntity player) {
        player.openHandledScreen(new ExtendedScreenHandlerFactory() {
            @Override
            public void writeScreenOpeningData(ServerPlayerEntity player, PacketByteBuf buf) {
                buf.writeInt(ServoSkullEntity.this.getId());
            }

            @Override
            public Text getDisplayName() {
                return ServoSkullEntity.this.hasCustomName() ?
                        ServoSkullEntity.this.getCustomName() :
                        Text.translatable("entity.whcraft.servo_skull");
            }

            @Nullable
            @Override
            public ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
                return new ServoSkullScreenHandler(syncId, inv, ServoSkullEntity.this);
            }
        });
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        UUID ownerUuid = getOwnerUuid();
        if (ownerUuid != null) nbt.putUuid("Owner", ownerUuid);
        inventory.writeNbt(nbt);
        nbt.putString("Retrofits", this.dataTracker.get(RETROFITS_STRING));
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        if (nbt.containsUuid("Owner")) {
            UUID uuid = nbt.getUuid("Owner");
            this.dataTracker.set(OWNER_UUID, Optional.of(uuid));
            this.cachedOwner = null;
        }
        if (nbt.contains("Inventory", NbtElement.LIST_TYPE)) {
            inventory.readNbt(nbt);
        }
        if (nbt.contains("Retrofits", NbtElement.STRING_TYPE)) {
            this.dataTracker.set(RETROFITS_STRING, nbt.getString("Retrofits"));
        }
    }

    @Override public boolean cannotDespawn() { return true; }
    @Override public boolean canBeLeashedBy(PlayerEntity player) { return false; }
    @Override protected boolean isAffectedByDaylight() { return false; }
    @Override public boolean isPushable() { return true; }

    public void onOwnerReconnect(ServerPlayerEntity player) {
        this.setOwner(player);
        this.teleportToOwner(player);
        this.stuckTicks = 0;
        this.lastDistanceSq = Double.MAX_VALUE;
    }

    public Set<String> getRetrofits() {
        String raw = this.dataTracker.get(RETROFITS_STRING);
        if (raw.isEmpty()) return Collections.emptySet();
        Set<String> result = new HashSet<>();
        for (String part : raw.split(",")) {
            String candidate = part.trim();
            if (!candidate.isEmpty() && ServoSkullRetrofit.SERVO_SKULL_RETROFIT_IDS.contains(candidate)) {
                result.add(candidate);
            }
        }
        return Collections.unmodifiableSet(result);
    }

    public void setRetrofits(Set<String> retrofits) {
        Set<String> valid = new HashSet<>();
        for (String r : retrofits) {
            if (ServoSkullRetrofit.SERVO_SKULL_RETROFIT_IDS.contains(r)) {
                valid.add(r);
            }
        }
        String data = valid.isEmpty() ? "" : String.join(",", valid);
        this.dataTracker.set(RETROFITS_STRING, data);
    }

    public boolean addRetrofit(String retrofit) {
        if (!ServoSkullRetrofit.SERVO_SKULL_RETROFIT_IDS.contains(retrofit)) return false;
        Set<String> current = new HashSet<>(getRetrofits());
        if (!current.add(retrofit)) return false;
        setRetrofits(current);
        return true;
    }
}