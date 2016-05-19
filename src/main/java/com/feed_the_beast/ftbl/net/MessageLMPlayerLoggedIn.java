package com.feed_the_beast.ftbl.net;

import com.feed_the_beast.ftbl.api.ForgePlayerMP;
import com.feed_the_beast.ftbl.api.ForgePlayerSP;
import com.feed_the_beast.ftbl.api.ForgeWorldSP;
import com.feed_the_beast.ftbl.api.net.LMNetworkWrapper;
import com.feed_the_beast.ftbl.api.net.MessageToClient;
import com.mojang.authlib.GameProfile;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.UUID;

public class MessageLMPlayerLoggedIn extends MessageToClient<MessageLMPlayerLoggedIn>
{
    public UUID playerID;
    public String playerName;
    public boolean isFirst;
    public NBTTagCompound data;

    public MessageLMPlayerLoggedIn()
    {
    }

    public MessageLMPlayerLoggedIn(ForgePlayerMP p, boolean first, boolean self)
    {
        playerID = p.getProfile().getId();
        playerName = p.getProfile().getName();
        isFirst = first;
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
        playerID = readUUID(io);
        playerName = readString(io);
        isFirst = io.readBoolean();
        data = readTag(io);
    }

    @Override
    public void toBytes(ByteBuf io)
    {
        writeUUID(io, playerID);
        writeString(io, playerName);
        io.writeBoolean(isFirst);
        writeTag(io, data);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void onMessage(MessageLMPlayerLoggedIn m, Minecraft mc)
    {
        if(ForgeWorldSP.inst != null)
        {
            ForgePlayerSP p = ForgeWorldSP.inst.getPlayer(m.playerID);
            if(p == null)
            {
                p = new ForgePlayerSP(new GameProfile(m.playerID, m.playerName));
            }
            p.init();
            p.readFromNet(m.data, p.isMCPlayer());
            ForgeWorldSP.inst.playerMap.put(p.getProfile().getId(), p);
            p.onLoggedIn(m.isFirst);
        }
    }
}