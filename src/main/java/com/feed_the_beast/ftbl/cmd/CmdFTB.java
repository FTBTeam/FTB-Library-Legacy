package com.feed_the_beast.ftbl.cmd;

import com.feed_the_beast.ftbl.api.events.RegisterFTBCommandsEvent;
import com.feed_the_beast.ftbl.cmd.team.CmdTeam;
import com.feed_the_beast.ftbl.lib.util.LMUtils;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.server.command.CommandTreeBase;

/**
 * Created by LatvianModder on 08.06.2016.
 */
public class CmdFTB extends CommandTreeBase
{
    public CmdFTB(boolean dedi)
    {
        addSubcommand(new CmdReload());
        addSubcommand(new CmdReloadClient());
        addSubcommand(new CmdMyServerSettings());
        addSubcommand(new CmdTeam());
        addSubcommand(new CmdPackMode());
        addSubcommand(new CmdNotify());
        addSubcommand(new CmdSetItemName());
        addSubcommand(new CmdHeal());
        addSubcommand(new CmdEditConfig());

        if(LMUtils.DEV_ENV)
        {
            addSubcommand(new CmdAddFakePlayer());
        }

        MinecraftForge.EVENT_BUS.post(new RegisterFTBCommandsEvent(this, dedi));
    }

    @Override
    public String getCommandName()
    {
        return "ftb";
    }

    @Override
    public int getRequiredPermissionLevel()
    {
        return 0;
    }

    @Override
    public boolean checkPermission(MinecraftServer server, ICommandSender sender)
    {
        return true;
    }

    @Override
    public String getCommandUsage(ICommandSender sender)
    {
        return "command.ftb.usage";
    }
}