package ftb.lib.mod.net;

import com.mojang.authlib.GameProfile;
import ftb.lib.FTBLib;
import ftb.lib.api.*;
import ftb.lib.api.config.*;
import ftb.lib.api.events.ReloadEvent;
import ftb.lib.api.net.*;
import ftb.lib.api.notification.*;
import ftb.lib.mod.FTBLibMod;
import ftb.lib.mod.client.*;
import io.netty.buffer.ByteBuf;
import latmod.lib.LMUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.*;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.network.simpleimpl.*;
import net.minecraftforge.fml.relauncher.*;

import java.util.UUID;

public class MessageReload extends MessageLM<MessageReload>
{
	public boolean reloadClient;
	public boolean modeChanged;
	public GameProfile profile;
	public NBTTagCompound worldData;
	public NBTTagCompound syncedData;
	
	public MessageReload() { }
	
	public MessageReload(ForgePlayerMP self, boolean l, boolean reload, boolean mode)
	{
		reloadClient = reload;
		modeChanged = mode;
		profile = l ? self.getProfile() : null;
		worldData = new NBTTagCompound();
		self.getWorld().toWorldMP().writeDataToNet(worldData, self, l);
		
		syncedData = new NBTTagCompound();
		ConfigRegistry.synced.writeToNBT(syncedData, false);
		
		if(FTBLib.DEV_ENV) FTBLib.dev_logger.info("Synced config TX: " + ConfigRegistry.synced);
	}
	
	public LMNetworkWrapper getWrapper()
	{ return FTBLibNetHandler.NET; }
	
	public void fromBytes(ByteBuf io)
	{
		reloadClient = io.readBoolean();
		modeChanged = io.readBoolean();
		boolean login = io.readBoolean();
		
		if(login)
		{
			UUID id = readUUID(io);
			String s = readString(io);
			profile = new GameProfile(id, s);
		}
		else profile = null;
		
		worldData = readTag(io);
		syncedData = readTag(io);
	}
	
	public void toBytes(ByteBuf io)
	{
		io.writeBoolean(reloadClient);
		io.writeBoolean(modeChanged);
		io.writeBoolean(profile != null);
		
		if(profile != null)
		{
			writeUUID(io, profile.getId());
			writeString(io, profile.getName());
		}
		
		writeTag(io, worldData);
		writeTag(io, syncedData);
	}
	
	@SideOnly(Side.CLIENT)
	public IMessage onMessage(MessageReload m, MessageContext ctx)
	{
		long ms = LMUtils.millis();
		
		ConfigGroup syncedGroup = new ConfigGroup(ConfigRegistry.synced.getID());
		syncedGroup.readFromNBT(m.syncedData, false);
		ConfigRegistry.synced.loadFromGroup(syncedGroup, true);
		
		if(FTBLib.DEV_ENV) FTBLib.dev_logger.info("Synced config RX: " + syncedGroup.getSerializableElement());
		
		if(m.profile != null || ForgeWorldSP.inst == null)
		{
			ForgeWorldSP.inst = new ForgeWorldSP(m.profile);
			ForgeWorldSP.inst.init();
		}
		
		ForgeWorldSP.inst.readDataFromNet(m.worldData, m.profile != null);
		
		if(m.reloadClient || m.modeChanged || m.profile != null)
		{
			reloadClient(ms, true, m.modeChanged);
		}
		else
		{
			Notification n = new Notification("reload_client_config", FTBLibMod.mod.chatComponent("reload_client_config"), 7000);
			n.title.getChatStyle().setColor(EnumChatFormatting.WHITE);
			n.desc = new ChatComponentText('/' + FTBLibModClient.reload_client_cmd.getAsString());
			n.setColor(0xFF333333);
			ClientNotifications.add(n);
			return null;
		}
		
		return null;
	}
	
	@SideOnly(Side.CLIENT)
	public static void reloadClient(long ms, boolean printMessage, boolean modeChanged)
	{
		if(ms == 0L) ms = LMUtils.millis();
		GameModes.reload();
		Shortcuts.load();
		EntityPlayer ep = FTBLibMod.proxy.getClientPlayer();
		ReloadEvent event = new ReloadEvent(ForgeWorldSP.inst, ep, true, modeChanged);
		if(FTBLib.ftbu != null) FTBLib.ftbu.onReloaded(event);
		MinecraftForge.EVENT_BUS.post(event);
		
		if(printMessage)
		{
			FTBLib.printChat(ep, FTBLibMod.mod.chatComponent("reloaded_client", ((LMUtils.millis() - ms) + "ms")));
		}
	}
}