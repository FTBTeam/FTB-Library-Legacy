package com.feed_the_beast.ftbl.cmd;

import com.feed_the_beast.ftbl.api.cmd.CommandLevel;
import com.feed_the_beast.ftbl.api.cmd.CommandSubLM;
import com.feed_the_beast.ftbl.api.events.RegisterAdminCommandsEvent;
import com.feed_the_beast.ftbl.config.FTBLibConfigCmd;
import com.feed_the_beast.ftbl.util.FTBLib;
import net.minecraftforge.common.MinecraftForge;

/**
 * Created by LatvianModder on 28.05.2016.
 */
public class CmdAdmin extends CommandSubLM
{
    public CmdAdmin()
    {
        super(FTBLibConfigCmd.name_admin.getAsString(), CommandLevel.OP);

        add(new CmdReload());

        if(FTBLib.DEV_ENV)
        {
            add(new CmdAddFakePlayer());
        }

        MinecraftForge.EVENT_BUS.post(new RegisterAdminCommandsEvent(this));
    }
}