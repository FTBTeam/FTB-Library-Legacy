package com.feed_the_beast.ftbl.api;

import com.feed_the_beast.ftbl.FTBLibLang;
import com.feed_the_beast.ftbl.api.config.IConfigContainer;
import com.feed_the_beast.ftbl.api.config.IConfigValueProvider;
import com.feed_the_beast.ftbl.api.events.ReloadType;
import com.feed_the_beast.ftbl.api.info.IInfoPage;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.PlayerNotFoundException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ITickable;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Map;

/**
 * Created by LatvianModder on 11.08.2016.
 */
public interface FTBLibAPI
{
    Collection<ITickable> ticking();

    IPackModes getPackModes();

    ISharedData getServerData();

    ISharedData getClientData();

    Map<String, IConfigValueProvider> getConfigValueProviders();

    @Nullable
    IUniverse getUniverse();

    void addServerCallback(int timer, Runnable runnable);

    void reload(ICommandSender sender, ReloadType type);

    void openGui(ResourceLocation guiID, EntityPlayerMP player, @Nullable NBTTagCompound data);

    void sendNotification(@Nullable EntityPlayerMP player, INotification n);

    void editServerConfig(EntityPlayerMP player, @Nullable NBTTagCompound nbt, IConfigContainer configContainer);

    void displayInfoGui(EntityPlayerMP player, IInfoPage page);

    default IForgePlayer getForgePlayer(Object o) throws CommandException
    {
        IForgePlayer p = getUniverse().getPlayer(o);

        if(p == null || p.isFake())
        {
            throw new PlayerNotFoundException();
        }

        return p;
    }

    default IForgeTeam getTeam(String s) throws CommandException
    {
        IForgeTeam team = getUniverse().getTeam(s);

        if(team != null)
        {
            return team;
        }

        throw FTBLibLang.TEAM_NOT_FOUND.commandError();
    }
}