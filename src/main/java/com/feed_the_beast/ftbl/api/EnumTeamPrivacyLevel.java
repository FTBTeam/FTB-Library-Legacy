package com.feed_the_beast.ftbl.api;

import com.feed_the_beast.ftbl.api.config.EnumNameMap;

/**
 * Created by LatvianModder on 02.06.2016.
 */
public enum EnumTeamPrivacyLevel
{
    EVERYONE,
    ALLIES,
    MEMBERS;

    public static final EnumNameMap<EnumTeamPrivacyLevel> NAME_MAP = new EnumNameMap<>(false, EnumTeamPrivacyLevel.values());
}
