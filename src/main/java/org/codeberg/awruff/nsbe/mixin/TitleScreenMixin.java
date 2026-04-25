package org.codeberg.awruff.nsbe.mixin;

import net.minecraft.client.gui.screen.TitleScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TitleScreen.class)
abstract class TitleScreenMixin {

	@Inject(
		method = "init",
		at = @At("TAIL")
	)
	private void nsbe$onInit(CallbackInfo ci) {

	}

}
