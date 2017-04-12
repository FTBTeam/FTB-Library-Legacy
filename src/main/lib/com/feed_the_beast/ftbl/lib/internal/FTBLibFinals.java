package com.feed_the_beast.ftbl.lib.internal;

import net.minecraft.util.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class FTBLibFinals
{
    public static final String MOD_ID = "ftbl";
    public static final Logger LOGGER = LogManager.getLogger("FTBLib");
    public static final String DEPENDENCIES = /*"required-after:Forge@["
            + net.minecraftforge.common.ForgeVersion.majorVersion + '.'
            + net.minecraftforge.common.ForgeVersion.minorVersion + '.'
            + net.minecraftforge.common.ForgeVersion.revisionVersion + '.'
            + net.minecraftforge.common.ForgeVersion.buildVersion + ",);" +
            */"after:Baubles;" +
            "after:JEI;" +
            "after:nei;" +
            "after:Waila;" +
            "after:MineTweaker3;" +
            "after:mcmultipart;" +
            "after:chiselsandbits";

    public static ResourceLocation get(String path)
    {
        return new ResourceLocation(MOD_ID, path);
    }
}