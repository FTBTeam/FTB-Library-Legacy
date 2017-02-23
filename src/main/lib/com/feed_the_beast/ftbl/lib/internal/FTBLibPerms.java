package com.feed_the_beast.ftbl.lib.internal;

import net.minecraftforge.server.permission.DefaultPermissionLevel;
import net.minecraftforge.server.permission.PermissionAPI;

/**
 * Created by LatvianModder on 10.11.2016.
 */
public class FTBLibPerms
{
    public static final String TEAM_CAN_JOIN = "can_join";
    public static final String TEAM_IS_ALLY = "ally";
    public static final String TEAM_IS_ENEMY = "enemy";
    public static final String TEAM_EDIT_SETTINGS = "edit_settings";
    public static final String TEAM_EDIT_PERMISSIONS = "edit_permissions";
    public static final String TEAM_MANAGE_MEMBERS = "manage_members";
    public static final String TEAM_MANAGE_ALLIES = "manage_allies";
    public static final String TEAM_MANAGE_ENEMIES = "manage_enemies";

    public static final String SHOW_OP_BUTTONS = "ftbl.op_buttons";

    public static void init()
    {
        PermissionAPI.registerNode(SHOW_OP_BUTTONS, DefaultPermissionLevel.OP, "Node for displaying OP-specific buttons in inventory");
    }
}