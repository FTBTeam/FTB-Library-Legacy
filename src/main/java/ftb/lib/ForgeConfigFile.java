package ftb.lib;

public class ForgeConfigFile// implements IConfigFile // ConfigFile
{
	/*
	public final ConfigGroup configGroup;
	public final Configuration config;
	
	public ForgeConfigFile(String id, Configuration c)
	{
		configGroup = new ConfigGroup(id);
		configGroup.parentFile = this;
		config = c;
		
		for(String s : c.getCategoryNames())
		{
			ConfigGroup g = new ConfigGroup(s);
			ConfigCategory cat = c.getCategory(s);
			
			for(java.util.Map.Entry<String, Property> e0 : cat.entrySet())
			{
				ConfigEntry e = null;
				
				String s1 = e0.getKey();
				Property p = e0.getValue();
				
				if(p.isList())
				{
					if(p.isBooleanList()) ;
					else if(p.isIntList()) e = new ConfigEntryIntArray(s1, IntList.asList(p.getIntList()));
					else if(p.isDoubleList()) e = new ConfigEntryDoubleArray(s1, p.getDoubleList());
					else e = new ConfigEntryStringArray(s1, Arrays.asList(p.getStringList()));
				}
				else
				{
					if(p.isBooleanValue()) e = new ConfigEntryBool(s1, p.getBoolean());
					else if(p.isIntValue())
						e = new ConfigEntryInt(s1, new IntBounds(p.getInt(), Integer.parseInt(p.getMinValue()), Integer.parseInt(p.getMaxValue())));
					else if(p.isDoubleValue())
						e = new ConfigEntryDouble(s1, new DoubleBounds((float) p.getDouble(), (float) Double.parseDouble(p.getMinValue()), (float) Double.parseDouble(p.getMaxValue())));
					else e = new ConfigEntryString(s1, p.getString());
				}
				
				if(e != null)
				{
					g.add(e, false);
					e.setInfo(p.comment);
				}
			}
			
			configGroup.add(g, false);
		}
	}
	
	public ConfigGroup getGroup()
	{ return configGroup; }
	
	public void load()
	{ config.load(); }
	
	public void save()
	{ config.save(); }
	*/
}