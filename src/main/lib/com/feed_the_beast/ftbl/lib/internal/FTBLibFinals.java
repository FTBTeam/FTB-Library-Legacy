package com.feed_the_beast.ftbl.lib.internal;

import net.minecraft.util.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class FTBLibFinals
{
	public static final String MOD_ID = "ftbl";
	public static final String MOD_NAME = "FTBLib";
	public static final String VERSION = "@VERSION@";
	public static final Logger LOGGER = LogManager.getLogger(MOD_NAME);

	public static final String BAUBLES = "baubles";
	public static final String JEI = "jei";
	public static final String NEI = "nei";
	public static final String MC_MULTIPART = "mcmultipart";
	public static final String CHISELS_AND_BITS = "chiselsandbits";
	public static final String ICHUN_UTIL = "ichunutil";
	public static final String FORESTRY = "forestry";

	public static final String DEPENDENCIES = "after:" + BAUBLES + ";after:" + JEI + ";after:" + NEI + ";after:" + MC_MULTIPART + ";after:" + CHISELS_AND_BITS + ";after:" + ICHUN_UTIL;

	public static ResourceLocation get(String path)
	{
		return new ResourceLocation(MOD_ID, path);
	}
}