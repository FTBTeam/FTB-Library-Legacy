package ftb.lib.api.gui;

import cpw.mods.fml.relauncher.*;
import latmod.lib.LMListUtils;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;

import java.util.HashMap;

/**
 * Created by LatvianModder on 17.01.2016.
 */
@SideOnly(Side.CLIENT)
public class GuiScreenRegistry
{
	private static final HashMap<String, Entry> map = new HashMap<>();
	
	public static void register(String s, Entry e)
	{
		if(s != null && e != null && !map.containsKey(s)) map.put(s, e);
	}
	
	public static String[] getKeys()
	{ return LMListUtils.toStringArray(map.keySet()); }
	
	public static GuiScreen openGui(EntityPlayer ep, String id)
	{
		Entry e = map.get(id);
		if(e != null) return e.openGui(ep);
		return null;
	}
	
	public static interface Entry
	{
		GuiScreen openGui(EntityPlayer ep);
	}
}
