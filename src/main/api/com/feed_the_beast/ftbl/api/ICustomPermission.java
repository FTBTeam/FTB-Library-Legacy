package com.feed_the_beast.ftbl.api;

/**
 * @author LatvianModder
 */
public interface ICustomPermission
{
	default void setCustomPermissionPrefix(String prefix)
	{
	}

	String getCustomPermission();
}