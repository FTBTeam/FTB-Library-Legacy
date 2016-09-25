package com.feed_the_beast.ftbl.cmd;

import com.feed_the_beast.ftbl.api.cmd.CmdEditConfigBase;
import com.feed_the_beast.ftbl.api.config.IConfigContainer;
import com.feed_the_beast.ftbl.api_impl.ConfigManager;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraftforge.server.command.CommandTreeBase;

public class CmdEditConfig extends CommandTreeBase
{
    public static class CmdEditConfigFile extends CmdEditConfigBase
    {
        private IConfigContainer configContainer;

        public CmdEditConfigFile(String id, IConfigContainer c)
        {
            super(id);
            configContainer = c;
        }

        @Override
        public IConfigContainer getConfigContainer(ICommandSender sender) throws CommandException
        {
            return configContainer;
        }
    }

    public CmdEditConfig()
    {
        ConfigManager.INSTANCE.CONFIG_FILES.forEach((key, value) ->
        {
            if(value != ConfigManager.INSTANCE.CLIENT_CONFIG_FILE)
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