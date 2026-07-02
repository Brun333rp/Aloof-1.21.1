package com.syndicatemc.aloof.core.other;

import net.minecraft.util.RandomSource;

public class AloofUtils {
    public static float getVariance(RandomSource random, float variance) {
        return -variance + random.nextFloat() * variance * 2;
    }
}
