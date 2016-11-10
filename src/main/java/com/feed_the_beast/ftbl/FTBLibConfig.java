package com.feed_the_beast.ftbl;

import com.feed_the_beast.ftbl.api.RegistryObject;
import com.feed_the_beast.ftbl.api.config.ConfigValue;
import com.feed_the_beast.ftbl.api.config.IConfigFileProvider;
import com.feed_the_beast.ftbl.lib.config.PropertyBool;
import com.feed_the_beast.ftbl.lib.internal.FTBLibFinals;
import com.feed_the_beast.ftbl.lib.util.LMUtils;

import java.io.File;

/**
 * Created by LatvianModder on 28.09.2016.
 */
public class FTBLibConfig
{
    @RegistryObject(FTBLibFinals.MOD_ID)
    public static final IConfigFileProvider FILE = () -> new File(LMUtils.folderLocal, "ftbl.json");

    @ConfigValue(id = "teams.autocreate_on_login", file = FTBLibFinals.MOD_ID)
    public static final PropertyBool AUTOCREATE_TEAMS = new PropertyBool(true);

    @ConfigValue(id = "command.use_ftb_prefix", file = FTBLibFinals.MOD_ID)
    public static final PropertyBool USE_FTB_COMMAND_PREFIX = new PropertyBool(true);
}