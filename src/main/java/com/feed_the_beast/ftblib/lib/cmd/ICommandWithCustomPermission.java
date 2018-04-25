package com.feed_the_beast.ftblib.lib.cmd;

import com.feed_the_beast.ftblib.lib.util.misc.Node;
import net.minecraft.command.ICommand;

/**
 * @author LatvianModder
 */
public interface ICommandWithCustomPermission extends ICommand
{
	Node getCustomPermissionNode();

	static Node getPermissionNode(ICommand command)
	{
		if (command instanceof ICommandWithCustomPermission)
		{
			return ((ICommandWithCustomPermission) command).getCustomPermissionNode();
		}

		return Node.COMMAND.append(ICommandWithParent.getFullPath(command));
	}
}