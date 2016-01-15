package ftb.lib.mod.net;

import ftb.lib.api.*;
import ftb.lib.api.gui.IClientActionItem;
import latmod.lib.ByteCount;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.simpleimpl.*;

public class MessageClientItemAction extends MessageLM
{
	public MessageClientItemAction() { super(ByteCount.INT); }
	
	public MessageClientItemAction(String s, NBTTagCompound tag)
	{
		this();
		io.writeUTF(s);
		writeTag(tag);
	}
	
	public LMNetworkWrapper getWrapper()
	{ return FTBLibNetHandler.NET_GUI; }
	
	public IMessage onMessage(MessageContext ctx)
	{
		String action = io.readUTF();
		
		EntityPlayerMP ep = ctx.getServerHandler().playerEntity;
		
		ItemStack is = ep.inventory.mainInventory[ep.inventory.currentItem];
		
		if(is != null && is.getItem() instanceof IClientActionItem)
			is = ((IClientActionItem) is.getItem()).onClientAction(is, ep, action, readTag());
		
		if(is != null && is.stackSize <= 0) is = null;
		
		ep.inventory.mainInventory[ep.inventory.currentItem] = (is == null) ? null : is.copy();
		ep.inventory.markDirty();
		ep.openContainer.detectAndSendChanges();
		return null;
	}
}