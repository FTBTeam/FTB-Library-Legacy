package ftb.lib.api.permission;

import latmod.lib.config.ConfigData;

import java.lang.reflect.Field;
import java.util.*;

/**
 * Created by LatvianModder on 16.02.2016.
 */
public class ForgePermissionRegistry
{
	private static final Map<String, ForgePermission> permissions = new HashMap<>();
	static IPermissionHandler handler = null;
	
	public static final void setHandler(IPermissionHandler h)
	{
		if(h != null /*&& handler == null*/)
		{
			handler = h;
		}
	}
	
	/**
	 * id can contain '*', but then it must be the last character. If its either null or "*", all values are returned
	 */
	public static Collection<ForgePermission> values(String id)
	{
		if(id == null || id.isEmpty() || id.charAt(0) == '*') return permissions.values();
		else
		{
			id = ForgePermission.getID(id);
			
			int index = id.indexOf('*');
			
			if(index == -1)
			{
				ForgePermission p = permissions.get(id);
				return (p == null) ? Collections.EMPTY_SET : Collections.singleton(p);
			}
			
			id = id.substring(0, index - 1);
			
			ArrayList<ForgePermission> list = new ArrayList<>();
			
			for(ForgePermission p : permissions.values())
			{
				if(p.ID.startsWith(id)) list.add(p);
			}
			
			return list;
		}
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
				
				if(obj instanceof ForgePermission)
				{
					ForgePermission p = (ForgePermission) obj;
					
					if(permissions.containsKey(p.ID))
					{
						throw new RuntimeException("Duplicate permission ID found: " + p.ID + " (" + p.getClass().getName() + " & " + permissions.get(p.ID).getClass().getName() + ")");
					}
					
					permissions.put(p.ID, p);
					ConfigData.inject(f, null, p);
				}
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
}
