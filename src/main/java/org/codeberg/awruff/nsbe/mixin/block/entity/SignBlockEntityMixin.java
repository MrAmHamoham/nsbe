package org.codeberg.awruff.nsbe.mixin.block.entity;

import net.minecraft.block.entity.SignBlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import org.codeberg.awruff.nsbe.impl.ISignText;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(SignBlockEntity.class)
abstract class SignBlockEntityMixin implements ISignText {

	@Final
	@Shadow
	public Text[] lines;

	@Unique
	private final Text[] nsbe$cachedLines = new Text[4];
	@Unique
	@SuppressWarnings("rawtypes")
	private final List[] nsbe$wrappedLines = new List[4];

	@SuppressWarnings("unchecked")
	@Override
	public List<Text> nsbe$getWrappedText(Text line) {
		int row = nsbe$rowOf(line);

		if (row < 0 || this.nsbe$cachedLines[row] != line) {
			return null;
		}

		return (List<Text>) this.nsbe$wrappedLines[row];
	}

	@Override
	public void nsbe$putWrappedText(Text line, List<Text> wrapped) {
		int row = nsbe$rowOf(line);

		if (row < 0) {
			return;
		}

		this.nsbe$cachedLines[row] = line;
		this.nsbe$wrappedLines[row] = wrapped;
	}

	@Override
	public void nsbe$clearTextCache() {
		for (int row = 0; row < this.nsbe$cachedLines.length; row++) {
			this.nsbe$cachedLines[row] = null;
			this.nsbe$wrappedLines[row] = null;
		}
	}

	@Unique
	private int nsbe$rowOf(Text line) {
		for (int row = 0; row < this.lines.length && row < this.nsbe$cachedLines.length; row++) {
			if (this.lines[row] == line) {
				return row;
			}
		}

		return -1;
	}

	@Inject(method = "readNbt", at = @At("TAIL"))
	private void nsbe$resetTextCache(NbtCompound nbt, CallbackInfo ci) {
		nsbe$clearTextCache();
	}

}
