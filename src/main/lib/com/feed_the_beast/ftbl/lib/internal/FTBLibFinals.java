package com.feed_the_beast.ftbl.lib.internal;

import net.minecraft.util.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class FTBLibFinals
{
    public static final String MOD_ID = "ftbl";
    public static final Logger LOGGER = LogManager.getLogger("FTBLib");

    public static ResourceLocation get(String path)
    {
        return new ResourceLocation(MOD_ID, path);
    }
}