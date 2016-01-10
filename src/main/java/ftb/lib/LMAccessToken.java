package ftb.lib;

import net.minecraft.entity.player.EntityPlayerMP;

import java.util.*;

public class LMAccessToken
{
	private static final HashMap<UUID, Long> tokens = new HashMap<>();
	private static final Random random = new Random();
	
	public static long generate(EntityPlayerMP ep)
	{
		long token = random.nextLong();
		tokens.put(ep.getUniqueID(), token);
		return token;
	}
	
	public static boolean equals(EntityPlayerMP ep, long token)
	{
		Long t = tokens.get(ep.getUniqueID());
		
		if(t != null && t.longValue() == token)
		{
			tokens.remove(ep.getUniqueID());
			return true;
		}
		
		return false;
	}
}