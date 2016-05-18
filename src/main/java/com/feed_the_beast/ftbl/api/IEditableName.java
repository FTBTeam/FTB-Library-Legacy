package com.feed_the_beast.ftbl.api;

import net.minecraft.command.ICommandSender;
import net.minecraft.world.IWorldNameable;

/**
 * Created by LatvianModder on 14.05.2016.
 */
public interface IEditableName extends IWorldNameable
{
    boolean setName(String name, ICommandSender player);
}