package org.codeberg.awruff.nsbe.impl;

import net.minecraft.block.entity.BeaconBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.block.entity.EnderChestBlockEntity;
import net.minecraft.block.entity.SignBlockEntity;
import net.minecraft.block.entity.SkullBlockEntity;
import net.minecraft.text.Text;

public class BlockEntityRenderers {

	public static boolean needsRenderer(BlockEntity blockEntity) {
		if (blockEntity instanceof ChestBlockEntity || blockEntity instanceof EnderChestBlockEntity) {
			return ChestAnimation.isOpen(blockEntity);
		}

		if (blockEntity instanceof SkullBlockEntity) {
			return ((SkullBlockEntity) blockEntity).getType() == SkullType.PLAYER_ID;
		}

		if (blockEntity instanceof SignBlockEntity) {
			return hasText((SignBlockEntity) blockEntity);
		}

		if (blockEntity instanceof BeaconBlockEntity) {
			return !((BeaconBlockEntity) blockEntity).getBeamSections().isEmpty();
		}

		return true;
	}

	private static boolean hasText(SignBlockEntity sign) {
		for (Text line : sign.lines) {
			if (line != null && !line.getString().isEmpty()) {
				return true;
			}
		}

		return false;
	}
}
