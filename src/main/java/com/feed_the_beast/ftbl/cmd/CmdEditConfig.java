package com.feed_the_beast.ftbl.cmd;

import com.feed_the_beast.ftbl.FTBLibMod;
import com.feed_the_beast.ftbl.FTBLibModCommon;
import com.feed_the_beast.ftbl.api.config.IConfigContainer;
import com.feed_the_beast.ftbl.lib.cmd.CmdEditConfigBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraftforge.server.command.CommandTreeBase;

public class CmdEditConfig extends CommandTreeBase
{
    public static class CmdEditConfigFile extends CmdEditConfigBase
    {
        private String commandName;
        private IConfigContainer configContainer;

        public CmdEditConfigFile(String id, IConfigContainer c)
        {
            commandName = id;
            configContainer = c;
        }

        @Override
        public String getCommandName()
        {
            return commandName;
        }

        @Override
        public IConfigContainer getConfigContainer(ICommandSender sender) throws CommandException
        {
            return configContainer;
        }
    }

    public CmdEditConfig()
    {
        FTBLibModCommon.CONFIG_FILES.forEach((key, value) ->
        {
            if(value != FTBLibMod.PROXY.getClientConfig())
            {
                addSubcommand(new CmdEditConfigFile(key, value));
            }
        });
    }

    @Override
    public String getCommandName()
    {
        return "edit_config";
    }

    @Override
    public String getCommandUsage(ICommandSender sender)
    {
        return "command.ftb.edit_config.usage";
    }
}