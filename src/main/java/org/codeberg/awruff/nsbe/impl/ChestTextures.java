package org.codeberg.awruff.nsbe.impl;

import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ChestTextures {
	private static final String NAMESPACE = "minecraft";
	private static final Map<String, String> CHRISTMAS_TEXTURES;

	static {
		Map<String, String> textures = new HashMap<String, String>();
		textures.put("entity/chest/normal", "entity/chest/christmas");
		textures.put("entity/chest/normal_double", "entity/chest/christmas_double");
		textures.put("entity/chest/trapped", "entity/chest/christmas");
		textures.put("entity/chest/trapped_double", "entity/chest/christmas_double");
		CHRISTMAS_TEXTURES = Collections.unmodifiableMap(textures);
	}

	private static boolean christmas;

	public static void refresh() {
		Calendar calendar = Calendar.getInstance();
		int day = calendar.get(Calendar.DAY_OF_MONTH);

		christmas = calendar.get(Calendar.MONTH) + 1 == 12 && day >= 24 && day <= 26;
	}

	public static String resolve(String texture) {
		if (!christmas || texture == null || texture.isEmpty()) {
			return texture;
		}

		int colon = texture.indexOf(':');
		String namespace = colon < 0 ? null : texture.substring(0, colon);

		if (namespace != null && !NAMESPACE.equals(namespace)) {
			return texture;
		}

		String swapped = CHRISTMAS_TEXTURES.get(colon < 0 ? texture : texture.substring(colon + 1));

		if (swapped == null) {
			return texture;
		}

		return namespace == null ? swapped : namespace + ':' + swapped;
	}
}
