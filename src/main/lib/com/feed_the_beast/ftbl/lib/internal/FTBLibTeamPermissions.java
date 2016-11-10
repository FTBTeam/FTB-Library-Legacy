package com.feed_the_beast.ftbl.lib.internal;

import com.feed_the_beast.ftbl.api.TeamPlayerPermission;
import net.minecraft.util.ResourceLocation;

/**
 * Created by LatvianModder on 10.11.2016.
 */
public class FTBLibTeamPermissions
{
    @TeamPlayerPermission
    public static final ResourceLocation CAN_JOIN = FTBLibFinals.get("can_join");

    @TeamPlayerPermission
    public static final ResourceLocation IS_ENEMY = FTBLibFinals.get("enemy");

    @TeamPlayerPermission
    public static final ResourceLocation EDIT_SETTINGS = FTBLibFinals.get("edit_settings");

    @TeamPlayerPermission
    public static final ResourceLocation EDIT_PERMISSIONS = FTBLibFinals.get("edit_permissions");

    @TeamPlayerPermission
    public static final ResourceLocation MANAGE_MEMBERS = FTBLibFinals.get("manage_members");

    @TeamPlayerPermission
    public static final ResourceLocation MANAGE_ALLIES = FTBLibFinals.get("manage_allies");
}