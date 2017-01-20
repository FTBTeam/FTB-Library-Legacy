package com.feed_the_beast.ftbl.lib.gui;

import com.feed_the_beast.ftbl.api.gui.IImageProvider;
import com.feed_the_beast.ftbl.lib.client.FTBLibClient;
import com.feed_the_beast.ftbl.lib.internal.FTBLibFinals;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public enum GuiIcons implements IImageProvider
{
    UP("up"),
    DOWN("down"),
    LEFT("left"),
    RIGHT("right"),
    ACCEPT("accept"),
    ADD("add"),
    REMOVE("remove"),
    INFO("info"),
    ACCEPT_GRAY("accept_gray"),
    ADD_GRAY("add_gray"),
    REMOVE_GRAY("remove_gray"),
    INFO_GRAY("info_gray"),
    SETTINGS("settings"),
    SETTINGS_RED("settings_red"),
    CANCEL("cancel"),
    BACK("back"),
    CLOSE("close"),
    REFRESH("refresh"),
    PLAYER("player"),
    PLAYER_GRAY("player_gray"),
    ONLINE("online"),
    SORT_AZ("sort_az"),
    FRIENDS("friends"),
    BUG("bug"),
    JACKET("jacket"),
    BED("bed"),
    BELL("bell"),
    COMPASS("compass"),
    MAP("map"),
    SHIELD("shield"),
    ART("art"),
    MONEY_BAG("money_bag"),
    CONTROLLER("controller"),
    FEATHER("feather"),
    CAMERA("camera"),
    INV_IO("inv_io"),
    INV_IN("inv_in"),
    INV_OUT("inv_out"),
    INV_NONE("inv_none"),
    RS_NONE("rs_none"),
    RS_HIGH("rs_high"),
    RS_LOW("rs_low"),
    RS_PULSE("rs_pulse"),
    SECURITY_PUBLIC("security_public"),
    SECURITY_PRIVATE("security_private"),
    SECURITY_TEAM("security_team"),
    COLOR_BLANK("color_blank"),
    COLOR_HSB("color_hsb"),
    COLOR_RGB("color_rgb"),
    ONLINE_RED("online_red"),
    NOTES("notes"),
    CHAT("chat"),
    BIN("bin"),
    MARKER("marker"),
    BEACON("beacon"),
    DICE("dice"),
    DIAMOND("diamond"),
    TIME("time"),
    GLOBE("globe"),
    MONEY("money"),
    CHECK("check"),
    STAR("star"),
    HEART("heart"),
    BOOK("book"),
    BOOK_RED("book_red"),
    TOGGLE_GAMEMODE("toggle_gamemode"),
    TOGGLE_RAIN("toggle_rain"),
    TOGGLE_DAY("toggle_day"),
    TOGGLE_NIGHT("toggle_night");

    public static final GuiIcons[] VALUES = values();

    private final ResourceLocation texture;

    GuiIcons(String s)
    {
        texture = FTBLibFinals.get("textures/icons/" + s + ".png");
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