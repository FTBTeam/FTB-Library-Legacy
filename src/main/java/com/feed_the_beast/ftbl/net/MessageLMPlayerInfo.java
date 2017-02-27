package com.feed_the_beast.ftbl.net;

import com.feed_the_beast.ftbl.api_impl.ForgePlayer;
import com.feed_the_beast.ftbl.lib.net.LMNetworkWrapper;
import com.feed_the_beast.ftbl.lib.net.MessageToClient;
import com.feed_the_beast.ftbl.lib.util.LMNetUtils;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.fml.common.network.ByteBufUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class MessageLMPlayerInfo extends MessageToClient<MessageLMPlayerInfo>
{
    private UUID playerID;
    private List<ITextComponent> info;
    private Map<EntityEquipmentSlot, ItemStack> armor;

    public MessageLMPlayerInfo()
    {
    }

    public MessageLMPlayerInfo(ForgePlayer owner, ForgePlayer p)
    {
        playerID = p.getId();

        info = new ArrayList<>();
        p.getInfo(owner, info);

        armor = p.getArmor();
    }

    @Override
    public LMNetworkWrapper getWrapper()
    {
        return FTBLibNetHandler.NET;
    }

    @Override
    public void fromBytes(ByteBuf io)
    {
        playerID = LMNetUtils.readUUID(io);

        info = new ArrayList<>();

        int s = io.readUnsignedByte();

        for(int i = 0; i < s; i++)
        {
            info.add(LMNetUtils.readTextComponent(io));
        }

        armor = new HashMap<>();
        s = io.readUnsignedByte();

        for(int i = 0; i < s; i++)
        {
            EntityEquipmentSlot e = EntityEquipmentSlot.values()[io.readByte()];
            ItemStack is = ByteBufUtils.readItemStack(io);
            armor.put(e, is);
        }
    }

    @Override
    public void toBytes(ByteBuf io)
    {
        LMNetUtils.writeUUID(io, playerID);

        int infosize = Math.min(255, info.size());

        for(int i = 0; i < infosize; i++)
        {
            LMNetUtils.writeTextComponent(io, info.get(infosize));
        }

        io.writeByte(armor.size());

        for(Map.Entry<EntityEquipmentSlot, ItemStack> e : armor.entrySet())
        {
            io.writeByte(e.getKey().ordinal());
            ByteBufUtils.writeItemStack(io, e.getValue());
        }
    }

    @Override
    public void onMessage(MessageLMPlayerInfo m, EntityPlayer player)
    {
        //TODO: Open FriendsGUI
        /*
        p.receiveInfo(m.info);

        p.lastArmor.clear();
        p.lastArmor.putAll(m.armor);

        FTBLibClient.onGuiClientAction();
        */
    }
}