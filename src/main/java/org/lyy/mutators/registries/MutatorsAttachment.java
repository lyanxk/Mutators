package org.lyy.mutators.registries;

import com.mojang.serialization.Codec;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import org.lyy.mutators.data.DoubleEdgeData;

public class MutatorsAttachment {
    public static final DeferredRegister<AttachmentType<?>> ATTACHMENTS =
            DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, "mutators");

    public static final DeferredHolder<AttachmentType<?>, AttachmentType<DoubleEdgeData>> DOUBLE_EDGE =
            ATTACHMENTS.register("double_edge", () ->
                    AttachmentType.<DoubleEdgeData>builder(() -> new DoubleEdgeData(0f))
                            .serialize(Codec.FLOAT.xmap(DoubleEdgeData::new, DoubleEdgeData::healDebt))
                            .build()
            );

}
