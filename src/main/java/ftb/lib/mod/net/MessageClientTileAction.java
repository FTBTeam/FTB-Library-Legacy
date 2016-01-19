package ftb.lib.mod.net;

import cpw.mods.fml.common.network.simpleimpl.*;
import ftb.lib.api.net.*;
import ftb.lib.api.tile.IClientActionTile;
import latmod.lib.ByteCount;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

public class MessageClientTileAction extends MessageLM
{
	public MessageClientTileAction() { super(ByteCount.INT); }
	
	public MessageClientTileAction(TileEntity t, String s, NBTTagCompound tag)
	{
		this();
		io.writeInt(t.xCoord);
		io.writeInt(t.yCoord);
		io.writeInt(t.zCoord);
		io.writeUTF(s);
		writeTag(tag);
	}
	
	public LMNetworkWrapper getWrapper()
	{ return FTBLibNetHandler.NET_GUI; }
	
	public IMessage onMessage(MessageContext ctx)
	{
		int x = io.readInt();
		int y = io.readInt();
		int z = io.readInt();
		String action = io.readUTF();
		NBTTagCompound data = readTag();
		
		EntityPlayerMP ep = ctx.getServerHandler().playerEntity;
		TileEntity te = ep.worldObj.getTileEntity(x, y, z);
		
		if(te instanceof IClientActionTile) ((IClientActionTile) te).onClientAction(ep, action, data);
		
		return null;
	}
}