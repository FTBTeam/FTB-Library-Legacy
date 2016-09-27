package com.feed_the_beast.ftbl.cmd;

import com.feed_the_beast.ftbl.api.config.IConfigContainer;
import com.feed_the_beast.ftbl.api_impl.FTBLibRegistries;
import com.feed_the_beast.ftbl.lib.cmd.CmdEditConfigBase;
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
        FTBLibRegistries.INSTANCE.CONFIG_FILES.forEach((key, value) ->
        {
            if(value != FTBLibRegistries.INSTANCE.CLIENT_CONFIG)
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