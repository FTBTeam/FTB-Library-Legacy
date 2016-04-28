package ftb.lib.mod.cmd;

import ftb.lib.api.cmd.CommandLM;
import ftb.lib.api.cmd.CommandLevel;
import ftb.lib.mod.client.FTBLibModClient;
import ftb.lib.mod.net.MessageReload;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class CmdReloadClient extends CommandLM
{
	public CmdReloadClient()
	{ super(FTBLibModClient.reload_client_cmd.getAsString(), CommandLevel.OP); }
	
	@Override
	public void execute(MinecraftServer server, ICommandSender ics, String[] args) throws CommandException
	{
		MessageReload.reloadClient(0L, true, false);
	}
}