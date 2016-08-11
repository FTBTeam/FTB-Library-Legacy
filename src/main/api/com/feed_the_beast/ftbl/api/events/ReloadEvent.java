package com.feed_the_beast.ftbl.api.events;

import com.feed_the_beast.ftbl.util.ReloadType;
import net.minecraft.command.ICommandSender;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.relauncher.Side;

import javax.annotation.Nonnull;

public class ReloadEvent extends Event
{
    private final Side side;
    private final ICommandSender sender;
    private final ReloadType type;
    private final boolean login;

    public ReloadEvent(@Nonnull Side s, @Nonnull ICommandSender ics, @Nonnull ReloadType t, boolean b)
    {
        side = s;
        sender = ics;
        type = t;
        login = b;
    }

    @Nonnull
    public Side getSide()
    {
        return side;
    }

    @Nonnull
    public ICommandSender getSender()
    {
        return sender;
    }

    @Nonnull
    public ReloadType getReloadType()
    {
        return type;
    }

    public boolean isAtLogin()
    {
        return login;
    }
}