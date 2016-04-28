package ftb.lib.mod.client;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import ftb.lib.FTBLib;
import ftb.lib.api.PlayerAction;
import ftb.lib.api.notification.ClickAction;
import latmod.lib.LMFileUtils;
import latmod.lib.LMJsonUtils;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by LatvianModder on 17.01.2016.
 */
//TODO: Rewrite me to work with KeyBindings
public class Shortcuts
{
	public static final List<ClickActionKey> keys = new ArrayList<>();
	public static final List<PlayerAction> actions = new ArrayList<>();
	private static File file = null;
	
	@SideOnly(Side.CLIENT)
	public static void load()
	{
		keys.clear();
		actions.clear();
		
		if(file == null) file = LMFileUtils.newFile(new File(FTBLib.folderLocal, "client/shortcuts.json"));
		JsonElement e = LMJsonUtils.fromJson(file);
		
		if(e.isJsonObject())
		{
			JsonObject o = e.getAsJsonObject();
			
			if(o.has("keys"))
			{
				JsonArray a = o.get("keys").getAsJsonArray();
				
				for(int i = 0; i < a.size(); i++)
				{
					ClickActionKey action = new ClickActionKey();
					action.setJson(a.get(i));
					keys.add(action);
				}
			}
			
			/*
			if(o.has("buttons"))
			{
				JsonArray a = o.get("buttons").getAsJsonArray();
				
				for(int i = 0; i < a.size(); i++)
				{
					JsonObject o1 = a.get(i).getAsJsonObject();
					if(o1.has("type"))
					{
						final ClickAction action = new ClickAction();
						action.fromJson(o1);
						final String name = o1.has("name") ? o1.get("name").getAsString() : "Unnamed";
						TextureCoords tex = o1.has("icon") ? GuiIcons.iconMap.get(o1.get("icon").getAsString()) : null;
						if(tex == null) tex = GuiIcons.marker;
						int priority = o1.has("priority") ? o1.get("priority").getAsInt() : -100;
						
						PlayerAction pa = new PlayerAction(PlayerAction.Type.SELF, "temp-" + UUID.randomUUID(), priority, tex)
						{
							public void onClicked(ForgePlayer self, ForgePlayer other)
							{ action.onClicked(); }
							
							public String getDisplayName()
							{ return name; }
						};
						
						actions.add(pa);
					}
				}
			}
			*/
		}
		
		save();
	}
	
	@SideOnly(Side.CLIENT)
	public static void save()
	{
		if(file == null) load();
		
		JsonObject o = new JsonObject();
		JsonObject o1;
		JsonArray a = new JsonArray();
		
		for(ClickActionKey k : keys)
			a.add(k.getJson());
		
		o.add("keys", a);
		
		//a = new JsonArray();
		//o.add("buttons", a);
		
		LMJsonUtils.toJson(file, o);
	}
	
	@SideOnly(Side.CLIENT)
	public static void onKeyPressed(int key)
	{
		if(key == Keyboard.KEY_NONE) return;
		
		for(ClickActionKey k : keys)
		{
			if(k.key == key) k.click.onClicked();
		}
	}
	
	public static class ClickActionKey
	{
		public final ClickAction click;
		public int key;
		
		public ClickActionKey()
		{
			click = new ClickAction();
			key = 0;
		}
		
		@SideOnly(Side.CLIENT)
		public JsonElement getJson()
		{
			JsonObject o = (JsonObject) click.getSerializableElement();
			o.add("key", new JsonPrimitive(Keyboard.getKeyName(key)));
			return o;
		}
		
		@SideOnly(Side.CLIENT)
		public void setJson(JsonElement e)
		{
			JsonObject o = (JsonObject) e;
			click.fromJson(o);
			key = Keyboard.getKeyIndex(o.get("key").getAsString());
		}
	}
}