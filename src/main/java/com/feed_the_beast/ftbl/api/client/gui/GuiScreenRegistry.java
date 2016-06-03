package com.feed_the_beast.ftbl.api.client.gui;

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
public class GuiScreenRegistry
{
    public static final Map<ResourceLocation, Entry> map = new HashMap<>();

    public interface Entry
    {
        @SideOnly(Side.CLIENT)
        GuiScreen openGui(EntityPlayer ep);
    }

    public static void register(ResourceLocation s, Entry e)
    {
        if(s != null && e != null && !map.containsKey(s))
        {
            map.put(s, e);
        }
    }

    @SideOnly(Side.CLIENT)
    public static GuiScreen openGui(EntityPlayer ep, ResourceLocation id)
    {
        Entry e = map.get(id);
        if(e != null)
        {
            return e.openGui(ep);
        }
        return null;
    }
}
