package com.syndicatemc.aloof.core.registry;

import com.syndicatemc.aloof.core.Aloof;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.CriterionTrigger;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.PlayerTrigger;
import net.minecraft.core.registries.Registries;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.Optional;

public class AloofCriteriaTriggers {
    public static final DeferredRegister<CriterionTrigger<?>> TRIGGERS = DeferredRegister.create(Registries.TRIGGER_TYPE, Aloof.MOD_ID);

    public static final DeferredHolder<CriterionTrigger<?>, PlayerTrigger> USE_SALVE_ON_WARDEN = TRIGGERS.register("use_salve_on_warden", PlayerTrigger::new);

    public static Criterion<PlayerTrigger.TriggerInstance> useSalveOnWarden() {
        return USE_SALVE_ON_WARDEN.get().createCriterion(new PlayerTrigger.TriggerInstance(EntityPredicate.wrap(Optional.empty())));
    }
}
