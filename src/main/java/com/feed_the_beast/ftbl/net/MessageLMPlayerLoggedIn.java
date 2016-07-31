package com.feed_the_beast.ftbl.net;

import com.feed_the_beast.ftbl.api.ForgePlayerMP;
import com.feed_the_beast.ftbl.api.ForgePlayerSP;
import com.feed_the_beast.ftbl.api.ForgePlayerSPSelf;
import com.feed_the_beast.ftbl.api.ForgeWorldSP;
import com.feed_the_beast.ftbl.api.events.ForgePlayerEvent;
import com.feed_the_beast.ftbl.api.net.LMNetworkWrapper;
import com.feed_the_beast.ftbl.api.net.MessageToClient;
import com.feed_the_beast.ftbl.util.LMNetUtils;
import com.mojang.authlib.GameProfile;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.UUID;

public class MessageLMPlayerLoggedIn extends MessageToClient<MessageLMPlayerLoggedIn>
{
    public UUID playerID;
    public String playerName;
    public boolean isFirst, self;
    public NBTTagCompound data;

    public MessageLMPlayerLoggedIn()
    {
    }

    public MessageLMPlayerLoggedIn(ForgePlayerMP p, boolean first, boolean s)
    {
        playerID = p.getProfile().getId();
        playerName = p.getProfile().getName();
        isFirst = first;
        self = s;
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
        playerName = LMNetUtils.readString(io);
        isFirst = io.readBoolean();
        self = io.readBoolean();
        data = LMNetUtils.readTag(io);
    }

    @Override
    public void toBytes(ByteBuf io)
    {
        LMNetUtils.writeUUID(io, playerID);
        LMNetUtils.writeString(io, playerName);
        io.writeBoolean(isFirst);
        io.writeBoolean(self);
        LMNetUtils.writeTag(io, data);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void onMessage(MessageLMPlayerLoggedIn m, Minecraft mc)
    {
        if(m.self)
        {
            ForgeWorldSP.inst = new ForgeWorldSP();
            ForgeWorldSP.inst.clientPlayer = new ForgePlayerSPSelf(mc.getSession().getProfile());
            ForgeWorldSP.inst.playerMap.put(ForgeWorldSP.inst.clientPlayer.getProfile().getId(), ForgeWorldSP.inst.clientPlayer);
        }

        ForgePlayerSP p = ForgeWorldSP.inst.getPlayer(m.playerID);
        if(p == null)
        {
            p = new ForgePlayerSP(new GameProfile(m.playerID, m.playerName));
        }

        p.readFromNet(m.data, p.isMCPlayer());
        ForgeWorldSP.inst.playerMap.put(p.getProfile().getId(), p);

        p.onLoggedIn(m.isFirst);

        if(p.equalsPlayer(ForgeWorldSP.inst.clientPlayer))
        {
            MinecraftForge.EVENT_BUS.post(new ForgePlayerEvent.LoggedIn(p, m.isFirst));
        }
    }
}