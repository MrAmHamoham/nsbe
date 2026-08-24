package org.codeberg.awruff.nsbe.mixin.block;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.block.Block;
import net.minecraft.block.BlockWithBlockEntity;
import net.minecraft.block.ChestBlock;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.StateDefinition;
import net.minecraft.block.state.property.DirectionProperty;
import net.minecraft.block.state.property.EnumProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.WorldView;
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

	@ModifyExpressionValue(
		method = "<init>",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/block/state/BlockState;set(Lnet/minecraft/block/state/property/Property;Ljava/lang/Comparable;)Lnet/minecraft/block/state/BlockState;"
		)
	)
	private BlockState nsbe$setDefaults(BlockState state) {
		return state.set(TYPE, ChestType.SINGLE);
	}

	@ModifyReturnValue(
		method = "createStateDefinition",
		at = @At("RETURN")
	)
	private StateDefinition nsbe$addProperties(StateDefinition original) {
		return new StateDefinition((ChestBlock) (Object) this, FACING, TYPE);
	}

	@Override
	public BlockState resolveVirtualProperties(BlockState state, WorldView world, BlockPos pos) {
		Block self = state.getBlock();
		Direction facing = state.get(FACING);

		Direction clockwise = facing.clockwiseY();
		Direction counterClockwise = facing.counterClockwiseY();

		Direction negative = clockwise.getAxisDirection() == Direction.AxisDirection.NEGATIVE ? clockwise : counterClockwise;
		Direction positive = negative.getOpposite();

		if (nsbe$isSameChest(world, pos.offset(negative), self)) {
			if (nsbe$isSameChest(world, pos.offset(negative, 2), self)) {
				return state.set(TYPE, ChestType.SINGLE);
			}

			return state.set(TYPE, negative == clockwise ? ChestType.LEFT : ChestType.RIGHT);
		}

		if (nsbe$isSameChest(world, pos.offset(positive), self)) {
			return state.set(TYPE, positive == clockwise ? ChestType.LEFT : ChestType.RIGHT);
		}

		return state.set(TYPE, ChestType.SINGLE);
	}

	@Unique
	private static boolean nsbe$isSameChest(WorldView world, BlockPos pos, Block self) {
		return world.getBlockState(pos).getBlock() == self;
	}
}
