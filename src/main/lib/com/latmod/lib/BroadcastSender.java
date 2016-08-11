package com.latmod.lib;

import com.feed_the_beast.ftbl.util.FTBLib;
import net.minecraft.command.CommandResultStats;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

import javax.annotation.Nonnull;

public enum BroadcastSender implements ICommandSender
{
    INSTANCE;

    @Nonnull
    @Override
    public String getName()
    {
        return "[Server]";
    }

    @Nonnull
    @Override
    public ITextComponent getDisplayName()
    {
        return new TextComponentString(getName());
    }

    @Override
    public void addChatMessage(@Nonnull ITextComponent component)
    {
        FTBLib.getServer().getPlayerList().sendChatMsgImpl(component, true);
    }

    @Override
    public boolean canCommandSenderUseCommand(int permLevel, @Nonnull String commandName)
    {
        return true;
    }

    @Nonnull
    @Override
    public BlockPos getPosition()
    {
        return ((WorldServer) FTBLib.getServer().getEntityWorld()).getSpawnCoordinate();
    }

    @Nonnull
    @Override
    public Vec3d getPositionVector()
    {
        return new Vec3d(getPosition());
    }

    @Nonnull
    @Override
    public World getEntityWorld()
    {
        return FTBLib.getServer().getEntityWorld();
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
    public void setCommandStat(@Nonnull CommandResultStats.Type type, int amount)
    {
    }

    @Override
    public MinecraftServer getServer()
    {
        return FTBLib.getServer();
    }
}