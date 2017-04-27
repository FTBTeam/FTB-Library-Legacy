package com.feed_the_beast.ftbl.cmd.client;

import com.feed_the_beast.ftbl.FTBLibMod;
import com.feed_the_beast.ftbl.lib.cmd.CmdBase;
import com.feed_the_beast.ftbl.lib.gui.misc.GuiEditConfig;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;

/**
 * @author LatvianModder
 */
public class CmdClientConfig extends CmdBase
{
    public CmdClientConfig()
    {
        super("client_config", Level.ALL);
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
    {
        new GuiEditConfig(null, FTBLibMod.PROXY.getClientConfig()).openGui();
    }
}