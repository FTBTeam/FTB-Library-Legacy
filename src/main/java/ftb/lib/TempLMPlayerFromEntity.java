package ftb.lib;

import com.mojang.authlib.GameProfile;
import cpw.mods.fml.relauncher.Side;
import ftb.lib.api.friends.ILMPlayer;
import net.minecraft.entity.player.EntityPlayer;

/**
 * Created by LatvianModder on 15.01.2016.
 */
public class TempLMPlayerFromEntity implements ILMPlayer
{
	public final Side side;
	public final EntityPlayer entity;
	
	public TempLMPlayerFromEntity(Side s, EntityPlayer ep)
	{
		side = s;
		entity = ep;
	}
	
	@Override
	public Side getSide()
	{ return side; }
	
	@Override
	public boolean isOnline()
	{ return true; }
	
	@Override
	public EntityPlayer getPlayer()
	{ return entity; }
	
	@Override
	public GameProfile getProfile()
	{ return entity.getGameProfile(); }
	
	@Override
	public boolean isFriendRaw(ILMPlayer p)
	{ return true; }
	
	@Override
	public boolean isFriend(ILMPlayer p)
	{ return true; }
	
	@Override
	public boolean allowInteractSecure()
	{ return false; }
}
