package com.feed_the_beast.ftbl.api_impl;

import com.feed_the_beast.ftbl.FTBLibMod;
import com.feed_the_beast.ftbl.FTBLibModCommon;
import com.feed_the_beast.ftbl.api.EnumTeamColor;
import com.feed_the_beast.ftbl.api.EnumTeamStatus;
import com.feed_the_beast.ftbl.api.IForgePlayer;
import com.feed_the_beast.ftbl.api.IForgeTeam;
import com.feed_the_beast.ftbl.api.config.IConfigKey;
import com.feed_the_beast.ftbl.api.config.IConfigTree;
import com.feed_the_beast.ftbl.api.events.team.ForgeTeamOwnerChangedEvent;
import com.feed_the_beast.ftbl.api.events.team.ForgeTeamPlayerJoinedEvent;
import com.feed_the_beast.ftbl.api.events.team.ForgeTeamPlayerLeftEvent;
import com.feed_the_beast.ftbl.api.events.team.ForgeTeamSettingsEvent;
import com.feed_the_beast.ftbl.lib.EnumNameMap;
import com.feed_the_beast.ftbl.lib.FinalIDObject;
import com.feed_the_beast.ftbl.lib.NBTDataStorage;
import com.feed_the_beast.ftbl.lib.config.ConfigKey;
import com.feed_the_beast.ftbl.lib.config.PropertyEnum;
import com.feed_the_beast.ftbl.lib.config.PropertyString;
import com.feed_the_beast.ftbl.lib.internal.FTBLibTeamPermissions;
import com.feed_the_beast.ftbl.lib.util.LMStringUtils;
import gnu.trove.TShortCollection;
import gnu.trove.set.hash.TShortHashSet;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.INBTSerializable;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by LatvianModder on 26.05.2016.
 */
public final class ForgeTeam extends FinalIDObject implements IForgeTeam
{
    private static final EnumNameMap<EnumTeamColor> COLOR_NAME_MAP = new EnumNameMap<>(EnumTeamColor.values(), false);
    private static final IConfigKey KEY_COLOR = new ConfigKey("display.color", new PropertyEnum<>(COLOR_NAME_MAP, EnumTeamColor.BLUE), new TextComponentTranslation("ftbteam.config.display.color"));
    private static final IConfigKey KEY_TITLE = new ConfigKey("display.title", new PropertyString(""), new TextComponentTranslation("ftbteam.config.display.title"));
    private static final IConfigKey KEY_DESC = new ConfigKey("display.desc", new PropertyString(""), new TextComponentTranslation("ftbteam.config.display.desc"));

    private final NBTDataStorage dataStorage;
    private EnumTeamColor color;
    private IForgePlayer owner;
    private String title;
    private String desc;
    private byte flags;
    private Map<UUID, TShortCollection> playerPermissions;

    public ForgeTeam(String id)
    {
        super(id);
        color = EnumTeamColor.BLUE;

        title = "";
        desc = "";
        flags = 0;

        dataStorage = FTBLibMod.PROXY.createDataStorage(this, FTBLibModCommon.DATA_PROVIDER_TEAM);
    }

    @Override
    @Nullable
    public INBTSerializable<?> getData(ResourceLocation id)
    {
        return dataStorage == null ? null : dataStorage.get(id);
    }

    @Override
    public NBTTagCompound serializeNBT()
    {
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setString("Owner", LMStringUtils.fromUUID(owner.getProfile().getId()));
        nbt.setByte("Flags", flags);
        nbt.setString("Color", EnumNameMap.getEnumName(color));

        if(!title.isEmpty())
        {
            nbt.setString("Title", title);
        }

        if(!desc.isEmpty())
        {
            nbt.setString("Desc", desc);
        }

        if(playerPermissions != null && !playerPermissions.isEmpty())
        {
            NBTTagCompound nbt1 = new NBTTagCompound();

            for(Map.Entry<UUID, TShortCollection> entry : playerPermissions.entrySet())
            {
                if(!entry.getValue().isEmpty())
                {
                    short[] as = entry.getValue().toArray();
                    int[] ai = new int[as.length];

                    for(int i = 0; i < as.length; i++)
                    {
                        ai[i] = as[i];
                    }

                    nbt1.setIntArray(LMStringUtils.fromUUID(entry.getKey()), ai);
                }
            }

            nbt.setTag("Perms", nbt1);
        }

        if(dataStorage != null)
        {
            nbt.setTag("Data", dataStorage.serializeNBT());
        }

        return nbt;
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt)
    {
        owner = Universe.INSTANCE.getPlayer(LMStringUtils.fromString(nbt.getString("Owner")));
        flags = nbt.getByte("Flags");
        color = COLOR_NAME_MAP.get(nbt.getString("Color"));
        title = nbt.getString("Title");
        desc = nbt.getString("Desc");

        if(playerPermissions != null)
        {
            playerPermissions.clear();
        }

        if(nbt.hasKey("Perms"))
        {
            if(playerPermissions == null)
            {
                playerPermissions = new HashMap<>();
            }

            NBTTagCompound nbt1 = nbt.getCompoundTag("Perms");

            for(String s : nbt1.getKeySet())
            {
                UUID id = LMStringUtils.fromString(s);

                if(id != null)
                {
                    int[] ai = nbt1.getIntArray(s);
                    TShortHashSet set = new TShortHashSet(ai.length);

                    for(int i : ai)
                    {
                        set.add((short) i);
                    }

                    playerPermissions.put(id, set);
                }
            }
        }

        if(nbt.hasKey("Invited"))
        {
            NBTTagList list = nbt.getTagList("Invited", Constants.NBT.TAG_STRING);

            for(int i = 0; i < list.tagCount(); i++)
            {
                UUID id = LMStringUtils.fromString(list.getStringTagAt(i));
                if(id != null)
                {
                    setHasPermission(id, FTBLibTeamPermissions.CAN_JOIN, true);
                }
            }
        }

        if(dataStorage != null)
        {
            dataStorage.deserializeNBT(nbt.hasKey("Caps") ? nbt.getCompoundTag("Caps") : nbt.getCompoundTag("Data"));
        }
    }

