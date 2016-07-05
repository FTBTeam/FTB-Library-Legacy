package com.feed_the_beast.ftbl.api;

import com.feed_the_beast.ftbl.FTBLibStats;
import com.feed_the_beast.ftbl.api.config.ConfigEntryEnum;
import com.feed_the_beast.ftbl.api.config.ConfigGroup;
import com.feed_the_beast.ftbl.api.events.ForgePlayerEvent;
import com.feed_the_beast.ftbl.api.item.LMInvUtils;
import com.feed_the_beast.ftbl.net.MessageLMPlayerInfo;
import com.feed_the_beast.ftbl.net.MessageLMPlayerLoggedIn;
import com.feed_the_beast.ftbl.net.MessageLMPlayerLoggedOut;
import com.feed_the_beast.ftbl.net.MessageLMPlayerUpdate;
import com.feed_the_beast.ftbl.net.MessageReload;
import com.feed_the_beast.ftbl.util.BlockDimPos;
import com.feed_the_beast.ftbl.util.EntityDimPos;
import com.feed_the_beast.ftbl.util.EnumNotificationDisplay;
import com.feed_the_beast.ftbl.util.FTBLib;
import com.feed_the_beast.ftbl.util.ReloadType;
import com.latmod.lib.util.LMStringUtils;
import com.mojang.authlib.GameProfile;
import net.minecraft.command.CommandException;
import net.minecraft.command.PlayerNotFoundException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.stats.StatList;
import net.minecraft.stats.StatisticsManagerServer;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Map;

/**
 * Created by LatvianModder on 09.02.2016.
 */
public class ForgePlayerMP extends ForgePlayer implements INBTSerializable<NBTTagCompound>
{
    public final ConfigEntryEnum<EnumNotificationDisplay> notifications;
    public BlockDimPos lastPos, lastDeath;
    private StatisticsManagerServer statsManager;
    private EntityPlayerMP entityPlayer;

    public ForgePlayerMP(GameProfile p)
    {
        super(p);
        notifications = new ConfigEntryEnum<>(EnumNotificationDisplay.SCREEN, EnumNotificationDisplay.NAME_MAP);
    }

    public static ForgePlayerMP get(Object o) throws CommandException
    {
        ForgePlayerMP p = ForgeWorldMP.inst.getPlayer(o);
        if(p == null || p.isFake())
        {
            throw new PlayerNotFoundException();
        }
        return p;
    }

    public boolean isOnline()
    {
        return getPlayer() != null;
    }

    @Override
    public EntityPlayerMP getPlayer()
    {
        return entityPlayer;
    }

    public void setPlayer(EntityPlayerMP ep)
    {
        entityPlayer = ep;
    }

