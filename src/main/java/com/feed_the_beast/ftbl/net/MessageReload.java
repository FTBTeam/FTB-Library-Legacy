package com.feed_the_beast.ftbl.net;

import com.feed_the_beast.ftbl.FTBLibLang;
import com.feed_the_beast.ftbl.FTBLibMod;
import com.feed_the_beast.ftbl.FTBLibNotifications;
import com.feed_the_beast.ftbl.api.ForgePlayerMP;
import com.feed_the_beast.ftbl.api.ForgeWorldMP;
import com.feed_the_beast.ftbl.api.ForgeWorldSP;
import com.feed_the_beast.ftbl.api.PackModes;
import com.feed_the_beast.ftbl.api.events.ForgeWorldEvent;
import com.feed_the_beast.ftbl.api.events.ReloadEvent;
import com.feed_the_beast.ftbl.api.net.LMNetworkWrapper;
import com.feed_the_beast.ftbl.api.net.MessageToClient;
import com.feed_the_beast.ftbl.api.notification.ClientNotifications;
import com.feed_the_beast.ftbl.api.notification.Notification;
import com.feed_the_beast.ftbl.util.LMNetUtils;
import com.feed_the_beast.ftbl.util.ReloadType;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class MessageReload extends MessageToClient<MessageReload>
{
    public int typeID;
    public boolean login;
    public NBTTagCompound syncData;
    public NBTTagCompound worldData;

    public MessageReload()
    {
    }

    public MessageReload(ReloadType t, ForgePlayerMP self, boolean l)
    {
        typeID = t.ordinal();
        login = l;
        worldData = new NBTTagCompound();
        ForgeWorldMP.inst.writeDataToNet(worldData, self, l);
        syncData = ForgeWorldEvent.Sync.generateData(self, login);
    }

    public static void reloadClient(long ms, ReloadType type, boolean login)
    {
        if(ms == 0L)
        {
            ms = System.currentTimeMillis();
        }

        PackModes.reload();
        EntityPlayer ep = FTBLibMod.proxy.getClientPlayer();
        MinecraftForge.EVENT_BUS.post(new ReloadEvent(ForgeWorldSP.inst, ep, type, login));

        if(!login)
        {
            FTBLibLang.reload_client.printChat(ep, (System.currentTimeMillis() - ms) + "ms");
        }

        FTBLibMod.logger.info("Current Mode: " + ForgeWorldSP.inst.getMode());
    }

    @Override
    public LMNetworkWrapper getWrapper()
    {
        return FTBLibNetHandler.NET;
    }

    @Override
    public void fromBytes(ByteBuf io)
    {
        typeID = io.readUnsignedByte();
        login = io.readBoolean();
        worldData = LMNetUtils.readTag(io);
        syncData = LMNetUtils.readTag(io);
    }

    @Override
    public void toBytes(ByteBuf io)
    {
        io.writeByte(typeID);
        io.writeBoolean(login);
        LMNetUtils.writeTag(io, worldData);
        LMNetUtils.writeTag(io, syncData);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void onMessage(MessageReload m, Minecraft mc)
    {
        long ms = System.currentTimeMillis();

        ReloadType type = ReloadType.values()[m.typeID];

        ForgeWorldSP.inst.readDataFromNet(m.worldData, m.login);
        ForgeWorldEvent.Sync.readData(m.syncData, m.login);

        //TODO: new EventFTBWorldClient(ForgeWorldSP.inst).post();

        if(type.reload(Side.CLIENT))
        {
            reloadClient(ms, type, m.login);
        }
        else if(type == ReloadType.SERVER_ONLY_NOTIFY_CLIENT)
        {
            Notification n = new Notification(FTBLibNotifications.RELOAD_CLIENT_CONFIG);
            n.addText(FTBLibLang.reload_client_config.textComponent());
            n.addText(new TextComponentString("/ftb reload_client"));
            n.setTimer(7000);
            n.setColor(0xFF333333);
            ClientNotifications.add(n);
        }
    }
}