package com.feed_the_beast.ftbl.api;

import com.feed_the_beast.ftbl.lib.LangKey;
import com.feed_the_beast.ftbl.lib.NameMap;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.text.TextFormatting;

import java.util.Collection;
import java.util.LinkedHashSet;

/**
 * @author LatvianModder
 */
public enum EnumTeamStatus implements IStringSerializable
{
	ENEMY(-10, "enemy", TextFormatting.RED, true),
	NONE(0, "none", TextFormatting.WHITE, true),
	REQUESTING_INVITE(5, "requesting_invite", TextFormatting.WHITE, false),
	INVITED(10, "invited", TextFormatting.DARK_AQUA, true),
	ALLY(30, "ally", TextFormatting.BLUE, true),
	MEMBER(50, "member", TextFormatting.GREEN, false),
	MOD(80, "mod", TextFormatting.DARK_PURPLE, true),
	OWNER(100, "owner", TextFormatting.GOLD, false);


	public static final EnumTeamStatus[] VALUES = values();
	public static final NameMap<EnumTeamStatus> NAME_MAP = NameMap.create(NONE, VALUES);
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
		langKey = LangKey.of("ftbl.lang.team_status." + name);
		canBeSet = cs;
	}

	@Override
	public String getName()
	{
		return name;
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