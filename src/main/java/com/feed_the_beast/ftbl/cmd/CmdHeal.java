package com.feed_the_beast.ftbl.cmd;

import com.feed_the_beast.ftbl.lib.cmd.CommandLM;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;

public class CmdHeal extends CommandLM
{
    @Override
    public String getCommandName()
    {
        return "heal";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
    {
        EntityPlayerMP ep = getCommandSenderAsPlayer(sender);
        ep.setHealth(ep.getMaxHealth());
        ep.getFoodStats().addStats(40, 40F);
        ep.extinguish();
    }
}