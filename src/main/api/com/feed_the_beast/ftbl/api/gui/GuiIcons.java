package com.feed_the_beast.ftbl.api.gui;

import com.feed_the_beast.ftbl.FTBLibFinals;
import com.latmod.lib.EnumNameMap;
import com.latmod.lib.ITextureCoords;
import net.minecraft.util.ResourceLocation;

public enum GuiIcons implements ITextureCoords
{
    UP,
    DOWN,
    LEFT,
    RIGHT,
    ACCEPT,
    ADD,
    REMOVE,
    INFO,
    ACCEPT_GRAY,
    ADD_GRAY,
    REMOVE_GRAY,
    INFO_GRAY,
    SETTINGS,
    SETTINGS_RED,
    CANCEL,
    BACK,
    CLOSE,
    REFRESH,
    PLAYER,
    PLAYER_GRAY,
    ONLINE,
    SORT_AZ,
    FRIENDS,
    BUG,
    JACKET,
    BED,
    BELL,
    COMPASS,
    MAP,
    SHIELD,
    ART,
    MONEY_BAG,
    CONTROLLER,
    FEATHER,
    CAMERA,
    INV_IO,
    INV_IN,
    INV_OUT,
    INV_NONE,
    RS_NONE,
    RS_HIGH,
    RS_LOW,
    RS_PULSE,
    SECURITY_PUBLIC,
    SECURITY_PRIVATE,
    SECURITY_TEAM,
    COLOR_BLANK,
    COLOR_HSB,
    COLOR_RGB,
    ONLINE_RED,
    NOTES,
    CHAT,
    BIN,
    MARKER,
    BEACON,
    DICE,
    DIAMOND,
    TIME,
    GLOBE,
    MONEY,
    CHECK,
    STAR,
    HEART,
    BOOK,
    BOOK_RED,
    TOGGLE_GAMEMODE,
    TOGGLE_RAIN,
    TOGGLE_DAY,
    TOGGLE_NIGHT;

    public static final ITextureCoords[] VALUES = values();

    private ResourceLocation texture;

    GuiIcons()
    {
        texture = new ResourceLocation(FTBLibFinals.MOD_ID, "textures/icons/" + EnumNameMap.createName(this) + ".png");
    }

    @Override
    public ResourceLocation getTexture()
    {
        return texture;
    }

    @Override
    public double getMinU()
    {
        return 0D;
    }

    @Override
    public double getMinV()
    {
        return 0D;
    }

    @Override
    public double getMaxU()
    {
        return 1D;
    }

    @Override
    public double getMaxV()
    {
        return 1D;
    }

    @Override
    public boolean isValid()
    {
        return true;
    }
}