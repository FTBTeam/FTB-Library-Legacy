package ftb.lib.api.config;

import latmod.lib.*;

/**
 * Created by LatvianModder on 30.03.2016.
 */
public enum ConfigType
{
	CUSTOM,
	GROUP,
	BOOLEAN,
	INT,
	DOUBLE,
	STRING,
	ENUM,
	INT_ARRAY,
	STRING_ARRAY,
	COLOR;
	
	public static final ConfigType[] VALUES = values();
	
	public ConfigEntry createNew(String id)
	{
		switch(this)
		{
			case CUSTOM:
				return new ConfigEntryCustom(id);
			case GROUP:
				return new ConfigGroup(id);
			case BOOLEAN:
				return new ConfigEntryBool(id, false);
			case INT:
				return new ConfigEntryInt(id, 0);
			case DOUBLE:
				return new ConfigEntryDouble(id, 0D);
			case STRING:
				return new ConfigEntryString(id, null);
			case ENUM:
				return new ConfigEntryEnumExtended(id);
			case INT_ARRAY:
				return new ConfigEntryIntArray(id, (IntList) null);
			case STRING_ARRAY:
				return new ConfigEntryStringArray(id);
			case COLOR:
				return new ConfigEntryColor(id, new LMColor.RGB());
			default:
				return null;
		}
	}
	
	public boolean isArray()
	{ return this == INT_ARRAY || this == STRING_ARRAY; }
}