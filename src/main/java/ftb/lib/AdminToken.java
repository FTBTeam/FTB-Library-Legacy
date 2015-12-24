package ftb.lib;

import net.minecraft.entity.player.EntityPlayerMP;

import java.util.Random;

public class AdminToken
{
	private static final Random random = new Random();
	private static final String TAG = "TempAT";
	
	public static long generate(EntityPlayerMP ep)
	{
		long token = random.nextLong();
		ep.getEntityData().setLong(TAG, token);
		return token;
	}
	
	public static boolean equals(EntityPlayerMP ep, long token)
	{
		if(ep.getEntityData().getLong(TAG) == token)
		{
			ep.getEntityData().removeTag(TAG);
			return true;
		}
		
		return false;
	}
}