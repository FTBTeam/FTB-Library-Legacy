package ftb.lib.api.config;

import com.google.gson.*;
import latmod.lib.LMColor;
import net.minecraft.nbt.NBTTagCompound;

public class ConfigEntryColor extends ConfigEntry
{
	public final LMColor.RGB value;
	public final LMColor.RGB defValue;
	
	public ConfigEntryColor(String id, LMColor def)
	{
		super(id);
		value = new LMColor.RGB();
		value.set(def);
		
		defValue = new LMColor.RGB();
		defValue.set(def);
	}
	
	@Override
	public ConfigEntryType getConfigType()
	{ return ConfigEntryType.COLOR; }
	
	@Override
	public int getColor()
	{ return value.color(); }
	
	@Override
	public void func_152753_a(JsonElement o)
	{ value.setRGBA(o.getAsInt()); }
	
	@Override
	public JsonElement getSerializableElement()
	{ return new JsonPrimitive(value.color()); }
	
	@Override
	public String getAsString()
	{ return value.toString(); }
	
	@Override
	public int getAsInt()
	{ return value.color(); }
	
	@Override
	public String getDefValueString()
	{ return defValue.toString(); }
	
	@Override
	public void writeToNBT(NBTTagCompound tag, boolean extended)
	{
		super.writeToNBT(tag, extended);
		tag.setInteger("V", value.color());
		
		if(extended)
		{
			tag.setInteger("D", defValue.color());
		}
	}
	
	@Override
	public void readFromNBT(NBTTagCompound tag, boolean extended)
	{
		super.readFromNBT(tag, extended);
		value.setRGBA(tag.getInteger("V"));
		
		if(extended)
		{
			defValue.setRGBA(tag.getInteger("D"));
		}
	}
}