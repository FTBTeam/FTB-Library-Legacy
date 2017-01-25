package com.feed_the_beast.ftbl.client;

import com.feed_the_beast.ftbl.FTBLibModCommon;
import com.feed_the_beast.ftbl.api.IFTBLibClientRegistry;
import com.feed_the_beast.ftbl.api.IFTBLibPlugin;
import com.feed_the_beast.ftbl.api.config.IConfigFile;
import com.feed_the_beast.ftbl.api.config.IConfigKey;
import com.feed_the_beast.ftbl.api.config.IConfigValue;
import com.feed_the_beast.ftbl.api.gui.IGuiProvider;
import com.feed_the_beast.ftbl.api.gui.ISidebarButton;
import com.feed_the_beast.ftbl.lib.client.ParticleColoredDust;
import com.feed_the_beast.ftbl.lib.config.ConfigFile;
import com.feed_the_beast.ftbl.lib.config.ConfigKey;
import com.feed_the_beast.ftbl.lib.gui.misc.GuiConfigs;
import com.feed_the_beast.ftbl.lib.internal.FTBLibFinals;
import com.feed_the_beast.ftbl.lib.internal.FTBLibIntegrationInternal;
import com.feed_the_beast.ftbl.lib.util.LMColorUtils;
import com.feed_the_beast.ftbl.lib.util.LMStringUtils;
import com.feed_the_beast.ftbl.lib.util.LMUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;

