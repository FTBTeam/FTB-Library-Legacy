package com.feed_the_beast.ftbl.api.gui;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

/**
 * Created by LatvianModder on 17.01.2016.
 */
public class GuiScreenRegistry
{
    public static final Map<ResourceLocation, Supplier<GuiScreen>> map = new HashMap<>();

    public static void register(@Nonnull ResourceLocation id, @Nonnull Supplier<GuiScreen> s)
    {
        if(!map.containsKey(id))
        {
            map.put(id, s);
        }
    }

    @Nullable
    @SideOnly(Side.CLIENT)
    public static GuiScreen openGui(@Nonnull ResourceLocation id)
    {
        Supplier<GuiScreen> e = map.get(id);
        return (e == null) ? null : e.get();
    }
}
