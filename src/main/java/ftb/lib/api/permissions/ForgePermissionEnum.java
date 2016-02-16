package ftb.lib.api.permissions;

import com.google.gson.JsonPrimitive;
import net.minecraft.entity.player.EntityPlayerMP;

import java.util.*;

/**
 * Created by LatvianModder on 14.02.2016.
 */
public class ForgePermissionEnum<E extends Enum<E>> extends ForgePermission
{
	private final Map<String, E> enumMap;
	
	public ForgePermissionEnum(String id, E defPlayerValue, E defOPValue, E[] validEnums, boolean addNull)
	{
		super(id, new JsonPrimitive(getName(defPlayerValue)), new JsonPrimitive(getName(defOPValue)));
		enumMap = new HashMap<>();
		
		for(E e : validEnums)
		{
			enumMap.put(getName(e), e);
		}
		
		if(addNull)
		{
			enumMap.put("-", null);
		}
	}
	
	private static String getName(Enum<?> e)
	{ return e == null ? "-" : e.name().toLowerCase(); }
	
	public E getEnum(EntityPlayerMP player)
	{
		return enumMap.get(getString(player));
	}
}
