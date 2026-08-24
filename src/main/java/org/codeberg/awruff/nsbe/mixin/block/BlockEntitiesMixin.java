package org.codeberg.awruff.nsbe.mixin.block;

import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({
	ChestBlock.class,
	EnderChestBlock.class,
	BannerBlock.class,
	SignBlock.class,
	SkullBlock.class
})
abstract class BlockEntitiesMixin extends BlockWithBlockEntity {
	private BlockEntitiesMixin(Material material) {
		super(material);
	}

	@Override
	public int getRenderType() {
		return 3;
	}
}
