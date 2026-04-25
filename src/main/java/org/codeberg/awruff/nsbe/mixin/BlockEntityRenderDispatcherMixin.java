package org.codeberg.awruff.nsbe.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.ChestRenderer;
import net.minecraft.client.render.block.entity.EnderChestRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(BlockEntityRenderDispatcher.class)
abstract class BlockEntityRenderDispatcherMixin {

	@ModifyReturnValue(
		method = "getRenderer(Ljava/lang/Class;)Lnet/minecraft/client/render/block/entity/BlockEntityRenderer;",
		at = @At("RETURN")
	)
	private <T extends BlockEntity> BlockEntityRenderer<T> idk(BlockEntityRenderer<T> original) {
		if (original instanceof ChestRenderer || original instanceof EnderChestRenderer) {
			return null;
		}

		return original;
	}

}
