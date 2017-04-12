package com.feed_the_beast.ftbl.lib;

import com.feed_the_beast.ftbl.lib.util.ServerUtils;
import net.minecraft.command.CommandResultStats;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

public enum BroadcastSender implements ICommandSender
{
    INSTANCE;

    @Override
    public String getName()
    {
        return "[Server]";
    }

    @Override
    public ITextComponent getDisplayName()
    {
        return new TextComponentString(getName());
    }

    @Override
    public void sendMessage(ITextComponent component)
    {
        ServerUtils.getServer().getPlayerList().sendChatMsgImpl(component, true);
    }

    @Override
    public boolean canUseCommand(int permLevel, String commandName)
    {
        return true;
    }

    @Override
    public BlockPos getPosition()
    {
        return ServerUtils.getServerWorld().getSpawnCoordinate();
    }

    @Override
    public Vec3d getPositionVector()
    {
        return new Vec3d(getPosition());
    }

    @Override
    public World getEntityWorld()
    {
        return ServerUtils.getServerWorld();
    }

    @Override
    public Entity getCommandSenderEntity()
    {
        return null;
    }

    @Override
    public boolean sendCommandFeedback()
    {
        return false;
    }

    @Override
    public void setCommandStat(CommandResultStats.Type type, int amount)
    {
    }

    @Override
    public MinecraftServer getServer()
    {
        return ServerUtils.getServer();
    }
}