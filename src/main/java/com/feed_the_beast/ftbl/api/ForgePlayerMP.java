package com.feed_the_beast.ftbl.api;

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
import com.mojang.authlib.GameProfile;
import net.minecraft.command.CommandException;
import net.minecraft.command.PlayerNotFoundException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.stats.StatisticsManagerServer;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;
import java.util.Map;

/**
 * Created by LatvianModder on 09.02.2016.
 */
public class ForgePlayerMP extends ForgePlayer implements INBTSerializable<NBTTagCompound>
{
    public final ForgePlayerStats stats;
    public final ConfigEntryEnum<EnumNotificationDisplay> notifications;
    public BlockDimPos lastPos, lastDeath;
    private EntityPlayerMP entityPlayer;

    public ForgePlayerMP(GameProfile p)
    {
        super(p);
        stats = new ForgePlayerStats();
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

    @Override
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

        return lastPos;
    }

    // Reading / Writing //

    public void getInfo(ForgePlayerMP owner, List<ITextComponent> info)
    {
        refreshStats();
        long currentTime = System.currentTimeMillis();

        if(hasTeam())
        {
            ForgeTeam team = getTeam();

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

        stats.getInfo(this, info, currentTime);
        MinecraftForge.EVENT_BUS.post(new ForgePlayerEvent.AddInfo(this, info, currentTime));
    }

    public void refreshStats()
    {
        if(isOnline())
        {
            stats.refresh(this, false);
            getPos();
        }
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

        stats.load(tag);

        lastPos = null;
        if(tag.hasKey("Pos"))
        {
            lastPos = new BlockDimPos(tag.getIntArray("LastPos"));
        }

        lastDeath = null;
        if(tag.hasKey("LastDeath"))
        {
            lastDeath = new BlockDimPos(tag.getIntArray("LastDeath"));
        }
    }

    @Override
    public NBTTagCompound serializeNBT()
    {
        refreshStats();

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

        stats.save(tag);

        if(lastPos != null)
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
        refreshStats();

        NBTTagCompound syncData = new NBTTagCompound();
        MinecraftForge.EVENT_BUS.post(new ForgePlayerEvent.Sync(this, syncData, self));

        if(!syncData.hasNoTags())
        {
            tag.setTag("SY", syncData);
        }

        //Rank rank = getRank();

        if(isOnline())
        {
            tag.setBoolean("O", true);
        }

        if(hasTeam())
        {
            tag.setString("T", getTeamID());
        }
    }

    @Override
    public void onLoggedIn(boolean firstLogin)
    {
        EntityPlayerMP ep = getPlayer();

        super.onLoggedIn(firstLogin);

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
        super.onLoggedOut();
        MinecraftForge.EVENT_BUS.post(new ForgePlayerEvent.LoggedOut(this));
        new MessageLMPlayerLoggedOut(this).sendTo(null);
    }

    @Override
    public void onDeath()
    {
        if(!isOnline())
        {
            return;
        }

        lastDeath = new EntityDimPos(getPlayer()).toBlockDimPos();
        stats.refresh(this, false);

        super.onDeath();

        MinecraftForge.EVENT_BUS.post(new ForgePlayerEvent.OnDeath(this));
    }

    public StatisticsManagerServer getStatFile(boolean force)
    {
        if(isOnline())
        {
            return getPlayer().getStatFile();
        }
        return force ? FTBLib.getServer().getPlayerList().getPlayerStatsFile(new FakePlayer(FTBLib.getServerWorld(), getProfile())) : null;
    }

    public void getSettings(ConfigGroup group)
    {
        group.add("notifications", notifications);

        ConfigGroup group1 = new ConfigGroup();
        MinecraftForge.EVENT_BUS.post(new ForgePlayerEvent.GetSettings(this, group1));

        if(!group1.entryMap.isEmpty())
        {
            group.add("mods", group1);
        }
    }
}