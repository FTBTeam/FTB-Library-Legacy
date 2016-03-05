package ftb.lib.api.players;

import net.minecraft.nbt.NBTTagCompound;

/**
 * Created by LatvianModder on 10.02.2016.
 */
public abstract class ForgePlayerData implements Comparable<ForgePlayerData>
{
	public final String ID;
	public final ForgePlayer player;
	
	public ForgePlayerData(String id, ForgePlayer p)
	{
		ID = id.trim().toLowerCase();
		player = p;
	}
	
	public final String toString()
	{ return ID; }
	
	public final int hashCode()
	{ return ID.hashCode(); }
	
	public final boolean equals(Object o)
	{ return o.toString().equals(ID); }
	
	public final int compareTo(ForgePlayerData o)
	{ return ID.compareTo(o.ID); }
	
	/**
	 * For loading data from server's world folder
	 */
	public void readFromServer(NBTTagCompound tag)
	{
	}
	
	/**
	 * For saving data in server's world folder
	 */
	public void writeToServer(NBTTagCompound tag)
	{
	}
	
	/**
	 * Received data from server
	 */
	public void readFromNet(NBTTagCompound tag, boolean self)
	{
	}
	
	/**
	 * Sends data to client. You can send 'private' data, if self == true
	 */
	public void writeToNet(NBTTagCompound tag, boolean self)
	{
	}
	
	public void onLoggedIn(boolean firstTime)
	{
	}
	
	public void onLoggedOut()
	{
	}
	
	/**
	 * @Server: Called when player dies<br>
	 * @Client: only called if Minecraft.thePlayer dies
	 */
	public void onDeath()
	{
	}
}