package ftb.lib.api;

import net.minecraft.util.EnumChatFormatting;

/**
 * Created by LatvianModder on 15.01.2016.
 */
public enum FriendStatus
{
	NONE(EnumChatFormatting.WHITE),
	FRIEND(EnumChatFormatting.GREEN),
	INVITING(EnumChatFormatting.GOLD),
	INVITED(EnumChatFormatting.BLUE);
	
	public final EnumChatFormatting color;
	
	FriendStatus(EnumChatFormatting c)
	{
		color = c;
	}
	
	public boolean isFriend()
	{ return this == FRIEND; }
	
	public boolean isFriendRaw()
	{ return this == FRIEND || this == INVITING; }
	
	public static FriendStatus get(ForgePlayer p1, ForgePlayer p2)
	{
		if(p1 == null || p2 == null) return NONE;
		
		boolean b1 = p1.isFriendRaw(p2);
		boolean b2 = p2.isFriendRaw(p1);
		
		if(b1 && b2) return FRIEND;
		if(b1) return INVITING;
		if(b2) return INVITED;
		return NONE;
	}
	
	public static int compare(ForgePlayer owner, ForgePlayer p1, ForgePlayer p2)
	{
		FriendStatus s0 = get(owner, p1);
		FriendStatus s1 = get(owner, p2);
		
		if(s0 == NONE && s1 != NONE) return 1;
		if(s0 != NONE && s1 == NONE) return -1;
		
		//if(s0 == s1)
		return Integer.compare(s0.ordinal(), s1.ordinal());
	}
}
