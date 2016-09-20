package com.feed_the_beast.ftbl.api.events;

import net.minecraft.command.ICommand;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.server.command.CommandTreeBase;

/**
 * Created by LatvianModder on 28.05.2016.
 */
public class RegisterFTBCommandsEvent extends Event
{
    private final boolean isDedi;
    private CommandTreeBase command;

    public RegisterFTBCommandsEvent(CommandTreeBase c, boolean dedi)
    {
        command = c;
        isDedi = dedi;
    }

    public boolean isDedicatedServer()
    {
        return isDedi;
    }

    public void add(ICommand cmd)
    {
        command.addSubcommand(cmd);
    }
}
