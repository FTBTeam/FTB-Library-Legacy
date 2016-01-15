package ftb.lib;

import com.mojang.authlib.GameProfile;
import ftb.lib.api.ILMPlayer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.relauncher.Side;

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
	
	public Side getSide()
	{ return side; }
	
	public int getPlayerID()
	{ return 0; }
	
	public boolean isOnline()
	{ return true; }
	
	public EntityPlayer getPlayer()
	{ return entity; }
	
	public GameProfile getProfile()
	{ return entity.getGameProfile(); }
	
	public boolean isFriendRaw(ILMPlayer p)
	{ return true; }
	
	public boolean isFriend(ILMPlayer p)
	{ return true; }
}
