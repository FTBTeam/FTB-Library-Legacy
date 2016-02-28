package ftb.lib.api.cmd;

import net.minecraft.command.ICommandSender;
import net.minecraft.util.IChatComponent;

import java.util.List;

/**
 * Created by LatvianModder on 23.02.2016.
 */
public interface ICustomCommandInfo
{
	void addInfo(List<IChatComponent> list, ICommandSender sender);
}
