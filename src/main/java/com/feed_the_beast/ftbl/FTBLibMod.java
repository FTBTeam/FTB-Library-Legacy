package com.feed_the_beast.ftbl;

import com.feed_the_beast.ftbl.api.events.ReloadType;
import com.feed_the_beast.ftbl.api.recipes.IRecipes;
import com.feed_the_beast.ftbl.api_impl.FTBLibAPI_Impl;
import com.feed_the_beast.ftbl.api_impl.FTBLibRegistries;
import com.feed_the_beast.ftbl.api_impl.LMRecipes;
import com.feed_the_beast.ftbl.api_impl.PackModes;
import com.feed_the_beast.ftbl.api_impl.TickHandler;
import com.feed_the_beast.ftbl.api_impl.Universe;
import com.feed_the_beast.ftbl.cmd.CmdFTB;
import com.feed_the_beast.ftbl.lib.item.ODItems;
import com.feed_the_beast.ftbl.lib.util.LMServerUtils;
import com.feed_the_beast.ftbl.lib.util.LMUtils;
import com.feed_the_beast.ftbl.net.FTBLibNetHandler;
import net.minecraft.command.ICommand;
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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;

@Mod(modid = FTBLibFinals.MOD_ID, name = FTBLibFinals.MOD_ID, version = "0.0.0", useMetadata = true, acceptableRemoteVersions = "*", dependencies = "after:Baubles;after:JEI;after:Waila;after:MineTweaker3;after:mcmultipart;after:chiselsandbits")
public class FTBLibMod
{
    public static final Logger LOGGER = LogManager.getLogger("FTBLib");

    @Mod.Instance(FTBLibFinals.MOD_ID)
    public static FTBLibMod INST;

    @SidedProxy(serverSide = "com.feed_the_beast.ftbl.FTBLibModCommon", clientSide = "com.feed_the_beast.ftbl.client.FTBLibModClient")
    public static FTBLibModCommon PROXY;

    @Mod.EventHandler
    public void onPreInit(FMLPreInitializationEvent event)
    {
        LOGGER.info("Loading FTBLib, DevEnv:" + LMUtils.DEV_ENV);
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
        FTBLibRegistries.INSTANCE.reloadConfig();

        IRecipes recipes = new LMRecipes();
        FTBLibRegistries.INSTANCE.RECIPE_HANDLERS.forEach(h -> h.loadRecipes(recipes));
        PROXY.postInit();
    }

    @Mod.EventHandler
    public void onServerStarting(FMLServerStartingEvent event)
    {
        CmdFTB cmd = new CmdFTB(event.getServer().isDedicatedServer());

        if(FTBLibConfig.USE_FTB_COMMAND_PREFIX.getBoolean())
        {
            event.registerServerCommand(cmd);
        }
        else
        {
            for(ICommand cmd1 : cmd.getSubCommands())
            {
                event.registerServerCommand(cmd1);
            }
        }
    }

    @Mod.EventHandler
    public void onServerAboutToStart(FMLServerAboutToStartEvent event)
    {
        TickHandler.INSTANCE = new TickHandler();
        FTBLibRegistries.INSTANCE.reloadConfig();
        LMUtils.folderWorld = new File(FMLCommonHandler.instance().getSavesDirectory(), event.getServer().getFolderName());
        Universe.INSTANCE = new Universe();
        Universe.INSTANCE.init();
        FTBLibRegistries.INSTANCE.worldLoaded();
        LMServerUtils.addTickable(event.getServer(), TickHandler.INSTANCE);
    }

    @Mod.EventHandler
    public void onServerStarted(FMLServerStartedEvent event)
    {
        FTBLibIntegrationInternal.API.reload(LMServerUtils.getServer(), ReloadType.SERVER_ONLY);
    }

    @Mod.EventHandler
    public void onServerShutDown(FMLServerStoppedEvent event)
    {
        Universe.INSTANCE.onClosed();
        Universe.INSTANCE = null;
    }
}