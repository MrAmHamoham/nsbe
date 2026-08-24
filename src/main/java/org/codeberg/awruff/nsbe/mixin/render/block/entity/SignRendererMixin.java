package org.codeberg.awruff.nsbe.mixin.render.block.entity;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.block.entity.SignBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.menu.SignEditScreen;
import net.minecraft.client.render.TextRenderer;
import net.minecraft.client.render.block.entity.SignRenderer;
import net.minecraft.client.render.model.block.entity.SignModel;
import net.minecraft.text.Text;
import org.codeberg.awruff.nsbe.impl.ISignText;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

import java.util.List;

@Mixin(SignRenderer.class)
abstract class SignRendererMixin {

	@WrapWithCondition(
		method = "render(Lnet/minecraft/block/entity/SignBlockEntity;DDDFI)V",
		at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/model/block/entity/SignModel;render()V")
	)
	private boolean nsbe$onlyWhileEditing(SignModel model) {
		return nsbe$isInEditScreen();
	}

	@WrapOperation(
		method = "render(Lnet/minecraft/block/entity/SignBlockEntity;DDDFI)V",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/client/render/TextRenderUtils;wrapText(Lnet/minecraft/text/Text;ILnet/minecraft/client/render/TextRenderer;ZZ)Ljava/util/List;"
		)
	)
	private List<Text> nsbe$cacheWrappedText(
		Text line, int maxWidth, TextRenderer textRenderer, boolean retainStyle, boolean firstLineOnly,
		Operation<List<Text>> original, @Local(argsOnly = true) SignBlockEntity sign
	) {
		ISignText cache = (ISignText) sign;
		List<Text> wrapped = cache.nsbe$getWrappedText(line);

		if (wrapped != null) {
			return wrapped;
		}

		wrapped = original.call(line, maxWidth, textRenderer, retainStyle, firstLineOnly);
		cache.nsbe$putWrappedText(line, wrapped);

		return wrapped;
	}

	@Unique
	private static boolean nsbe$isInEditScreen() {
		return Minecraft.getInstance().screen instanceof SignEditScreen;
	}

}
