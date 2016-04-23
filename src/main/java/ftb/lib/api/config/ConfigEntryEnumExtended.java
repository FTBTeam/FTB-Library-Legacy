package ftb.lib.api.config;

import com.google.gson.*;
import ftb.lib.api.IClickable;
import net.minecraft.nbt.*;

import java.util.*;

public final class ConfigEntryEnumExtended extends ConfigEntry implements IClickable
{
	private final List<String> values;
	private String value;
	private String defValue;
	
	public ConfigEntryEnumExtended(String id)
	{
		super(id);
		values = new ArrayList<>();
	}
	
	public ConfigEntryEnumExtended(String id, List<String> vals, String def)
	{
		super(id);
		values = vals;
		value = defValue = def;
	}
	
	@Override
	public ConfigEntryType getConfigType()
	{ return ConfigEntryType.ENUM; }
	
	@Override
	public int getColor()
	{ return 0x0094FF; }
	
	public void set(String s)
	{ value = s; }
	
	public int getIndex()
	{ return values.indexOf(getAsString()); }
	
	@Override
	public void func_152753_a(JsonElement o)
	{ set(o.getAsString()); }
	
	@Override
	public JsonElement getSerializableElement()
	{ return new JsonPrimitive(getAsString()); }
	
	@Override
	public void writeToNBT(NBTTagCompound tag, boolean extended)
	{
		super.writeToNBT(tag, extended);
		tag.setString("V", getAsString());
		
		if(extended)
		{
			tag.setString("D", defValue);
			
			if(!values.isEmpty())
			{
				NBTTagList list = new NBTTagList();
				
				for(String s : values)
				{
					list.appendTag(new NBTTagString(s));
				}
				
				tag.setTag("VL", list);
			}
		}
	}
	
	@Override
	public void readFromNBT(NBTTagCompound tag, boolean extended)
	{
		super.readFromNBT(tag, extended);
		set(tag.getString("V"));
		
		if(extended)
		{
			defValue = tag.getString("D");
			
			values.clear();
			
			if(tag.hasKey("VL"))
			{
				NBTTagList list = (NBTTagList) tag.getTag("VL");
				
				for(int i = 0; i < list.tagCount(); i++)
				{
					values.add(list.getStringTagAt(i));
				}
			}
		}
	}
	
	@Override
	public void onClicked(boolean leftClick)
	{
		int i = getIndex() + (leftClick ? 1 : -1);
		if(i < 0) i = values.size() - 1;
		if(i >= values.size()) i = 0;
		set(values.get(i));
	}
	
	@Override
	public String getAsString()
	{ return value; }
	
	@Override
	public boolean getAsBoolean()
	{ return getAsString() != null; }
	
	@Override
	public int getAsInt()
	{ return getIndex(); }
	
	@Override
	public String getDefValueString()
	{ return defValue; }
}