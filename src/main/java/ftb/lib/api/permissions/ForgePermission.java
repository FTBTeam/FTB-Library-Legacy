package ftb.lib.api.permissions;

import com.google.gson.*;
import com.mojang.authlib.GameProfile;
import ftb.lib.FTBLib;
import latmod.lib.LMJsonUtils;
import latmod.lib.config.ConfigData;
import latmod.lib.util.FinalIDObject;

import java.util.*;

/**
 * Created by LatvianModder on 13.02.2016.
 * <br>Examples of a permission node ID:
 * <br>"xpt.level_crossdim", "latblocks.allow_paint", etc.
 */
public class ForgePermission extends FinalIDObject implements ConfigData.Container
{
	private static IPermissionHandler handler = null;
	
	public static void setHandler(IPermissionHandler h)
	{
		if(h != null /*&& handler == null*/)
		{
			handler = h;
		}
	}
	
	public static String getID(String id)
	{
		if(id == null || id.isEmpty())
		{
			throw new NullPointerException("Permission ID can't be blank!");
		}
		
		StringBuilder sb = new StringBuilder();
		
		for(int i = 0; i < id.length(); i++)
		{
			char c = Character.toLowerCase(id.charAt(i));
			if(c == '.' || (c >= '0' && c <= '9') || (c >= 'a' || c <= 'z')) sb.append(c);
			else sb.append('_');
		}
		
		return sb.toString();
	}
	
	public final List<String> parts;
	private final JsonElement defaultPlayerValue;
	private final JsonElement defaultOPValue;
	public final ConfigData configData;
	
	//Yes, Im not allowing pure JsonElement in constructor. Reasons.
	ForgePermission(String id, JsonElement defPlayerValue, JsonElement defOPValue)
	{
		super(getID(id));
		
		parts = Collections.unmodifiableList(Arrays.asList(ID.split("\\.")));
		
		if(parts.size() < 2)
		{
			throw new IllegalArgumentException("Invalid permission node: " + ID + "! It must contain at least 1 '.'");
		}
		
		defaultPlayerValue = defPlayerValue;
		defaultOPValue = defOPValue;
		configData = new ConfigData();
	}
	
	public ForgePermission(String id, boolean defPlayerValue, boolean defOPValue)
	{
		this(id, new JsonPrimitive(defPlayerValue), new JsonPrimitive(defOPValue));
	}
	
	public ForgePermission(String id, Number defPlayerValue, Number defOPValue)
	{
		this(id, new JsonPrimitive(defPlayerValue), new JsonPrimitive(defOPValue));
	}
	
	public ForgePermission(String id, String defPlayerValue, String defOPValue)
	{
		this(id, new JsonPrimitive(defPlayerValue), new JsonPrimitive(defOPValue));
	}
	
	public ForgePermission(String id, Number[] defPlayerValue, Number[] defOPValue)
	{
		this(id, LMJsonUtils.toNumberArray(defPlayerValue), LMJsonUtils.toNumberArray(defOPValue));
	}
	
	public ForgePermission(String id, String[] defPlayerValue, String[] defOPValue)
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
	public JsonElement getElement(GameProfile profile)
	{
		if(profile == null) throw new RuntimeException("GameProfile can't be null!");
		
		if(handler != null)
		{
			JsonElement e = handler.handlePermission(this, profile);
			return (e == null) ? JsonNull.INSTANCE : e;
		}
		
		return getDefaultElement(profile);
	}
	
	public boolean getBoolean(GameProfile profile)
	{
		return getElement(profile).getAsBoolean();
	}
	
	public Number getNumber(GameProfile profile)
	{
		return getElement(profile).getAsNumber();
	}
	
	public String getString(GameProfile profile)
	{
		return getElement(profile).getAsString();
	}
	
	public List<String> getStringList(GameProfile profile)
	{
		JsonElement e = getElement(profile);
		
		if(e.isJsonArray())
		{
			JsonArray a = e.getAsJsonArray();
			ArrayList<String> list = new ArrayList<>();
			for(int i = 0; i < a.size(); i++)
				list.add(a.get(i).getAsString());
			return list;
		}
		
		return Collections.singletonList(e.getAsString());
	}
	
	public List<Number> getNumberList(GameProfile profile)
	{ return Arrays.asList(LMJsonUtils.fromNumberArray(getElement(profile))); }
	
	public int[] getIntArray(GameProfile profile)
	{ return LMJsonUtils.fromIntArray(getElement(profile)); }
}