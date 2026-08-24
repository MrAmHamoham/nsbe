package org.codeberg.awruff.nsbe.mixin.render.texture;

import net.minecraft.client.render.texture.TextureAtlasSprite;
import net.minecraft.client.resource.metadata.AnimationMetadata;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

@Mixin(TextureAtlasSprite.class)
abstract class TextureAtlasSpriteMixin {

	@Unique
	private static final String ENTITY_PATH = "entity/";

	@Shadow
	protected List<int[][]> frames;
	@Shadow
	protected int width;
	@Shadow
	protected int height;
	@Shadow
	protected int activeFrame;
	@Shadow
	protected int frameTicks;
	@Shadow
	private AnimationMetadata animation;

	@Shadow
	public abstract String getName();

	@Shadow
	public abstract void setFrames(List<int[][]> frames);

	// FIXME: Better (more simple) way to do this than use an overwrite?
	@Inject(method = "load", at = @At("HEAD"), cancellable = true)
	private void nsbe$loadEntityTexture(BufferedImage[] images, AnimationMetadata animation, CallbackInfo ci) {
		if (animation != null || images.length == 0 || images[0] == null) {
			return;
		}

		int width = images[0].getWidth();
		int height = images[0].getHeight();

		if (width == height || !this.getName().contains(ENTITY_PATH)) {
			return;
		}

		this.animation = null;
		this.setFrames(new ArrayList<>());
		this.activeFrame = 0;
		this.frameTicks = 0;

		this.width = width;
		this.height = height;

		int[][] mipmaps = new int[images.length][];

		for (int level = 0; level < images.length; level++) {
			BufferedImage image = images[level];

			if (image == null) {
				continue;
			}

			if (level > 0 && (image.getWidth() != width >> level || image.getHeight() != height >> level)) {
				throw new RuntimeException(String.format(
					"Unable to load miplevel: %d, image is size: %dx%d, expected %dx%d",
					level, image.getWidth(), image.getHeight(), width >> level, height >> level
				));
			}

			mipmaps[level] = new int[image.getWidth() * image.getHeight()];
			image.getRGB(0, 0, image.getWidth(), image.getHeight(), mipmaps[level], 0, image.getWidth());
		}

		this.frames.add(mipmaps);
		ci.cancel();
	}
}
