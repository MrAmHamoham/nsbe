package org.codeberg.awruff.nsbe.mixin;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.render.block.BlockModelShaper;
import net.minecraft.client.resource.model.BlockModels;
import net.minecraft.client.resource.model.VariantBlockModelProvider;
import org.codeberg.awruff.nsbe.impl.IBlockModels;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Arrays;
import java.util.List;

@Mixin(BlockModelShaper.class)
abstract class BlockModelShaperMixin {

	@Unique
	private static final List<Block> DATA_DRIVEN_BLOCKS = Arrays.asList(
		Blocks.CHEST,
		Blocks.TRAPPED_CHEST,
		Blocks.ENDER_CHEST
	);
	@Final
	@Shadow
	private BlockModels models;

	@Inject(method = "init", at = @At("TAIL"))
	private void makeChestsDataDriven(CallbackInfo ci) {
		for (Block block : DATA_DRIVEN_BLOCKS) {
			((IBlockModels) this.models).nsbe$unregister(block);

			this.models.register(block,
				new VariantBlockModelProvider.Builder()
					.build()
			);
		}
	}

}
