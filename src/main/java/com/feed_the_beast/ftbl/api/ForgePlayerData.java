package com.feed_the_beast.ftbl.api;

import latmod.lib.util.FinalIDObject;
import net.minecraft.nbt.NBTTagCompound;

/**
 * Created by LatvianModder on 10.02.2016.
 */
public abstract class ForgePlayerData extends FinalIDObject
{
	public final ForgePlayer player;
	
	public ForgePlayerData(String id, ForgePlayer p)
	{
		super(id);
		player = p;
	}
	
	@Override
	public final String toString()
	{ return getID() + '_' + player.getProfile().getName(); }
	
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