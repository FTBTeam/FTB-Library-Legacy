package com.feed_the_beast.ftbl.api;

import com.feed_the_beast.ftbl.api.config.IConfigTree;
import com.feed_the_beast.ftbl.api.security.EnumTeamPrivacyLevel;
import com.feed_the_beast.ftbl.lib.INBTData;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IStringSerializable;
import net.minecraftforge.common.util.INBTSerializable;

import javax.annotation.Nullable;
import java.util.Collection;

/**
 * Created by LatvianModder on 11.08.2016.
 */
public interface IForgeTeam extends IStringSerializable, INBTSerializable<NBTTagCompound>
{
    @Nullable
    INBTData getData(String id);

    IForgePlayer getOwner();

    String getTitle();

    String getDesc();

    boolean isFreeToJoin();

    boolean isHidden();

    EnumTeamColor getColor();

    boolean hasStatus(IForgePlayer player, EnumTeamStatus status);

    boolean isAllyTeam(String team);

    Collection<IForgePlayer> getPlayersWithStatus(Collection<IForgePlayer> c, EnumTeamStatus status);

    boolean isInvited(IForgePlayer player);

    default boolean canInteract(IForgePlayer player, EnumTeamPrivacyLevel level)
    {
        switch(level)
        {
            case EVERYONE:
                return true;
            case MEMBERS:
                return hasStatus(player, EnumTeamStatus.MEMBER);
            case ALLIES:
                return hasStatus(player, EnumTeamStatus.ALLY);
            default:
                return false;
        }
    }

    boolean addPlayer(IForgePlayer p);

    void removePlayer(IForgePlayer p);

    void changeOwner(IForgePlayer o);

    boolean inviteMember(IForgePlayer p1);

    boolean addAllyTeam(String arg);

    boolean removeAllyTeam(String name);

    void getSettings(IConfigTree tree);
}