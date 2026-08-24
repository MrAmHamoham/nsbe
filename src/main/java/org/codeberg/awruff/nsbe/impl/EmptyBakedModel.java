package org.codeberg.awruff.nsbe.impl;

import net.minecraft.client.render.model.block.ModelTransformations;
import net.minecraft.client.render.texture.TextureAtlasSprite;
import net.minecraft.client.resource.model.BakedModel;
import net.minecraft.client.resource.model.BakedQuad;
import net.minecraft.util.math.Direction;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class EmptyBakedModel implements BakedModel {

	private static final Map<BakedModel, BakedModel> CACHE = new ConcurrentHashMap<>();

	private final BakedModel original;

	private EmptyBakedModel(BakedModel original) {
		this.original = original;
	}

	public static BakedModel of(BakedModel original) {
		return CACHE.computeIfAbsent(original, EmptyBakedModel::new);
	}

	public static void clearCache() {
		CACHE.clear();
	}

	@Override
	public List<BakedQuad> getQuads(Direction face) {
		return Collections.emptyList();
	}

	@Override
	public List<BakedQuad> getQuads() {
		return Collections.emptyList();
	}

	@Override
	public boolean useAmbientOcclusion() {
		return this.original.useAmbientOcclusion();
	}

	@Override
	public boolean isGui3d() {
		return this.original.isGui3d();
	}

	@Override
	public boolean isCustomRenderer() {
		return this.original.isCustomRenderer();
	}

	@Override
	public TextureAtlasSprite getParticleIcon() {
		return this.original.getParticleIcon();
	}

	@Override
	public ModelTransformations getTransformations() {
		return this.original.getTransformations();
	}
}
