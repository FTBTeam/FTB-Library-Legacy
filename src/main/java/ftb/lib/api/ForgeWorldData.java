package ftb.lib.api;

import latmod.lib.util.FinalIDObject;
import latmod.lib.util.Phase;
import net.minecraft.nbt.NBTTagCompound;

/**
 * Created by LatvianModder on 10.02.2016.
 */
public abstract class ForgeWorldData extends FinalIDObject
{
	boolean isLoaded;
	
	public ForgeWorldData(String id)
	{
		super(id);
	}
	
	public abstract void onLoaded(ForgeWorld w);
	
	public boolean syncID()
	{ return true; }
	
	public final boolean isLoaded()
	{ return isLoaded; }
	
	public void loadData(NBTTagCompound tag, Phase phase)
	{
	}
	
	public void saveData(NBTTagCompound tag)
	{
	}
	
	public void readFromNet(NBTTagCompound tag, boolean login)
	{
	}
	
	public void writeToNet(NBTTagCompound tag, ForgePlayerMP self, boolean login)
	{
	}
	
	public void onClosed()
	{
	}
}