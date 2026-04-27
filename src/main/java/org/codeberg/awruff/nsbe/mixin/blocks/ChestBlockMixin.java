package org.codeberg.awruff.nsbe.mixin.blocks;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.block.ChestBlock;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.StateDefinition;
import net.minecraft.block.state.property.DirectionProperty;
import net.minecraft.block.state.property.EnumProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.codeberg.awruff.nsbe.NotShitBlockEntities;
import org.codeberg.awruff.nsbe.impl.ChestType;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ChestBlock.class)
abstract class ChestBlockMixin {
	@Unique
	private static final EnumProperty<ChestType> TYPE = EnumProperty.of("type", ChestType.class);
	@Shadow
	@Final
	public static DirectionProperty FACING;

	@ModifyReturnValue(
		method = "getRenderType",
		at = @At("RETURN")
	)
	private int nsbe$setRenderTypeStandard(int original) {
		return 3;
	}

	@ModifyReturnValue(
		method = "createStateDefinition",
		at = @At("RETURN")
	)
	private StateDefinition nsbe$addTypeDefinition(StateDefinition original) {
		return new StateDefinition((ChestBlock) (Object) this, FACING, TYPE);
	}

	@ModifyReturnValue(
		method = "updateState",
		at = @At("RETURN")
	)
	private BlockState nsbe$updateChestType(
		BlockState original,
		@Local(argsOnly = true) World world,
		@Local(argsOnly = true) BlockPos pos
	) {
		if (world.isClient) return original;

		Direction facing = original.get(FACING);

		Direction leftDir = facing.counterClockwiseY();
		Direction rightDir = facing.clockwiseY();

		BlockPos leftPos = pos.offset(leftDir);
		BlockPos rightPos = pos.offset(rightDir);

		BlockState leftState = world.getBlockState(leftPos);
		BlockState rightState = world.getBlockState(rightPos);

		boolean leftMatch =
			leftState.getBlock() instanceof ChestBlock &&
				leftState.get(FACING) == facing &&
				leftState.get(TYPE) == ChestType.SINGLE;

		boolean rightMatch =
			rightState.getBlock() instanceof ChestBlock &&
				rightState.get(FACING) == facing &&
				rightState.get(TYPE) == ChestType.SINGLE;

		NotShitBlockEntities.LOGGER.info("=== Chest Update Debug ===");
		NotShitBlockEntities.LOGGER.info("Chest at {} facing {}", pos, facing);

		NotShitBlockEntities.LOGGER.info("LeftDir: {}  RightDir: {}", leftDir, rightDir);

		NotShitBlockEntities.LOGGER.info("LeftPos: {}  RightPos: {}", leftPos, rightPos);
		NotShitBlockEntities.LOGGER.info("LeftState: {}  RightState: {}", leftState, rightState);

		NotShitBlockEntities.LOGGER.info("Left is chest: {}", leftState.getBlock() instanceof ChestBlock);
		NotShitBlockEntities.LOGGER.info("Right is chest: {}", rightState.getBlock() instanceof ChestBlock);

		if (leftState.getBlock() instanceof ChestBlock) {
			NotShitBlockEntities.LOGGER.info("Left facing: {}  Left type: {}", leftState.get(FACING), leftState.get(TYPE));
		}
		if (rightState.getBlock() instanceof ChestBlock) {
			NotShitBlockEntities.LOGGER.info("Right facing: {}  Right type: {}", rightState.get(FACING), rightState.get(TYPE));
		}

		NotShitBlockEntities.LOGGER.info("leftMatch: {}  rightMatch: {}", leftMatch, rightMatch);

		ChestType type = ChestType.SINGLE;

		if (leftMatch && !rightMatch) {
			type = ChestType.RIGHT;
		} else if (rightMatch && !leftMatch) {
			type = ChestType.LEFT;
		}

		NotShitBlockEntities.LOGGER.info("Final chosen type: {}", type);
		NotShitBlockEntities.LOGGER.info("==========================");

		return original.set(TYPE, type);
	}
}
