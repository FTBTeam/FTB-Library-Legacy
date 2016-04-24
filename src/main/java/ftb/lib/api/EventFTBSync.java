package ftb.lib.api;

import cpw.mods.fml.common.eventhandler.Event;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ftb.lib.FTBLib;
import ftb.lib.FTBWorld;
import ftb.lib.api.client.FTBLibClient;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.MinecraftForge;

/**
 * Created by LatvianModder on 23.04.2016.
 */
public class EventFTBSync extends Event
{
	public final FTBWorld world;
	public final EntityPlayer player;
	public final NBTTagCompound syncData;
	public final boolean login;
	
	private EventFTBSync(FTBWorld w, EntityPlayer ep, NBTTagCompound t, boolean b)
	{
		world = w;
		player = ep;
		syncData = t;
		login = b;
	}
	
	public static NBTTagCompound generateData(EntityPlayerMP ep, boolean login)
	{
		EventFTBSync event = new EventFTBSync(FTBWorld.server, ep, new NBTTagCompound(), login);
		MinecraftForge.EVENT_BUS.post(event);
		
		if(FTBLib.DEV_ENV) FTBLib.dev_logger.info("Synced data TX: " + event.syncData);
		
		return event.syncData;
	}
	
	@SideOnly(Side.CLIENT)
	public static void readData(NBTTagCompound data, boolean login)
	{
		EventFTBSync event = new EventFTBSync(FTBWorld.client, FTBLibClient.mc.thePlayer, data, login);
		MinecraftForge.EVENT_BUS.post(event);
		
		if(FTBLib.DEV_ENV) FTBLib.dev_logger.info("Synced data RX: " + data);
	}
}