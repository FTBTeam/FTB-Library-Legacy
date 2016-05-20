package com.feed_the_beast.ftbl.cmd;

import com.feed_the_beast.ftbl.FTBLibLang;
import com.feed_the_beast.ftbl.api.ForgeWorldMP;
import com.feed_the_beast.ftbl.api.PackModes;
import com.feed_the_beast.ftbl.api.cmd.CommandLM;
import com.feed_the_beast.ftbl.api.cmd.CommandLevel;
import com.feed_the_beast.ftbl.api.cmd.CommandSubLM;
import com.feed_the_beast.ftbl.util.FTBLib;
import com.feed_the_beast.ftbl.util.ReloadType;
import latmod.lib.LMStringUtils;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;

import java.util.List;

public class CmdMode extends CommandSubLM
{
    public static class CmdSet extends CommandLM
    {
        public CmdSet(String s)
        {
            super(s, CommandLevel.OP);
        }

        @Override
        public String getCommandUsage(ICommandSender ics)
        {
            return '/' + commandName + " <modeID>";
        }

        @Override
        public List<String> getTabCompletionOptions(MinecraftServer server, ICommandSender ics, String[] args, BlockPos pos)
        {
            if(args.length == 1)
            {
                return getListOfStringsMatchingLastWord(args, PackModes.instance().getModes());
            }

            return super.getTabCompletionOptions(server, ics, args, pos);
        }

        @Override
        public void execute(MinecraftServer server, ICommandSender ics, String[] args) throws CommandException
        {
            if(args.length == 0)
            {
                throw FTBLibLang.raw.commandError(getCommandUsage(ics));
            }

            ITextComponent c;

            int i = ForgeWorldMP.inst.setMode(args[0]);

            if(i == 1)
            {
                c = FTBLibLang.mode_not_found.textComponent();
                c.getStyle().setColor(TextFormatting.RED);
            }
            else if(i == 2)
            {
                c = FTBLibLang.mode_already_set.textComponent();
                c.getStyle().setColor(TextFormatting.RED);
            }
            else
            {
                c = FTBLibLang.mode_loaded.textComponent(args[0]);
                c.getStyle().setColor(TextFormatting.GREEN);
                FTBLib.reload(ics, ReloadType.SERVER_AND_CLIENT, true);
            }

            ics.addChatMessage(c);
        }
    }

    public static class CmdGet extends CommandLM
    {
        public CmdGet(String s)
        {
            super(s, CommandLevel.OP);
        }

        @Override
        public void execute(MinecraftServer server, ICommandSender ics, String[] args) throws CommandException
        {
            ITextComponent c = FTBLibLang.mode_current.textComponent(ForgeWorldMP.inst.getMode().getID());
            c.getStyle().setColor(TextFormatting.AQUA);
            ics.addChatMessage(c);
        }
    }

    public static class CmdList extends CommandLM
    {
        public CmdList(String s)
        {
            super(s, CommandLevel.OP);
        }

        @Override
        public void execute(MinecraftServer server, ICommandSender ics, String[] args) throws CommandException
        {
            ITextComponent c = FTBLibLang.mode_list.textComponent(LMStringUtils.strip(PackModes.instance().getModes()));
            c.getStyle().setColor(TextFormatting.AQUA);
            ics.addChatMessage(c);
        }
    }

    public CmdMode()
    {
        super("ftb_mode", CommandLevel.OP);
        add(new CmdSet("set"));
        add(new CmdGet("getMode"));
        add(new CmdList("list"));
    }
}