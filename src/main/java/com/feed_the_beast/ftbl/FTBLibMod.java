package com.feed_the_beast.ftbl;

import com.feed_the_beast.ftbl.api.events.ReloadType;
import com.feed_the_beast.ftbl.api.recipes.IRecipes;
import com.feed_the_beast.ftbl.api_impl.FTBLibAPI_Impl;
import com.feed_the_beast.ftbl.api_impl.FTBLibCaps;
import com.feed_the_beast.ftbl.api_impl.FTBLibRegistries;
import com.feed_the_beast.ftbl.api_impl.LMRecipes;
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
import net.minecraftforge.fml.common.network.NetworkCheckHandler;
import net.minecraftforge.fml.relauncher.Side;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.util.Map;

@Mod(modid = FTBLibFinals.MOD_ID, name = FTBLibFinals.MOD_NAME, version = FTBLibFinals.MOD_VERSION, dependencies = "after:Baubles;after:JEI;after:Waila;after:MineTweaker3;after:mcmultipart;after:chiselsandbits")
public class FTBLibMod
{
    public static final Logger logger = LogManager.getLogger("FTBLib");

    @Mod.Instance(FTBLibFinals.MOD_ID)
    public static FTBLibMod inst;

    @SidedProxy(serverSide = "com.feed_the_beast.ftbl.FTBLibModCommon", clientSide = "com.feed_the_beast.ftbl.client.FTBLibModClient")
    public static FTBLibModCommon proxy;

    @Mod.EventHandler
    public void onPreInit(FMLPreInitializationEvent event)
    {
        if(LMUtils.DEV_ENV)
        {
            logger.info("Loading FTBLib, DevEnv");
        }
        else
        {
            logger.info("Loading FTBLib, v" + FTBLibFinals.MOD_VERSION);
        }

        FTBLibAPI_Impl.INSTANCE.init(event.getAsmData());

        LMUtils.init(event.getModConfigurationDirectory());
        FTBLibNetHandler.init();
        ODItems.preInit();
        FTBLibStats.init();
        MinecraftForge.EVENT_BUS.register(new FTBLibEventHandler());
        FTBLibCaps.init();
        proxy.preInit();
    }

    @Mod.EventHandler
    public void onPostInit(FMLPostInitializationEvent event)
    {
        FTBLibAPI_Impl.INSTANCE.reloadPackModes();
        FTBLibRegistries.INSTANCE.reloadConfig();

        IRecipes recipes = new LMRecipes();
        FTBLibRegistries.INSTANCE.RECIPE_HANDLERS.forEach(h -> h.loadRecipes(recipes));
        proxy.postInit();
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
    public void onServerStarted(FMLServerAboutToStartEvent event)
    {
        FTBLibAPI_Impl.INSTANCE.reloadPackModes();
        FTBLibRegistries.INSTANCE.reloadConfig();
        LMUtils.folderWorld = new File(FMLCommonHandler.instance().getSavesDirectory(), event.getServer().getFolderName());
        FTBLibAPI_Impl.INSTANCE.createAndLoadWorld();
        LMServerUtils.addTickable(event.getServer(), FTBLibAPI_Impl.INSTANCE);
    }

    @Mod.EventHandler
    public void onServerStarted(FMLServerStartedEvent event)
    {
        FTBLibAPI_Impl.INSTANCE.reload(LMServerUtils.getServer(), ReloadType.SERVER_ONLY);
    }

    @Mod.EventHandler
    public void onServerShutDown(FMLServerStoppedEvent event)
    {
        FTBLibAPI_Impl.INSTANCE.closeWorld();
    }

    @NetworkCheckHandler
    public boolean checkNetwork(Map<String, String> m, Side side)
    {
        String s = m.get(FTBLibFinals.MOD_ID);
        return s == null || s.equals(FTBLibFinals.MOD_VERSION);
    }
}