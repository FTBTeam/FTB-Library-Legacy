package ftb.lib.api.permissions;

import com.google.gson.*;
import com.mojang.authlib.GameProfile;
import ftb.lib.FTBLib;
import latmod.lib.LMJsonUtils;
import latmod.lib.annotations.*;
import latmod.lib.util.FinalIDObject;

/**
 * Created by LatvianModder on 13.02.2016.
 * <br>Examples of a permission node ID:
 * <br>"xpt.level_crossdim", "latblocks.allow_paint", etc.
 */
public class RankConfig extends FinalIDObject implements INumberBoundsContainer, IInfoContainer
{
	private final JsonElement defaultPlayerValue;
	private final JsonElement defaultOPValue;
	private Double minValue, maxValue;
	private String[] info;
	
	public RankConfig(String id, JsonElement defPlayerValue, JsonElement defOPValue)
	{
		super(ForgePermissionContainer.getID(id));
		defaultPlayerValue = defPlayerValue;
		defaultOPValue = defOPValue;
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
	
	private JsonElement filter(JsonElement e)
	{
		if(e == null || (minValue == null && maxValue == null) || !e.isJsonPrimitive()) return e;
		double n = e.getAsDouble();
		if(n < minValue) n = minValue;
		if(n > maxValue) n = maxValue;
		return new JsonPrimitive(n);
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
			return (e == null) ? JsonNull.INSTANCE : filter(e);
		}
		
		return filter(getDefaultElement(profile));
	}
	
	public void setBounds(double min, double max)
	{
		minValue = (min == Double.NEGATIVE_INFINITY) ? null : min;
		maxValue = (max == Double.POSITIVE_INFINITY) ? null : max;
	}
	
	public double getMin()
	{ return minValue == null ? Double.NEGATIVE_INFINITY : minValue; }
	
	public double getMax()
	{ return maxValue == null ? Double.POSITIVE_INFINITY : maxValue; }
	
	public void setInfo(String[] s)
	{ info = s; }
	
	public String[] getInfo()
	{ return info; }
}