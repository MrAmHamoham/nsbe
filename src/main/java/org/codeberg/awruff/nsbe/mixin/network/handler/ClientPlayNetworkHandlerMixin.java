package org.codeberg.awruff.nsbe.mixin.network.handler;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.SignBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.handler.ClientPlayNetworkHandler;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.network.packet.s2c.play.SignUpdateS2CPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayNetworkHandler.class)
abstract class ClientPlayNetworkHandlerMixin {

	@Inject(method = "handleSignBlockEntityUpdate", at = @At("TAIL"))
	private void nsbe$updateSign(SignUpdateS2CPacket packet, CallbackInfo ci) {
		ClientWorld world = Minecraft.getInstance().world;

		if (world == null) {
			return;
		}

		BlockEntity blockEntity = world.getBlockEntity(packet.getPos());

		if (blockEntity instanceof SignBlockEntity) {
			world.notifyBlockChanged(packet.getPos());
		}
	}

}
