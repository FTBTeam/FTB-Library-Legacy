package com.feed_the_beast.ftbl.lib;

import com.feed_the_beast.ftbl.api.EnumTeamStatus;
import net.minecraft.util.IStringSerializable;

public enum EnumTeamPrivacyLevel implements IStringSerializable
{
    EVERYONE("everyone", EnumTeamStatus.NONE),
    ALLIES("allies", EnumTeamStatus.ALLY),
    MEMBERS("members", EnumTeamStatus.MEMBER);

    /**
     * @author LatvianModder
     */
    public static final EnumTeamPrivacyLevel[] VALUES = values();
    public static final EnumNameMap<EnumTeamPrivacyLevel> NAME_MAP = new EnumNameMap<>(VALUES, false);

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