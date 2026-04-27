package org.codeberg.awruff.nsbe.mixin.blocks;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.block.EnderChestBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(EnderChestBlock.class)
abstract class EnderChestBlockMixin {
	@ModifyReturnValue(
		method = "getRenderType",
		at = @At("RETURN")
	)
	int nsbe$setRenderTypeStandard(int original) {
		return 3;
	}
}
