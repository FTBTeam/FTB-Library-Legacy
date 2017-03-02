package com.feed_the_beast.ftbl.client;

import com.feed_the_beast.ftbl.FTBLibModCommon;
import com.feed_the_beast.ftbl.api.IFTBLibClientRegistry;
import com.feed_the_beast.ftbl.api.IFTBLibPlugin;
import com.feed_the_beast.ftbl.api.INotification;
import com.feed_the_beast.ftbl.api.config.IConfigFile;
import com.feed_the_beast.ftbl.api.config.IConfigKey;
import com.feed_the_beast.ftbl.api.config.IConfigValue;
import com.feed_the_beast.ftbl.api.gui.IGuiProvider;
import com.feed_the_beast.ftbl.api.gui.ISidebarButton;
import com.feed_the_beast.ftbl.api_impl.FTBLibAPI_Impl;
import com.feed_the_beast.ftbl.lib.SidebarButton;
import com.feed_the_beast.ftbl.lib.client.FTBLibClient;
import com.feed_the_beast.ftbl.lib.client.ParticleColoredDust;
import com.feed_the_beast.ftbl.lib.config.ConfigFile;
import com.feed_the_beast.ftbl.lib.config.ConfigKey;
import com.feed_the_beast.ftbl.lib.gui.PlayerHeadImage;
import com.feed_the_beast.ftbl.lib.gui.misc.GuiConfigs;
import com.feed_the_beast.ftbl.lib.gui.misc.GuiInfo;
import com.feed_the_beast.ftbl.lib.info.InfoPage;
import com.feed_the_beast.ftbl.lib.internal.FTBLibFinals;
import com.feed_the_beast.ftbl.lib.internal.FTBLibIntegrationInternal;
import com.feed_the_beast.ftbl.lib.net.MessageLM;
import com.feed_the_beast.ftbl.lib.util.LMColorUtils;
import com.feed_the_beast.ftbl.lib.util.LMStringUtils;
import com.feed_the_beast.ftbl.lib.util.LMUtils;
import com.mojang.authlib.GameProfile;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiNewChat;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.event.HoverEvent;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.toposort.TopologicalSort;

