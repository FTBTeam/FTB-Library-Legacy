package com.feed_the_beast.ftbl.lib.gui;

import com.feed_the_beast.ftbl.lib.icon.Icon;
import com.feed_the_beast.ftbl.lib.internal.FTBLibFinals;

public class GuiIcons
{
	public static final Icon BLUE_BUTTON = get("blue_button");
	public static final Icon UP = get("up");
	public static final Icon DOWN = get("down");
	public static final Icon LEFT = get("left");
	public static final Icon RIGHT = get("right");
	public static final Icon ACCEPT = get("accept");
	public static final Icon ADD = get("add");
	public static final Icon REMOVE = get("remove");
	public static final Icon INFO = get("info");
	public static final Icon ACCEPT_GRAY = get("accept_gray");
	public static final Icon ADD_GRAY = get("add_gray");
	public static final Icon REMOVE_GRAY = get("remove_gray");
	public static final Icon INFO_GRAY = get("info_gray");
	public static final Icon SETTINGS = get("settings");
	public static final Icon SETTINGS_RED = get("settings_red");
	public static final Icon CANCEL = get("cancel");
	public static final Icon BACK = get("back");
	public static final Icon CLOSE = get("close");
	public static final Icon REFRESH = get("refresh");
	public static final Icon PLAYER = get("player");
	public static final Icon PLAYER_GRAY = get("player_gray");
	public static final Icon ONLINE = get("online");
	public static final Icon SORT_AZ = get("sort_az");
	public static final Icon FRIENDS = get("friends");
	public static final Icon BUG = get("bug");
	public static final Icon JACKET = get("jacket");
	public static final Icon BED = get("bed");
	public static final Icon BELL = get("bell");
	public static final Icon COMPASS = get("compass");
	public static final Icon MAP = get("map");
	public static final Icon SHIELD = get("shield");
	public static final Icon ART = get("art");
	public static final Icon MONEY_BAG = get("money_bag");
	public static final Icon CONTROLLER = get("controller");
	public static final Icon FEATHER = get("feather");
	public static final Icon CAMERA = get("camera");
	public static final Icon INV_IO = get("inv_io");
	public static final Icon INV_IN = get("inv_in");
	public static final Icon INV_OUT = get("inv_out");
	public static final Icon INV_NONE = get("inv_none");
	public static final Icon RS_NONE = get("rs_none");
	public static final Icon RS_HIGH = get("rs_high");
	public static final Icon RS_LOW = get("rs_low");
	public static final Icon RS_PULSE = get("rs_pulse");
	public static final Icon SECURITY_PUBLIC = get("security_public");
	public static final Icon SECURITY_PRIVATE = get("security_private");
	public static final Icon SECURITY_TEAM = get("security_team");
	public static final Icon COLOR_BLANK = get("color_blank");
	public static final Icon COLOR_HSB = get("color_hsb");
	public static final Icon COLOR_RGB = get("color_rgb");
	public static final Icon ONLINE_RED = get("online_red");
	public static final Icon NOTES = get("notes");
	public static final Icon CHAT = get("chat");
	public static final Icon BIN = get("bin");
	public static final Icon MARKER = get("marker");
	public static final Icon BEACON = get("beacon");
	public static final Icon DICE = get("dice");
	public static final Icon DIAMOND = get("diamond");
	public static final Icon TIME = get("time");
	public static final Icon GLOBE = get("globe");
	public static final Icon MONEY = get("money");
	public static final Icon CHECK = get("check");
	public static final Icon STAR = get("star");
	public static final Icon HEART = get("heart");
	public static final Icon BOOK = Icon.getIcon("minecraft:items/book_normal");
	public static final Icon BOOK_RED = Icon.getIcon("minecraft:items/book_enchanted");
	public static final Icon BARRIER = Icon.getIcon("minecraft:items/barrier");
	public static final Icon TOGGLE_GAMEMODE = get("toggle_gamemode");
	public static final Icon TOGGLE_RAIN = get("toggle_rain");
	public static final Icon TOGGLE_DAY = get("toggle_day");
	public static final Icon TOGGLE_NIGHT = get("toggle_night");
	public static final Icon LOCK = get("lock");
	public static final Icon LOCK_OPEN = get("lock_open");

	private static Icon get(String id)
	{
		return Icon.getIcon(FTBLibFinals.MOD_ID + ":icons/" + id);
	}
}