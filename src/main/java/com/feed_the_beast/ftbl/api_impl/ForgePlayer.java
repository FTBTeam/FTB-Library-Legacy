package com.feed_the_beast.ftbl.api_impl;

import com.feed_the_beast.ftbl.FTBLibMod;
import com.feed_the_beast.ftbl.FTBLibModCommon;
import com.feed_the_beast.ftbl.api.IForgePlayer;
import com.feed_the_beast.ftbl.api.config.IConfigKey;
import com.feed_the_beast.ftbl.api.config.IConfigTree;
import com.feed_the_beast.ftbl.api.events.player.ForgePlayerDeathEvent;
import com.feed_the_beast.ftbl.api.events.player.ForgePlayerLoggedInEvent;
import com.feed_the_beast.ftbl.api.events.player.ForgePlayerLoggedOutEvent;
import com.feed_the_beast.ftbl.api.events.player.ForgePlayerSettingsEvent;
import com.feed_the_beast.ftbl.lib.NBTDataStorage;
import com.feed_the_beast.ftbl.lib.config.ConfigKey;
import com.feed_the_beast.ftbl.lib.config.ConfigTree;
import com.feed_the_beast.ftbl.lib.config.PropertyBool;
import com.feed_the_beast.ftbl.lib.internal.FTBLibFinals;
import com.feed_the_beast.ftbl.lib.internal.FTBLibStats;
import com.feed_the_beast.ftbl.lib.io.Bits;
import com.feed_the_beast.ftbl.lib.util.LMNBTUtils;
import com.feed_the_beast.ftbl.lib.util.LMServerUtils;
import com.feed_the_beast.ftbl.lib.util.LMUtils;
import com.feed_the_beast.ftbl.net.MessageLogin;
import com.google.common.base.Preconditions;
import com.mojang.authlib.GameProfile;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.stats.StatisticsManagerServer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.common.util.INBTSerializable;

import javax.annotation.Nullable;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by LatvianModder on 09.02.2016.
 */
public class ForgePlayer implements IForgePlayer, Comparable<ForgePlayer>
{
    private static final IConfigKey HIDE_TEAM_NOTIFICATION = new ConfigKey("ftbl.hide_team_notification", new PropertyBool(false));
    private static final IConfigKey HIDE_NEW_TEAM_MSG_NOTIFICATION = new ConfigKey("ftbl.hide_new_team_msg_notification", new PropertyBool(false));
    private static FakePlayer playerForStats;

    private final UUID playerId;
    private String playerName;
    private final NBTDataStorage dataStorage;
    private ForgeTeam team = null;
    private final PropertyBool hideTeamNotification;
    private final PropertyBool hideNewTeamMsgNotification;
    private EntityPlayerMP entityPlayer;
    private NBTTagCompound playerNBT;
    private final IConfigTree cachedConfig;

    public ForgePlayer(UUID id, String name)
    {
        playerId = id;
        playerName = name;
        dataStorage = FTBLibMod.PROXY.createDataStorage(this, FTBLibModCommon.DATA_PROVIDER_PLAYER);
        hideTeamNotification = new PropertyBool();
        hideNewTeamMsgNotification = new PropertyBool();

        cachedConfig = new ConfigTree();
        ForgePlayerSettingsEvent event = new ForgePlayerSettingsEvent(this, cachedConfig);
        MinecraftForge.EVENT_BUS.post(event);
        String group = FTBLibFinals.MOD_ID;
        event.add(group, "hide_team_notification", hideTeamNotification);
        event.add(group, "hide_new_team_msg_notification", hideNewTeamMsgNotification);
    }

    @Override
    public final void setTeamID(String id)
    {
        team = Universe.INSTANCE.getTeam(id);
    }

    @Override
    @Nullable
    public final ForgeTeam getTeam()
    {
        if(team != null && !team.isValid())
        {
            return null;
        }

        return team;
    }

    @Override
    public final GameProfile getProfile()
    {
        if(isOnline())
        {
            return entityPlayer.getGameProfile();
        }

        return new GameProfile(playerId, playerName);
    }

    @Override
    public final UUID getId()
    {
        return playerId;
    }

    @Override
    public final String getName()
    {
        return playerName;
    }

    public final void setUsername(String n)
    {
        playerName = n;
    }

    @Override
    @Nullable
    public INBTSerializable<?> getData(ResourceLocation id)
    {
        return dataStorage == null ? null : dataStorage.get(id);
    }

    @Override
    public final int compareTo(ForgePlayer o)
    {
        return getName().compareToIgnoreCase(o.getName());
    }

    public final String toString()
    {
        return playerName;
    }

    public final int hashCode()
    {
        return playerId.hashCode();
    }

