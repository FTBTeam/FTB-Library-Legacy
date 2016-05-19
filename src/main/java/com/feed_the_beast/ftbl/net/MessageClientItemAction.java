package com.feed_the_beast.ftbl.net;

import com.feed_the_beast.ftbl.api.item.IClientActionItem;
import com.feed_the_beast.ftbl.api.net.LMNetworkWrapper;
import com.feed_the_beast.ftbl.api.net.MessageToServer;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class MessageClientItemAction extends MessageToServer<MessageClientItemAction>
{
    public String action;
    public NBTTagCompound data;

    public MessageClientItemAction()
    {
    }

    public MessageClientItemAction(String s, NBTTagCompound tag)
    {
        action = s;
        data = tag;
    }

    @Override
    public LMNetworkWrapper getWrapper()
    {
        return FTBLibNetHandler.NET;
    }

    @Override
    public void fromBytes(ByteBuf io)
    {
        action = readString(io);
        data = readTag(io);
    }

    @Override
    public void toBytes(ByteBuf io)
    {
        writeString(io, action);
        writeTag(io, data);
    }

    @Override
    public void onMessage(MessageClientItemAction m, EntityPlayerMP ep)
    {
        ItemStack is = ep.inventory.mainInventory[ep.inventory.currentItem];

        if(is != null && is.getItem() instanceof IClientActionItem)
        {
            is = ((IClientActionItem) is.getItem()).onClientAction(is, ep, m.action, m.data);
        }

        if(is != null && is.stackSize <= 0)
        {
            is = null;
        }

        ep.inventory.mainInventory[ep.inventory.currentItem] = (is == null) ? null : is.copy();
        ep.inventory.markDirty();
        ep.openContainer.detectAndSendChanges();
    }
}