package org.codeberg.awruff.nsbe.mixin.block.entity;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.ChestBlockEntity;
import org.codeberg.awruff.nsbe.impl.ChestAnimation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ChestBlockEntity.class)
abstract class ChestBlockEntityMixin extends BlockEntity {

	@Shadow
	public boolean doubleChest;
	@Shadow
	public float animationProgress;
	@Shadow
	public float lastAnimationProgress;
	@Shadow
	public int viewerCount;

	@Unique
	private boolean nsbe$open;

	@Inject(method = "tick", at = @At("HEAD"), cancellable = true)
	private void nsbe$skipIdleTick(CallbackInfo ci) {
		if (this.world == null || !this.world.isClient) {
			return;
		}

		if (!this.doubleChest || this.nsbe$open) {
			return;
		}

		if (this.viewerCount != 0 || this.animationProgress != 0.0F || this.lastAnimationProgress != 0.0F) {
			return;
		}

		if (ChestAnimation.isOpen(this)) {
			return;
		}

		ci.cancel();
	}

	@Inject(method = "tick", at = @At("TAIL"))
	private void nsbe$rerenderOnOpen(CallbackInfo ci) {
		if (this.world == null || !this.world.isClient) {
			return;
		}

		boolean open = ChestAnimation.isOpen(this);

		if (open != this.nsbe$open) {
			this.nsbe$open = open;
			this.world.notifyBlockChanged(this.pos);
		}
	}

}
