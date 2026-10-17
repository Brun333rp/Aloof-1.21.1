package com.syndicatemc.aloof.common.item;

import com.syndicatemc.aloof.core.other.AloofUtils;
import com.syndicatemc.aloof.core.other.tags.AloofEntityTypeTags;
import com.syndicatemc.aloof.core.registry.AloofCriteriaTriggers;
import com.syndicatemc.aloof.core.registry.AloofParticleTypes;
import com.syndicatemc.aloof.core.registry.AloofSoundEvents;
import com.teamabnormals.blueprint.common.network.particle.SpawnParticlesPayload;
import com.teamabnormals.blueprint.core.util.NetworkUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.entity.monster.warden.Warden;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;

public class HealingSalveItem extends Item {
    public HealingSalveItem(Item.Properties properties) {
        super(properties);
    }

    @Override
    public void releaseUsing(ItemStack stack, Level level, LivingEntity entity, int timeCharged) {
        if (entity instanceof Player player) {
            player.getCooldowns().addCooldown(stack.getItem(), 30);
        }
    }

    @Override
    public void onUseTick(Level level, LivingEntity entity, ItemStack stack, int duration) {
        int maxDamage = stack.getMaxDamage();
        int currentDamage = stack.getDamageValue();
        boolean lastHit = currentDamage == maxDamage - 1;
        RandomSource random = level.random;

        if (entity.getMaxHealth() == entity.getHealth()) {
            entity.stopUsingItem();
            if (entity instanceof Player player) player.getCooldowns().addCooldown(stack.getItem(), 30);
            return;
        }
        if (duration % getHealInterval() == 0 && duration < this.getUseDuration(stack, entity) - getInitialDelay()) {
            if (entity instanceof Player player) {
                player.heal(1.0F);
                spawnParticles(level, player, random);
                playSound(level, player, random, lastHit ? AloofSoundEvents.HEALING_SALVE_CONSUME.get() : AloofSoundEvents.HEALING_SALVE_USE.get());
                if (!player.isCreative()) stack.setDamageValue(currentDamage + 1);
                if (lastHit) {
                    if (player.getInventory().contains(Items.BOWL.getDefaultInstance())) {
                        stack.shrink(1);
                        player.getInventory().add(Items.BOWL.getDefaultInstance());
                    } else {
                        player.getInventory().items.set(player.getInventory().findSlotMatchingItem(stack), Items.BOWL.getDefaultInstance());
                    }
                }
            }
        }
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack stack, Player player, LivingEntity entity, InteractionHand hand) {
        Level level = player.level();
        RandomSource random = level.random;
        int maxDamage = stack.getMaxDamage();
        int currentDamage = stack.getDamageValue();
        int remainingDamage = maxDamage - currentDamage;
        float maxHealth = entity.getMaxHealth();
        float currentHealth = entity.getHealth();
        float missingHealth = maxHealth - currentHealth;
        Vec3 pos = player.position();

        if (maxHealth != currentHealth && !entity.isDeadOrDying() && !player.getCooldowns().isOnCooldown(stack.getItem()) && !entity.getType().is(AloofEntityTypeTags.HEALING_BLACKLIST)) {
            float amount = Math.min(Math.min(remainingDamage, 5), missingHealth);
            entity.heal(amount);
            for (int i = 0; i < Math.round(amount); i++) spawnParticles(level, entity, random);
            playSound(level, entity, random, AloofSoundEvents.HEALING_SALVE_USE.get());
            if (!player.isCreative()) stack.setDamageValue(currentDamage + Math.round(amount));
            if (remainingDamage - Math.round(amount) == 0) {
                if (player.getInventory().contains(Items.BOWL.getDefaultInstance())) {
                    stack.shrink(1);
                    player.getInventory().add(Items.BOWL.getDefaultInstance());
                } else {
                    player.getInventory().items.set(player.getInventory().findSlotMatchingItem(stack), Items.BOWL.getDefaultInstance());
                }
                playSound(level, player, random, AloofSoundEvents.HEALING_SALVE_CONSUME.get());
            }
            player.getCooldowns().addCooldown(stack.getItem(), 30);
            if (player instanceof ServerPlayer serverPlayer && !serverPlayer.getCommandSenderWorld().isClientSide && entity instanceof Warden warden) {
                warden.clearAnger(player);
                AloofCriteriaTriggers.USE_SALVE_ON_WARDEN.get().trigger(serverPlayer);
            }

            if (level instanceof ServerLevel serverLevel) {
                List<SpawnParticlesPayload.ParticleInstance> pollenParticles = new ArrayList<>();
                for (int l = 0; l < 10; ++l) {
                    double x = pos.x + AloofUtils.getVariance(random, 0.2F);
                    double y = pos.y + AloofUtils.getVariance(random, 0.2F) + player.getEyeHeight();
                    double z = pos.z + AloofUtils.getVariance(random, 0.2F);
                    double dx = AloofUtils.getVariance(random, 0.1F) + player.getLookAngle().x * 0.3F;
                    double dy = AloofUtils.getVariance(random, 0.1F) + player.getLookAngle().y * 0.3F;
                    double dz = AloofUtils.getVariance(random, 0.1F) + player.getLookAngle().z * 0.3F ;
                    pollenParticles.add(new SpawnParticlesPayload.ParticleInstance(x, y, z, dx, dy, dz));
                }
                NetworkUtil.spawnParticle(serverLevel, AloofParticleTypes.SILPHIUM_POLLEN.get(), pollenParticles);
            }

            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.BOW;
    }

    private int getHealInterval() {
        return 5;
    }

    private int getInitialDelay() {
        return 5;
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        return this.getMaxDamage(stack) * getHealInterval() + getInitialDelay() + 1;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (player.getHealth() != player.getMaxHealth()) {
            player.startUsingItem(hand);
            return InteractionResultHolder.consume(stack);
        } else {
            return InteractionResultHolder.fail(stack);
        }
    }

    public static void spawnParticles(Level level, LivingEntity entity, RandomSource random) {
        if (level instanceof ServerLevel serverLevel) {
            NetworkUtil.spawnParticle(serverLevel, AloofParticleTypes.SILPHIUM_HEART.get(), List.of(
                    new SpawnParticlesPayload.ParticleInstance(
                            entity.getX() + AloofUtils.getVariance(random, 0.5F),
                            entity.getY() + entity.getBbHeight() + 0.05F + AloofUtils.getVariance(random, 0.05F),
                            entity.getZ() + AloofUtils.getVariance(random, 0.5F),
                            0.0D, 0.0D, 0.0D
                    ))
            );
        }
    }

    public static void playSound(Level level, LivingEntity entity, RandomSource random, SoundEvent sound) {
        level.playSound(null, entity.getX(), entity.getY(), entity.getZ(), sound, SoundSource.PLAYERS, 1.0F, 1.0F + random.nextFloat() * 0.2F);
    }
}
