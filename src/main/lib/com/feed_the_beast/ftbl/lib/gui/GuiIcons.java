package com.feed_the_beast.ftbl.lib.gui;

import com.feed_the_beast.ftbl.api.gui.IDrawableObject;
import com.feed_the_beast.ftbl.lib.client.ImageProvider;
import com.feed_the_beast.ftbl.lib.internal.FTBLibFinals;

public class GuiIcons
{
	//@formatter:off
    public static final IDrawableObject
	BLUE_BUTTON = get("blue_button"),
    UP = get("up"),
    DOWN = get("down"),
    LEFT = get("left"),
    RIGHT = get("right"),
    ACCEPT = get("accept"),
    ADD = get("add"),
    REMOVE = get("remove"),
    INFO = get("info"),
    ACCEPT_GRAY = get("accept_gray"),
    ADD_GRAY = get("add_gray"),
    REMOVE_GRAY = get("remove_gray"),
    INFO_GRAY = get("info_gray"),
    SETTINGS = get("settings"),
    SETTINGS_RED = get("settings_red"),
    CANCEL = get("cancel"),
    BACK = get("back"),
    CLOSE = get("close"),
    REFRESH = get("refresh"),
    PLAYER = get("player"),
    PLAYER_GRAY = get("player_gray"),
    ONLINE = get("online"),
    SORT_AZ = get("sort_az"),
    FRIENDS = get("friends"),
    BUG = get("bug"),
    JACKET = get("jacket"),
    BED = get("bed"),
    BELL = get("bell"),
    COMPASS = get("compass"),
    MAP = get("map"),
    SHIELD = get("shield"),
    ART = get("art"),
    MONEY_BAG = get("money_bag"),
    CONTROLLER = get("controller"),
    FEATHER = get("feather"),
    CAMERA = get("camera"),
    INV_IO = get("inv_io"),
    INV_IN = get("inv_in"),
    INV_OUT = get("inv_out"),
    INV_NONE = get("inv_none"),
    RS_NONE = get("rs_none"),
    RS_HIGH = get("rs_high"),
    RS_LOW = get("rs_low"),
    RS_PULSE = get("rs_pulse"),
    SECURITY_PUBLIC = get("security_public"),
    SECURITY_PRIVATE = get("security_private"),
    SECURITY_TEAM = get("security_team"),
    COLOR_BLANK = get("color_blank"),
    COLOR_HSB = get("color_hsb"),
    COLOR_RGB = get("color_rgb"),
    ONLINE_RED = get("online_red"),
    NOTES = get("notes"),
    CHAT = get("chat"),
    BIN = get("bin"),
    MARKER = get("marker"),
    BEACON = get("beacon"),
    DICE = get("dice"),
    DIAMOND = get("diamond"),
    TIME = get("time"),
    GLOBE = get("globe"),
    MONEY = get("money"),
    CHECK = get("check"),
    STAR = get("star"),
    HEART = get("heart"),
	BOOK = ImageProvider.get("minecraft:items/book_normal"),
	BOOK_RED = ImageProvider.get("minecraft:items/book_enchanted"),
	BARRIER = ImageProvider.get("minecraft:items/barrier"),
    TOGGLE_GAMEMODE = get("toggle_gamemode"),
    TOGGLE_RAIN = get("toggle_rain"),
    TOGGLE_DAY = get("toggle_day"),
			TOGGLE_NIGHT = get("toggle_night"),
			LOCK = get("lock"),
			LOCK_OPEN = get("lock_open");
    //@formatter:on

	private static IDrawableObject get(String id)
	{
		return ImageProvider.get(FTBLibFinals.MOD_ID + ":icons/" + id);
	}
}