package ftb.lib.api.players;

import com.google.gson.JsonObject;
import latmod.lib.util.FinalIDObject;
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
	
	public void readFromNet(NBTTagCompound tag)
	{
	}
	
	public void writeToNet(NBTTagCompound tag)
	{
	}
	
	public void onClosed()
	{
	}
}