package com.feed_the_beast.ftbl.config;

import com.feed_the_beast.ftbl.api.config.ConfigEntryBool;
import com.feed_the_beast.ftbl.api.config.ConfigEntryString;
import latmod.lib.annotations.Info;

public class FTBLibConfigCmd
{
    @Info("A new layout for /list command")
    public static final ConfigEntryBool override_list = new ConfigEntryBool(true);

    @Info("Can fix some /help problems")
    public static final ConfigEntryBool override_help = new ConfigEntryBool(true);

    public static final ConfigEntryString name_admin = new ConfigEntryString("admin");

    public static final ConfigEntryBool edit_config = new ConfigEntryBool(true);
    public static final ConfigEntryBool set_item_name = new ConfigEntryBool(true);
    public static final ConfigEntryBool heal = new ConfigEntryBool(true);
}