    public boolean equals(Object o)
    {
        if(o == null)
        {
            return false;
        }
        else if(o == this || o == playerId)
        {
            return true;
        }
        else if(o instanceof UUID)
        {
            return playerId.equals(o);
        }
        else if(o instanceof IForgePlayer)
        {
            return equalsPlayer((IForgePlayer) o);
        }
        return equalsPlayer(Universe.INSTANCE.getPlayer(o));
    }

    @Override
    public boolean equalsPlayer(@Nullable IForgePlayer p)
    {
        return p == this || (p != null && getId().equals(p.getId()));
    }

    public Map<EntityEquipmentSlot, ItemStack> getArmor()
    {
        Map<EntityEquipmentSlot, ItemStack> map = new HashMap<>();

        EntityPlayerMP ep = getPlayer();

        if(ep != null)
        {
            for(EntityEquipmentSlot e : EntityEquipmentSlot.values())
            {
                ItemStack is = ep.getItemStackFromSlot(e);

                if(is != null)
                {
                    map.put(e, is.copy());
                }
            }
        }

        return map;
    }

    @Override
    public boolean isOnline()
    {
        return entityPlayer != null;
    }

    @Override
    public EntityPlayerMP getPlayer()
    {
        Preconditions.checkNotNull(entityPlayer, "EntityPlayer can't be null!");
        return entityPlayer;
    }

    @Override
    public boolean isFake()
    {
        return entityPlayer instanceof FakePlayer;
    }

    @Override
    public boolean isOP()
    {
        return LMServerUtils.isOP(getProfile());
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt)
    {
        if(nbt.hasKey("Flags"))
        {
            int flags = nbt.getInteger("Flags");
            hideTeamNotification.setBoolean(Bits.getFlag(flags, 1));
            hideNewTeamMsgNotification.setBoolean(Bits.getFlag(flags, 2));
        }
        else
        {
            hideTeamNotification.setBoolean(nbt.getBoolean("HideTeamNotification"));
            hideNewTeamMsgNotification.setBoolean(nbt.getBoolean("HideNewTeamMsgNotification"));
        }

        setTeamID(nbt.getString("TeamID"));

        if(dataStorage != null)
        {
            dataStorage.deserializeNBT(nbt.hasKey("Caps") ? nbt.getCompoundTag("Caps") : nbt.getCompoundTag("Data"));
        }
    }

    @Override
    public NBTTagCompound serializeNBT()
    {
        NBTTagCompound nbt = new NBTTagCompound();

        nbt.setBoolean("HideTeamNotification", hideTeamNotification.getBoolean());
        nbt.setBoolean("HideNewTeamMsgNotification", hideNewTeamMsgNotification.getBoolean());

        if(team != null && team.isValid())
        {
            nbt.setString("TeamID", team.getName());
        }

        if(dataStorage != null)
        {
            nbt.setTag("Data", dataStorage.serializeNBT());
        }

        return nbt;
    }

    public void onLoggedIn(EntityPlayerMP ep, boolean firstLogin)
    {
        entityPlayer = ep;
        playerNBT = null;

        if(!isFake())
        {
            FTBLibStats.updateLastSeen(stats());
            new MessageLogin(ep, this).sendTo(entityPlayer);
        }

        MinecraftForge.EVENT_BUS.post(new ForgePlayerLoggedInEvent(this, firstLogin));
    }

    public void onLoggedOut()
    {
        FTBLibStats.updateLastSeen(stats());
        MinecraftForge.EVENT_BUS.post(new ForgePlayerLoggedOutEvent(this));
        entityPlayer = null;
        playerNBT = null;
    }

    public void onDeath(EntityPlayerMP ep, DamageSource ds)
    {
        entityPlayer = ep;

        if(isOnline())
        {
            FTBLibStats.updateLastSeen(stats());
            MinecraftForge.EVENT_BUS.post(new ForgePlayerDeathEvent(this, ds));
        }
    }

    @Override
    public StatisticsManagerServer stats()
    {
        if(playerForStats == null)
        {
            playerForStats = new FakePlayer(LMServerUtils.getServerWorld(), new GameProfile(new UUID(0L, 0L), "_unknown"));
        }

        playerForStats.setUniqueId(getId());
        return LMServerUtils.getServer().getPlayerList().getPlayerStatsFile(playerForStats);
    }

    @Override
    public IConfigTree getSettings()
    {
        return cachedConfig;
    }

    @Override
    public NBTTagCompound getPlayerNBT()
    {
        if(isOnline())
        {
            return getPlayer().serializeNBT();
        }

        if(playerNBT == null)
        {
            try
            {
                playerNBT = LMNBTUtils.readTag(new File(LMUtils.folderWorld, "playerdata/" + getId() + ".dat"));
            }
            catch(Exception ex)
            {
                ex.printStackTrace();
            }
        }

        return playerNBT;
    }

    @Override
    public boolean hideTeamNotification()
    {
        return hideTeamNotification.getBoolean();
    }

    @Override
    public boolean hideNewTeamMsgNotification()
    {
        return hideNewTeamMsgNotification.getBoolean();
    }
}