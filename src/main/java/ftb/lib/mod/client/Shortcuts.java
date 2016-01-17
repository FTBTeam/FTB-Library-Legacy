package ftb.lib.mod.client;

import com.google.gson.*;
import ftb.lib.FTBLib;
import ftb.lib.notification.*;
import latmod.lib.*;
import latmod.lib.json.IJsonObject;
import net.minecraftforge.fml.relauncher.*;
import org.lwjgl.input.Keyboard;

import java.io.File;
import java.util.*;

/**
 * Created by LatvianModder on 17.01.2016.
 */
public class Shortcuts
{
	public static final List<KeyAction> keys = new ArrayList<>();
	public static final List<ButtonAction> buttons = new ArrayList<>();
	private static File file = null;
	
	@SideOnly(Side.CLIENT)
	public static void load()
	{
		keys.clear();
		buttons.clear();
		
		if(file == null) file = LMFileUtils.newFile(new File(FTBLib.folderLocal, "client/shortcuts.json"));
		JsonElement e = LMJsonUtils.getJsonElement(file);
		
		if(e.isJsonObject())
		{
			JsonObject o = e.getAsJsonObject();
			
			if(o.has("keys"))
			{
				JsonArray a = o.get("keys").getAsJsonArray();
				
				for(int i = 0; i < a.size(); i++)
				{
					KeyAction b = new KeyAction();
					b.setJson(a.get(i));
					keys.add(b);
				}
			}
			
			if(o.has("buttons"))
			{
				JsonArray a = o.get("buttons").getAsJsonArray();
				
				for(int i = 0; i < a.size(); i++)
				{
					ButtonAction b = new ButtonAction();
					b.setJson(a.get(i));
					buttons.add(b);
				}
			}
		}
		else
		{
			JsonObject o = new JsonObject();
			o.add("keys", new JsonArray());
			o.add("buttons", new JsonArray());
			e = o;
		}
		
		save();
	}
	
	@SideOnly(Side.CLIENT)
	public static void save()
	{
		if(file == null) load();
		
		JsonObject o = new JsonObject();
		JsonArray a = new JsonArray();
		
		for(KeyAction b : keys)
			a.add(b.getJson());
		
		o.add("keys", a);
		
		a = new JsonArray();
		
		for(ButtonAction b : buttons)
			a.add(b.getJson());
		
		o.add("buttons", a);
		
		LMJsonUtils.toJsonFile(file, o);
	}
	
	public static class KeyAction implements IJsonObject
	{
		public ClickAction action;
		public JsonElement data;
		public int key;
		
		public void setJson(JsonElement e)
		{
			JsonObject o = e.getAsJsonObject();
			action = ClickActionRegistry.get(o.get("type").getAsString());
			if(action != null && o.has("data")) data = o.get("data");
			key = Keyboard.getKeyIndex(o.get("key").getAsString());
		}
		
		public JsonElement getJson()
		{
			JsonObject o = new JsonObject();
			o.add("type", new JsonPrimitive(action.ID));
			if(data != null && !data.isJsonNull()) o.add("data", data);
			o.add("key", new JsonPrimitive(Keyboard.getKeyName(key)));
			return o;
		}
	}
	
	public static class ButtonAction implements IJsonObject
	{
		public ClickAction action;
		public JsonElement data;
		public String icon;
		public String name;
		public int priority;
		
		public void setJson(JsonElement e)
		{
			JsonObject o = e.getAsJsonObject();
			action = ClickActionRegistry.get(o.get("type").getAsString());
			if(action != null && o.has("data")) data = o.get("data");
			icon = o.has("icon") ? o.get("icon").getAsString() : "marker";
			name = o.has("name") ? o.get("name").getAsString() : "Unnamed";
			priority = o.has("priority") ? o.get("priority").getAsInt() : 0;
		}
		
		public JsonElement getJson()
		{
			JsonObject o = new JsonObject();
			o.add("type", new JsonPrimitive(action.ID));
			if(data != null && !data.isJsonNull()) o.add("data", data);
			o.add("icon", new JsonPrimitive(icon));
			o.add("name", new JsonPrimitive(name));
			o.add("priority", new JsonPrimitive(priority));
			return o;
		}
	}
}