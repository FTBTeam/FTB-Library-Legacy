package com.feed_the_beast.ftbl.api.permissions;

import com.feed_the_beast.ftbl.util.FTBLib;
import com.google.gson.JsonElement;
import com.mojang.authlib.GameProfile;
import latmod.lib.util.FinalIDObject;

/**
 * Created by LatvianModder on 13.02.2016.
 * <br>Examples of a permission node ID:
 * <br>"xpt.level_crossdim", "latblocks.allow_paint", etc.
 */
public abstract class RankConfig extends FinalIDObject
{
	public RankConfig(String id)
	{
		super(ForgePermissionRegistry.getID(id));
	}
	
	public abstract JsonElement getDefaultValue(boolean op);
	
	
	/**
	 * Player can't be null, but it can be FakePlayer, if implementation supports that
	 */
	public final JsonElement get(GameProfile profile)
	{
		if(profile == null)
		{
			throw new RuntimeException("GameProfile can't be null!");
		}
		
		if(ForgePermissionRegistry.getPermissionHandler() != null)
		{
			return ForgePermissionRegistry.getPermissionHandler().handleRankConfig(this, profile);
		}
		
		return getDefaultValue(FTBLib.isOP(profile));
	}
}