import javax.annotation.Nullable;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FTBLibModClient extends FTBLibModCommon implements IFTBLibClientRegistry
{
    private IConfigFile clientConfig;
    private static final Map<String, ISidebarButton> SIDEBAR_BUTTON_MAP = new HashMap<>();
    private static final List<ISidebarButton> SIDEBAR_BUTTONS = new ArrayList<>();
    private static final ISidebarButton DBUTTON_BEFORE_ALL = new SidebarButton.Dummy(new ResourceLocation("before_all"));
    private static final ISidebarButton DBUTTON_AFTER_ALL = new SidebarButton.Dummy(new ResourceLocation("after_all"));
    private static final ISidebarButton DBUTTON_BEFORE = new SidebarButton.Dummy(new ResourceLocation("before"));
    private static final ISidebarButton DBUTTON_AFTER = new SidebarButton.Dummy(new ResourceLocation("after"));
    private static final Map<ResourceLocation, IGuiProvider> GUI_PROVIDERS = new HashMap<>();

    @Override
    public void preInit()
    {
        super.preInit();

        clientConfig = new ConfigFile(new TextComponentTranslation("sidebar_button." + FTBLibActions.SETTINGS.getName()), () -> new File(LMUtils.folderLocal, "client_config.json"));

        addClientConfig(FTBLibFinals.MOD_ID, "item_ore_names", FTBLibClientConfig.ITEM_ORE_NAMES);
        addClientConfig(FTBLibFinals.MOD_ID, "action_buttons_on_top", FTBLibClientConfig.ACTION_BUTTONS_ON_TOP);
        addClientConfig(FTBLibFinals.MOD_ID, "notifications", FTBLibClientConfig.NOTIFICATIONS);

        addClientConfig(FTBLibFinals.MOD_ID, "gui.info.border_width", GuiConfigs.INFO_BORDER_WIDTH, IConfigKey.USE_SCROLL_BAR);
        addClientConfig(FTBLibFinals.MOD_ID, "gui.info.border_height", GuiConfigs.INFO_BORDER_HEIGHT, IConfigKey.USE_SCROLL_BAR);
        addClientConfig(FTBLibFinals.MOD_ID, "gui.info.color_background", GuiConfigs.INFO_BACKGROUND);
        addClientConfig(FTBLibFinals.MOD_ID, "gui.info.color_text", GuiConfigs.INFO_TEXT);
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
        });

        TopologicalSort.DirectedGraph<ISidebarButton> sidebarButtonGraph = new TopologicalSort.DirectedGraph<>();
        sidebarButtonGraph.addNode(DBUTTON_BEFORE_ALL);
        sidebarButtonGraph.addNode(DBUTTON_BEFORE);
        sidebarButtonGraph.addNode(DBUTTON_AFTER_ALL);
        sidebarButtonGraph.addNode(DBUTTON_AFTER);
        sidebarButtonGraph.addEdge(DBUTTON_BEFORE, DBUTTON_AFTER);
        sidebarButtonGraph.addEdge(DBUTTON_BEFORE_ALL, DBUTTON_BEFORE);
        sidebarButtonGraph.addEdge(DBUTTON_AFTER, DBUTTON_AFTER_ALL);

        SIDEBAR_BUTTON_MAP.values().forEach(sidebarButtonGraph::addNode);

        for(ISidebarButton button : SIDEBAR_BUTTON_MAP.values())
        {
            boolean preDepAdded = false;
            boolean postDepAdded = false;

            for(Map.Entry<String, Boolean> entry : button.getDependencies().entrySet())
            {
                if(entry.getValue())
                {
                    for(String id : button.getDependencies().keySet())
                    {
                        postDepAdded = true;

                        if(id.equals("*"))
                        {
                            sidebarButtonGraph.addEdge(DBUTTON_BEFORE_ALL, button);
                            sidebarButtonGraph.addEdge(button, DBUTTON_BEFORE);
                            preDepAdded = true;
                        }
                        else
                        {
                            sidebarButtonGraph.addEdge(button, DBUTTON_AFTER);
                            if(SIDEBAR_BUTTON_MAP.containsKey(id))
                            {
                                sidebarButtonGraph.addEdge(button, SIDEBAR_BUTTON_MAP.get(id));
                            }
                        }
                    }
                }
                else
                {
                    preDepAdded = true;
                    String id = entry.getKey();

                    if(id.equals("*"))
                    {
                        sidebarButtonGraph.addEdge(button, DBUTTON_AFTER_ALL);
                        sidebarButtonGraph.addEdge(DBUTTON_AFTER, button);
                        postDepAdded = true;
                    }
                    else
                    {
                        sidebarButtonGraph.addEdge(DBUTTON_BEFORE, button);
                        if(SIDEBAR_BUTTON_MAP.containsKey(id))
                        {
                            sidebarButtonGraph.addEdge(SIDEBAR_BUTTON_MAP.get(id), button);
                        }
                    }
                }
            }

            if(!preDepAdded)
            {
                sidebarButtonGraph.addEdge(DBUTTON_BEFORE, button);
            }

            if(!postDepAdded)
            {
                sidebarButtonGraph.addEdge(button, DBUTTON_AFTER);
            }
        }

        List<ISidebarButton> sortedSidebarButtonList = TopologicalSort.topologicalSort(sidebarButtonGraph);
        sortedSidebarButtonList.removeAll(Arrays.asList(DBUTTON_BEFORE_ALL, DBUTTON_BEFORE, DBUTTON_AFTER, DBUTTON_AFTER_ALL));
        SIDEBAR_BUTTONS.clear();
        SIDEBAR_BUTTONS.addAll(sortedSidebarButtonList);
        FTBLibFinals.LOGGER.info("Sorted sidebar buttons to " + SIDEBAR_BUTTONS);

        MinecraftForge.EVENT_BUS.register(new FTBLibClientEventHandler());

        //For Dev reasons
        GameProfile profile = Minecraft.getMinecraft().getSession().getProfile();
        if(profile.getId().equals(LMStringUtils.fromString("5afb9a5b207d480e887967bc848f9a8f")))
        {
            LMUtils.userIsLatvianModder = true;
        }

        FTBLibClient.localPlayerHead = new PlayerHeadImage(profile.getName());
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

    @Override
    public void handleClientMessage(MessageLM<?> message)
    {
        Minecraft.getMinecraft().addScheduledTask(() ->
        {
            message.onMessage(LMUtils.cast(message), Minecraft.getMinecraft().thePlayer);

            if(FTBLibAPI_Impl.LOG_NET)
            {
                LMUtils.DEV_LOGGER.info("RX MessageLM: " + message.getClass().getName());
            }
        });
    }

    @Override
    public void displayInfoGui(InfoPage page)
    {
        new GuiInfo(page).openGui();
    }

    @Override
    public void displayNotification(EnumNotificationDisplay display, INotification n)
    {
        if(display == EnumNotificationDisplay.SCREEN)
        {
            ClientNotifications.add(n);
            return;
        }

        List<ITextComponent> list = n.getText();

        if(list.isEmpty())
        {
            return;
        }

        if(list.size() > 1)
        {
            list.get(0).getStyle().setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, list.get(1)));
        }

        GuiNewChat chat = Minecraft.getMinecraft().ingameGUI.getChatGUI();

        if(display == EnumNotificationDisplay.CHAT)
        {
            chat.printChatMessageWithOptionalDeletion(list.get(0), n.getId().getChatMessageID());
        }
        else
        {
            chat.printChatMessage(list.get(0));
        }
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