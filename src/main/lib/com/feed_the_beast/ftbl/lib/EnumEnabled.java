package com.feed_the_beast.ftbl.lib;

public enum EnumEnabled
{
	ENABLED,
	DISABLED;

	public static final EnumNameMap<EnumEnabled> NAME_MAP = new EnumNameMap<>(values(), false);
	public static final EnumNameMap<EnumEnabled> NAME_MAP_WITH_NULL = new EnumNameMap<>(values(), true);
}