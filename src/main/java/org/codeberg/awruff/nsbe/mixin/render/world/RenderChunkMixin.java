package org.codeberg.awruff.nsbe.mixin.render.world;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.block.entity.*;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.world.RenderChunk;
import net.minecraft.text.Text;
import org.codeberg.awruff.nsbe.impl.ChestAnimation;
import org.codeberg.awruff.nsbe.impl.SkullType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(RenderChunk.class)
abstract class RenderChunkMixin {

	@ModifyExpressionValue(
		method = "compile",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/client/render/block/entity/BlockEntityRenderDispatcher;getRenderer(Lnet/minecraft/block/entity/BlockEntity;)Lnet/minecraft/client/render/block/entity/BlockEntityRenderer;"
		)
	)
	private BlockEntityRenderer<BlockEntity> nsbe$skipBakedRenderer(BlockEntityRenderer<BlockEntity> renderer, @Local BlockEntity blockEntity) {
		if (blockEntity != null && !nsbe$needsRenderer(blockEntity)) {
			return null;
		}

		return renderer;
	}

	@Unique
	private static boolean nsbe$needsRenderer(BlockEntity blockEntity) {
		if (blockEntity instanceof ChestBlockEntity || blockEntity instanceof EnderChestBlockEntity) {
			return ChestAnimation.isOpen(blockEntity);
		}

		if (blockEntity instanceof SkullBlockEntity) {
			return ((SkullBlockEntity) blockEntity).getType() == SkullType.PLAYER_ID;
		}

		if (blockEntity instanceof SignBlockEntity) {
			return nsbe$hasText((SignBlockEntity) blockEntity);
		}

		return true;
	}

	@Unique
	private static boolean nsbe$hasText(SignBlockEntity sign) {
		for (Text line : sign.lines) {
			if (line != null && !line.getString().isEmpty()) {
				return true;
			}
		}

		return false;
	}
}
