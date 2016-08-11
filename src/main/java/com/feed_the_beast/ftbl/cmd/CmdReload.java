package com.feed_the_beast.ftbl.cmd;

import com.feed_the_beast.ftbl.api.FTBLibAPI;
import com.feed_the_beast.ftbl.api.cmd.CommandLM;
import com.feed_the_beast.ftbl.util.ReloadType;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;

import javax.annotation.Nonnull;

public class CmdReload extends CommandLM
{
    public CmdReload()
    {
        super("reload");
    }

    @Override
    public void execute(@Nonnull MinecraftServer server, @Nonnull ICommandSender ics, @Nonnull String[] args) throws CommandException
    {
        FTBLibAPI.INSTANCE.reload(ics, ReloadType.SERVER_ONLY_NOTIFY_CLIENT, false);
    }
}