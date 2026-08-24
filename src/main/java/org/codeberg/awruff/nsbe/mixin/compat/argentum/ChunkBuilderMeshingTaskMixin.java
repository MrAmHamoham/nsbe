package org.codeberg.awruff.nsbe.mixin.compat.argentum;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import org.codeberg.awruff.nsbe.impl.BlockEntityRenderers;
import org.spongepowered.asm.mixin.Dynamic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;

@Pseudo
@Mixin(
	targets = {
		"dev.rdh.argentum.impl.render.terrain.compile.task.ChunkBuilderMeshingTask",
		"org.taumc.celeritas.impl.render.terrain.compile.task.ChunkBuilderMeshingTask" // for older argentum builds
	}
)
abstract class ChunkBuilderMeshingTaskMixin {

	@Dynamic("Argentum")
	@ModifyExpressionValue(
		method = "executeTimed",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/client/render/block/entity/BlockEntityRenderDispatcher;getRenderer(Lnet/minecraft/block/entity/BlockEntity;)Lnet/minecraft/client/render/block/entity/BlockEntityRenderer;"
		),
		require = 0
	)
	private BlockEntityRenderer<BlockEntity> nsbe$skipBakedRenderer(BlockEntityRenderer<BlockEntity> renderer, @Local BlockEntity blockEntity) {
		if (blockEntity != null && !BlockEntityRenderers.needsRenderer(blockEntity)) {
			return null;
		}

		return renderer;
	}
}
