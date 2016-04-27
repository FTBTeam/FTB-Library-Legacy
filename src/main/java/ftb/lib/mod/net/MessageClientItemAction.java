package ftb.lib.mod.net;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import ftb.lib.api.item.IClientActionItem;
import ftb.lib.api.net.LMNetworkWrapper;
import ftb.lib.api.net.MessageLM;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class MessageClientItemAction extends MessageLM<MessageClientItemAction>
{
	public String id;
	public NBTTagCompound tag;
	
	public MessageClientItemAction() { }
	
	public MessageClientItemAction(String s, NBTTagCompound t)
	{
		id = s;
		tag = t;
	}
	
	@Override
	public LMNetworkWrapper getWrapper()
	{ return FTBLibNetHandler.NET; }
	
	@Override
	public void fromBytes(ByteBuf io)
	{
		id = readString(io);
		tag = readTag(io);
	}
	
	@Override
	public void toBytes(ByteBuf io)
	{
		writeString(io, id);
		writeTag(io, tag);
	}
	
	@Override
	public IMessage onMessage(MessageClientItemAction m, MessageContext ctx)
	{
		EntityPlayerMP ep = ctx.getServerHandler().playerEntity;
		
		ItemStack is = ep.inventory.mainInventory[ep.inventory.currentItem];
		
		if(is != null && is.getItem() instanceof IClientActionItem)
		{
			is = ((IClientActionItem) is.getItem()).onClientAction(is, ep, m.id, m.tag);
		}
		
		if(is != null && is.stackSize <= 0) is = null;
		
		ep.inventory.mainInventory[ep.inventory.currentItem] = (is == null) ? null : is.copy();
		ep.inventory.markDirty();
		ep.openContainer.detectAndSendChanges();
		return null;
	}
}