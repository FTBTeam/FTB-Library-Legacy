package ftb.lib.api.players;

import net.minecraft.entity.player.*;
import net.minecraftforge.fml.relauncher.*;

/**
 * Created by LatvianModder on 09.02.2016.
 */
public final class LMPlayerTemp extends LMPlayer
{
	public final Side side;
	public final EntityPlayer player;
	
	public LMPlayerTemp(EntityPlayer ep)
	{
		super(ep.getGameProfile());
		side = (ep instanceof EntityPlayerMP) ? Side.SERVER : Side.CLIENT;
		player = ep;
	}
	
	public Side getSide()
	{ return side; }
	
	public boolean isOnline()
	{ return player != null; }
	
	public EntityPlayer getPlayer()
	{ return player; }
	
	public LMPlayerMP toPlayerMP()
	{ return null; }
	
	@SideOnly(Side.CLIENT)
	public LMPlayerSP toPlayerSP()
	{ return null; }
	
	public LMWorld getWorld()
	{ return LMWorld.getFrom(side); }
}