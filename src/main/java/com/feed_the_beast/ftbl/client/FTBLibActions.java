package com.feed_the_beast.ftbl.client;

import com.feed_the_beast.ftbl.FTBLibFinals;
import com.feed_the_beast.ftbl.api.FTBLibAPI;
import com.feed_the_beast.ftbl.api.client.FTBLibClient;
import com.feed_the_beast.ftbl.api.gui.GuiHelper;
import com.feed_the_beast.ftbl.api.gui.GuiIcons;
import com.feed_the_beast.ftbl.api.gui.IMouseButton;
import com.feed_the_beast.ftbl.api_impl.SidebarButton;
import com.feed_the_beast.ftbl.api_impl.config.ConfigManager;
import com.feed_the_beast.ftbl.gui.GuiEditConfig;
import com.feed_the_beast.ftbl.gui.GuiInfo;
import com.feed_the_beast.ftbl.gui.friends.InfoFriendsGUI;
import com.latmod.lib.EnumEnabled;
import com.latmod.lib.TextureCoords;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;

import javax.annotation.Nullable;

public class FTBLibActions
{
    public static void init()
    {
        FTBLibAPI.get().getRegistries().sidebarButtons().register(new ResourceLocation(FTBLibFinals.MOD_ID, "teams_gui"), new SidebarButton(995, TextureCoords.fromUV(new ResourceLocation(FTBLibFinals.MOD_ID, "textures/gui/teams.png")), null)
        {
            @Override
            public void onClicked(IMouseButton button)
            {
                new GuiInfo(new InfoFriendsGUI()).openGui();
            }

            @Override
            @Nullable
            public ITextComponent getDisplayNameOverride()
            {
                return new TextComponentString("TeamsGUI");
            }

            @Override
            public void postRender(int ax, int ay)
            {
                if(!ClientNotifications.Perm.MAP.isEmpty())
                {
                    Minecraft mc = Minecraft.getMinecraft();
                    String n = String.valueOf(ClientNotifications.Perm.MAP.size());
                    int nw = mc.fontRendererObj.getStringWidth(n);
                    int width = 16;
                    GlStateManager.color(1F, 0.13F, 0.13F, 0.66F);
                    GuiHelper.drawBlankRect(ax + width - nw, ay - 4, nw + 1, 9);
                    GlStateManager.color(1F, 1F, 1F, 1F);
                    mc.fontRendererObj.drawString(n, ax + width - nw + 1, ay - 3, 0xFFFFFFFF);
                }
            }
        });

        FTBLibAPI.get().getRegistries().sidebarButtons().register(new ResourceLocation(FTBLibFinals.MOD_ID, "settings"), new SidebarButton(990, GuiIcons.SETTINGS, null)
        {
            @Override
            public void onClicked(IMouseButton button)
            {
                new GuiEditConfig(null, ConfigManager.INSTANCE.CLIENT_CONFIG_CONTAINER).openGui();
            }
        });

        FTBLibAPI.get().getRegistries().sidebarButtons().register(new ResourceLocation(FTBLibFinals.MOD_ID, "my_server_settings"), new SidebarButton(985, GuiIcons.SETTINGS_RED, EnumEnabled.ENABLED)
        {
            @Override
            public void onClicked(IMouseButton button)
            {
                FTBLibClient.execClientCommand("/ftb my_settings", false);
            }
        });

        FTBLibAPI.get().getRegistries().sidebarButtons().register(new ResourceLocation(FTBLibFinals.MOD_ID, "heal"), new SidebarButton(200, GuiIcons.HEART, EnumEnabled.ENABLED)
        {
            @Override
            public void onClicked(IMouseButton button)
            {
                FTBLibClient.execClientCommand("/ftb heal", false);
            }
        });

        FTBLibAPI.get().getRegistries().sidebarButtons().register(new ResourceLocation(FTBLibFinals.MOD_ID, "toggle_gamemode"), new SidebarButton(195, GuiIcons.TOGGLE_GAMEMODE, EnumEnabled.ENABLED)
        {
            @Override
            public void onClicked(IMouseButton button)
            {
                int i = Minecraft.getMinecraft().thePlayer.capabilities.isCreativeMode ? 0 : 1;
                FTBLibClient.execClientCommand("/gamemode " + i, false);
            }
        });

        FTBLibAPI.get().getRegistries().sidebarButtons().register(new ResourceLocation(FTBLibFinals.MOD_ID, "toggle_rain"), new SidebarButton(190, GuiIcons.TOGGLE_RAIN, EnumEnabled.ENABLED)
        {
            @Override
            public void onClicked(IMouseButton button)
            {
                FTBLibClient.execClientCommand("/toggledownfall", false);
            }
        });

        FTBLibAPI.get().getRegistries().sidebarButtons().register(new ResourceLocation(FTBLibFinals.MOD_ID, "set_day"), new SidebarButton(185, GuiIcons.TOGGLE_DAY, EnumEnabled.ENABLED)
        {
            @Override
            public void onClicked(IMouseButton button)
            {
                FTBLibClient.execClientCommand("/time set 6000", false);
            }
        });

        FTBLibAPI.get().getRegistries().sidebarButtons().register(new ResourceLocation(FTBLibFinals.MOD_ID, "set_night"), new SidebarButton(180, GuiIcons.TOGGLE_NIGHT, EnumEnabled.ENABLED)
        {
            @Override
            public void onClicked(IMouseButton button)
            {
                FTBLibClient.execClientCommand("/time set 18000", false);
            }
        });
    }
}