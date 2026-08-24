package org.codeberg.awruff.nsbe.impl;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.block.entity.EnderChestBlockEntity;

public class ChestAnimation {
	public static boolean isOpen(BlockEntity blockEntity) {
		if (blockEntity instanceof ChestBlockEntity) {
			return isChestOpen((ChestBlockEntity) blockEntity);
		}

		if (blockEntity instanceof EnderChestBlockEntity) {
			EnderChestBlockEntity chest = (EnderChestBlockEntity) blockEntity;

			return isOpen(chest.animationProgress, chest.lastAnimationProgress, chest.viewerCount);
		}

		return false;
	}

	private static boolean isChestOpen(ChestBlockEntity chest) {
		return isHalfOpen(chest)
			|| isHalfOpen(chest.northNeighbor)
			|| isHalfOpen(chest.southNeighbor)
			|| isHalfOpen(chest.eastNeighbor)
			|| isHalfOpen(chest.westNeighbor);
	}

	private static boolean isHalfOpen(ChestBlockEntity chest) {
		return chest != null && isOpen(chest.animationProgress, chest.lastAnimationProgress, chest.viewerCount);
	}

	private static boolean isOpen(float animationProgress, float lastAnimationProgress, int viewerCount) {
		return viewerCount > 0 || animationProgress > 0.0F || lastAnimationProgress > 0.0F;
	}
}
