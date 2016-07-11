package com.feed_the_beast.ftbl.net;

import com.feed_the_beast.ftbl.api.net.LMNetworkWrapper;
import com.feed_the_beast.ftbl.api.net.MessageToClient;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumHand;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Created by LatvianModder on 11.07.2016.
 */
public class MessageUpdateHeldItem extends MessageToClient<MessageUpdateHeldItem>
{
    public NBTTagCompound data;
    public EnumHand hand;

    public MessageUpdateHeldItem()
    {
    }

    public MessageUpdateHeldItem(EntityPlayerMP player, EnumHand h)
    {
        hand = h;
        data = new NBTTagCompound();
        ItemStack is = player.getHeldItem(hand);

        if(is != null)
        {
            is.writeToNBT(data);
        }
    }

    @Override
    public LMNetworkWrapper getWrapper()
    {
        return FTBLibNetHandler.NET;
    }

    @Override
    public void toBytes(ByteBuf io)
    {
        io.writeBoolean(hand == EnumHand.MAIN_HAND);
        writeTag(io, data);
    }

    @Override
    public void fromBytes(ByteBuf io)
    {
        hand = io.readBoolean() ? EnumHand.MAIN_HAND : EnumHand.OFF_HAND;
        data = readTag(io);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void onMessage(MessageUpdateHeldItem m, Minecraft mc)
    {
        ItemStack is = mc.thePlayer.getHeldItem(m.hand);

        if(is != null)
        {
            is.readFromNBT(m.data);
        }
    }
}