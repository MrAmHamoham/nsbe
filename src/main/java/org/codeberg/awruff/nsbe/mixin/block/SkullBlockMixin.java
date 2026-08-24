package org.codeberg.awruff.nsbe.mixin.block;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockWithBlockEntity;
import net.minecraft.block.SkullBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.SkullBlockEntity;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.StateDefinition;
import net.minecraft.block.state.property.BooleanProperty;
import net.minecraft.block.state.property.DirectionProperty;
import net.minecraft.block.state.property.EnumProperty;
import net.minecraft.block.state.property.IntegerProperty;
import net.minecraft.client.render.block.BlockLayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.WorldView;
import org.codeberg.awruff.nsbe.impl.SkullType;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(SkullBlock.class)
abstract class SkullBlockMixin extends BlockWithBlockEntity {
	@Unique
	private static final EnumProperty<SkullType> TYPE = EnumProperty.of("type", SkullType.class);
	@Unique
	private static final IntegerProperty ROTATION = IntegerProperty.of("rotation", 0, 15);

	@Shadow
	@Final
	public static DirectionProperty FACING;
	@Shadow
	@Final
	public static BooleanProperty NODROP;

	private SkullBlockMixin(Material material) {
		super(material);
	}

	@ModifyExpressionValue(
		method = "<init>",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/block/state/BlockState;set(Lnet/minecraft/block/state/property/Property;Ljava/lang/Comparable;)Lnet/minecraft/block/state/BlockState;",
			ordinal = 1
		)
	)
	private BlockState nsbe$setDefaults(BlockState state) {
		return state
			.set(TYPE, SkullType.SKELETON)
			.set(ROTATION, 0);
	}

	@ModifyReturnValue(
		method = "createStateDefinition",
		at = @At("RETURN")
	)
	private StateDefinition nsbe$addProperties(StateDefinition original) {
		return new StateDefinition((SkullBlock) (Object) this, FACING, NODROP, TYPE, ROTATION);
	}

	@Environment(EnvType.CLIENT)
	@Override
	public BlockLayer getRenderLayer() {
		return BlockLayer.CUTOUT;
	}

	@Override
	public BlockState resolveVirtualProperties(BlockState state, WorldView world, BlockPos pos) {
		BlockEntity blockEntity = world.getBlockEntity(pos);

		if (!(blockEntity instanceof SkullBlockEntity)) {
			return state;
		}

		SkullBlockEntity skull = (SkullBlockEntity) blockEntity;
		boolean floor = state.get(FACING) == Direction.UP;

		return state
			.set(TYPE, SkullType.byId(skull.getType()))
			.set(ROTATION, floor ? skull.getRotation() & 15 : 0);
	}
}
