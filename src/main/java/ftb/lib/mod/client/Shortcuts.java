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
	public static final List<Shortcut> shortcuts = new ArrayList<>();
	private static File file = null;
	
	@SideOnly(Side.CLIENT)
	public static void load()
	{
		shortcuts.clear();
		
		if(file == null) file = LMFileUtils.newFile(new File(FTBLib.folderLocal, "client/shortcuts.json"));
		JsonElement e = LMJsonUtils.getJsonElement(file);
		
		if(e.isJsonObject())
		{
			JsonObject o = e.getAsJsonObject();
			
			if(o.has("shortcuts"))
			{
				JsonArray a = o.get("shortcuts").getAsJsonArray();
				
				for(int i = 0; i < a.size(); i++)
				{
					JsonObject o1 = a.get(i).getAsJsonObject();
					Shortcut s;
					
					if(o1.has("key")) s = new KeyAction();
					else s = new ButtonAction();
					s.setJson(o1);
					shortcuts.add(s);
				}
			}
		}
		
		save();
	}
	
	@SideOnly(Side.CLIENT)
	public static void save()
	{
		if(file == null) load();
		
		JsonObject o = new JsonObject();
		JsonArray a = new JsonArray();
		
		for(Shortcut s : shortcuts)
			a.add(s.getJson());
		
		o.add("shortcuts", a);
		
		LMJsonUtils.toJsonFile(file, o);
	}
	
	public static abstract class Shortcut implements IJsonObject
	{
		public ClickAction action;
		public JsonElement data;
		
		public void setJson(JsonElement e)
		{
			JsonObject o = e.getAsJsonObject();
			action = ClickActionRegistry.get(o.get("type").getAsString());
			if(action != null && o.has("data")) data = o.get("data");
		}
		
		public JsonElement getJson()
		{
			JsonObject o = new JsonObject();
			o.add("type", new JsonPrimitive(action.ID));
			if(data != null && !data.isJsonNull()) o.add("data", data);
			return o;
		}
		
		public String getTitle()
		{ return "-"; }
		
		public boolean isKeyPressed(int k)
		{ return false; }
	}
	
	public static class KeyAction extends Shortcut
	{
		public int key;
		
		public void setJson(JsonElement e)
		{
			super.setJson(e);
			JsonObject o = e.getAsJsonObject();
			key = Keyboard.getKeyIndex(o.get("key").getAsString());
		}
		
		public JsonElement getJson()
		{
			JsonObject o = (JsonObject) super.getJson();
			o.add("key", new JsonPrimitive(Keyboard.getKeyName(key)));
			return o;
		}
		
		public String getTitle()
		{ return Keyboard.getKeyName(key) + " : " + action.getDisplayName() + " : '" + data + "'"; }
		
		public boolean isKeyPressed(int k)
		{ return key == k; }
	}
	
	public static class ButtonAction extends Shortcut
	{
		public String icon;
		public String name;
		public int priority;
		
		public void setJson(JsonElement e)
		{
			super.setJson(e);
			JsonObject o = e.getAsJsonObject();
			icon = o.has("icon") ? o.get("icon").getAsString() : "marker";
			name = o.has("name") ? o.get("name").getAsString() : "Unnamed";
			priority = o.has("priority") ? o.get("priority").getAsInt() : -100;
		}
		
		public JsonElement getJson()
		{
			JsonObject o = (JsonObject) super.getJson();
			o.add("icon", new JsonPrimitive(icon));
			o.add("name", new JsonPrimitive(name));
			o.add("priority", new JsonPrimitive(priority));
			return o;
		}
		
		public String getTitle()
		{ return name + " : " + action.getDisplayName() + " : '" + data + "'"; }
	}
}