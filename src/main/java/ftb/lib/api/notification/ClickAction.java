package ftb.lib.api.notification;

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import net.minecraft.util.IJsonSerializable;
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
		}
	}
	
	@SideOnly(Side.CLIENT)
	public void onClicked()
	{ if(type != null) type.onClicked(data == null ? JsonNull.INSTANCE : data); }
	
	@Override
	public String toString()
	{
		if(data == null) return type.getID();
		return data.toString();
	}
}