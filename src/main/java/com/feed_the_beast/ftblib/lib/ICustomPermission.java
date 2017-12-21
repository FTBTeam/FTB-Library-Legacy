package com.feed_the_beast.ftblib.lib;

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