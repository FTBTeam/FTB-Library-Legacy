package com.feed_the_beast.ftbl.util;

import net.minecraftforge.common.MinecraftForge;

public class EventBusHelper
{
	public static void register(Object o)
	{
		if(o == null) { return; }
		MinecraftForge.EVENT_BUS.register(o);
	}
	
	public static void unregister(Object o)
	{
		if(o == null) { return; }
		MinecraftForge.EVENT_BUS.unregister(o);
	}
}