package ftb.lib.api.players;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.*;

/**
 * Created by LatvianModder on 09.02.2016.
 */
@SideOnly(Side.CLIENT)
public class ForgePlayerSPSelf extends ForgePlayerSP
{
	public ForgePlayerSPSelf(GameProfile p)
	{
		super(p);
	}
	
	public boolean isClientPlayer()
	{ return true; }
	
	public boolean isOnline()
	{ return true; }
	
	public EntityPlayer getPlayer()
	{ return Minecraft.getMinecraft().thePlayer; }
	
	public ForgePlayerSPSelf toPlayerSPSelf()
	{ return this; }
	
	/*public Rank getRank()
	{
		if(rank == null) rank = new Rank("Client");
		return rank;
	}*/
	
	public void readFromNet(NBTTagCompound tag, boolean self) // LMPlayerServer
	{
		super.readFromNet(tag, self);
	}
}
