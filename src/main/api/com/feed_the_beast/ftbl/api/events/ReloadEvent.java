package com.feed_the_beast.ftbl.api.events;

import com.feed_the_beast.ftbl.util.ReloadType;
import net.minecraft.command.ICommandSender;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.relauncher.Side;

public class ReloadEvent extends Event
{
    private final Side side;
    private final ICommandSender sender;
    private final ReloadType type;

    public ReloadEvent(Side s, ICommandSender ics, ReloadType t)
    {
        side = s;
        sender = ics;
        type = t;
    }

    public Side getSide()
    {
        return side;
    }

    public ICommandSender getSender()
    {
        return sender;
    }

    public ReloadType getReloadType()
    {
        return type;
    }

    public boolean isAtLogin()
    {
        return getReloadType() == ReloadType.LOGIN;
    }
}