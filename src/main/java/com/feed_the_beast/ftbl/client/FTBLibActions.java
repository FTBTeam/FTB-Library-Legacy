package com.feed_the_beast.ftbl.client;

import com.feed_the_beast.ftbl.FTBLibMod;
import com.feed_the_beast.ftbl.api.gui.IMouseButton;
import com.feed_the_beast.ftbl.api.gui.ISidebarButton;
import com.feed_the_beast.ftbl.client.teamsgui.MyTeamData;
import com.feed_the_beast.ftbl.lib.SidebarButton;
import com.feed_the_beast.ftbl.lib.client.FTBLibClient;
import com.feed_the_beast.ftbl.lib.client.ImageProvider;
import com.feed_the_beast.ftbl.lib.config.PropertyBool;
import com.feed_the_beast.ftbl.lib.gui.GuiHelper;
import com.feed_the_beast.ftbl.lib.gui.GuiIcons;
import com.feed_the_beast.ftbl.lib.gui.misc.GuiEditConfig;
import com.feed_the_beast.ftbl.lib.gui.misc.GuiLoading;
import com.feed_the_beast.ftbl.lib.internal.FTBLibFinals;
import com.feed_the_beast.ftbl.lib.internal.FTBLibIntegrationInternal;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;

import javax.annotation.Nullable;

public class FTBLibActions
{
    public static final ISidebarButton TEAMS_GUI = new SidebarButton(FTBLibFinals.get("teams_gui"), new ImageProvider(new ResourceLocation(FTBLibFinals.MOD_ID, "textures/gui/teams.png")), null, "before:*")
    {
        @Override
        public void onClicked(IMouseButton button)
        {
            FTBLibClient.execClientCommand("/ftb team gui");
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
            if(MyTeamData.unreadMessages > 0)
            {
                Minecraft mc = Minecraft.getMinecraft();
                String n = String.valueOf(MyTeamData.unreadMessages);
                int nw = mc.fontRendererObj.getStringWidth(n);
                int width = 16;
                GlStateManager.color(1F, 0.13F, 0.13F, 0.66F);
                GuiHelper.drawBlankRect(ax + width - nw, ay - 4, nw + 1, 9);
                GlStateManager.color(1F, 1F, 1F, 1F);
                mc.fontRendererObj.drawString(n, ax + width - nw + 1, ay - 3, 0xFFFFFFFF);
            }
        }

        @Override
        public boolean isVisible()
        {
            return FTBLibIntegrationInternal.API.getClientData().hasOptionalServerMod(null);
        }
    };

    public static final ISidebarButton SETTINGS = new SidebarButton(FTBLibFinals.get("settings"), GuiIcons.SETTINGS, null, "after:ftbl.teams_gui")
    {
        @Override
        public void onClicked(IMouseButton button)
        {
            new GuiEditConfig(null, FTBLibMod.PROXY.getClientConfig()).openGui();
        }
    };

    public static final ISidebarButton MY_SERVER_SETTINGS = new SidebarButton(FTBLibFinals.get("my_server_settings"), GuiIcons.SETTINGS_RED, new PropertyBool(true), "after:ftbl.settings")
    {
        @Override
        public void onClicked(IMouseButton button)
        {
            new GuiLoading().openGui();
            FTBLibClient.execClientCommand("/ftb my_settings");
        }

        @Override
        public boolean isVisible()
        {
            return FTBLibIntegrationInternal.API.getClientData().hasOptionalServerMod(null);
        }
    };
}