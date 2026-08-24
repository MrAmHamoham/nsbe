package org.codeberg.awruff.nsbe.mixin.render.block;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.block.Block;
import net.minecraft.block.ChestBlock;
import net.minecraft.block.EnderChestBlock;
import net.minecraft.block.state.BlockState;
import net.minecraft.client.render.block.BlockRenderDispatcher;
import net.minecraft.client.render.texture.TextureAtlasSprite;
import net.minecraft.client.resource.model.BakedModel;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldView;
import org.codeberg.awruff.nsbe.impl.ChestAnimation;
import org.codeberg.awruff.nsbe.impl.EmptyBakedModel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BlockRenderDispatcher.class)
abstract class BlockRenderDispatcherMixin {

	@ModifyReturnValue(
		method = "getModel(Lnet/minecraft/block/state/BlockState;Lnet/minecraft/world/WorldView;Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/client/resource/model/BakedModel;",
		at = @At("RETURN")
	)
	private BakedModel nsbe$hideOpenChests(
		BakedModel original,
		@Local(argsOnly = true) BlockState state, @Local(argsOnly = true) WorldView world, @Local(argsOnly = true) BlockPos pos
	) {
		if (nsbe$isOpenChest(state, world, pos)) {
			return EmptyBakedModel.of(original);
		}

		return original;
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
