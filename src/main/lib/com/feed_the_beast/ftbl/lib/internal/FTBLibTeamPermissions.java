package com.feed_the_beast.ftbl.lib.internal;

import com.feed_the_beast.ftbl.api.TeamPlayerPermission;

/**
 * Created by LatvianModder on 10.11.2016.
 */
public class FTBLibTeamPermissions
{
    @TeamPlayerPermission
    public static final String CAN_JOIN = "can_join";

    @TeamPlayerPermission
    public static final String IS_ENEMY = "enemy";

    @TeamPlayerPermission
    public static final String EDIT_SETTINGS = "edit_settings";

    @TeamPlayerPermission
    public static final String EDIT_PERMISSIONS = "edit_permissions";

    @TeamPlayerPermission
    public static final String MANAGE_MEMBERS = "manage_members";

    @TeamPlayerPermission
    public static final String MANAGE_ALLIES = "manage_allies";
}