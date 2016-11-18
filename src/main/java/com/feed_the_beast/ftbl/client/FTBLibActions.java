package com.feed_the_beast.ftbl.client;

import com.feed_the_beast.ftbl.FTBLibMod;
import com.feed_the_beast.ftbl.api.client.FTBLibClient;
import com.feed_the_beast.ftbl.api.gui.IMouseButton;
import com.feed_the_beast.ftbl.api.gui.ISidebarButton;
import com.feed_the_beast.ftbl.lib.SidebarButton;
import com.feed_the_beast.ftbl.lib.client.TextureCoords;
import com.feed_the_beast.ftbl.lib.config.PropertyBool;
import com.feed_the_beast.ftbl.lib.gui.GuiHelper;
import com.feed_the_beast.ftbl.lib.gui.GuiIcons;
import com.feed_the_beast.ftbl.lib.gui.misc.GuiEditConfig;
import com.feed_the_beast.ftbl.lib.gui.misc.GuiLoading;
import com.feed_the_beast.ftbl.lib.internal.FTBLibFinals;
import com.feed_the_beast.ftbl.lib.internal.FTBLibIntegrationInternal;
import com.feed_the_beast.ftbl.net.MessageTeamsGuiRequest;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;

import javax.annotation.Nullable;

public class FTBLibActions
{
    public static final ISidebarButton TEAMS_GUI = new SidebarButton(995, TextureCoords.fromUV(new ResourceLocation(FTBLibFinals.MOD_ID, "textures/gui/teams.png")), null)
    {
        @Override
        public void onClicked(IMouseButton button)
        {
            new GuiLoading().openGui();
            new MessageTeamsGuiRequest().sendToServer();
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

        @Override
        public boolean isVisible()
        {
            return FTBLibIntegrationInternal.API.getClientData().hasOptionalServerMod(null);
        }
    };

    public static final ISidebarButton SETTINGS = new SidebarButton(990, GuiIcons.SETTINGS, null)
    {
        @Override
        public void onClicked(IMouseButton button)
        {
            new GuiEditConfig(null, FTBLibMod.PROXY.getClientConfig()).openGui();
        }
    };

    public static final ISidebarButton MY_SERVER_SETTINGS = new SidebarButton(985, GuiIcons.SETTINGS_RED, new PropertyBool(true))
    {
        @Override
        public void onClicked(IMouseButton button)
        {
            FTBLibClient.execClientCommand(FTBLibIntegrationInternal.API.getClientData().useFTBPrefix() ? "/ftb my_settings" : "/my_settings", false);
        }

        @Override
        public boolean isVisible()
        {
            return FTBLibIntegrationInternal.API.getClientData().hasOptionalServerMod(null);
        }
    };
}