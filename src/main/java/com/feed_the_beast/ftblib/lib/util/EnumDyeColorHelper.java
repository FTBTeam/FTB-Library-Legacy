package com.feed_the_beast.ftblib.lib.util;

import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;

/**
 * @author LatvianModder
 */
public class EnumDyeColorHelper // ItemDye
{
	public static final EnumDyeColorHelper[] HELPERS = new EnumDyeColorHelper[EnumDyeColor.values().length];

	static
	{
		for (EnumDyeColor c : EnumDyeColor.values())
		{
			HELPERS[c.ordinal()] = new EnumDyeColorHelper(c);
		}
	}

	private final EnumDyeColor dye;
	private final String langKey;
	private final String oreName;

	private EnumDyeColorHelper(EnumDyeColor col)
	{
		dye = col;
		langKey = "item.fireworksCharge." + col.getTranslationKey();
		oreName = StringUtils.firstUppercase(col.getTranslationKey());
	}

	public static EnumDyeColorHelper get(EnumDyeColor dye)
	{
		return HELPERS[dye.ordinal()];
	}

	public ItemStack getDye(int s)
	{
		return new ItemStack(Items.DYE, s, dye.getDyeDamage());
	}

	@Override
	public String toString()
	{
		return dye.getName();
	}

	@Override
	public int hashCode()
	{
		return dye.ordinal();
	}

	public EnumDyeColor getDye()
	{
		return dye;
	}

	public String getLangKey()
	{
		return langKey;
	}

	public String getOreName()
	{
		return oreName;
	}

	public String getDyeName()
	{
		return "dye" + getOreName();
	}

	public String getGlassName()
	{
		return "blockGlass" + getOreName();
	}

	public String getPaneName()
	{
		return "paneGlass" + getOreName();
	}
}