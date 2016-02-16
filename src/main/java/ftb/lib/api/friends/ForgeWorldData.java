package ftb.lib.api.friends;

import com.google.gson.JsonObject;
import latmod.lib.util.FinalIDObject;
import net.minecraft.nbt.NBTTagCompound;

/**
 * Created by LatvianModder on 10.02.2016.
 */
public abstract class ForgeWorldData extends FinalIDObject
{
	public final LMWorld world;
	
	public ForgeWorldData(String id, LMWorld w)
	{
		super(id);
		world = w;
	}
	
	public abstract void init();
	
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