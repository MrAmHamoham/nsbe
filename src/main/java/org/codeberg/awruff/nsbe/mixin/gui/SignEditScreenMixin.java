package org.codeberg.awruff.nsbe.mixin.gui;

import net.minecraft.block.entity.SignBlockEntity;
import net.minecraft.client.gui.screen.inventory.menu.SignEditScreen;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SignEditScreen.class)
abstract class SignEditScreenMixin {

	@Shadow
	private SignBlockEntity sign;

	@Inject(method = "keyPressed", at = @At("TAIL"))
	private void nsbe$updateSign(char character, int key, CallbackInfo ci) {
		World world = this.sign.getWorld();

		if (world != null) {
			world.notifyBlockChanged(this.sign.getPos());
		}
	}

}
