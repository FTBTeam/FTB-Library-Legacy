package com.feed_the_beast.ftbl.lib;

import com.feed_the_beast.ftbl.api.EnumTeamStatus;
import net.minecraft.util.IStringSerializable;

/**
 * @author LatvianModder
 */
public enum EnumTeamPrivacyLevel implements IStringSerializable
{
	EVERYONE("everyone", EnumTeamStatus.NONE),
	ALLIES("allies", EnumTeamStatus.ALLY),
	MEMBERS("members", EnumTeamStatus.MEMBER);

	public static final EnumTeamPrivacyLevel[] VALUES = values();
	public static final NameMap<EnumTeamPrivacyLevel> NAME_MAP = NameMap.create(EVERYONE, VALUES);

	private final String name;
	private final EnumTeamStatus status;

	EnumTeamPrivacyLevel(String n, EnumTeamStatus s)
	{
		name = n;
		status = s;
	}

	@Override
	public String getName()
	{
		return name;
	}

	public EnumTeamStatus getRequiredStatus()
	{
		return status;
	}
}