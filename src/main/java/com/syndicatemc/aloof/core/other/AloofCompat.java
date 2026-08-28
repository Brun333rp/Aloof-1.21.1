package com.syndicatemc.aloof.core.other;

import com.syndicatemc.aloof.common.dispenser.HealingBombDispenseBehavior;
import com.syndicatemc.aloof.common.dispenser.HealingSalveDispenseBehavior;
import com.syndicatemc.aloof.core.registry.AloofBlocks;
import com.syndicatemc.aloof.core.registry.AloofItems;
import net.minecraft.world.level.block.DispenserBlock;

public class AloofCompat {
    public static void registerCompat() {
        registerDispenserBehaviors();
    }

    private static void registerDispenserBehaviors() {
        DispenserBlock.registerBehavior(AloofItems.HEALING_SALVE, new HealingSalveDispenseBehavior());
        DispenserBlock.registerBehavior(AloofBlocks.HEALING_BOMB, new HealingBombDispenseBehavior());
    }
}
