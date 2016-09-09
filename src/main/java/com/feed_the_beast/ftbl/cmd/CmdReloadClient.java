package com.feed_the_beast.ftbl.cmd;

import com.feed_the_beast.ftbl.api.FTBLibAPI;
import com.feed_the_beast.ftbl.api.cmd.CommandLM;
import com.feed_the_beast.ftbl.api.events.ReloadType;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;

public class CmdReloadClient extends CommandLM
{
    public CmdReloadClient()
    {
        super("reload_client");
    }

    @Override
    public int getRequiredPermissionLevel()
    {
        return 0;
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
    {
        FTBLibAPI.get().reload(sender, ReloadType.CLIENT_ONLY);
    }
}