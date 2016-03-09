package ftb.lib.mod.cmd;

import com.mojang.authlib.GameProfile;
import ftb.lib.api.*;
import ftb.lib.api.cmd.*;
import latmod.lib.json.UUIDTypeAdapterLM;
import net.minecraft.command.*;
import net.minecraft.util.ChatComponentText;

import java.util.UUID;

/**
 * Created by LatvianModder on 04.03.2016.
 */
public class CmdAddFakePlayer extends CommandLM
{
	public CmdAddFakePlayer(String s)
	{ super(s, CommandLevel.OP); }
	
	public String getCommandUsage(ICommandSender ics)
	{ return '/' + commandName + " <player>"; }
	
	public boolean isUsernameIndex(String[] args, int i)
	{ return i == 0; }
	
	public void processCommand(ICommandSender ics, String[] args) throws CommandException
	{
		checkArgs(args, 2);
		
		UUID id = UUIDTypeAdapterLM.getUUID(args[0]);
		if(id == null)
		{
			throw new RawCommandException("Invalid UUID!");
		}
		
		if(ForgeWorldMP.inst.getPlayer(id) != null || ForgeWorldMP.inst.getPlayer(args[1]) != null)
			throw new RawCommandException("Player already exists!");
		
		ForgePlayerMP p = new ForgePlayerMP(new GameProfile(id, args[1]));
		ForgeWorldMP.inst.playerMap.put(p.getProfile().getId(), p);
		p.refreshStats();
		
		ics.addChatMessage(new ChatComponentText("Fake player " + args[1] + " added!"));
	}
}
