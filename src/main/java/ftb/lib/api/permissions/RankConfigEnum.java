package ftb.lib.api.permissions;

import com.google.gson.JsonPrimitive;
import com.mojang.authlib.GameProfile;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by LatvianModder on 14.02.2016.
 */
public class RankConfigEnum<E extends Enum<E>> extends RankConfig
{
	private final Map<String, E> enumMap;
	
	public RankConfigEnum(String id, E defPlayerValue, E defOPValue, E[] validEnums, boolean addNull)
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
	
	public E getEnum(GameProfile profile)
	{
		return enumMap.get(get(profile).getAsString());
	}
}
