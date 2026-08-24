package org.codeberg.awruff.nsbe.mixin.render.block.entity;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.block.entity.BannerBlockEntity;
import net.minecraft.client.render.block.entity.BannerRenderer;
import net.minecraft.client.render.model.block.entity.BannerModel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(BannerRenderer.class)
abstract class BannerRendererMixin {

	@WrapOperation(
		method = "render(Lnet/minecraft/block/entity/BannerBlockEntity;DDDFI)V",
		at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/model/block/entity/BannerModel;render()V")
	)
	private void nsbe$hideBannerPole(BannerModel model, Operation<Void> original, @Local(argsOnly = true) BannerBlockEntity banner) {
		if (banner.getWorld() != null) {
			model.pole.visible = false;
			model.bar.visible = false;
		}

		original.call(model);

		model.pole.visible = true;
		model.bar.visible = true;
	}
}
