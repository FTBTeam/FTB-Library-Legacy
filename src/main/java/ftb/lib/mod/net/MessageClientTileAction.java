package ftb.lib.mod.net;

import ftb.lib.api.net.*;
import ftb.lib.api.tile.IClientActionTile;
import latmod.lib.ByteCount;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.*;

public class MessageClientTileAction extends MessageLM
{
	public MessageClientTileAction() { super(ByteCount.INT); }
	
	public MessageClientTileAction(TileEntity t, String s, NBTTagCompound tag)
	{
		this();
		BlockPos pos = t.getPos();
		io.writeInt(pos.getX());
		io.writeInt(pos.getY());
		io.writeInt(pos.getZ());
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
		TileEntity te = ep.worldObj.getTileEntity(new BlockPos(x, y, z));
		
		if(te instanceof IClientActionTile) ((IClientActionTile) te).onClientAction(ep, action, data);
		
		return null;
	}
}