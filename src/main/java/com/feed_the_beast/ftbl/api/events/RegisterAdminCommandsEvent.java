package com.feed_the_beast.ftbl.api.events;

import com.feed_the_beast.ftbl.api.cmd.CommandSubLM;
import net.minecraft.command.ICommand;
import net.minecraftforge.fml.common.eventhandler.Event;

import javax.annotation.Nonnull;

/**
 * Created by LatvianModder on 28.05.2016.
 */
public class RegisterAdminCommandsEvent extends Event
{
    private CommandSubLM command;

    public RegisterAdminCommandsEvent(CommandSubLM c)
    {
        command = c;
    }

    public void add(@Nonnull ICommand cmd)
    {
        command.add(cmd);
    }
}
