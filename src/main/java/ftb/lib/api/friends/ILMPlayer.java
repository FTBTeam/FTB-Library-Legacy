package ftb.lib.api.friends;

import com.mojang.authlib.GameProfile;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.entity.player.EntityPlayer;

/**
 * Created by LatvianModder on 15.01.2016.
 */
public interface ILMPlayer
{
	Side getSide();
	int getPlayerID();
	boolean isOnline();
	EntityPlayer getPlayer();
	GameProfile getProfile();
	boolean isFriendRaw(ILMPlayer p);
	boolean isFriend(ILMPlayer p);
	boolean allowInteractSecure();
}