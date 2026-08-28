package com.syndicatemc.aloof.common.dispenser;

import com.syndicatemc.aloof.common.item.HealingSalveItem;
import com.syndicatemc.aloof.core.other.tags.AloofEntityTypeTags;
import com.syndicatemc.aloof.core.registry.AloofSoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.core.dispenser.BlockSource;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.phys.AABB;

public class HealingSalveDispenseBehavior extends DefaultDispenseItemBehavior {
    @Override
    protected ItemStack execute(BlockSource source, ItemStack stack) {
        ServerLevel level = source.level();
        BlockPos pos = source.pos().relative(source.state().getValue(DispenserBlock.FACING));
        RandomSource random = level.random;
        for(LivingEntity entity : level.getEntitiesOfClass(LivingEntity.class, new AABB(pos), EntitySelector.NO_SPECTATORS)) {
            int maxDamage = stack.getMaxDamage();
            int currentDamage = stack.getDamageValue();
            int remainingDamage = maxDamage - currentDamage;
            float maxHealth = entity.getMaxHealth();
            float currentHealth = entity.getHealth();
            float missingHealth = maxHealth - currentHealth;
            if (maxHealth != currentHealth && !entity.isDeadOrDying() && !entity.getType().is(AloofEntityTypeTags.HEALING_BLACKLIST)) {
                float amount = Math.min(Math.min(remainingDamage, 5), missingHealth);
                entity.heal(amount);
                for (int i = 0; i < Math.round(amount); i++) HealingSalveItem.spawnParticles(level, entity, random);
                HealingSalveItem.playSound(level, entity, random, AloofSoundEvents.HEALING_SALVE_USE.get());
                stack.setDamageValue(currentDamage + Math.round(amount));
                if (remainingDamage - Math.round(amount) == 0) {
                    return this.consumeWithRemainder(source, stack, new ItemStack(Items.BOWL));
                }
            }
        }
        return stack;
    }
}
