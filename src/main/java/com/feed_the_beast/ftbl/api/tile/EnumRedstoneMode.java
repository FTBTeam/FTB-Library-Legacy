package com.feed_the_beast.ftbl.api.tile;

import com.feed_the_beast.ftbl.api.gui.GuiIcons;
import com.feed_the_beast.ftbl.util.TextureCoords;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public enum EnumRedstoneMode
{
    DISABLED("disabled"),
    ACTIVE_HIGH("high"),
    ACTIVE_LOW("low");

    public static final EnumRedstoneMode[] VALUES = values();

    public final int ID;
    public final String uname;

    EnumRedstoneMode(String s)
    {
        ID = ordinal();
        uname = s;
    }

    public boolean cancel(boolean b)
    {
        if(this == DISABLED)
        {
            return false;
        }
        if(this == ACTIVE_HIGH && !b)
        {
            return true;
        }
        return this == ACTIVE_LOW && b;
    }

    public TextureCoords getIcon()
    {
        return GuiIcons.redstone[ordinal()];
    }

    @SideOnly(Side.CLIENT)
    public String getText()
    {
        return I18n.format("ftbl.redstonemode." + uname);
    }

    @SideOnly(Side.CLIENT)
    public String getTitle()
    {
        return I18n.format("ftbl.redstonemode");
    }
}