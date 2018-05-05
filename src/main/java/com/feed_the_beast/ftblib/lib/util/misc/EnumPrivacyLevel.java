package com.feed_the_beast.ftblib.lib.util.misc;

import com.feed_the_beast.ftblib.lib.gui.GuiIcons;
import com.feed_the_beast.ftblib.lib.icon.Icon;
import net.minecraft.util.IStringSerializable;

/**
 * @author LatvianModder
 */
public enum EnumPrivacyLevel implements IStringSerializable
{
	PUBLIC("public"),
	PRIVATE("private"),
	TEAM("team");

	public static final EnumPrivacyLevel[] VALUES = values();
	public static final String ENUM_LANG_KEY = "ftblib.privacy";
	public static final NameMap<EnumPrivacyLevel> NAME_MAP = NameMap.create(PUBLIC, VALUES);

	private final String name;
	private final String langKey;

	EnumPrivacyLevel(String n)
	{
		name = n;
		langKey = "ftblib.privacy." + name;
	}

	@Override
	public String getName()
	{
		return name;
	}

	public String getLangKey()
	{
		return langKey;
	}

	public Icon getIcon()
	{
		switch (this)
		{
			case PRIVATE:
				return GuiIcons.SECURITY_PRIVATE;
			case TEAM:
				return GuiIcons.SECURITY_TEAM;
			default:
				return GuiIcons.SECURITY_PUBLIC;
		}
	}

	public boolean isPublic()
	{
		return this == PUBLIC;
	}
}