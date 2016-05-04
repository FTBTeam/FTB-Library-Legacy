package ftb.lib.mod.net;

import ftb.lib.LMNBTUtils;
import ftb.lib.api.client.FTBLibClient;
import ftb.lib.api.net.LMNetworkWrapper;
import ftb.lib.api.net.MessageLM;
import ftb.lib.api.tile.TileLM;
import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessageMarkTileDirty extends MessageLM<MessageMarkTileDirty>
{
	public int posX, posY, posZ;
	public NBTTagCompound data;
	
	public MessageMarkTileDirty() { }
	
	public MessageMarkTileDirty(TileLM t)
	{
		posX = t.getPos().getX();
		posY = t.getPos().getY();
		posZ = t.getPos().getZ();
		data = new NBTTagCompound();
		t.writeTileClientData(data);
		
		if(t.ownerID != null && t.useOwnerID())
		{
			LMNBTUtils.setUUID(data, "OID", t.ownerID, false);
		}
	}
	
	@Override
	public LMNetworkWrapper getWrapper()
	{ return FTBLibNetHandler.NET; }
	
	@Override
	public void fromBytes(ByteBuf io)
	{
		posX = io.readInt();
		posY = io.readInt();
		posZ = io.readInt();
		data = readTag(io);
	}
	
	@Override
	public void toBytes(ByteBuf io)
	{
		io.writeInt(posX);
		io.writeInt(posY);
		io.writeInt(posZ);
		writeTag(io, data);
	}
	
	@Override
	public IMessage onMessage(MessageMarkTileDirty m, MessageContext ctx)
	{
		TileEntity te = FTBLibClient.mc.theWorld.getTileEntity(new BlockPos(m.posX, m.posY, m.posZ));
		
		if(te instanceof TileLM)
		{
			TileLM t = (TileLM) te;
			t.ownerID = t.useOwnerID() ? LMNBTUtils.getUUID(data, "OID", false) : null;
			t.readTileClientData(m.data);
			t.onUpdatePacket();
		}
		
		return null;
	}
}