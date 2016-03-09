package ftb.lib.api;

import net.minecraft.entity.player.*;
import net.minecraftforge.fml.relauncher.*;

/**
 * Created by LatvianModder on 09.02.2016.
 */
public final class ForgePlayerTemp extends ForgePlayer
{
	public final Side side;
	public final EntityPlayer player;
	
	public ForgePlayerTemp(EntityPlayer ep)
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
	
	public ForgePlayerMP toPlayerMP()
	{ return null; }
	
	@SideOnly(Side.CLIENT)
	public ForgePlayerSP toPlayerSP()
	{ return null; }
	
	public ForgeWorld getWorld()
	{ return ForgeWorld.getFrom(side); }
}