package com.feed_the_beast.ftbl.api.config;

//@SideOnly(Side.CLIENT)
public interface IConfigProvider
{
    String getGroupTitle(ConfigGroup g);

    String getEntryTitle(ConfigEntry e);

    ConfigGroup getConfigGroup();

    void save();
}