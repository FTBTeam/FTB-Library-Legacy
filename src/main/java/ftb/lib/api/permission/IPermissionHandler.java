package ftb.lib.api.permission;

import com.google.gson.JsonElement;
import com.mojang.authlib.GameProfile;

/**
 * Created by LatvianModder on 13.02.2016.
 */
public interface IPermissionHandler
{
	JsonElement handlePermission(ForgePermission permission, GameProfile profile);
}
