package com.syndicatemc.aloof.core.data.client;

import com.syndicatemc.aloof.core.Aloof;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.common.data.SpriteSourceProvider;

import java.util.concurrent.CompletableFuture;

public class AloofSpriteSourceProvider extends SpriteSourceProvider {
    public AloofSpriteSourceProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookup, ExistingFileHelper helper) {
        super(output, lookup, Aloof.MOD_ID, helper);
    }

    @Override
    protected void gather() {

    }
}
