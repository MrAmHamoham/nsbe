package org.codeberg.awruff.nsbe.mixin.render.model.block;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.client.render.model.block.BlockModel;
import org.codeberg.awruff.nsbe.impl.ChestTextures;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(BlockModel.class)
abstract class BlockModelMixin {

	@ModifyReturnValue(
		method = "getTexture(Ljava/lang/String;)Ljava/lang/String;",
		at = @At("RETURN")
	)
	private String nsbe$swapChestTexture(String original) {
		return ChestTextures.resolve(original);
	}
}