    @Override
    public IForgePlayer getOwner()
    {
        return owner;
    }

    @Override
    public String getTitle()
    {
        return title.isEmpty() ? (owner.getProfile().getName() + (owner.getProfile().getName().endsWith("s") ? "' Team" : "'s Team")) : title;
    }

    @Override
    public String getDesc()
    {
        return desc;
    }

    @Override
    public EnumTeamColor getColor()
    {
        return color;
    }

    public void setColor(EnumTeamColor col)
    {
        color = col;
    }

    @Override
    public boolean hasStatus(IForgePlayer player, EnumTeamStatus status)
    {
        IForgeTeam team = player.getTeam();

        switch(status)
        {
            case ENEMY:
                return hasPermission(player.getProfile().getId(), FTBLibTeamPermissions.IS_ENEMY);
            case OWNER:
                return owner.equalsPlayer(player);
            case MEMBER:
                return owner.equalsPlayer(player) || (team != null && team.equals(this));
            case ALLY:
                return owner.equalsPlayer(player) || (team != null && (team.equals(this) || hasPermission(player.getProfile().getId(), FTBLibTeamPermissions.IS_ALLY)));
            default:
                return false;
        }
    }

    @Override
    public Collection<IForgePlayer> getPlayersWithStatus(Collection<IForgePlayer> c, EnumTeamStatus status)
    {
        for(IForgePlayer p : Universe.INSTANCE.getPlayers())
        {
            if(hasStatus(p, status))
            {
                c.add(p);
            }
        }

        return c;
    }

    @Override
    public boolean addPlayer(IForgePlayer player)
    {
        if(!hasStatus(player, EnumTeamStatus.MEMBER) && hasPermission(player.getProfile().getId(), FTBLibTeamPermissions.CAN_JOIN))
        {
            player.setTeamID(getName());
            MinecraftForge.EVENT_BUS.post(new ForgeTeamPlayerJoinedEvent(this, player));
            setHasPermission(player.getProfile().getId(), FTBLibTeamPermissions.CAN_JOIN, false);
            return true;
        }

        return false;
    }

    @Override
    public void removePlayer(IForgePlayer player)
    {
        if(hasStatus(player, EnumTeamStatus.MEMBER))
        {
            player.setTeamID("");
            MinecraftForge.EVENT_BUS.post(new ForgeTeamPlayerLeftEvent(this, player));
        }
    }

    @Override
    public void changeOwner(IForgePlayer newOwner)
    {
        if(owner == null)
        {
            owner = newOwner;
            newOwner.setTeamID(getName());
        }
        else
        {
            IForgePlayer oldOwner = owner;

            if(!oldOwner.equalsPlayer(newOwner) && hasStatus(newOwner, EnumTeamStatus.MEMBER))
            {
                owner = newOwner;
                MinecraftForge.EVENT_BUS.post(new ForgeTeamOwnerChangedEvent(this, oldOwner, newOwner));
            }
        }
    }

    @Override
    public void getSettings(IConfigTree tree)
    {
        MinecraftForge.EVENT_BUS.post(new ForgeTeamSettingsEvent(this, tree));

        tree.add(KEY_COLOR, new PropertyEnum<EnumTeamColor>(COLOR_NAME_MAP, EnumTeamColor.BLUE)
        {
            @Override
            public EnumTeamColor get()
            {
                return color;
            }

            @Override
            public void set(@Nullable EnumTeamColor e)
            {
                color = e == null ? EnumTeamColor.BLUE : e;
            }
        });

        tree.add(KEY_TITLE, new PropertyString(title)
        {
            @Override
            public void setString(String v)
            {
                title = v.trim();
            }

            @Override
            public String getString()
            {
                return title;
            }
        });

        tree.add(KEY_DESC, new PropertyString(desc)
        {
            @Override
            public void setString(String v)
            {
                desc = v.trim();
            }

            @Override
            public String getString()
            {
                return desc;
            }
        });
    }

    @Override
    public boolean hasPermission(UUID playerID, String permission)
    {
        return playerID.equals(owner.getProfile().getId());

    }

    @Override
    public boolean setHasPermission(UUID playerID, String permission, boolean val)
    {
        if(val)
        {
            if(playerPermissions == null)
            {
                playerPermissions = new HashMap<>();
            }

            TShortCollection permissions = playerPermissions.get(playerID);

            if(permissions == null)
            {
                permissions = new TShortHashSet();
                playerPermissions.put(playerID, permissions);
            }

            short id = Universe.INSTANCE.teamPlayerPermisssionIDs.generateID(permission);
            return id != 0 && permissions.add(id);
        }
        else if(playerPermissions != null)
        {
            short id = Universe.INSTANCE.teamPlayerPermisssionIDs.getIDFromKey(permission);

            if(id != 0)
            {
                return false;
            }

            TShortCollection permissions = playerPermissions.get(playerID);

            if(permissions != null)
            {
                return permissions.remove(id);
            }
        }

        return false;
    }
}