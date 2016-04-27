package ftb.lib.mod.net;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ftb.lib.api.client.FTBLibClient;
import ftb.lib.api.net.LMNetworkWrapper;
import ftb.lib.api.net.MessageLM;
import ftb.lib.api.tile.IGuiTile;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

public class MessageOpenGuiTile extends MessageLM<MessageOpenGuiTile>
{
	public int x, y, z, windowID;
	public NBTTagCompound tag;
	
	public MessageOpenGuiTile() { }
	
	public MessageOpenGuiTile(TileEntity te, NBTTagCompound t, int wid)
	{
		x = te.xCoord;
		y = te.yCoord;
		z = te.zCoord;
		windowID = wid;
		tag = t;
	}
	
	@Override
	public LMNetworkWrapper getWrapper()
	{ return FTBLibNetHandler.NET; }
	
	@Override
	public void fromBytes(ByteBuf io)
	{
		x = io.readInt();
		y = io.readInt();
		z = io.readInt();
		windowID = io.readInt();
		tag = readTag(io);
	}
	
	@Override
	public void toBytes(ByteBuf io)
	{
		io.writeInt(x);
		io.writeInt(y);
		io.writeInt(z);
		io.writeInt(windowID);
		writeTag(io, tag);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public IMessage onMessage(MessageOpenGuiTile m, MessageContext ctx)
	{
		TileEntity te = FTBLibClient.mc.theWorld.getTileEntity(m.x, m.y, m.z);
		
		if(te != null && te instanceof IGuiTile)
		{
			GuiScreen gui = ((IGuiTile) te).getGui(FTBLibClient.mc.thePlayer, m.tag);
			
			if(gui != null)
			{
				FTBLibClient.openGui(gui);
				FTBLibClient.mc.thePlayer.openContainer.windowId = m.windowID;
			}
		}
		
		return null;
	}
}