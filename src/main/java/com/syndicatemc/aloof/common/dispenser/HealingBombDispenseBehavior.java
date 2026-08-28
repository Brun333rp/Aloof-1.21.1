package com.syndicatemc.aloof.common.dispenser;

import com.syndicatemc.aloof.common.entity.HealingBomb;
import net.minecraft.core.BlockPos;
import net.minecraft.core.dispenser.BlockSource;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.gameevent.GameEvent;

public class HealingBombDispenseBehavior extends DefaultDispenseItemBehavior {
    @Override
    protected ItemStack execute(BlockSource source, ItemStack stack) {
        Level level = source.level();
        BlockPos pos = source.pos().relative(source.state().getValue(DispenserBlock.FACING));
        PrimedTnt bomb = new HealingBomb(level, pos.getX() + 0.5F, pos.getY(), pos.getZ() + 0.5F, null);
        level.addFreshEntity(bomb);
        level.playSound(null, bomb.getX(), bomb.getY(), bomb.getZ(), SoundEvents.TNT_PRIMED, SoundSource.BLOCKS, 1.0F, 1.0F);
        level.gameEvent(null, GameEvent.ENTITY_PLACE, pos);
        stack.shrink(1);
        return stack;
    }
}
