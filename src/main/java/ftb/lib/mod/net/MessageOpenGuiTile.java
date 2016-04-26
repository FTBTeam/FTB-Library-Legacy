package ftb.lib.mod.net;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ftb.lib.api.client.FTBLibClient;
import ftb.lib.api.net.LMNetworkWrapper;
import ftb.lib.api.net.MessageLM_IO;
import ftb.lib.api.tile.IGuiTile;
import latmod.lib.ByteCount;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

public class MessageOpenGuiTile extends MessageLM_IO
{
	public MessageOpenGuiTile() { super(ByteCount.INT); }
	
	public MessageOpenGuiTile(TileEntity t, NBTTagCompound tag, int wid)
	{
		this();
		io.writeInt(t.xCoord);
		io.writeInt(t.yCoord);
		io.writeInt(t.zCoord);
		writeTag(tag);
		io.writeByte(wid);
	}
	
	@Override
	public LMNetworkWrapper getWrapper()
	{ return FTBLibNetHandler.NET; }
	
	@Override
	@SideOnly(Side.CLIENT)
	public IMessage onMessage(MessageContext ctx)
	{
		int x = io.readInt();
		int y = io.readInt();
		int z = io.readInt();
		
		TileEntity te = FTBLibClient.mc.theWorld.getTileEntity(x, y, z);
		
		if(te != null && !te.isInvalid() && te instanceof IGuiTile)
		{
			GuiScreen gui = ((IGuiTile) te).getGui(FTBLibClient.mc.thePlayer, readTag());
			
			if(gui != null)
			{
				FTBLibClient.openGui(gui);
				FTBLibClient.mc.thePlayer.openContainer.windowId = io.readUnsignedByte();
			}
		}
		
		return null;
	}
}