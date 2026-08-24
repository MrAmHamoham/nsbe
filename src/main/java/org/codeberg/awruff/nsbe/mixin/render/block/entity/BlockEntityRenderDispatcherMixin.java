package org.codeberg.awruff.nsbe.mixin.render.block.entity;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import org.codeberg.awruff.nsbe.impl.BlockEntityRenderers;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(BlockEntityRenderDispatcher.class)
public class BlockEntityRenderDispatcherMixin {
	@ModifyReturnValue(
		method = "getRenderer(Lnet/minecraft/block/entity/BlockEntity;)Lnet/minecraft/client/render/block/entity/BlockEntityRenderer;",
		at = @At("RETURN")
	)
	private <T extends BlockEntity> BlockEntityRenderer<T> meow(BlockEntityRenderer<T> original, @Local(argsOnly = true) BlockEntity blockEntity) {
		if (blockEntity != null && !BlockEntityRenderers.needsRenderer(blockEntity)) {
			return null;
		}

		return original;
	}
}
