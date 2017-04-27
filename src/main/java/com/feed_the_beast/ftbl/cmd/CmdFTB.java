package com.feed_the_beast.ftbl.cmd;

import com.feed_the_beast.ftbl.api.IFTBLibPlugin;
import com.feed_the_beast.ftbl.cmd.team.CmdTeam;
import com.feed_the_beast.ftbl.lib.cmd.CmdTreeBase;
import com.feed_the_beast.ftbl.lib.internal.FTBLibIntegrationInternal;
import com.feed_the_beast.ftbl.lib.util.LMUtils;

/**
 * Created by LatvianModder on 08.06.2016.
 */
public class CmdFTB extends CmdTreeBase
{
    public CmdFTB(boolean dedi)
    {
        super("ftb");
        addSubcommand(new CmdReload());
        addSubcommand(new CmdReloadClient());
        addSubcommand(new CmdMySettings());
        addSubcommand(new CmdTeam());
        addSubcommand(new CmdPackMode());
        addSubcommand(new CmdNotify());
        addSubcommand(new CmdEditGamerules());
        addSubcommand(new CmdEditConfig());

        if(LMUtils.DEV_ENV)
        {
            addSubcommand(new CmdAddFakePlayer());
        }

        for(IFTBLibPlugin plugin : FTBLibIntegrationInternal.API.getAllPlugins())
        {
            plugin.registerFTBCommands(this, dedi);
        }

        setCustomPermissionPrefix("");
    }
}