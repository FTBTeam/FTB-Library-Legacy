package com.feed_the_beast.ftbl.cmd;

import com.feed_the_beast.ftbl.api.cmd.CommandLM;
import com.feed_the_beast.ftbl.api.cmd.CommandLevel;
import latmod.lib.LMStringUtils;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;

public class CmdSetItemName extends CommandLM
{
    public CmdSetItemName()
    { super("set_item_name", CommandLevel.OP); }
    
    @Override
    public String getCommandUsage(ICommandSender ics)
    { return '/' + commandName + " <name...>"; }
    
    @Override
    public void execute(MinecraftServer server, ICommandSender ics, String[] args) throws CommandException
    {
        checkArgs(args, 1);
        EntityPlayerMP ep = getCommandSenderAsPlayer(ics);
        
        if(ep.inventory.getCurrentItem() != null)
        {
            ep.inventory.getCurrentItem().setStackDisplayName(LMStringUtils.unsplit(args, " "));
            ep.openContainer.detectAndSendChanges();
            ics.addChatMessage(new TextComponentString("Item name set to '" + ep.inventory.getCurrentItem().getDisplayName() + "'!"));
        }
    }
}