package com.feed_the_beast.ftbl.api;

import latmod.lib.IIDObject;
import latmod.lib.LMUtils;
import net.minecraft.util.ResourceLocation;

/**
 * Created by LatvianModder on 14.05.2016.
 */
public class FinalResourceLocationObject implements IResourceLocationObject, IIDObject
{
	private final ResourceLocation ID;
	
	public FinalResourceLocationObject(ResourceLocation id)
	{
		if(id == null)
		{
			throw new NullPointerException("ID can't be null!");
		}
		
		ID = id;
	}
	
	@Override
	public final ResourceLocation getResourceLocation()
	{
		return ID;
	}
	
	@Override
	public final String getID()
	{
		return ID.toString();
	}
	
	@Override
	public String toString()
	{
		return getID();
	}
	
	@Override
	public final boolean equals(Object o)
	{
		if(o == null) { return false; }
		else if(o == this) { return true; }
		else if(o instanceof IResourceLocationObject)
		{
			return ((IResourceLocationObject) o).getResourceLocation().equals(ID);
		}
		else if(o instanceof ResourceLocation)
		{
			return o.equals(ID);
		}
		else
		{
			return LMUtils.getID(o).equals(getID());
		}
	}
	
	@Override
	public final int hashCode()
	{
		return ID.hashCode();
	}
}