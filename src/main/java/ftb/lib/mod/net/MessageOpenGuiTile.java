package ftb.lib.mod.net;

import ftb.lib.api.client.FTBLibClient;
import ftb.lib.api.gui.IGuiTile;
import ftb.lib.api.net.*;
import latmod.lib.ByteCount;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.*;
import net.minecraftforge.fml.relauncher.*;

public class MessageOpenGuiTile extends MessageLM
{
	public MessageOpenGuiTile() { super(ByteCount.INT); }
	
	public MessageOpenGuiTile(TileEntity t, NBTTagCompound tag, int wid)
	{
		this();
		BlockPos pos = t.getPos();
		io.writeInt(pos.getX());
		io.writeInt(pos.getY());
		io.writeInt(pos.getZ());
		writeTag(tag);
		io.writeByte(wid);
	}
	
	public LMNetworkWrapper getWrapper()
	{ return FTBLibNetHandler.NET_GUI; }
	
	@SideOnly(Side.CLIENT)
	public IMessage onMessage(MessageContext ctx)
	{
		int x = io.readInt();
		int y = io.readInt();
		int z = io.readInt();
		
		TileEntity te = FTBLibClient.mc.theWorld.getTileEntity(new BlockPos(x, y, z));
		
		if(te != null && !te.isInvalid() && te instanceof IGuiTile)
		{
			GuiScreen gui = ((IGuiTile) te).getGui(FTBLibClient.mc.thePlayer, readTag());
			
			if(gui != null)
			{
				FTBLibClient.mc.displayGuiScreen(gui);
				FTBLibClient.mc.thePlayer.openContainer.windowId = io.readUnsignedByte();
			}
		}
		
		return null;
	}
}