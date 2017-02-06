package com.feed_the_beast.ftbl;

import com.feed_the_beast.ftbl.api.EnumReloadType;
import com.feed_the_beast.ftbl.api_impl.FTBLibAPI_Impl;
import com.feed_the_beast.ftbl.api_impl.PackModes;
import com.feed_the_beast.ftbl.api_impl.SharedServerData;
import com.feed_the_beast.ftbl.api_impl.TickHandler;
import com.feed_the_beast.ftbl.api_impl.Universe;
import com.feed_the_beast.ftbl.cmd.CmdFTB;
import com.feed_the_beast.ftbl.lib.internal.FTBLibFinals;
import com.feed_the_beast.ftbl.lib.internal.FTBLibIntegrationInternal;
import com.feed_the_beast.ftbl.lib.internal.FTBLibStats;
import com.feed_the_beast.ftbl.lib.item.ODItems;
import com.feed_the_beast.ftbl.lib.util.LMServerUtils;
import com.feed_the_beast.ftbl.lib.util.LMUtils;
import com.feed_the_beast.ftbl.net.FTBLibNetHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerAboutToStartEvent;
import net.minecraftforge.fml.common.event.FMLServerStartedEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.event.FMLServerStoppedEvent;

import java.io.File;
import java.util.Locale;

@Mod(modid = FTBLibFinals.MOD_ID, name = FTBLibFinals.MOD_ID, version = "0.0.0", useMetadata = true, acceptableRemoteVersions = "*",
        dependencies = "required-after:Forge@[12.18.2.2121,);after:Baubles;after:JEI;after:nei;after:Waila;after:MineTweaker3;after:mcmultipart;after:chiselsandbits")
public class FTBLibMod
{
    @Mod.Instance(FTBLibFinals.MOD_ID)
    public static FTBLibMod INST;

    @SidedProxy(serverSide = "com.feed_the_beast.ftbl.FTBLibModCommon", clientSide = "com.feed_the_beast.ftbl.client.FTBLibModClient")
    public static FTBLibModCommon PROXY;

    @Mod.EventHandler
    public void onPreInit(FMLPreInitializationEvent event)
    {
        Locale.setDefault(Locale.US);
        FTBLibFinals.LOGGER.info("Loading FTBLib, DevEnv:" + LMUtils.DEV_ENV);
        new FTBLibAPI_Impl().init(event.getAsmData());
        LMUtils.init(event.getModConfigurationDirectory());
        PackModes.INSTANCE.load();
        FTBLibNetHandler.init();
        ODItems.preInit();
        FTBLibStats.init();

        MinecraftForge.EVENT_BUS.register(new FTBLibEventHandler());

        PROXY.preInit();
    }

    @Mod.EventHandler
    public void onPostInit(FMLPostInitializationEvent event)
    {
        PROXY.postInit();
    }

    @Mod.EventHandler
    public void onServerStarting(FMLServerStartingEvent event)
    {
        CmdFTB cmd = new CmdFTB(event.getServer().isDedicatedServer());
        event.registerServerCommand(cmd);

        if(FTBLibConfig.MIRROR_FTB_COMMANDS.getBoolean())
        {
            cmd.getSubCommands().forEach(event::registerServerCommand);
        }
    }

    @Mod.EventHandler
    public void onServerAboutToStart(FMLServerAboutToStartEvent event)
    {
        FTBLibFinals.LOGGER.info("FTBLib Loaded");
        SharedServerData.INSTANCE.reset();
        TickHandler.INSTANCE = new TickHandler();
        PROXY.reloadConfig();
        LMUtils.folderWorld = new File(FMLCommonHandler.instance().getSavesDirectory(), event.getServer().getFolderName());
        Universe.INSTANCE = new Universe();
        Universe.INSTANCE.init();
        PROXY.worldLoaded();
        LMServerUtils.addTickable(event.getServer(), TickHandler.INSTANCE);
        FTBLibIntegrationInternal.API.loadWorldData(event.getServer());
    }

    @Mod.EventHandler
    public void onServerStarted(FMLServerStartedEvent event)
    {
        FTBLibIntegrationInternal.API.reload(LMServerUtils.getServer(), EnumReloadType.SERVER_STARTED);
    }

    @Mod.EventHandler
    public void onServerShutDown(FMLServerStoppedEvent event)
    {
        Universe.INSTANCE.onClosed();
        Universe.INSTANCE = null;
    }
}