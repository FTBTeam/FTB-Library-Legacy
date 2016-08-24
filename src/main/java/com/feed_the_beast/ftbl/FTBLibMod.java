package com.feed_the_beast.ftbl;

import com.feed_the_beast.ftbl.api.FTBLibAPI;
import com.feed_the_beast.ftbl.api.FTBLibCapabilities;
import com.feed_the_beast.ftbl.api.item.ODItems;
import com.feed_the_beast.ftbl.api.recipes.IRecipeHandler;
import com.feed_the_beast.ftbl.api.recipes.IRecipes;
import com.feed_the_beast.ftbl.api_impl.FTBLibAPI_Impl;
import com.feed_the_beast.ftbl.api_impl.LMRecipes;
import com.feed_the_beast.ftbl.cmd.CmdFTB;
import com.feed_the_beast.ftbl.net.FTBLibNetHandler;
import com.feed_the_beast.ftbl.util.FTBLib;
import com.feed_the_beast.ftbl.util.ReloadType;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ITickable;
import net.minecraft.util.ResourceLocation;
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
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.minecraftforge.fml.relauncher.Side;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

@Mod(modid = FTBLibFinals.MOD_ID, name = FTBLibFinals.MOD_NAME, version = FTBLibFinals.MOD_VERSION, dependencies = FTBLibFinals.MOD_DEP)
public class FTBLibMod
{
    public static final Logger logger = LogManager.getLogger("FTBLib");

    @Mod.Instance(FTBLibFinals.MOD_ID)
    public static FTBLibMod inst;

    @SidedProxy(serverSide = "com.feed_the_beast.ftbl.FTBLibModCommon", clientSide = "com.feed_the_beast.ftbl.client.FTBLibModClient")
    public static FTBLibModCommon proxy;

    @Mod.EventHandler
    public void onPreInit(FMLPreInitializationEvent e)
    {
        if(FTBLib.DEV_ENV)
        {
            logger.info("Loading FTBLib, DevEnv");
        }
        else
        {
            logger.info("Loading FTBLib, v" + FTBLibFinals.MOD_VERSION);
        }

        FTBLibAPI.setAPI(FTBLibAPI_Impl.get());
        FTBLibAPI.get().getRegistries().syncedData().register(new ResourceLocation(FTBLibFinals.MOD_ID, "guis"), FTBLibAPI.get().getRegistries().guis());
        FTBLibAPI.get().getRegistries().syncedData().register(new ResourceLocation(FTBLibFinals.MOD_ID, "notifications"), FTBLibAPI.get().getRegistries().notifications());

        FTBLib.init(e.getModConfigurationDirectory());
        FTBLibNetHandler.init();
        ODItems.preInit();
        FTBLibStats.init();

        MinecraftForge.EVENT_BUS.register(new FTBLibEventHandler());
        FTBLibCapabilities.init();
        FTBLibNotifications.init();

        proxy.preInit();
    }

    @Mod.EventHandler
    public void onPostInit(FMLPostInitializationEvent e)
    {
        FTBLibAPI_Impl.get().reloadPackModes();
        FTBLibAPI_Impl.get().getRegistries().reloadConfig();

        IRecipes recipes = new LMRecipes();

        for(IRecipeHandler handler : FTBLibAPI_Impl.get().getRegistries().recipeHandlers().getValues())
        {
            handler.loadRecipes(recipes);
        }

        proxy.postInit();
    }

    @Mod.EventHandler
    public void onServerStarting(FMLServerStartingEvent e)
    {
        e.registerServerCommand(new CmdFTB(e.getServer().isDedicatedServer()));
    }

    @Mod.EventHandler
    public void onServerStarted(FMLServerAboutToStartEvent e)
    {
        FTBLibAPI_Impl.get().reloadPackModes();
        FTBLibAPI_Impl.get().getRegistries().reloadConfig();
        FTBLib.folderWorld = new File(FMLCommonHandler.instance().getSavesDirectory(), e.getServer().getFolderName());
        FTBLibAPI_Impl.get().createAndLoadWorld();

        try
        {
            Field field = ReflectionHelper.findField(MinecraftServer.class, "tickables", "field_71322_p");
            field.setAccessible(true);
            @SuppressWarnings("unchecked")
            List<ITickable> list = (List<ITickable>) field.get(e.getServer());
            list.add(FTBLibAPI_Impl.get().getRegistries());
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }

    @Mod.EventHandler
    public void onServerStarted(FMLServerStartedEvent e)
    {
        FTBLibAPI.get().reload(FTBLib.getServer(), ReloadType.SERVER_ONLY);
    }

    @Mod.EventHandler
    public void onServerShutDown(FMLServerStoppedEvent e)
    {
        FTBLibAPI_Impl.get().closeWorld();
    }

    @NetworkCheckHandler
    public boolean checkNetwork(Map<String, String> m, Side side)
    {
        if(side.isServer())
        {
            FTBLibAPI_Impl.get().setHasServer(m.containsKey(FTBLibFinals.MOD_ID));
        }

        String s = m.get(FTBLibFinals.MOD_ID);
        return s == null || s.equals(FTBLibFinals.MOD_VERSION);
    }
}