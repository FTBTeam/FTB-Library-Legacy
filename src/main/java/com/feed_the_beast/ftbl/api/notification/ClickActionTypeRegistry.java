package com.feed_the_beast.ftbl.api.notification;

import com.feed_the_beast.ftbl.api.ForgeWorldSP;
import com.feed_the_beast.ftbl.api.RegistryBase;
import com.feed_the_beast.ftbl.api.client.FTBLibClient;
import com.feed_the_beast.ftbl.api.client.gui.GuiLM;
import com.feed_the_beast.ftbl.api.client.gui.GuiScreenRegistry;
import com.feed_the_beast.ftbl.api.client.gui.guibuttons.ActionButton;
import com.feed_the_beast.ftbl.api.client.gui.guibuttons.ActionButtonRegistry;
import com.feed_the_beast.ftbl.gui.GuiInfo;
import com.latmod.lib.util.LMUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ResourceLocation;

import java.io.File;
import java.net.URI;

public class ClickActionTypeRegistry
{
    public static final RegistryBase<String, IClickActionType> INSTANCE = new RegistryBase<>(0);
    public static final String SIDEBAR_BUTTON = "sidebar_button";
    public static final String CMD = "cmd";
    public static final String SHOW_CMD = "show_cmd";
    public static final String URL = "url";
    public static final String FILE = "file";
    public static final String GUI = "gui";
    public static final String CHANGE_PAGE = "change_page";

    public static void init()
    {
        INSTANCE.register(SIDEBAR_BUTTON, (data, button) ->
        {
            ActionButton a = ActionButtonRegistry.INSTANCE.get(new ResourceLocation(data.getAsString()));

            if(a != null && a.isVisibleFor(ForgeWorldSP.inst.clientPlayer))
            {
                a.onClicked(ForgeWorldSP.inst.clientPlayer);
            }
        });

        INSTANCE.register(CMD, (data, button) -> FTBLibClient.execClientCommand("/" + data.getAsString(), false));
        INSTANCE.register(SHOW_CMD, (data, button) -> Minecraft.getMinecraft().displayGuiScreen(new GuiChat(data.getAsString())));

        INSTANCE.register(URL, (data, button) ->
        {
            try
            {
                LMUtils.openURI(new URI(data.getAsString()));
            }
            catch(Exception ex)
            {
                ex.printStackTrace();
            }
        });

        INSTANCE.register(FILE, (data, button) ->
        {
            try
            {
                LMUtils.openURI(new File(data.getAsString()).toURI());
            }
            catch(Exception ex)
            {
                ex.printStackTrace();
            }
        });

        INSTANCE.register(GUI, (data, button) ->
        {
            GuiScreen gui = GuiScreenRegistry.openGui(new ResourceLocation(data.getAsString()));

            if(gui != null)
            {
                Minecraft.getMinecraft().displayGuiScreen(gui);
            }
        });

        INSTANCE.register(CHANGE_PAGE, (data, button) ->
        {
            GuiInfo gui = GuiLM.getWrappedGui(Minecraft.getMinecraft().currentScreen, GuiInfo.class);

            if(gui != null)
            {
                //FIXME change current info page
            }
        });
    }
}