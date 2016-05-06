package ftb.lib.api.notification;

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import net.minecraft.util.IJsonSerializable;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Created by LatvianModder on 23.01.2016.
 */
public class ClickAction implements IJsonSerializable
{
	public ClickActionType type;
	public JsonElement data;
	
	public ClickAction() {}
	
	public ClickAction(ClickActionType t, JsonElement d)
	{
		type = t;
		data = d;
	}
	
	@Override
	public JsonElement getSerializableElement()
	{
		if(data == null || data.isJsonNull())
		{
			return new JsonPrimitive(type.getID());
		}
		
		JsonObject o = new JsonObject();
		o.add("type", new JsonPrimitive(type.getID()));
		o.add("data", data);
		return o;
	}
	
	@Override
	public void fromJson(JsonElement e)
	{
		if(e.isJsonPrimitive())
		{
			type = ClickActionRegistry.get(e.getAsString());
			data = null;
		}
		else
		{
			JsonObject o = e.getAsJsonObject();
			type = ClickActionRegistry.get(o.get("type").getAsString());
			data = o.get("data");
			
			if(data == JsonNull.INSTANCE)
			{
				data = null;
			}
		}
	}
	
	@SideOnly(Side.CLIENT)
	public void onClicked()
	{ if(type != null) { type.onClicked(data == null ? JsonNull.INSTANCE : data); } }
	
	@Override
	public String toString()
	{
		return (data == null) ? type.getID() : data.toString();
	}
	
	public static ClickAction from(ClickEvent e)
	{
		if(e != null)
		{
			JsonPrimitive p = new JsonPrimitive(e.getValue());
			
			switch(e.getAction())
			{
				case RUN_COMMAND:
					return new ClickAction(ClickActionType.CMD, p);
				case OPEN_FILE:
					return new ClickAction(ClickActionType.FILE, p);
				case SUGGEST_COMMAND:
					return new ClickAction(ClickActionType.SHOW_CMD, p);
				case OPEN_URL:
					return new ClickAction(ClickActionType.URL, p);
			}
		}
		
		return null;
	}
}