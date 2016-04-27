package ftb.lib.mod.net;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import ftb.lib.api.net.LMNetworkWrapper;
import ftb.lib.api.net.MessageLM;
import ftb.lib.api.tile.IClientActionTile;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

public class MessageClientTileAction extends MessageLM<MessageClientTileAction>
{
	public int x, y, z;
	public String id;
	public NBTTagCompound tag;
	
	public MessageClientTileAction() { }
	
	public MessageClientTileAction(TileEntity te, String s, NBTTagCompound t)
	{
		x = te.xCoord;
		y = te.yCoord;
		z = te.zCoord;
		id = s;
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
		id = readString(io);
		tag = readTag(io);
	}
	
	@Override
	public void toBytes(ByteBuf io)
	{
		io.writeInt(x);
		io.writeInt(y);
		io.writeInt(z);
		writeString(io, id);
		writeTag(io, tag);
	}
	
	@Override
	public IMessage onMessage(MessageClientTileAction m, MessageContext ctx)
	{
		EntityPlayerMP ep = ctx.getServerHandler().playerEntity;
		TileEntity te = ep.worldObj.getTileEntity(m.x, m.y, m.z);
		
		if(te instanceof IClientActionTile)
		{
			((IClientActionTile) te).onClientAction(ep, m.id, m.tag);
		}
		
		return null;
	}
}