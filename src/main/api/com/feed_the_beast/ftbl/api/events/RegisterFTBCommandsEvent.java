package com.feed_the_beast.ftbl.api.events;

import com.feed_the_beast.ftbl.api.cmd.CommandTreeBase;
import net.minecraft.command.ICommand;
import net.minecraftforge.fml.common.eventhandler.Event;

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
        command.add(cmd);
    }
}
