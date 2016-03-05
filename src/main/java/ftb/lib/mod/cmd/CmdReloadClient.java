package ftb.lib.mod.cmd;

import ftb.lib.api.cmd.*;
import ftb.lib.mod.client.FTBLibModClient;
import ftb.lib.mod.net.MessageReload;
import net.minecraft.command.*;
import net.minecraftforge.fml.relauncher.*;

@SideOnly(Side.CLIENT)
public class CmdReloadClient extends CommandLM
{
	public CmdReloadClient()
	{ super(FTBLibModClient.reload_client_cmd.get(), CommandLevel.OP); }
	
	public void processCommand(ICommandSender ics, String[] args) throws CommandException
	{
		MessageReload.reloadClient(0L, true);
	}
}