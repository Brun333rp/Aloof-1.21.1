package com.syndicatemc.aloof.core.other;

import com.syndicatemc.aloof.core.Aloof;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.*;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;

@EventBusSubscriber(modid = Aloof.MOD_ID)
public class AloofEvents {
    @SubscribeEvent
    public static void creativeTabEvent(BuildCreativeModeTabContentsEvent event) {
        ResourceKey<CreativeModeTab> tab = event.getTabKey();
        if (tab == CreativeModeTabs.NATURAL_BLOCKS) {
            removeItems(event, Items.TORCHFLOWER.getDefaultInstance(), Items.TORCHFLOWER_SEEDS.getDefaultInstance());
        }
    }

    private static void removeItems(BuildCreativeModeTabContentsEvent event, ItemStack... items) {
        for (ItemStack item : items) event.remove(item, CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
    }
}
