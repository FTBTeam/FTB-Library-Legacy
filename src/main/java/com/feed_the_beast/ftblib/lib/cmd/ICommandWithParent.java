package com.feed_the_beast.ftblib.lib.cmd;

import net.minecraft.command.ICommand;

import javax.annotation.Nullable;

/**
 * @author LatvianModder
 */
public interface ICommandWithParent extends ICommand
{
	void setParent(@Nullable ICommand parent);

	@Nullable
	ICommand getParent();

	static String getFullPath(ICommand command)
	{
		if (command instanceof ICommandWithParent)
		{
			ICommand parent = ((ICommandWithParent) command).getParent();

			if (parent != null)
			{
				return getFullPath(parent) + "." + command.getName();
			}
		}

		return command.getName();
	}
}