package ftb.lib.api.permission;

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
	public final ConfigData configData;
	private final JsonElement defaultPlayerValue;
	private final JsonElement defaultOPValue;
	
	//Yes, Im not allowing pure JsonElement in constructor. Reasons.
	ForgePermission(String id, JsonElement defPlayerValue, JsonElement defOPValue)
	{
		super(getID(id));
		
		parts = Collections.unmodifiableList(Arrays.asList(ID.split("\\.")));
		
		configData = new ConfigData();
		
		if(parts.size() < 2)
		{
			throw new IllegalArgumentException("Invalid permission node: " + ID + "! It must contain at least 1 '.'");
		}
		
		defaultPlayerValue = defPlayerValue;
		defaultOPValue = defOPValue;
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
	
	protected JsonElement getDefaultElement(GameProfile profile)
	{
		return FTBLib.isOP(profile) ? defaultOPValue : defaultPlayerValue;
	}
	
	/**
	 * Player can't be null, but it can be FakePlayer, if implementation supports that
	 */
	public JsonElement getElement(GameProfile profile)
	{
		if(profile == null) throw new RuntimeException("GameProfile can't be null!");
		
		if(ForgePermissionRegistry.handler != null)
		{
			JsonElement e = ForgePermissionRegistry.handler.handlePermission(this, profile);
			if(e != null && !e.isJsonNull()) return e;
		}
		
		return getDefaultElement(profile);
	}
	
	public boolean getBoolean(GameProfile profile)
	{
		return getElement(profile).getAsBoolean();
	}
	
	public Number getNumber(GameProfile profile)
	{
		return configData.getNumber(getElement(profile).getAsNumber());
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
	{
		JsonElement e = getElement(profile);
		if(e == null || e.isJsonNull()) return null;
		
		if(e.isJsonArray())
		{
			List<Number> list = new ArrayList<>();
			JsonArray a = e.getAsJsonArray();
			for(int i = 0; i < a.size(); i++)
				list.add(configData.getNumber(a.get(i).getAsNumber()));
			return list;
		}
		
		return Collections.singletonList(configData.getNumber(e.getAsNumber()));
	}
	
	public int[] getIntArray(GameProfile profile)
	{
		List<Number> l = getNumberList(profile);
		if(l == null) return null;
		else if(l.isEmpty()) return new int[0];
		else
		{
			int[] ai = new int[l.size()];
			for(int i = 0; i < l.size(); i++)
				ai[i] = l.get(i).intValue();
			return ai;
		}
	}
}