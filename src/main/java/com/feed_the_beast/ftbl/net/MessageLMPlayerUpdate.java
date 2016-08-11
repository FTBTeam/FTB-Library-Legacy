package com.feed_the_beast.ftbl.net;

import com.feed_the_beast.ftbl.api.ForgePlayerMP;
import com.feed_the_beast.ftbl.api.ForgePlayerSP;
import com.feed_the_beast.ftbl.api.ForgeWorldSP;
import com.feed_the_beast.ftbl.api.client.FTBLibClient;
import com.feed_the_beast.ftbl.api.net.LMNetworkWrapper;
import com.feed_the_beast.ftbl.api.net.MessageToClient;
import com.latmod.lib.util.LMNetUtils;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.UUID;

public class MessageLMPlayerUpdate extends MessageToClient<MessageLMPlayerUpdate>
{
    public UUID playerID;
    public boolean isSelf;
    public NBTTagCompound data;

    public MessageLMPlayerUpdate()
    {
    }

    public MessageLMPlayerUpdate(ForgePlayerMP p, boolean self)
    {
        this();
        playerID = p.getProfile().getId();
        isSelf = self;
        data = new NBTTagCompound();
        p.writeToNet(data, self);
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
        isSelf = io.readBoolean();
        data = LMNetUtils.readTag(io);
    }

    @Override
    public void toBytes(ByteBuf io)
    {
        LMNetUtils.writeUUID(io, playerID);
        io.writeBoolean(isSelf);
        LMNetUtils.writeTag(io, data);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void onMessage(MessageLMPlayerUpdate m, Minecraft mc)
    {
        ForgePlayerSP p = ForgeWorldSP.inst.getPlayer(m.playerID).toSP();
        p.readFromNet(m.data, m.isSelf);
        FTBLibClient.onGuiClientAction();
    }
}