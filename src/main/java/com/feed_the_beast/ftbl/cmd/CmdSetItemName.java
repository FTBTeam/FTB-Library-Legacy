package com.feed_the_beast.ftbl.cmd;

import com.feed_the_beast.ftbl.lib.cmd.CommandLM;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;

public class CmdSetItemName extends CommandLM
{
    @Override
    public String getCommandName()
    {
        return "set_item_name";
    }

    @Override
    public int getRequiredPermissionLevel()
    {
        return 0;
    }

    @Override
    public String getCommandUsage(ICommandSender ics)
    {
        return '/' + getCommandName() + " <name...>";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender ics, String[] args) throws CommandException
    {
        checkArgs(args, 1, "<player>");
        EntityPlayerMP ep = getCommandSenderAsPlayer(ics);

        if(ep.inventory.getCurrentItem() != null)
        {
            ep.inventory.getCurrentItem().setStackDisplayName(String.join("", args));
            ep.openContainer.detectAndSendChanges();
            ics.addChatMessage(new TextComponentString("Item name set to '" + ep.inventory.getCurrentItem().getDisplayName() + "'!"));
        }
    }
}