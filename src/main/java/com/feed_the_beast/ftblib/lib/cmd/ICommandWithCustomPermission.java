package com.feed_the_beast.ftblib.lib.cmd;

import net.minecraft.command.ICommand;

/**
 * @author LatvianModder
 */
public interface ICommandWithCustomPermission extends ICommand
{
	String getCustomPermissionNode();

	static String getPermissionNode(ICommand command)
	{
		if (command instanceof ICommandWithCustomPermission)
		{
			return ((ICommandWithCustomPermission) command).getCustomPermissionNode();
		}

		return "command." + ICommandWithParent.getFullPath(command);
	}
}