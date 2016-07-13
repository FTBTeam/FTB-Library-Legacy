package com.feed_the_beast.ftbl;

import com.feed_the_beast.ftbl.api.FTBLibCapabilities;
import com.feed_the_beast.ftbl.api.ForgeWorldMP;
import com.feed_the_beast.ftbl.api.PackModes;
import com.feed_the_beast.ftbl.api.config.ConfigRegistry;
import com.feed_the_beast.ftbl.api.item.ODItems;
import com.feed_the_beast.ftbl.api.notification.ClickActionType;
import com.feed_the_beast.ftbl.cmd.CmdFTB;
import com.feed_the_beast.ftbl.net.FTBLibNetHandler;
import com.feed_the_beast.ftbl.util.FTBLib;
import com.feed_the_beast.ftbl.util.JsonHelper;
import com.feed_the_beast.ftbl.util.LMMod;
import com.feed_the_beast.ftbl.util.ReloadType;
import com.latmod.lib.OS;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLInterModComms;
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

@Mod(modid = FTBLibFinals.MOD_ID, name = FTBLibFinals.MOD_NAME, version = FTBLibFinals.MOD_VERSION, dependencies = FTBLibFinals.MOD_DEP)
public class FTBLibMod
{
    public static final Logger logger = LogManager.getLogger("FTBLib");

    @Mod.Instance(FTBLibFinals.MOD_ID)
    public static FTBLibMod inst;

    @SidedProxy(serverSide = "com.feed_the_beast.ftbl.FTBLibModCommon", clientSide = "com.feed_the_beast.ftbl.client.FTBLibModClient")
    public static FTBLibModCommon proxy;

    public static LMMod mod;

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

        logger.info("OS: " + OS.current + ", 64bit: " + OS.IS_64_ARCH);

        mod = LMMod.create(FTBLibFinals.MOD_ID);

        FTBLib.init(e.getModConfigurationDirectory());
        JsonHelper.init();
        FTBLibNetHandler.init();
        ODItems.preInit();
        FTBLibStats.init();

        MinecraftForge.EVENT_BUS.register(FTBLibEventHandler.instance);
        FTBLibCapabilities.init();
        ClickActionType.init();

        proxy.preInit();
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent e)
    {
        FMLInterModComms.sendMessage("Waila", "register", "com.feed_the_beast.ftbl.WailaDataProvider.registerHandlers");
    }

    @Mod.EventHandler
    public void onPostInit(FMLPostInitializationEvent e)
    {
        PackModes.reload();
        ConfigRegistry.reload();
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
        ConfigRegistry.reload();
        PackModes.reload();
        FTBLib.folderWorld = new File(FMLCommonHandler.instance().getSavesDirectory(), e.getServer().getFolderName());

        ForgeWorldMP.inst = new ForgeWorldMP();

        try
        {
            ForgeWorldMP.inst.load();
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }

        FTBLib.registerServerTickable(e.getServer(), FTBLibEventHandler.instance);
    }

    @Mod.EventHandler
    public void onServerStarted(FMLServerStartedEvent e)
    {
        FTBLib.reload(FTBLib.getServer(), ReloadType.SERVER_ONLY, false);
    }

    @Mod.EventHandler
    public void onServerShutDown(FMLServerStoppedEvent e)
    {
        ForgeWorldMP.inst.onClosed();
        ForgeWorldMP.inst = null;
    }

    @NetworkCheckHandler
    public boolean checkNetwork(Map<String, String> m, Side side)
    {
        String s = m.get(FTBLibFinals.MOD_ID);
        return s == null || s.equals(FTBLibFinals.MOD_VERSION);
    }
}