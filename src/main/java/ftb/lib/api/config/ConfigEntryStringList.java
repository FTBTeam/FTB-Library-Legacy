package ftb.lib.api.config;

import com.google.gson.*;
import latmod.lib.LMListUtils;
import net.minecraft.nbt.*;

import java.util.*;

/**
 * Created by LatvianModder on 26.03.2016.
 */
public class ConfigEntryStringList extends ConfigEntry
{
	public final List<String> defValue;
	private List<String> value;
	
	public ConfigEntryStringList(String id, List<String> def)
	{
		super(id);
		value = new ArrayList<>();
		defValue = new ArrayList<>();
		value.addAll(def);
		defValue.addAll(def);
	}
	
	public ConfigEntryStringList(String id, String... def)
	{
		this(id, Arrays.asList(def));
	}
	
	public final ConfigEntryType getType()
	{ return ConfigEntryType.STRING; }
	
	public ConfigEntry simpleCopy()
	{ return new ConfigEntryStringList(getID(), defValue); }
	
	public void setStringList(List<String> o)
	{
		value.clear();
		
		if(o != null && !o.isEmpty())
		{
			value.addAll(o);
		}
	}
	
	public final int getColor()
	{ return 0xFFAA49; }
	
	public final String getDefValueString()
	{ return LMListUtils.toString(defValue); }
	
	public final void fromJson(JsonElement o)
	{
		JsonArray a = o.getAsJsonArray();
		value.clear();
		for(int i = 0; i < a.size(); i++)
			value.add(a.get(i).getAsString());
		setStringList(LMListUtils.clone(value));
	}
	
	public final JsonElement getSerializableElement()
	{
		JsonArray a = new JsonArray();
		value = getAsStringList();
		for(String aValue : value) a.add(new JsonPrimitive(aValue));
		return a;
	}
	
	public final NBTBase serializeNBT()
	{
		NBTTagList list = new NBTTagList();
		value = getAsStringList();
		
		for(String s : value)
		{
			list.appendTag(new NBTTagString(s));
		}
		
		return list;
	}
	
	public final void deserializeNBT(NBTBase nbt)
	{
		NBTTagList list = (NBTTagList) nbt;
		
		if(list.hasNoTags()) setStringList(null);
		else
		{
			ArrayList<String> alist = new ArrayList<>();
			
			for(int i = 0; i < list.tagCount(); i++)
			{
				alist.add(list.getStringTagAt(i));
			}
			
			setStringList(alist);
		}
	}
	
	public final void writeToNBT(NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);
	}
	
	public final void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
	}
	
	public final String getAsString()
	{ return LMListUtils.toString(getAsStringList()); }
	
	public final int getAsInt()
	{ return getAsStringList().size(); }
	
	public final boolean getAsBoolean()
	{ return !getAsStringList().isEmpty(); }
	
	public List<String> getAsStringList()
	{ return value; }
}