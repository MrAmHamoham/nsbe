package org.codeberg.awruff.nsbe.mixin.render.world;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.world.CompiledChunk;
import net.minecraft.client.render.world.RenderChunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(RenderChunk.class)
public class RenderChunkMixin {
	// Fixes MC-112730
	@WrapWithCondition(method = "compile", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/world/CompiledChunk;addRenderableBlockEntity(Lnet/minecraft/block/entity/BlockEntity;)V"))
	private boolean nsbe$deduplicateGlobalBlockEntities(CompiledChunk instance, BlockEntity blockEntity, @Local BlockEntityRenderer<BlockEntity> blockEntityRenderer) {
		return !blockEntityRenderer.shouldRenderOffScreen();
	}
}
