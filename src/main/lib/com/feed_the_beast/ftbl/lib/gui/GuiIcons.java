package com.feed_the_beast.ftbl.lib.gui;

import com.feed_the_beast.ftbl.api.gui.IImageProvider;
import com.feed_the_beast.ftbl.lib.EnumNameMap;
import com.feed_the_beast.ftbl.lib.client.FTBLibClient;
import com.feed_the_beast.ftbl.lib.internal.FTBLibFinals;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public enum GuiIcons implements IImageProvider
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

    public static final GuiIcons[] VALUES = values();

    private final ResourceLocation texture;

    GuiIcons()
    {
        texture = FTBLibFinals.get("textures/icons/" + EnumNameMap.createName(this) + ".png");
    }

    @Override
    public boolean isValid()
    {
        return true;
    }

    @Override
    public ResourceLocation getImage()
    {
        return texture;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public ITextureObject bindTexture()
    {
        return FTBLibClient.bindTexture(texture);
    }
}