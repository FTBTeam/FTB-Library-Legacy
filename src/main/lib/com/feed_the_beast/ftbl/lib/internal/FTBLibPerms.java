package com.feed_the_beast.ftbl.lib.internal;

import net.minecraftforge.server.permission.DefaultPermissionLevel;
import net.minecraftforge.server.permission.PermissionAPI;

/**
 * @author LatvianModder
 */
public class FTBLibPerms
{
    public static final String SHOW_OP_BUTTONS = "ftbl.op_buttons";

    public static void init()
    {
        PermissionAPI.registerNode(SHOW_OP_BUTTONS, DefaultPermissionLevel.OP, "Node for displaying OP-specific buttons in inventory");
    }
}