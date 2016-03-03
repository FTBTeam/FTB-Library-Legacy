package ftb.lib.api.permissions;

import com.google.gson.*;
import com.mojang.authlib.GameProfile;
import ftb.lib.FTBLib;
import latmod.lib.LMJsonUtils;
import latmod.lib.config.ConfigData;
import latmod.lib.util.FinalIDObject;

/**
 * Created by LatvianModder on 13.02.2016.
 * <br>Examples of a permission node ID:
 * <br>"xpt.level_crossdim", "latblocks.allow_paint", etc.
 */
public class RankConfig extends FinalIDObject implements ConfigData.Container
{
	private final JsonElement defaultPlayerValue;
	private final JsonElement defaultOPValue;
	public final ConfigData configData;
	
	public RankConfig(String id, JsonElement defPlayerValue, JsonElement defOPValue)
	{
		super(ForgePermissionContainer.getID(id));
		defaultPlayerValue = defPlayerValue;
		defaultOPValue = defOPValue;
		configData = new ConfigData();
	}
	
	public RankConfig(String id, Number defPlayerValue, Number defOPValue)
	{
		this(id, new JsonPrimitive(defPlayerValue), new JsonPrimitive(defOPValue));
	}
	
	public RankConfig(String id, String defPlayerValue, String defOPValue)
	{
		this(id, new JsonPrimitive(defPlayerValue), new JsonPrimitive(defOPValue));
	}
	
	public RankConfig(String id, Number[] defPlayerValue, Number[] defOPValue)
	{
		this(id, LMJsonUtils.toNumberArray(defPlayerValue), LMJsonUtils.toNumberArray(defOPValue));
	}
	
	public RankConfig(String id, String[] defPlayerValue, String[] defOPValue)
	{
		this(id, LMJsonUtils.toStringArray(defPlayerValue), LMJsonUtils.toStringArray(defOPValue));
	}
	
	public void setConfigData(ConfigData d)
	{
		configData.setFrom(d);
	}
	
	public JsonElement getDefaultPlayerValue()
	{
		return defaultPlayerValue;
	}
	
	public JsonElement getDefaultOPValue()
	{
		return defaultOPValue;
	}
	
	protected JsonElement getDefaultElement(GameProfile profile)
	{
		return FTBLib.isOP(profile) ? getDefaultOPValue() : getDefaultPlayerValue();
	}
	
	/**
	 * Player can't be null, but it can be FakePlayer, if implementation supports that
	 */
	public JsonElement get(GameProfile profile)
	{
		if(profile == null) throw new RuntimeException("GameProfile can't be null!");
		
		if(ForgePermissionRegistry.handler != null)
		{
			JsonElement e = ForgePermissionRegistry.handler.handleRankConfig(this, profile);
			return (e == null) ? JsonNull.INSTANCE : e;
		}
		
		return getDefaultElement(profile);
	}
}