package com.feed_the_beast.ftbl.api.events;

import com.feed_the_beast.ftbl.api.cmd.CommandSubBase;
import net.minecraft.command.ICommand;
import net.minecraftforge.fml.common.eventhandler.Event;

import javax.annotation.Nonnull;

/**
 * Created by LatvianModder on 28.05.2016.
 */
public class RegisterFTBCommandsEvent extends Event
{
    private CommandSubBase command;

    public RegisterFTBCommandsEvent(CommandSubBase c)
    {
        command = c;
    }

    public void add(@Nonnull ICommand cmd)
    {
        command.add(cmd);
    }
}
