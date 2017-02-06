package com.feed_the_beast.ftbl.api;

import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.server.command.CommandTreeBase;

/**
 * Created by LatvianModder on 15.11.2016.
 */
public interface IFTBLibPlugin
{
    void init(FTBLibAPI api);

    default void loadWorldData(MinecraftServer server)
    {
    }

    default void onReload(Side side, ICommandSender sender, EnumReloadType type)
    {
    }

    default void registerCommon(IFTBLibRegistry reg)
    {
    }

    default void registerClient(IFTBLibClientRegistry reg)
    {
    }

    default void registerRecipes(IRecipes recipes)
    {
    }

    default void registerFTBCommands(CommandTreeBase command, boolean isDedicatedServer)
    {
    }
}