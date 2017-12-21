package com.feed_the_beast.ftblib.lib.item;

import net.minecraft.util.IStringSerializable;

/**
 * @author LatvianModder
 */
public enum ToolType implements IStringSerializable
{
	PICK("pick"),
	SHOVEL("shovel"),
	AXE("axe"),
	WRENCH("wrench");

	private String name;

	ToolType(String n)
	{
		name = n;
	}

	@Override
	public String getName()
	{
		return name;
	}
}