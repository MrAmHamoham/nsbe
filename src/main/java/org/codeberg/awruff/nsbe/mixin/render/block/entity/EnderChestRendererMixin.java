package org.codeberg.awruff.nsbe.mixin.render.block.entity;

import net.minecraft.block.entity.EnderChestBlockEntity;
import net.minecraft.client.render.block.entity.EnderChestRenderer;
import org.codeberg.awruff.nsbe.impl.ChestAnimation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EnderChestRenderer.class)
abstract class EnderChestRendererMixin {

	@Inject(
		method = "render(Lnet/minecraft/block/entity/EnderChestBlockEntity;DDDFI)V",
		at = @At("HEAD"),
		cancellable = true
	)
	private void nsbe$skipClosedChests(EnderChestBlockEntity chest, double x, double y, double z, float tickDelta, int miningProgress, CallbackInfo ci) {
		if (!ChestAnimation.isOpen(chest)) {
			ci.cancel();
		}
	}

}
