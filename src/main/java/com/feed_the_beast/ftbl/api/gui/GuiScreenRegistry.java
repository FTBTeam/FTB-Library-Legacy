package com.feed_the_beast.ftbl.api.gui;

import latmod.lib.LMListUtils;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by LatvianModder on 17.01.2016.
 */
@SideOnly(Side.CLIENT)
public class GuiScreenRegistry
{
	private static final Map<ResourceLocation, Entry> map = new HashMap<>();
	
	public static void register(ResourceLocation s, Entry e)
	{
		if(s != null && e != null && !map.containsKey(s)) { map.put(s, e); }
	}
	
	public static String[] getKeys()
	{ return LMListUtils.toStringArray(map.keySet()); }
	
	public static GuiScreen openGui(EntityPlayer ep, String id)
	{
		Entry e = map.get(id);
		if(e != null) { return e.openGui(ep); }
		return null;
	}
	
	public interface Entry
	{
		GuiScreen openGui(EntityPlayer ep);
	}
}
