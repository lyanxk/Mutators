package org.lyy.mutators.events;


import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import org.lyy.mutators.data.DoubleEdgeData;
import org.lyy.mutators.registries.MutatorsAttachment;

@EventBusSubscriber(modid = "mutators")
public class DoubleEdged {
    public static final ResourceKey<DamageType> DOUBLE_EDGE_KEY =
            ResourceKey.create(Registries.DAMAGE_TYPE,
                    ResourceLocation.fromNamespaceAndPath("mutators", "double_edge"));


    @SubscribeEvent
    public static void onLivingDamagePost(net.neoforged.neoforge.event.entity.living.LivingDamageEvent.Post event) {
        LivingEntity victim = event.getEntity();
        if (victim.level().isClientSide) return;

        var source = event.getSource();

        // 防递归
        if (source.typeHolder().is(DOUBLE_EDGE_KEY)) return;

        // 只处理玩家
        Entity srcEntity = source.getEntity();
        if (!(srcEntity instanceof ServerPlayer attacker)) return;

        // 因子存在判断
        if (!MutatorHooks.hasDoubleEdge(attacker)) return;

        float finalDamage = event.getNewDamage();
        if (finalDamage <= 0f) return;

        //造成伤害
        var ds = attacker.damageSources().source(DOUBLE_EDGE_KEY, victim, victim);
        attacker.hurt(ds, finalDamage);

        var type = MutatorsAttachment.DOUBLE_EDGE.get();

        var before = attacker.getData(type).healDebt();
        attacker.setData(type, new DoubleEdgeData(before + finalDamage));
        var after = attacker.getData(type).healDebt();

    }

    public final class MutatorHooks {
        public static boolean hasDoubleEdge(ServerPlayer player) {
            // TODO: 你自己的突变因子系统判断逻辑
            return true;
        }
    }

}
