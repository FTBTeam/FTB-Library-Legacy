package com.feed_the_beast.ftbl.lib.block;

import com.feed_the_beast.ftbl.lib.EnumNameMap;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;

public enum EnumHorizontalOffset implements IStringSerializable
{
	CENTER("center", new BlockPos(0, 0, 0)),
	NORTH("north", new BlockPos(0, 0, 1)),
	NORTH_EAST("north_east", new BlockPos(1, 0, 1)),
	EAST("east", new BlockPos(1, 0, 0)),
	SOUTH_EAST("south_east", new BlockPos(1, 0, -1)),
	SOUTH("south", new BlockPos(0, 0, -1)),
	SOUTH_WEST("south_west", new BlockPos(-1, 0, -1)),
	WEST("west", new BlockPos(-1, 0, 0)),
	NORTH_WEST("north_west", new BlockPos(-1, 0, 1));

	/**
	 * @author LatvianModder
	 */
	public static final EnumHorizontalOffset[] VALUES = values();
	private static final EnumHorizontalOffset[] OPPOSITES = {CENTER, SOUTH, SOUTH_WEST, WEST, NORTH_WEST, NORTH, NORTH_EAST, EAST, SOUTH_EAST};
	public static final EnumNameMap<EnumHorizontalOffset> NAME_MAP = new EnumNameMap<>(VALUES, false);

	private final String name;
	private final BlockPos offset;

	EnumHorizontalOffset(String n, BlockPos o)
	{
		name = n;
		offset = o;
	}

	@Override
	public String getName()
	{
		return name;
	}

	public BlockPos offset()
	{
		return offset;
	}

	public BlockPos offset(BlockPos pos)
	{
		return pos.add(offset);
	}

	public boolean isCenter()
	{
		return this == CENTER;
	}

	public EnumHorizontalOffset opposite()
	{
		return OPPOSITES[ordinal()];
	}
}