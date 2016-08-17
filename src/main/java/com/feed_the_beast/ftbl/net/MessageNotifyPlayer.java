package com.feed_the_beast.ftbl.net;

import com.feed_the_beast.ftbl.api.INotification;
import com.feed_the_beast.ftbl.api.net.LMNetworkWrapper;
import com.feed_the_beast.ftbl.api.net.MessageToClient;
import com.feed_the_beast.ftbl.api_impl.Notification;
import com.feed_the_beast.ftbl.client.ClientNotifications;
import com.feed_the_beast.ftbl.client.FTBLibModClient;
import com.feed_the_beast.ftbl.util.EnumNotificationDisplay;
import com.latmod.lib.util.LMNetUtils;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiNewChat;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.event.HoverEvent;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class MessageNotifyPlayer extends MessageToClient<MessageNotifyPlayer>
{
    private int ID;
    private NBTTagCompound data;

    public MessageNotifyPlayer()
    {
    }

    public MessageNotifyPlayer(INotification n)
    {
        ID = n.getID();

        data = new NBTTagCompound();

        if(!n.getText().isEmpty())
        {
            NBTTagList list = new NBTTagList();

            for(ITextComponent t : n.getText())
            {
                list.appendTag(new NBTTagString(ITextComponent.Serializer.componentToJson(t)));
            }

            data.setTag("L", list);
        }

        if(n.getTimer() != 3000)
        {
            data.setInteger("T", n.getTimer());
        }

        if(n.getItem() != null)
        {
            NBTTagCompound nbt1 = new NBTTagCompound();
            n.getItem().writeToNBT(nbt1);
            data.setTag("I", nbt1);
        }

        if(n.getColor() != 0xA0A0A0)
        {
            data.setInteger("C", n.getColor());
        }
    }

    @Override
    public LMNetworkWrapper getWrapper()
    {
        return FTBLibNetHandler.NET;
    }

    @Override
    public void fromBytes(ByteBuf io)
    {
        ID = io.readInt();
        data = LMNetUtils.readTag(io);
    }

    @Override
    public void toBytes(ByteBuf io)
    {
        io.writeInt(ID);
        LMNetUtils.writeTag(io, data);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void onMessage(MessageNotifyPlayer m, Minecraft mc)
    {
        EnumNotificationDisplay e = FTBLibModClient.notifications.get();
        Notification n = new Notification(m.ID);

        if(m.data.hasKey("L"))
        {
            NBTTagList list = m.data.getTagList("L", Constants.NBT.TAG_STRING);

            for(int i = 0; i < list.tagCount(); i++)
            {
                n.addText(ITextComponent.Serializer.jsonToComponent(list.getStringTagAt(i)));
            }
        }

        if(m.data.hasKey("T"))
        {
            n.setTimer(m.data.getInteger("T"));
        }

        if(m.data.hasKey("I"))
        {
            n.setItem(ItemStack.loadItemStackFromNBT(m.data.getCompoundTag("I")));
        }

        if(m.data.hasKey("C"))
        {
            n.setColor(m.data.getInteger("C"));
        }

        if(e == EnumNotificationDisplay.SCREEN)
        {
            ClientNotifications.add(n);
            return;
        }

        List<ITextComponent> list = n.getText();

        if(e != EnumNotificationDisplay.OFF && !list.isEmpty())
        {
            if(list.size() > 1)
            {
                list.get(0).getStyle().setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, list.get(1)));
            }

            GuiNewChat chat = Minecraft.getMinecraft().ingameGUI.getChatGUI();

            if(e == EnumNotificationDisplay.CHAT)
            {
                chat.printChatMessageWithOptionalDeletion(list.get(0), 42059283 + n.getID());
            }
            else
            {
                chat.printChatMessage(list.get(0));
            }
        }
    }
}