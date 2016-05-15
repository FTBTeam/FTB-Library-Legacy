package com.feed_the_beast.ftbl.api;

/**
 * Created by LatvianModder on 14.05.2016.
 */
public enum EnumSelf
{
	SELF,
	OTHER,
	BOTH;
	
	public boolean isSelf()
	{ return this == SELF || this == BOTH; }
	
	public boolean isOther()
	{ return this == OTHER || this == BOTH; }
	
	public boolean equalsType(EnumSelf t)
	{
		switch(t)
		{
			case SELF:
				return isSelf();
			case OTHER:
				return isOther();
			case BOTH:
				return true;
			default:
				return false;
		}
	}
}