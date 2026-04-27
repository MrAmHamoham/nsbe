package org.codeberg.awruff.nsbe.impl;

import net.minecraft.util.StringSerializable;

public enum ChestType implements StringSerializable {
	SINGLE("single"),
	LEFT("left"),
	RIGHT("right");

	private final String name;

	ChestType(final String name) {
		this.name = name;
	}

	@Override
	public String serializeToString() {
		return this.name;
	}
}

