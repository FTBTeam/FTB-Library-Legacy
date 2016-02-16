package ftb.lib.api.permissions;

import com.google.gson.JsonElement;
import net.minecraft.entity.player.EntityPlayerMP;

/**
 * Created by LatvianModder on 13.02.2016.
 */
public interface IPermissionHandler
{
	JsonElement handlePermission(ForgePermission permission, EntityPlayerMP player);
}
