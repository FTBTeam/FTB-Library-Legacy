package ftb.lib.api;

import com.mojang.authlib.GameProfile;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.relauncher.Side;

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
	boolean allowCreativeInteractSecure();
}