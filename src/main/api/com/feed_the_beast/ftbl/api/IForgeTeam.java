package com.feed_the_beast.ftbl.api;

import com.feed_the_beast.ftbl.api.config.IConfigTree;
import com.feed_the_beast.ftbl.lib.EnumTeamPrivacyLevel;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.INBTSerializable;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

/**
 * @author LatvianModder
 */
public interface IForgeTeam extends IStringSerializable, INBTSerializable<NBTTagCompound>
{
    boolean isValid();

    @Nullable
    INBTSerializable<?> getData(ResourceLocation id);

    IForgePlayer getOwner();

    String getTitle();

    String getDesc();

    EnumTeamColor getColor();

    EnumTeamStatus getHighestStatus(UUID playerId);

    boolean hasStatus(UUID playerId, EnumTeamStatus status);

    default EnumTeamStatus getHighestStatus(IForgePlayer player)
    {
        return getHighestStatus(player.getId());
    }

    default boolean hasStatus(IForgePlayer player, EnumTeamStatus status)
    {
        return hasStatus(player.getId(), status);
    }

    void setStatus(UUID playerId, EnumTeamStatus status);

    Collection<IForgePlayer> getPlayersWithStatus(Collection<IForgePlayer> c, EnumTeamStatus status);

    default boolean canInteract(UUID playerId, EnumTeamPrivacyLevel level)
    {
        return hasStatus(playerId, level.getRequiredStatus());
    }

    boolean addPlayer(IForgePlayer p);

    boolean removePlayer(IForgePlayer p);

    void changeOwner(IForgePlayer o);

    IConfigTree getSettings();

    void printMessage(ITeamMessage message);

    List<ITeamMessage> getMessages();

    boolean freeToJoin();
}