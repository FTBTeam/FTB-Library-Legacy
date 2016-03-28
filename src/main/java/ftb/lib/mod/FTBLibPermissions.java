package ftb.lib.mod;

import ftb.lib.api.permissions.ForgePermission;
import latmod.lib.annotations.Info;

/**
 * Created by LatvianModder on 08.03.2016.
 */
public class FTBLibPermissions
{
	@ForgePermission(false)
	@Info("Enabled access to protected blocks / chunks")
	public static final String interact_secure = "ftbl.interact_secure";
}
