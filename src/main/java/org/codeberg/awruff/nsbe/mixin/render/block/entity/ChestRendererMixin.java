package org.codeberg.awruff.nsbe.mixin.render.block.entity;

import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.client.render.block.entity.ChestRenderer;
import org.codeberg.awruff.nsbe.impl.ChestAnimation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ChestRenderer.class)
abstract class ChestRendererMixin {

	@Inject(
		method = "render(Lnet/minecraft/block/entity/ChestBlockEntity;DDDFI)V",
		at = @At("HEAD"),
		cancellable = true
	)
	private void nsbe$skipClosedChests(ChestBlockEntity chest, double x, double y, double z, float tickDelta, int miningProgress, CallbackInfo ci) {
		if (!ChestAnimation.isOpen(chest)) {
			ci.cancel();
		}
	}

}
