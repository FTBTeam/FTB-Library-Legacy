package ftb.lib.mod.net;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import ftb.lib.api.net.LMNetworkWrapper;
import ftb.lib.api.net.MessageLM_IO;
import ftb.lib.api.tile.IClientActionTile;
import latmod.lib.ByteCount;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

public class MessageClientTileAction extends MessageLM_IO
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
	
	@Override
	public LMNetworkWrapper getWrapper()
	{ return FTBLibNetHandler.NET; }
	
	@Override
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