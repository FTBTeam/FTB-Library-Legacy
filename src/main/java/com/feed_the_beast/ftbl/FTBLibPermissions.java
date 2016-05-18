package com.feed_the_beast.ftbl;

import com.feed_the_beast.ftbl.api.permissions.ForgePermissionRegistry;
import latmod.lib.annotations.Info;

/**
 * Created by LatvianModder on 08.03.2016.
 */
public class FTBLibPermissions
{
    @Info("Enabled access to protected blocks / chunks")
    public static final String interact_secure = ForgePermissionRegistry.registerPermission("ftbl.interact_secure", false);
    
    public static void init()
    {
    }
}
