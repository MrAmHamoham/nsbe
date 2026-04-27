package org.codeberg.awruff.nsbe.mixin;

import net.minecraft.block.Block;
import net.minecraft.client.resource.model.BlockModelProvider;
import net.minecraft.client.resource.model.BlockModels;
import org.codeberg.awruff.nsbe.impl.IBlockModels;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Map;
import java.util.Set;

@Mixin(BlockModels.class)
public class BlockModelsMixin implements IBlockModels {
	@Shadow
	private Set<Block> custom;

	@Shadow
	private Map<Block, BlockModelProvider> providers;

	@Override
	public void nsbe$unregister(Block block) {
		this.providers.remove(block);
		this.custom.remove(block);
	}

}
