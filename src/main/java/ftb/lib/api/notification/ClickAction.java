package ftb.lib.api.notification;

import com.google.gson.*;
import latmod.lib.json.IJsonObject;
import net.minecraftforge.fml.relauncher.*;

/**
 * Created by LatvianModder on 23.01.2016.
 */
public class ClickAction implements IJsonObject
{
	public ClickActionType type;
	public JsonElement data;
	
	public ClickAction() {}
	
	public ClickAction(ClickActionType t, JsonElement d)
	{
		type = t;
		data = d;
	}
	
	public JsonElement getJson()
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
	
	public void setJson(JsonElement e)
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
		}
	}
	
	@SideOnly(Side.CLIENT)
	public void onClicked()
	{ if(type != null) type.onClicked(data == null ? JsonNull.INSTANCE : data); }
	
	public String toString()
	{
		if(data == null) return type.getID();
		return data.toString();
	}
}