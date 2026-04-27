package org.codeberg.awruff.nsbe.mixin.blocks;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.block.BlockWithBlockEntity;
import net.minecraft.block.ChestBlock;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.StateDefinition;
import net.minecraft.block.state.property.DirectionProperty;
import net.minecraft.block.state.property.EnumProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.codeberg.awruff.nsbe.impl.ChestType;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ChestBlock.class)
abstract class ChestBlockMixin extends BlockWithBlockEntity {

	@Unique
	private static final EnumProperty<ChestType> TYPE = EnumProperty.of("type", ChestType.class);
	@Shadow
	@Final
	public static DirectionProperty FACING;

	private ChestBlockMixin(Material material) {
		super(material);
	}

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
		BlockState state,
		@Local(argsOnly = true) World world,
		@Local(argsOnly = true) BlockPos pos
	) {
		if (world.isClient) return state;

		if (state.get(TYPE) == null) {
			state = state.set(TYPE, ChestType.SINGLE);
		}

		Direction facing = state.get(FACING);

		Direction leftDir = facing.counterClockwiseY();
		Direction rightDir = facing.clockwiseY();

		BlockPos leftPos = pos.offset(leftDir);
		BlockPos rightPos = pos.offset(rightDir);

		BlockState leftState = world.getBlockState(leftPos);
		BlockState rightState = world.getBlockState(rightPos);

		boolean leftMatch =
			leftState.getBlock() instanceof ChestBlock &&
				leftState.get(FACING) == facing;

		boolean rightMatch =
			rightState.getBlock() instanceof ChestBlock &&
				rightState.get(FACING) == facing;

		ChestType type;

		if (leftMatch && !rightMatch) {
			type = ChestType.LEFT;
		} else if (rightMatch && !leftMatch) {
			type = ChestType.RIGHT;
		} else {
			type = ChestType.SINGLE;
		}

		if (state.get(TYPE) == type) return state;

		state = state.set(TYPE, type);

		if (leftMatch) {
			if (leftState.get(TYPE) != ChestType.RIGHT) {
				world.setBlockState(leftPos, leftState.set(TYPE, ChestType.RIGHT));
			}
		}
		if (rightMatch) {
			if (rightState.get(TYPE) != ChestType.LEFT) {
				world.setBlockState(rightPos, rightState.set(TYPE, ChestType.LEFT));
			}
		}

		return state;
	}

}
