package ftb.lib.api.events;

import ftb.lib.api.ForgeWorld;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.eventhandler.Event;

/**
 * Created by LatvianModder on 11.05.2016.
 */
public class SyncEvent extends Event
{
	public final ForgeWorld world;
	public final NBTTagCompound syncData;
	
	private SyncEvent(ForgeWorld w, NBTTagCompound data)
	{
		world = w;
		syncData = data;
	}
}