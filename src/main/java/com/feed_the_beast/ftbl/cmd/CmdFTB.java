package com.feed_the_beast.ftbl.cmd;

import com.feed_the_beast.ftbl.api.cmd.CommandTreeBase;
import com.feed_the_beast.ftbl.api.events.RegisterFTBCommandsEvent;
import com.feed_the_beast.ftbl.cmd.team.CmdTeam;
import com.latmod.lib.util.LMUtils;
import net.minecraftforge.common.MinecraftForge;

/**
 * Created by LatvianModder on 08.06.2016.
 */
public class CmdFTB extends CommandTreeBase
{
    public CmdFTB(boolean dedi)
    {
        super("ftb");

        add(new CmdReload());
        add(new CmdReloadClient());
        add(new CmdMyServerSettings());
        add(new CmdTeam());
        add(new CmdPackMode());
        add(new CmdNotify());
        add(new CmdSetItemName());
        add(new CmdHeal());
        add(new CmdEditConfig());

        if(LMUtils.DEV_ENV)
        {
            add(new CmdAddFakePlayer());
        }

        MinecraftForge.EVENT_BUS.post(new RegisterFTBCommandsEvent(this, dedi));
    }

    @Override
    public int getRequiredPermissionLevel()
    {
        return 0;
    }
}