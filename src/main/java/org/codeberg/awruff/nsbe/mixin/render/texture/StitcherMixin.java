package org.codeberg.awruff.nsbe.mixin.render.texture;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import net.minecraft.client.render.texture.Stitcher;
import net.minecraft.client.render.texture.TextureAtlasSprite;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Stitcher.class)
abstract class StitcherMixin {

	@WrapWithCondition(
		method = {"addSprite", "expand"},
		at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/texture/Stitcher$Holder;toggleRotated()V")
	)
	private boolean nsbe$isSquare(Stitcher.Holder holder) {
		TextureAtlasSprite sprite = holder.getSprite();

		return sprite.getWidth() == sprite.getHeight();
	}
}
