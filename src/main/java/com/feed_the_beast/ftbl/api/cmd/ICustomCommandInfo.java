package com.feed_the_beast.ftbl.api.cmd;

import net.minecraft.command.ICommandSender;
import net.minecraft.util.text.ITextComponent;

import java.util.List;

/**
 * Created by LatvianModder on 23.02.2016.
 */
public interface ICustomCommandInfo
{
    void addInfo(List<ITextComponent> list, ICommandSender sender);
}
