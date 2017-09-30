package com.feed_the_beast.ftbl.lib;

import com.feed_the_beast.ftbl.api.ICustomName;
import com.feed_the_beast.ftbl.lib.gui.GuiIcons;
import com.feed_the_beast.ftbl.lib.icon.Icon;
import com.feed_the_beast.ftbl.lib.internal.FTBLibFinals;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.text.ITextComponent;

/**
 * @author LatvianModder
 */
public enum EnumPrivacyLevel implements IStringSerializable, ICustomName
{
	PUBLIC("public"),
	PRIVATE("private"),
	TEAM("team");

	public static final EnumPrivacyLevel[] VALUES = values();
	public static final LangKey ENUM_LANG_KEY = FTBLibFinals.lang("privacy");
	public static final NameMap<EnumPrivacyLevel> NAME_MAP = NameMap.create(PUBLIC, VALUES);

	private final String name;
	private final LangKey langKey;

	EnumPrivacyLevel(String n)
	{
		name = n;
		langKey = FTBLibFinals.lang("privacy." + name);
	}

	@Override
	public String getName()
	{
		return name;
	}

	@Override
	public ITextComponent getCustomDisplayName()
	{
		return langKey.textComponent();
	}

	public LangKey getLangKey()
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