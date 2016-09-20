package com.feed_the_beast.ftbl.api;

import com.feed_the_beast.ftbl.api.events.ReloadType;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;

import javax.annotation.Nullable;

/**
 * Created by LatvianModder on 11.08.2016.
 */
public interface FTBLibAPI
{
    boolean hasServer();

    IConfigManager configManager();

    IFTBLibRegistries getRegistries();

    IPackModes getPackModes();

    ISharedData getSharedData(Side side);

    @Nullable
    IUniverse getUniverse();

    void addServerCallback(int timer, Runnable runnable);

    void reload(ICommandSender sender, ReloadType type);

    void openGui(ResourceLocation guiID, EntityPlayerMP ep, @Nullable NBTTagCompound data);

    void sendNotification(@Nullable EntityPlayerMP ep, INotification n);
}