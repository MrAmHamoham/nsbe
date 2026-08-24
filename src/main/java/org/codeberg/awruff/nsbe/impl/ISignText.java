package org.codeberg.awruff.nsbe.impl;

import net.minecraft.text.Text;

import java.util.List;

public interface ISignText {

	List<Text> nsbe$getWrappedText(Text line);

	void nsbe$putWrappedText(Text line, List<Text> wrapped);

	void nsbe$clearTextCache();
}
