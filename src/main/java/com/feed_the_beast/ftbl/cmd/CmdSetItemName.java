package com.feed_the_beast.ftbl.cmd;

import com.feed_the_beast.ftbl.lib.cmd.CmdBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;

public class CmdSetItemName extends CmdBase
{
    @Override
    public String getName()
    {
        return "set_item_name";
    }

    @Override
    public int getRequiredPermissionLevel()
    {
        return 0;
    }

    @Override
    public String getUsage(ICommandSender ics)
    {
        return '/' + getName() + " <name...>";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
    {
        checkArgs(args, 1, "<player>");
        EntityPlayerMP ep = getCommandSenderAsPlayer(sender);

        if(ep.inventory.getCurrentItem() != null)
        {
            ep.inventory.getCurrentItem().setStackDisplayName(String.join("", args));
            ep.openContainer.detectAndSendChanges();
            sender.sendMessage(new TextComponentString("Item name set to '" + ep.inventory.getCurrentItem().getDisplayName() + "'!"));
        }
    }
}