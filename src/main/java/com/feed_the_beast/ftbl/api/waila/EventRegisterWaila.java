package com.feed_the_beast.ftbl.api.waila;

import com.feed_the_beast.ftbl.api.tile.IWailaTile;
import com.feed_the_beast.ftbl.util.OtherMods;
import mcp.mobius.waila.api.IWailaRegistrar;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.common.eventhandler.Event;

import java.util.ArrayList;

public class EventRegisterWaila extends Event
{
	private static final ArrayList<WailaRegEntry> registry = new ArrayList<>();
	
	public void register(Class<?> block, BasicWailaHandler h)
	{ for(WailaType t : h.types) registry.add(new WailaRegEntry(block, h, t)); }
	
	@Optional.Method(modid = OtherMods.WAILA)
	public static void registerHandlers(IWailaRegistrar i)
	{
		EventRegisterWaila e = new EventRegisterWaila();
		e.register(IWailaTile.Stack.class, new WailaLMTile(e, WailaType.STACK));
		e.register(IWailaTile.Head.class, new WailaLMTile(e, WailaType.HEAD));
		e.register(IWailaTile.Body.class, new WailaLMTile(e, WailaType.BODY));
		e.register(IWailaTile.Tail.class, new WailaLMTile(e, WailaType.TAIL));
		MinecraftForge.EVENT_BUS.post(e);
		
		for(WailaRegEntry wre : registry)
		{
			if(wre.type == WailaType.STACK) { i.registerStackProvider(new WailaDataProvider(wre.handler), wre.block); }
			if(wre.type == WailaType.HEAD) { i.registerHeadProvider(new WailaDataProvider(wre.handler), wre.block); }
			if(wre.type == WailaType.BODY) { i.registerBodyProvider(new WailaDataProvider(wre.handler), wre.block); }
			if(wre.type == WailaType.TAIL) { i.registerTailProvider(new WailaDataProvider(wre.handler), wre.block); }
		}
	}
	
	private class WailaRegEntry
	{
		public final Class<?> block;
		public final BasicWailaHandler handler;
		public final WailaType type;
		
		public WailaRegEntry(Class<?> c, BasicWailaHandler h, WailaType t)
		{
			block = c;
			handler = h;
			type = t;
		}
	}
}