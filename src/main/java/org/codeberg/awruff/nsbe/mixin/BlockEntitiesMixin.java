package org.codeberg.awruff.nsbe.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.block.Block;
import net.minecraft.block.ChestBlock;
import net.minecraft.block.EnderChestBlock;
import net.minecraft.block.material.Material;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin({ ChestBlock.class, EnderChestBlock.class })
abstract class BlockEntitiesMixin extends Block {

	private BlockEntitiesMixin(Material material) {
		super(material);
	}

	@ModifyReturnValue(
		method = "getRenderType",
		at = @At("RETURN")
	)
	private int nsbe$setRenderTypeStandard(int original) {
		return 3;
	}

}
