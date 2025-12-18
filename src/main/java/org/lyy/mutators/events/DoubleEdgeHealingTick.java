package org.lyy.mutators.events;

import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import org.lyy.mutators.data.DoubleEdgeData;
import org.lyy.mutators.registries.MutatorsAttachment;

@EventBusSubscriber(modid = "mutators")
public final class DoubleEdgeHealingTick {
    private static final float HEAL_PER_TICK = 10f / 20f; // 0.5

    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Post event) {
        var p = event.getEntity();
        if (p.level().isClientSide()) return;
        if (!(p instanceof ServerPlayer player)) return;

        var type = MutatorsAttachment.DOUBLE_EDGE.get();
        var data = player.getData(type);

        float debt = data.healDebt();
        if (debt <= 0f) return;

        float healNow = Math.min(debt, HEAL_PER_TICK);
        player.heal(healNow);
        player.setData(type, new DoubleEdgeData(debt - healNow));
    }


}
