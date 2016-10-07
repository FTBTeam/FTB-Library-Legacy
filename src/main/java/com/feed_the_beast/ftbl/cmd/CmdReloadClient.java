package com.feed_the_beast.ftbl.cmd;

import com.feed_the_beast.ftbl.api.events.ReloadType;
import com.feed_the_beast.ftbl.api_impl.FTBLibAPI_Impl;
import com.feed_the_beast.ftbl.lib.cmd.CommandLM;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;

public class CmdReloadClient extends CommandLM
{
    @Override
    public String getCommandName()
    {
        return "reload_client";
    }

    @Override
    public int getRequiredPermissionLevel()
    {
        return 0;
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
    {
        FTBLibAPI_Impl.INSTANCE.reload(sender, ReloadType.CLIENT_ONLY);
    }
}