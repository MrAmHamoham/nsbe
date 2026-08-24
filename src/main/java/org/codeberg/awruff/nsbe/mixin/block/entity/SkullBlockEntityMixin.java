package org.codeberg.awruff.nsbe.mixin.block.entity;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.SkullBlockEntity;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SkullBlockEntity.class)
abstract class SkullBlockEntityMixin extends BlockEntity {

	@Inject(method = "readNbt", at = @At("TAIL"))
	private void nsbe$rerenderOnUpdate(NbtCompound nbt, CallbackInfo ci) {
		if (this.world != null && this.world.isClient) {
			this.world.notifyBlockChanged(this.pos);
		}
	}
}
