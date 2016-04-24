package ftb.lib.api;

import cpw.mods.fml.common.eventhandler.Event;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
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
	public final Side side;
	public final EntityPlayer player;
	public final NBTTagCompound syncData;
	public final boolean login;
	
	private EventFTBSync(Side s, EntityPlayer ep, NBTTagCompound t, boolean b)
	{
		side = s;
		player = ep;
		syncData = t;
		login = b;
	}
	
	public static NBTTagCompound generateData(EntityPlayerMP ep, boolean login)
	{
		EventFTBSync event = new EventFTBSync(Side.SERVER, ep, new NBTTagCompound(), login);
		MinecraftForge.EVENT_BUS.post(event);
		return event.syncData;
	}
	
	@SideOnly(Side.CLIENT)
	public static void readData(NBTTagCompound data, boolean login)
	{
		EventFTBSync event = new EventFTBSync(Side.CLIENT, FTBLibClient.mc.thePlayer, data, login);
		MinecraftForge.EVENT_BUS.post(event);
	}
}