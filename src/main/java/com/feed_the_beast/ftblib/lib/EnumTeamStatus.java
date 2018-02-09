package com.feed_the_beast.ftblib.lib;

import com.feed_the_beast.ftblib.FTBLibLang;
import com.feed_the_beast.ftblib.lib.util.LangKey;
import com.feed_the_beast.ftblib.lib.util.StringUtils;
import com.feed_the_beast.ftblib.lib.util.misc.NameMap;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;

import java.util.Collection;
import java.util.LinkedHashSet;

/**
 * @author LatvianModder
 */
public enum EnumTeamStatus implements IStringSerializable, ICustomName
{
	ENEMY(-10, "enemy", TextFormatting.RED, true),
	NONE(0, "none", TextFormatting.WHITE, true),
	INVITED(10, "invited", TextFormatting.DARK_AQUA, true),
	ALLY(30, "ally", TextFormatting.DARK_GREEN, true),
	MEMBER(50, "member", TextFormatting.BLUE, false),
	MOD(80, "mod", TextFormatting.BLUE, true),
	OWNER(100, "owner", TextFormatting.GOLD, false);

	public static final EnumTeamStatus[] VALUES = values();
	public static final NameMap<EnumTeamStatus> NAME_MAP = NameMap.create(NONE, VALUES);
	public static final NameMap<EnumTeamStatus> NAME_MAP_PERMS = NameMap.create(ALLY, NONE, ALLY, MEMBER);
	public static final Collection<EnumTeamStatus> VALID_VALUES = new LinkedHashSet<>();

	static
	{
		for (EnumTeamStatus s : VALUES)
		{
			if (s.canBeSet)
			{
				VALID_VALUES.add(s);
			}
		}
	}

	private final String name;
	private final int status;
	private final TextFormatting color;
	private final LangKey langKey;
	private final boolean canBeSet;

	EnumTeamStatus(int s, String n, TextFormatting c, boolean cs)
	{
		name = n;
		status = s;
		color = c;
		langKey = FTBLibLang.get("lang.team_status." + name);
		canBeSet = cs;
	}

	@Override
	public String getName()
	{
		return name;
	}

	@Override
	public ITextComponent getCustomDisplayName()
	{
		return StringUtils.color(langKey.textComponent(null), color);
	}

	public int getStatus()
	{
		return status;
	}

	public TextFormatting getColor()
	{
		return color;
	}

	public LangKey getLangKey()
	{
		return langKey;
	}

	public boolean canBeSet()
	{
		return canBeSet;
	}

	public boolean isNone()
	{
		return this == NONE;
	}

	public boolean isEqualOrGreaterThan(EnumTeamStatus s)
	{
		return status >= s.status;
	}

	public String toString()
	{
		return getName();
	}
}