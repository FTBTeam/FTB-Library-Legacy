package ftb.lib.mod.net;

import ftb.lib.api.net.*;
import ftb.lib.api.tile.IClientActionTile;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.*;

public class MessageClientTileAction extends MessageLM<MessageClientTileAction>
{
	public int posX, posY, posZ;
	public String action;
	public NBTTagCompound data;
	
	public MessageClientTileAction() { }
	
	public MessageClientTileAction(TileEntity t, String s, NBTTagCompound tag)
	{
		posX = t.getPos().getX();
		posY = t.getPos().getY();
		posZ = t.getPos().getZ();
		action = (s == null) ? "" : s;
		data = tag;
	}
	
	public LMNetworkWrapper getWrapper()
	{ return FTBLibNetHandler.NET_GUI; }
	
	public void fromBytes(ByteBuf io)
	{
		posX = io.readInt();
		posY = io.readInt();
		posZ = io.readInt();
		action = readString(io);
		data = readTag(io);
	}
	
	public void toBytes(ByteBuf io)
	{
		io.writeInt(posX);
		io.writeInt(posY);
		io.writeInt(posZ);
		writeString(io, action);
		writeTag(io, data);
	}
	
	public IMessage onMessage(MessageClientTileAction m, MessageContext ctx)
	{
		EntityPlayerMP ep = ctx.getServerHandler().playerEntity;
		TileEntity te = ep.worldObj.getTileEntity(new BlockPos(m.posX, m.posY, m.posZ));
		
		if(te instanceof IClientActionTile) ((IClientActionTile) te).onClientAction(ep, m.action, m.data);
		
		return null;
	}
}