package com.feed_the_beast.ftbl.cmd;

import com.feed_the_beast.ftbl.api.cmd.CmdEditConfigBase;
import com.feed_the_beast.ftbl.api.config.IConfigContainer;
import com.feed_the_beast.ftbl.api_impl.config.ConfigManager;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;

public class CmdEditConfig extends CmdEditConfigBase
{
    public CmdEditConfig()
    {
        super("edit_config");
    }

    @Override
    public IConfigContainer getConfigContainer(ICommandSender sender) throws CommandException
    {
        return ConfigManager.INSTANCE.CONFIG_CONTAINER;
    }
}