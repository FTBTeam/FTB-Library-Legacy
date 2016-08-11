package com.feed_the_beast.ftbl.api;

import com.feed_the_beast.ftbl.api.config.ConfigFile;
import com.feed_the_beast.ftbl.util.ReloadType;
import net.minecraft.command.ICommandSender;
import net.minecraftforge.fml.relauncher.Side;

/**
 * Created by LatvianModder on 11.08.2016.
 */
public abstract class FTBLibAPI
{
    public static FTBLibAPI INSTANCE = null;

    public abstract IPackModes getPackModes();

    public abstract ISharedData getSharedData(Side side);

    public abstract IForgeWorld getWorld();

    public abstract void addServerCallback(int timer, Runnable runnable);

    public abstract void reload(ICommandSender sender, ReloadType type, boolean login);

    public abstract void registerConfigFile(String id, ConfigFile file);
}