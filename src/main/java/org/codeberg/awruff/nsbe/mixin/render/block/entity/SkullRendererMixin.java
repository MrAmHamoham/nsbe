package org.codeberg.awruff.nsbe.mixin.render.block.entity;

import net.minecraft.block.entity.SkullBlockEntity;
import net.minecraft.client.render.block.entity.SkullRenderer;
import org.codeberg.awruff.nsbe.impl.SkullType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SkullRenderer.class)
abstract class SkullRendererMixin {

	@Inject(
		method = "render(Lnet/minecraft/block/entity/SkullBlockEntity;DDDFI)V",
		at = @At("HEAD"),
		cancellable = true
	)
	private void nsbe$skipBakedSkulls(
		SkullBlockEntity skull, double x, double y, double z, float tickDelta, int blockMiningProgress, CallbackInfo ci
	) {
		if (skull.getType() != SkullType.PLAYER_ID) {
			ci.cancel();
		}
	}
}
