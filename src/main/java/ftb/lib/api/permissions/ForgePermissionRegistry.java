package ftb.lib.api.permissions;

import com.mojang.authlib.GameProfile;
import ftb.lib.FTBLib;
import latmod.lib.annotations.AnnotationHelper;

import java.lang.reflect.Field;
import java.util.*;

/**
 * Created by LatvianModder on 16.02.2016.
 */
public class ForgePermissionRegistry
{
	private static final Map<String, ForgePermissionContainer> permissionContainers = new HashMap<>();
	private static final Map<String, RankConfig> rankConfig = new HashMap<>();
	static IPermissionHandler handler = null;
	
	public static void setHandler(IPermissionHandler h)
	{
		if(h != null /*&& handler == null*/)
		{
			handler = h;
		}
	}
	
	public static boolean hasPermission(String permission, GameProfile profile)
	{
		permission = ForgePermissionContainer.getID(permission);
		
		if(handler != null)
		{
			Boolean b = handler.handlePermission(permission, profile);
		}
		
		if(FTBLib.isOP(profile))
		{
			return true;
		}
		
		ForgePermissionContainer c = permissionContainers.get(permission);
		return (c == null) ? false : c.playerValue;
	}
	
	/**
	 * c - Class that contains fields. parent - null in case all fields are static
	 */
	public static void register(Class<?> c)
	{
		try
		{
			for(Field f : c.getDeclaredFields())
			{
				f.setAccessible(true);
				Object obj = f.get(null);
				
				if(obj instanceof String)
				{
					String id = ForgePermissionContainer.getID(obj.toString());
					
					ForgePermissionContainer container;
					ForgePermission permission = f.getAnnotation(ForgePermission.class);
					
					if(permission == null)
					{
						container = new ForgePermissionContainer(id, true);
					}
					else
					{
						container = new ForgePermissionContainer(id, permission.value());
					}
					
					AnnotationHelper.inject(f, null, container);
					permissionContainers.put(id, container);
				}
				else if(obj instanceof RankConfig)
				{
					RankConfig p = (RankConfig) obj;
					
					if(rankConfig.containsKey(p.getID()))
					{
						throw new RuntimeException("Duplicate RankConfig ID found: " + p.getID() + " (" + p.getClass().getName() + " & " + permissionContainers.get(p.getID()).getClass().getName() + ")");
					}
					
					rankConfig.put(p.getID(), p);
				}
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	public static Collection<ForgePermissionContainer> getRegistredPermissions()
	{ return permissionContainers.values(); }
	
	public static Collection<RankConfig> getRegistredConfig()
	{ return rankConfig.values(); }
	
	public static RankConfig getConfig(String s)
	{ return rankConfig.get(s); }
}