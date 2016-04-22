package ftb.lib.mod.cmd;

import com.mojang.authlib.GameProfile;
import ftb.lib.api.*;
import ftb.lib.api.cmd.*;
import latmod.lib.LMUtils;
import net.minecraft.command.*;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;

import java.util.UUID;

/**
 * Created by LatvianModder on 04.03.2016.
 */
public class CmdAddFakePlayer extends CommandLM
{
	public CmdAddFakePlayer(String s)
	{ super(s, CommandLevel.OP); }
	
	@Override
	public String getCommandUsage(ICommandSender ics)
	{ return '/' + commandName + " <player>"; }
	
	@Override
	public boolean isUsernameIndex(String[] args, int i)
	{ return i == 0; }
	
	@Override
	public void execute(MinecraftServer server, ICommandSender ics, String[] args) throws CommandException
	{
		checkArgs(args, 2);
		
		UUID id = LMUtils.fromString(args[0]);
		if(id == null)
		{
			throw new RawCommandException("Invalid UUID!");
		}
		
		if(ForgeWorldMP.inst.getPlayer(id) != null || ForgeWorldMP.inst.getPlayer(args[1]) != null)
			throw new RawCommandException("Player already exists!");
		
		ForgePlayerMP p = new ForgePlayerMP(new GameProfile(id, args[1]));
		p.init();
		ForgeWorldMP.inst.playerMap.put(p.getProfile().getId(), p);
		p.refreshStats();
		
		ics.addChatMessage(new TextComponentString("Fake player " + args[1] + " added!"));
	}
}
