package com.feed_the_beast.ftbl.client;

import com.feed_the_beast.ftbl.FTBLibFinals;
import com.feed_the_beast.ftbl.api.client.FTBLibClient;
import com.feed_the_beast.ftbl.api.gui.GuiHelper;
import com.feed_the_beast.ftbl.api.gui.GuiIcons;
import com.feed_the_beast.ftbl.api.gui.IMouseButton;
import com.feed_the_beast.ftbl.api.gui.ISidebarButton;
import com.feed_the_beast.ftbl.api.gui.SidebarButton;
import com.feed_the_beast.ftbl.api_impl.ConfigManager;
import com.feed_the_beast.ftbl.api_impl.SidebarButtonInst;
import com.feed_the_beast.ftbl.gui.GuiEditConfig;
import com.feed_the_beast.ftbl.gui.GuiInfo;
import com.feed_the_beast.ftbl.gui.friends.InfoFriendsGUI;
import com.latmod.lib.EnumEnabled;
import com.latmod.lib.client.TextureCoords;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;

import javax.annotation.Nullable;

public class FTBLibActions
{
    @SidebarButton
    public static final ISidebarButton TEAMS_GUI = new SidebarButtonInst(new ResourceLocation(FTBLibFinals.MOD_ID, "teams_gui"), 995, TextureCoords.fromUV(new ResourceLocation(FTBLibFinals.MOD_ID, "textures/gui/teams.png")), null)
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
    };

    @SidebarButton
    public static final ISidebarButton SETTINGS = new SidebarButtonInst(new ResourceLocation(FTBLibFinals.MOD_ID, "settings"), 990, GuiIcons.SETTINGS, null)
    {
        @Override
        public void onClicked(IMouseButton button)
        {
            new GuiEditConfig(null, ConfigManager.INSTANCE.CLIENT_CONFIG_CONTAINER).openGui();
        }
    };

    @SidebarButton
    public static final ISidebarButton MY_SERVER_SETTINGS = new SidebarButtonInst(new ResourceLocation(FTBLibFinals.MOD_ID, "my_server_settings"), 985, GuiIcons.SETTINGS_RED, EnumEnabled.ENABLED)
    {
        @Override
        public void onClicked(IMouseButton button)
        {
            FTBLibClient.execClientCommand("/ftb my_settings", false);
        }
    };

    @SidebarButton
    public static final ISidebarButton HEAL = new SidebarButtonInst(new ResourceLocation(FTBLibFinals.MOD_ID, "heal"), 200, GuiIcons.HEART, EnumEnabled.ENABLED)
    {
        @Override
        public void onClicked(IMouseButton button)
        {
            FTBLibClient.execClientCommand("/ftb heal", false);
        }
    };

    @SidebarButton
    public static final ISidebarButton TOGGLE_GAMEMODE = new SidebarButtonInst(new ResourceLocation(FTBLibFinals.MOD_ID, "toggle_gamemode"), 195, GuiIcons.TOGGLE_GAMEMODE, EnumEnabled.ENABLED)
    {
        @Override
        public void onClicked(IMouseButton button)
        {
            int i = Minecraft.getMinecraft().thePlayer.capabilities.isCreativeMode ? 0 : 1;
            FTBLibClient.execClientCommand("/gamemode " + i, false);
        }
    };

    @SidebarButton
    public static final ISidebarButton TOGGLE_RAIN = new SidebarButtonInst(new ResourceLocation(FTBLibFinals.MOD_ID, "toggle_rain"), 190, GuiIcons.TOGGLE_RAIN, EnumEnabled.ENABLED)
    {
        @Override
        public void onClicked(IMouseButton button)
        {
            FTBLibClient.execClientCommand("/toggledownfall", false);
        }
    };

    @SidebarButton
    public static final ISidebarButton SET_DAY = new SidebarButtonInst(new ResourceLocation(FTBLibFinals.MOD_ID, "set_day"), 185, GuiIcons.TOGGLE_DAY, EnumEnabled.ENABLED)
    {
        @Override
        public void onClicked(IMouseButton button)
        {
            FTBLibClient.execClientCommand("/time set 6000", false);
        }
    };

    @SidebarButton
    public static final ISidebarButton SET_NIGHT = new SidebarButtonInst(new ResourceLocation(FTBLibFinals.MOD_ID, "set_night"), 180, GuiIcons.TOGGLE_NIGHT, EnumEnabled.ENABLED)
    {
        @Override
        public void onClicked(IMouseButton button)
        {
            FTBLibClient.execClientCommand("/time set 18000", false);
        }
    };
}