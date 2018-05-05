package com.feed_the_beast.ftblib.lib.util;

import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;

import java.util.EnumMap;

/**
 * @author LatvianModder
 */
public class EnumDyeColorHelper // ItemDye
{
	public static final EnumMap<EnumDyeColor, EnumDyeColorHelper> HELPERS = new EnumMap<>(EnumDyeColor.class);

	static
	{
		for (EnumDyeColor c : EnumDyeColor.values())
		{
			HELPERS.put(c, new EnumDyeColorHelper(c));
		}
	}

	private final EnumDyeColor dye;
	private final String langKey;
	private final String oreName;

	private EnumDyeColorHelper(EnumDyeColor col)
	{
		dye = col;
		langKey = "item.fireworksCharge." + col.getUnlocalizedName();
		oreName = StringUtils.firstUppercase(col.getUnlocalizedName());
	}

	public static EnumDyeColorHelper get(EnumDyeColor dye)
	{
		return HELPERS.get(dye);
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