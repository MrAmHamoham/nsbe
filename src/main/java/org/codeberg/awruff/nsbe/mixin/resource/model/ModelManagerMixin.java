package org.codeberg.awruff.nsbe.mixin.resource.model;

import net.minecraft.client.resource.model.ModelManager;
import org.codeberg.awruff.nsbe.impl.ChestTextures;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.resource.manager.ResourceManager;

@Mixin(ModelManager.class)
abstract class ModelManagerMixin {

	@Inject(method = "reload", at = @At("HEAD"))
	private void nsbe$refreshChestTextures(ResourceManager resourceManager, CallbackInfo ci) {
		ChestTextures.refresh();
	}
}
