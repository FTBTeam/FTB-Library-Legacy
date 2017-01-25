package com.feed_the_beast.ftbl.cmd;

import com.feed_the_beast.ftbl.api.EnumReloadType;
import com.feed_the_beast.ftbl.api_impl.PackModes;
import com.feed_the_beast.ftbl.api_impl.SharedServerData;
import com.feed_the_beast.ftbl.lib.cmd.CommandLM;
import com.feed_the_beast.ftbl.lib.internal.FTBLibIntegrationInternal;
import com.feed_the_beast.ftbl.lib.internal.FTBLibLang;
import com.feed_the_beast.ftbl.lib.util.LMStringUtils;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.server.command.CommandTreeBase;

import javax.annotation.Nullable;
import java.util.List;

public class CmdPackMode extends CommandTreeBase
{
    public static class CmdSet extends CommandLM
    {
        @Override
        public String getCommandName()
        {
            return "set";
        }

        @Override
        public String getCommandUsage(ICommandSender ics)
        {
            return '/' + getCommandName() + " <modeID>";
        }

        @Override
        public List<String> getTabCompletionOptions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos pos)
        {
            if(args.length == 1)
            {
                return getListOfStringsMatchingLastWord(args, PackModes.INSTANCE.getModes());
            }

            return super.getTabCompletionOptions(server, sender, args, pos);
        }

        @Override
        public void execute(MinecraftServer server, ICommandSender ics, String[] args) throws CommandException
        {
            checkArgs(args, 1, "<modeID>");

            ITextComponent c;

            int i = SharedServerData.INSTANCE.setMode(args[0]);

            if(i == 1)
            {
                c = FTBLibLang.MODE_NOT_FOUND.textComponent();
                c.getStyle().setColor(TextFormatting.RED);
            }
            else if(i == 2)
            {
                c = FTBLibLang.MODE_ALREADY_SET.textComponent();
                c.getStyle().setColor(TextFormatting.RED);
            }
            else
            {
                c = FTBLibLang.MODE_LOADED.textComponent(args[0]);
                c.getStyle().setColor(TextFormatting.GREEN);
                FTBLibIntegrationInternal.API.reload(ics, EnumReloadType.SERVER_AND_CLIENT);
            }

            ics.addChatMessage(c);
        }
    }

    public static class CmdGet extends CommandLM
    {
        @Override
        public String getCommandName()
        {
            return "get";
        }

        @Override
        public int getRequiredPermissionLevel()
        {
            return 0;
        }

        @Override
        public void execute(MinecraftServer server, ICommandSender ics, String[] args) throws CommandException
        {
            ITextComponent c = FTBLibLang.MODE_CURRENT.textComponent(SharedServerData.INSTANCE.getPackMode().getName());
            c.getStyle().setColor(TextFormatting.AQUA);
            ics.addChatMessage(c);
        }
    }

    public static class CmdList extends CommandLM
    {
        @Override
        public String getCommandName()
        {
            return "list";
        }

        @Override
        public int getRequiredPermissionLevel()
        {
            return 0;
        }

        @Override
        public void execute(MinecraftServer server, ICommandSender ics, String[] args) throws CommandException
        {
            ITextComponent c = FTBLibLang.MODE_LIST.textComponent(LMStringUtils.strip(PackModes.INSTANCE.getModes()));
            c.getStyle().setColor(TextFormatting.AQUA);
            ics.addChatMessage(c);
        }
    }

    public CmdPackMode()
    {
        addSubcommand(new CmdSet());
        addSubcommand(new CmdGet());
        addSubcommand(new CmdList());
    }

    @Override
    public String getCommandName()
    {
        return "packmode";
    }

    @Override
    public int getRequiredPermissionLevel()
    {
        return 0;
    }

    @Override
    public boolean checkPermission(MinecraftServer server, ICommandSender ics)
    {
        return true;
    }

    @Override
    public String getCommandUsage(ICommandSender sender)
    {
        return "command.ftb.packmode.usage";
    }
}