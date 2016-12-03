package com.feed_the_beast.ftbl.api;

import com.feed_the_beast.ftbl.api.config.IConfigContainer;
import com.feed_the_beast.ftbl.api.config.IConfigValue;
import com.feed_the_beast.ftbl.lib.info.InfoPage;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ITickable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Map;

/**
 * Created by LatvianModder on 11.08.2016.
 */
public interface FTBLibAPI
{
    Collection<IFTBLibPlugin> getAllPlugins();

    Collection<ITickable> ticking();

    IPackModes getPackModes();

    ISharedServerData getServerData();

    ISharedClientData getClientData();

    @Nullable
    IUniverse getUniverse();

    void addServerCallback(int timer, Runnable runnable);

    void reload(ICommandSender sender, EnumReloadType type);

    void openGui(ResourceLocation guiID, EntityPlayerMP player, BlockPos pos, @Nullable NBTTagCompound data);

    void sendNotification(@Nullable EntityPlayerMP player, INotification n);

    void editServerConfig(EntityPlayerMP player, @Nullable NBTTagCompound nbt, IConfigContainer configContainer);

    void displayInfoGui(EntityPlayerMP player, InfoPage page);

    IConfigValue getConfigValueFromID(String id);

    Map<String, IRankConfig> getRankConfigRegistry();
}