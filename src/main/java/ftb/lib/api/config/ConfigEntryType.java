package ftb.lib.api.config;

/**
 * Created by LatvianModder on 26.03.2016.
 */
public enum ConfigEntryType
{
	BUTTON,
	JSON_ELEMENT,
	NBT_BASE,
	GROUP,
	BOOLEAN,
	INT,
	DOUBLE,
	STRING,
	ENUM,
	COLOR,
	INT_LIST,
	//0xFF4F34
	DOUBLE_LIST,
	STRING_LIST;
	
	public static ConfigEntry createNewEntry(String id, ConfigEntryType type)
	{
		if(id == null || id.isEmpty()) return null;
		
		switch(type)
		{
			case GROUP:
				return new ConfigGroup(id);
			case JSON_ELEMENT:
				return new ConfigEntryJsonElement(id);
			case NBT_BASE:
				return new ConfigEntryNBTBase(id);
			case BUTTON:
				return new ConfigEntryButton(id);
			case BOOLEAN:
				return new ConfigEntryBool(id, false);
			case INT:
				return new ConfigEntryInt(id, 0);
			case DOUBLE:
				return new ConfigEntryDouble(id, 0D);
			case STRING:
				return new ConfigEntryString(id, "");
			case ENUM:
				return new ConfigEntryEnum<>(id, null, null, false);
			case COLOR:
				return new ConfigEntryColor(id, null);
		}
		
		return null;
	}
	
	public boolean isArray()
	{ return this == INT_LIST || this == DOUBLE_LIST || this == STRING_LIST; }
}