    @Override
    public final ForgePlayerMP toMP()
    {
        return this;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public final ForgePlayerSP toSP()
    {
        return null;
    }

    @Override
    public final ForgeWorldMP getWorld()
    {
        return ForgeWorldMP.inst;
    }

    public boolean isFake()
    {
        return getPlayer() instanceof FakePlayer;
    }

    @Override
    public void sendUpdate()
    {
        //new EventLMPlayerServer.UpdateSent(this).post();
        EntityPlayerMP player = getPlayer();

        if(isOnline())
        {
            new MessageLMPlayerUpdate(this, true).sendTo(player);
        }

        for(EntityPlayerMP ep : FTBLib.getServer().getPlayerList().getPlayerList())
        {
            if(ep != player)
            {
                new MessageLMPlayerUpdate(this, false).sendTo(ep);
            }
        }
    }

    @Override
    public void sendInfoUpdate(ForgePlayer p)
    {
        new MessageLMPlayerInfo(this, p.toMP()).sendTo(getPlayer());
    }

    public boolean isOP()
    {
        return FTBLib.isOP(getProfile());
    }

    public BlockDimPos getPos()
    {
        EntityPlayerMP ep = getPlayer();

        if(ep != null)
        {
            lastPos = new EntityDimPos(ep).toBlockDimPos();
        }

        return lastPos == null ? null : lastPos.copy();
    }

    // Reading / Writing //

    public void getInfo(ForgePlayerMP owner, List<ITextComponent> info)
    {
        long ms = System.currentTimeMillis();
        ForgeTeam team = getTeam();

        if(team != null)
        {
            ITextComponent c = new TextComponentString("[" + team.getTitle() + "]");
            c.getStyle().setColor(team.getColor().textFormatting).setUnderlined(true);
            info.add(c);

            if(team.getDesc() != null)
            {
                c = new TextComponentString(team.getDesc());
                c.getStyle().setColor(TextFormatting.GRAY).setItalic(true);
                info.add(c);
            }

            info.add(null);
        }

        StatisticsManagerServer stats = stats();

        if(!owner.isOnline())
        {
            long lastSeen = FTBLibStats.getLastSeen(stats, false);

            if(lastSeen > 0L)
            {
                info.add(new TextComponentTranslation(FTBLibStats.LAST_SEEN.statId).appendText(": " + LMStringUtils.getTimeString(ms - lastSeen)));
            }
        }

        long firstJoined = FTBLibStats.getFirstJoined(stats);

        if(firstJoined > 0L)
        {
            info.add(new TextComponentTranslation(FTBLibStats.FIRST_JOINED.statId).appendText(": " + LMStringUtils.getTimeString(ms - firstJoined)));
        }

        if(stats.readStat(StatList.DEATHS) > 0)
        {
            info.add(new TextComponentTranslation(StatList.DEATHS.statId).appendText(": " + stats.readStat(StatList.DEATHS)));
        }

        if(stats.readStat(StatList.PLAY_ONE_MINUTE) > 0L)
        {
            ITextComponent c = StatList.PLAY_ONE_MINUTE.getStatName();
            c.getStyle().setColor(null);
            info.add(c.appendSibling(new TextComponentString(": " + LMStringUtils.getTimeString(stats.readStat(StatList.PLAY_ONE_MINUTE) * 50L))));
        }

        MinecraftForge.EVENT_BUS.post(new ForgePlayerEvent.AddInfo(this, info, ms));
    }

    @Override
    public void deserializeNBT(NBTTagCompound tag)
    {
        setTeamID(tag.getString("TeamID"));

        if(capabilities != null)
        {
            capabilities.deserializeNBT(tag.getCompoundTag("Caps"));
        }

        lastArmor.clear();

        if(tag.hasKey("LastItems"))
        {
            ItemStack[] lastArmorItems = new ItemStack[EntityEquipmentSlot.values().length];
            LMInvUtils.readItemsFromNBT(lastArmorItems, tag, "Armor");

            for(int i = 0; i < lastArmorItems.length; i++)
            {
                if(lastArmorItems[i] != null)
                {
                    lastArmor.put(EntityEquipmentSlot.values()[i], lastArmorItems[i]);
                }
            }
        }

        lastPos = null;
        if(tag.hasKey("Pos"))
        {
            int[] ai = tag.getIntArray("LastPos");
            lastPos = (ai.length == 4) ? new BlockDimPos(ai) : null;
        }

        lastDeath = null;
        if(tag.hasKey("LastDeath"))
        {
            int[] ai = tag.getIntArray("LastDeath");
            lastDeath = (ai.length == 4) ? new BlockDimPos(ai) : null;
        }
    }

    @Override
    public NBTTagCompound serializeNBT()
    {
        NBTTagCompound tag = new NBTTagCompound();

        if(hasTeam())
        {
            tag.setString("TeamID", getTeamID());
        }

        if(capabilities != null)
        {
            tag.setTag("Caps", capabilities.serializeNBT());
        }

        if(!lastArmor.isEmpty())
        {
            ItemStack[] lastArmorItems = new ItemStack[EntityEquipmentSlot.values().length];
            for(Map.Entry<EntityEquipmentSlot, ItemStack> e : lastArmor.entrySet())
            {
                lastArmorItems[e.getKey().ordinal()] = e.getValue();
            }

            LMInvUtils.writeItemsToNBT(lastArmorItems, tag, "Armor");
        }

        if(getPos() != null)
        {
            tag.setIntArray("Pos", lastPos.toIntArray());
        }

        if(lastDeath != null)
        {
            tag.setIntArray("LastDeath", lastDeath.toIntArray());
        }

        return tag;
    }

    public void writeToNet(NBTTagCompound tag, boolean self)
    {
        NBTTagCompound syncData = new NBTTagCompound();
        MinecraftForge.EVENT_BUS.post(new ForgePlayerEvent.Sync(this, syncData, self));

        if(!syncData.hasNoTags())
        {
            tag.setTag("SY", syncData);
        }

        if(hasTeam())
        {
            tag.setString("T", getTeamID());
        }
    }

    @Override
    public void onLoggedIn(boolean firstLogin)
    {
        super.onLoggedIn(firstLogin);
        FTBLibStats.updateLastSeen(stats());

        EntityPlayerMP ep = getPlayer();
        new MessageLMPlayerLoggedIn(this, firstLogin, true).sendTo(ep);
        new MessageReload(ReloadType.CLIENT_ONLY, this, true).sendTo(ep);

        for(EntityPlayerMP ep1 : FTBLib.getServer().getPlayerList().getPlayerList())
        {
            if(ep1 != ep)
            {
                new MessageLMPlayerLoggedIn(this, firstLogin, false).sendTo(ep1);
            }
        }

        MinecraftForge.EVENT_BUS.post(new ForgePlayerEvent.LoggedIn(this, firstLogin));
    }

    @Override
    public void onLoggedOut()
    {
        FTBLibStats.updateLastSeen(stats());
        super.onLoggedOut();
        MinecraftForge.EVENT_BUS.post(new ForgePlayerEvent.LoggedOut(this));
        new MessageLMPlayerLoggedOut(this).sendTo(null);
    }

    @Override
    public void onDeath()
    {
        if(isOnline())
        {
            lastDeath = new EntityDimPos(getPlayer()).toBlockDimPos();
            super.onDeath();
            FTBLibStats.updateLastSeen(stats());
            MinecraftForge.EVENT_BUS.post(new ForgePlayerEvent.OnDeath(this));
        }
    }

    @Nonnull
    public StatisticsManagerServer stats()
    {
        if(statsManager == null)
        {
            statsManager = FTBLib.getServer().getPlayerList().getPlayerStatsFile(entityPlayer == null ? new FakePlayer(FTBLib.getServerWorld(), getProfile()) : entityPlayer);
        }

        return statsManager;
    }

    public void getSettings(ConfigGroup group)
    {
        MinecraftForge.EVENT_BUS.post(new ForgePlayerEvent.GetSettings(this, group));

        group.add("notifications", notifications);
    }
}