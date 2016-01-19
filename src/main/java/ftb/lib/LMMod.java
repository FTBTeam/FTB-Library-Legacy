package ftb.lib;

import cpw.mods.fml.common.*;
import ftb.lib.mod.*;
import net.minecraft.util.ResourceLocation;
import org.apache.logging.log4j.*;

import java.lang.annotation.*;
import java.lang.reflect.Field;
import java.util.HashMap;

public class LMMod
{
	public static final HashMap<String, LMMod> modsMap = new HashMap<>();
	
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.FIELD)
	public static @interface Instance
	{
		String value();
	}
	
	private static LMMod getLMMod(Object o)
	{
		if(o == null) return null;
		
		try
		{
			Field[] fields = o.getClass().getDeclaredFields();
			
			for(Field f : fields)
			{
				if(f.isAnnotationPresent(Instance.class))
				{
					Instance m = f.getAnnotation(Instance.class);
					
					if(m.value() != null)
					{
						LMMod mod = new LMMod(m.value());
						f.set(o, mod);
						return mod;
					}
				}
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		return null;
	}
	
	public static void init(Object o)
	{
		LMMod mod = getLMMod(o);
		if(mod == null)
		{
			FTBLib.logger.warn("LMMod failed to load from " + o);
			return;
		}
		modsMap.put(mod.modID, mod);
		if(FTBLibFinals.DEV) FTBLib.logger.info("LMMod '" + mod.toString() + "' loaded");
	}
	
	// End of static //
	
	public final String modID;
	public final String lowerCaseModID;
	public final String assets;
	private ModContainer modContainer;
	
	public Logger logger;
	
	public LMMod(String id)
	{
		modID = id;
		lowerCaseModID = modID.toLowerCase();
		assets = lowerCaseModID + ":";
		
		logger = LogManager.getLogger(modID);
	}
	
	public ModContainer getModContainer()
	{
		if(modContainer == null) modContainer = Loader.instance().getModObjectList().inverse().get(modID);
		return modContainer;
	}
	
	public String toFullString()
	{ return modID + '-' + Loader.MC_VERSION + '-' + modContainer.getDisplayVersion(); }
	
	public String toString()
	{ return modID; }
	
	public ResourceLocation getLocation(String s)
	{ return new ResourceLocation(lowerCaseModID, s); }
	
	public String translate(String s, Object... args)
	{ return FTBLibMod.proxy.translate(assets + s, args); }
}