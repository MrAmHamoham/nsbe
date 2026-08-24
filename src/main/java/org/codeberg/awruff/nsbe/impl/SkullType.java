package org.codeberg.awruff.nsbe.impl;

import net.minecraft.util.StringSerializable;

public enum SkullType implements StringSerializable {
	SKELETON("skeleton"),
	WITHER("wither"),
	ZOMBIE("zombie"),
	PLAYER("player"),
	CREEPER("creeper");

	public static final int PLAYER_ID = 3;

	private static final SkullType[] BY_ID = values();

	private final String name;

	SkullType(final String name) {
		this.name = name;
	}

	public static SkullType byId(int id) {
		return BY_ID[id < 0 || id >= BY_ID.length ? 0 : id];
	}

	@Override
	public String serializeToString() {
		return this.name;
	}
}
