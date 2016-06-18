package com.feed_the_beast.ftbl.util;

import com.feed_the_beast.ftbl.api.LangKey;
import com.latmod.lib.util.LMStringUtils;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;

public class EnumDyeColorHelper // ItemDye
{
    public static final EnumDyeColorHelper[] HELPERS = new EnumDyeColorHelper[EnumDyeColor.values().length];

    static
    {
        for(EnumDyeColor c : EnumDyeColor.values())
        {
            HELPERS[c.ordinal()] = new EnumDyeColorHelper(c);
        }
    }

    public final EnumDyeColor dye;
    public final LangKey langKey;
    public final String dyeName;
    public final String glassName;
    public final String paneName;

    EnumDyeColorHelper(EnumDyeColor col)
    {
        dye = col;
        langKey = new LangKey("item.fireworksCharge." + col.getUnlocalizedName());

        String s = LMStringUtils.firstUppercase(col.getName());
        dyeName = "dye" + s;
        glassName = "blockGlass" + s;
        paneName = "paneGlass" + s;
    }

    public static EnumDyeColorHelper get(EnumDyeColor dye)
    {
        return HELPERS[dye.ordinal()];
    }

    public ItemStack getDye(int s)
    {
        return new ItemStack(Items.DYE, s, dye.ordinal());
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
}