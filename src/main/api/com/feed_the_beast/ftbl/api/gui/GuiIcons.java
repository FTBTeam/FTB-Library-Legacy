package com.feed_the_beast.ftbl.api.gui;

import com.feed_the_beast.ftbl.FTBLibFinals;
import com.latmod.lib.TextureCoords;
import net.minecraft.util.ResourceLocation;

import java.util.HashMap;
import java.util.Map;

public class GuiIcons
{
    public static final Map<String, TextureCoords> iconMap = new HashMap<>();
    public static final TextureCoords UP = getIcon("up");
    public static final TextureCoords DOWN = getIcon("down");
    public static final TextureCoords LEFT = getIcon("left");
    public static final TextureCoords RIGHT = getIcon("right");
    public static final TextureCoords ACCEPT = getIcon("accept");
    public static final TextureCoords ADD = getIcon("add");
    public static final TextureCoords REMOVE = getIcon("remove");
    public static final TextureCoords INFO = getIcon("info");
    public static final TextureCoords ACCEPT_GRAY = getIcon("accept_gray");
    public static final TextureCoords ADD_GRAY = getIcon("add_gray");
    public static final TextureCoords REMOVE_GRAY = getIcon("remove_gray");
    public static final TextureCoords INFO_GRAY = getIcon("info_gray");
    public static final TextureCoords SETTINGS = getIcon("settings");
    public static final TextureCoords SETTINGS_RED = getIcon("settings_red");
    public static final TextureCoords CANCEL = getIcon("cancel");
    public static final TextureCoords BACK = getIcon("back");
    public static final TextureCoords CLOSE = getIcon("close");
    public static final TextureCoords REFRESH = getIcon("refresh");
    public static final TextureCoords PLAYER = getIcon("player");
    public static final TextureCoords PLAYER_GRAY = getIcon("player_gray");
    public static final TextureCoords ONLINE = getIcon("online");
    public static final TextureCoords SORT_AZ = getIcon("sort_az");
    public static final TextureCoords FRIENDS = getIcon("friends");
    public static final TextureCoords BUG = getIcon("bug");
    public static final TextureCoords JACKET = getIcon("jacket");
    public static final TextureCoords BED = getIcon("bed");
    public static final TextureCoords BELL = getIcon("bell");
    public static final TextureCoords COMPASS = getIcon("compass");
    public static final TextureCoords MAP = getIcon("map");
    public static final TextureCoords SHIELD = getIcon("shield");
    public static final TextureCoords ART = getIcon("art");
    public static final TextureCoords MONEY_BAG = getIcon("money_bag");
    public static final TextureCoords CONTROLLER = getIcon("controller");
    public static final TextureCoords FEATHER = getIcon("feather");
    public static final TextureCoords CAMERA = getIcon("camera");
    public static final TextureCoords[] INV = {getIcon("inv_io"), getIcon("inv_in"), getIcon("inv_out"), getIcon("inv_none")};
    public static final TextureCoords[] REDSTONE = {getIcon("rs_none"), getIcon("rs_high"), getIcon("rs_low")};
    public static final TextureCoords REDSTONE_PULSE = getIcon("rs_pulse");
    public static final TextureCoords[] SECURITY = {getIcon("security_public"), getIcon("security_private"), getIcon("security_team")};
    public static final TextureCoords COLOR_BLANK = getIcon("color_blank");
    public static final TextureCoords COLOR_HSB = getIcon("color_hsb");
    public static final TextureCoords COLOR_RGB = getIcon("color_rgb");
    public static final TextureCoords ONLINE_RED = getIcon("online_red");
    public static final TextureCoords NOTES = getIcon("notes");
    public static final TextureCoords CHAT = getIcon("chat");
    public static final TextureCoords BIN = getIcon("bin");
    public static final TextureCoords MARKER = getIcon("marker");
    public static final TextureCoords BEACON = getIcon("beacon");
    public static final TextureCoords DICE = getIcon("dice");
    public static final TextureCoords DIAMOND = getIcon("diamond");
    public static final TextureCoords TIME = getIcon("time");
    public static final TextureCoords GLOBE = getIcon("globe");
    public static final TextureCoords MONEY = getIcon("money");
    public static final TextureCoords CHECK = getIcon("check");
    public static final TextureCoords STAR = getIcon("star");
    public static final TextureCoords HEART = getIcon("heart");
    public static final TextureCoords BOOK = getIcon("book");
    public static final TextureCoords BOOK_RED = getIcon("book_red");
    public static final TextureCoords TOGGLE_GAMEMODE = getIcon("toggle_gamemode");
    public static final TextureCoords TOGGLE_RAIN = getIcon("toggle_rain");
    public static final TextureCoords TOGGLE_DAY = getIcon("toggle_day");
    public static final TextureCoords TOGGLE_NIGHT = getIcon("toggle_night");

    private static TextureCoords getIcon(String s)
    {
        TextureCoords r = TextureCoords.fromUV(new ResourceLocation(FTBLibFinals.MOD_ID, "textures/icons/" + s + ".png"));
        iconMap.put(s, r);
        return r;
    }
}