import javax.annotation.Nullable;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FTBLibModClient extends FTBLibModCommon implements IFTBLibClientRegistry
{
    private IConfigFile clientConfig;
    private static final Map<String, ISidebarButton> SIDEBAR_BUTTON_MAP = new HashMap<>();
    private static final List<ISidebarButton> SIDEBAR_BUTTONS = new ArrayList<>();
    private static final Map<ResourceLocation, IGuiProvider> GUI_PROVIDERS = new HashMap<>();

    @Override
    public void preInit()
    {
        super.preInit();

        clientConfig = new ConfigFile(new TextComponentTranslation("sidebar_button." + FTBLibActions.SETTINGS.getName()), () -> new File(LMUtils.folderLocal, "client_config.json"));

        addClientConfig(FTBLibFinals.MOD_ID, "item_ore_names", FTBLibClientConfig.ITEM_ORE_NAMES);
        addClientConfig(FTBLibFinals.MOD_ID, "action_buttons_on_top", FTBLibClientConfig.ACTION_BUTTONS_ON_TOP);
        addClientConfig(FTBLibFinals.MOD_ID, "notifications", FTBLibClientConfig.NOTIFICATIONS);

        addClientConfig(FTBLibFinals.MOD_ID, "gui.info.unicode", GuiConfigs.UNICODE);
        addClientConfig(FTBLibFinals.MOD_ID, "gui.info.border_width", GuiConfigs.BORDER_WIDTH, IConfigKey.USE_SCROLL_BAR);
        addClientConfig(FTBLibFinals.MOD_ID, "gui.info.border_height", GuiConfigs.BORDER_HEIGHT, IConfigKey.USE_SCROLL_BAR);
        addClientConfig(FTBLibFinals.MOD_ID, "gui.enable_chunk_selector_depth", GuiConfigs.ENABLE_CHUNK_SELECTOR_DEPTH);

        addSidebarButton(FTBLibActions.TEAMS_GUI);
        addSidebarButton(FTBLibActions.SETTINGS);
        addSidebarButton(FTBLibActions.MY_SERVER_SETTINGS);

        for(IFTBLibPlugin plugin : FTBLibIntegrationInternal.API.getAllPlugins())
        {
            plugin.registerClient(this);
        }

        SIDEBAR_BUTTON_MAP.forEach((key, button) ->
        {
            IConfigValue value = button.getConfig();

            if(value != null)
            {
                clientConfig.add(new ConfigKey("sidebar_button." + button.getName(), value.copy()), value);
            }

            SIDEBAR_BUTTONS.add(button);
        });

        /*
        {
            HashMap<String, ModContainer> nameLookup = Maps.newHashMap();
            ModContainer beforeAll = new DummyModContainer("BeforeAll");
            ModContainer afterAll = new DummyModContainer("AfterAll");
            ModContainer before = new DummyModContainer("Before");
            ModContainer after = new DummyModContainer("After");

            TopologicalSort.DirectedGraph<ModContainer> modGraph = new TopologicalSort.DirectedGraph<>();
            modGraph.addNode(beforeAll);
            modGraph.addNode(before);
            modGraph.addNode(afterAll);
            modGraph.addNode(after);
            modGraph.addEdge(before, after);
            modGraph.addEdge(beforeAll, before);
            modGraph.addEdge(after, afterAll);

            for(ModContainer mod : nameLookup.values())
            {
                modGraph.addNode(mod);
            }

            for(ModContainer mod : nameLookup.values())
            {
                if(mod.isImmutable())
                {
                    modGraph.addEdge(beforeAll, mod);
                    modGraph.addEdge(mod, before);
                    continue;
                }
                boolean preDepAdded = false;
                boolean postDepAdded = false;

                for(ArtifactVersion dep : mod.getDependencies())
                {
                    preDepAdded = true;

                    String modid = dep.getLabel();
                    if(modid.equals("*"))
                    {
                        modGraph.addEdge(mod, afterAll);
                        modGraph.addEdge(after, mod);
                        postDepAdded = true;
                    }
                    else
                    {
                        modGraph.addEdge(before, mod);
                        if(nameLookup.containsKey(modid) || Loader.isModLoaded(modid))
                        {
                            modGraph.addEdge(nameLookup.get(modid), mod);
                        }
                    }
                }

                for(ArtifactVersion dep : mod.getDependants())
                {
                    postDepAdded = true;

                    String modid = dep.getLabel();
                    if(modid.equals("*"))
                    {
                        modGraph.addEdge(beforeAll, mod);
                        modGraph.addEdge(mod, before);
                        preDepAdded = true;
                    }
                    else
                    {
                        modGraph.addEdge(mod, after);
                        if(Loader.isModLoaded(modid))
                        {
                            modGraph.addEdge(mod, nameLookup.get(modid));
                        }
                    }
                }

                if(!preDepAdded)
                {
                    modGraph.addEdge(before, mod);
                }

                if(!postDepAdded)
                {
                    modGraph.addEdge(mod, after);
                }
            }

            List<ModContainer> sortedList = TopologicalSort.topologicalSort(modGraph);
            sortedList.removeAll(Arrays.asList(new ModContainer[] {beforeAll, before, after, afterAll}));
        }

        FTBLibFinals.LOGGER.info("Sorted to " + SIDEBAR_BUTTONS);
        */

        MinecraftForge.EVENT_BUS.register(new FTBLibClientEventHandler());

        //For Dev reasons
        if(Minecraft.getMinecraft().getSession().getProfile().getId().equals(LMStringUtils.fromString("5afb9a5b207d480e887967bc848f9a8f")))
        {
            LMUtils.userIsLatvianModder = true;
        }
    }

    @Override
    public void postInit()
    {
        super.postInit();
    }

    @Override
    public void spawnDust(World w, double x, double y, double z, int col)
    {
        float alpha = LMColorUtils.getAlphaF(col);
        float red = LMColorUtils.getRedF(col);
        float green = LMColorUtils.getGreenF(col);
        float blue = LMColorUtils.getBlueF(col);
        if(alpha <= 0F)
        {
            alpha = 1F;
        }

        Minecraft.getMinecraft().effectRenderer.addEffect(new ParticleColoredDust(w, x, y, z, red, green, blue, alpha));
    }

    @Override
    public void loadAllFiles()
    {
        super.loadAllFiles();
        clientConfig.load();
    }

    @Override
    public void saveAllFiles()
    {
        super.saveAllFiles();
        clientConfig.save();
    }

    @Override
    public void addClientConfig(IConfigKey key, IConfigValue value)
    {
        clientConfig.add(key, value);
    }

    @Override
    public void addGui(ResourceLocation id, IGuiProvider provider)
    {
        GUI_PROVIDERS.put(id, provider);
    }

    @Override
    public void addSidebarButton(ISidebarButton button)
    {
        SIDEBAR_BUTTON_MAP.put(button.getName(), button);
    }

    @Override
    public IConfigFile getClientConfig()
    {
        return clientConfig;
    }

    public static List<ISidebarButton> getSidebarButtons(boolean ignoreConfig)
    {
        if(ignoreConfig)
        {
            return SIDEBAR_BUTTONS;
        }

        List<ISidebarButton> list = new ArrayList<>();

        for(ISidebarButton button : SIDEBAR_BUTTONS)
        {
            if(button.isVisible() && (button.getConfig() == null || button.getConfig().getBoolean()))
            {
                list.add(button);
            }
        }

        return list;
    }

    @Nullable
    public static IGuiProvider getGui(ResourceLocation id)
    {
        return GUI_PROVIDERS.get(id);
    }
}