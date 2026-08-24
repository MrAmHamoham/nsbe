package org.codeberg.awruff.nsbe.mixin.render.block;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.SkullBlock;
import net.minecraft.client.render.block.BlockModelShaper;
import net.minecraft.client.resource.model.BlockModels;
import net.minecraft.client.resource.model.VariantBlockModelProvider;
import org.codeberg.awruff.nsbe.impl.EmptyBakedModel;
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
		Blocks.STANDING_BANNER,
		Blocks.WALL_BANNER,
		Blocks.CHEST,
		Blocks.TRAPPED_CHEST,
		Blocks.ENDER_CHEST,
		Blocks.STANDING_SIGN,
		Blocks.WALL_SIGN
	);

	@Final
	@Shadow
	private BlockModels models;

	@Inject(method = "init", at = @At("TAIL"))
	private void nsbe$useBakedModels(CallbackInfo ci) {
		EmptyBakedModel.clearCache();

		for (Block block : DATA_DRIVEN_BLOCKS) {
			nsbe$replaceProvider(block, new VariantBlockModelProvider.Builder());
		}

		nsbe$replaceProvider(Blocks.SKULL,
			new VariantBlockModelProvider.Builder()
				.setUnusedProperties(SkullBlock.NODROP)
		);
	}

	@Unique
	private void nsbe$replaceProvider(Block block, VariantBlockModelProvider.Builder provider) {
		((IBlockModels) this.models).nsbe$unregister(block);

		this.models.register(block, provider.build());
	}

}
