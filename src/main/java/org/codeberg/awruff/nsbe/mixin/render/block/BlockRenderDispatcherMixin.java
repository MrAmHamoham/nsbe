package org.codeberg.awruff.nsbe.mixin.render.block;

import net.minecraft.block.Block;
import net.minecraft.block.ChestBlock;
import net.minecraft.block.EnderChestBlock;
import net.minecraft.block.state.BlockState;
import net.minecraft.client.render.block.BlockRenderDispatcher;
import net.minecraft.client.render.texture.TextureAtlasSprite;
import net.minecraft.client.render.vertex.BufferBuilder;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldView;
import org.codeberg.awruff.nsbe.impl.ChestAnimation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockRenderDispatcher.class)
abstract class BlockRenderDispatcherMixin {

	@Inject(
		method = "render(Lnet/minecraft/block/state/BlockState;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/world/WorldView;Lnet/minecraft/client/render/vertex/BufferBuilder;)Z",
		at = @At("HEAD"),
		cancellable = true
	)
	private void nsbe$skipOpenChests(BlockState state, BlockPos pos, WorldView world, BufferBuilder buffer, CallbackInfoReturnable<Boolean> cir) {
		if (nsbe$isOpenChest(state, world, pos)) {
			cir.setReturnValue(false);
		}
	}

	@Inject(
		method = "renderMiningProgress",
		at = @At("HEAD"),
		cancellable = true
	)
	private void nsbe$skipOpenChestCracks(BlockState state, BlockPos pos, TextureAtlasSprite sprite, WorldView world, CallbackInfo ci) {
		if (nsbe$isOpenChest(state, world, pos)) {
			ci.cancel();
		}
	}

	@Unique
	private static boolean nsbe$isOpenChest(BlockState state, WorldView world, BlockPos pos) {
		Block block = state.getBlock();

		if (!(block instanceof ChestBlock) && !(block instanceof EnderChestBlock)) {
			return false;
		}

		return ChestAnimation.isOpen(world.getBlockEntity(pos));
	}

}
