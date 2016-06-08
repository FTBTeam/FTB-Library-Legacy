package com.feed_the_beast.ftbl.config;

import com.feed_the_beast.ftbl.api.config.ConfigEntryBool;
import latmod.lib.annotations.Info;

public class FTBLibConfigCmd
{
    @Info("A new layout for /list command")
    public static final ConfigEntryBool override_list = new ConfigEntryBool(true);

    @Info("Can fix some /help problems")
    public static final ConfigEntryBool override_help = new ConfigEntryBool(true);
}