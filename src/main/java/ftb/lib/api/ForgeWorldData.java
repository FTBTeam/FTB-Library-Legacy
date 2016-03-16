package ftb.lib.api;

import com.google.gson.JsonObject;
import latmod.lib.util.FinalIDObject;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;

/**
 * Created by LatvianModder on 10.02.2016.
 */
public abstract class ForgeWorldData extends FinalIDObject
{
	public final ForgeWorld world;
	
	public ForgeWorldData(String id, ForgeWorld w)
	{
		super(id);
		world = w;
	}
	
	public abstract void init();
	
	public boolean syncID()
	{ return true; }
	
	public void loadData(JsonObject o)
	{
	}
	
	public void saveData(JsonObject o)
	{
	}
	
	//FIXME: Implement me
	public void readFromNet(NBTTagCompound tag)
	{
	}
	
	//FIXME: Implement me
	public void writeToNet(NBTTagCompound tag, EntityPlayerMP to)
	{
	}
	
	public void onClosed()
	{
	}
}