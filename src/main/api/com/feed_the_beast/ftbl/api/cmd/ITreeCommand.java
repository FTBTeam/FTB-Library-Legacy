package com.feed_the_beast.ftbl.api.cmd;

import net.minecraft.command.ICommand;

import java.util.Collection;

/**
 * Created by LatvianModder on 30.08.2016.
 */
public interface ITreeCommand extends ICommand
{
    Collection<ICommand> getSubCommands();
}