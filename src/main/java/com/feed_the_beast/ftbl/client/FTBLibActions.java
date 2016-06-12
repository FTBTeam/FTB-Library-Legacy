package com.feed_the_beast.ftbl.client;

import com.feed_the_beast.ftbl.FTBLibFinals;
import com.feed_the_beast.ftbl.api.ForgePlayerSP;
import com.feed_the_beast.ftbl.api.client.FTBLibClient;
import com.feed_the_beast.ftbl.api.client.gui.GuiIcons;
import com.feed_the_beast.ftbl.api.client.gui.GuiLM;
import com.feed_the_beast.ftbl.api.client.gui.GuiScreenRegistry;
import com.feed_the_beast.ftbl.api.client.gui.guibuttons.ActionButton;
import com.feed_the_beast.ftbl.api.client.gui.guibuttons.ActionButtonRegistry;
import com.feed_the_beast.ftbl.api.client.gui.guibuttons.SidebarButton;
import com.feed_the_beast.ftbl.api.config.ClientConfigRegistry;
import com.feed_the_beast.ftbl.api.notification.ClientNotifications;
import com.feed_the_beast.ftbl.gui.GuiEditConfig;
import com.feed_the_beast.ftbl.gui.GuiNotifications;
import com.feed_the_beast.ftbl.gui.friends.InfoFriendsGUI;
import com.feed_the_beast.ftbl.gui.info.GuiInfo;
import com.feed_the_beast.ftbl.util.TextureCoords;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;

public class FTBLibActions
{
    public static final ActionButton NOTIFICATIONS = ActionButtonRegistry.add(new SidebarButton(new ResourceLocation(FTBLibFinals.MOD_ID, "notifications"), 1000, GuiIcons.chat, null)
    {
        @Override
        public void onClicked(ForgePlayerSP player)
        {
            new GuiNotifications().openGui();
        }

        @Override
        protected ITextComponent getDisplayName()
        {
            return new TextComponentTranslation("client_config.notifications");
        }

        @Override
        public boolean isVisibleFor(ForgePlayerSP player)
        {
            return !ClientNotifications.Perm.list.isEmpty();
        }

        @Override
        public void postRender(double ax, double ay)
        {
            String n = String.valueOf(ClientNotifications.Perm.list.size());
            int nw = FTBLibClient.mc().fontRendererObj.getStringWidth(n);
            int width = 16;
            GlStateManager.color(1F, 0.13F, 0.13F, 0.66F);
            GuiLM.drawBlankRect(ax + width - nw, ay - 4, nw + 1, 9);
            GlStateManager.color(1F, 1F, 1F, 1F);
            FTBLibClient.mc().fontRendererObj.drawString(n, (int) (ax + width - nw + 1), (int) (ay - 3), 0xFFFFFFFF);
        }
    });

    public static final ActionButton FRIENDS_GUI = ActionButtonRegistry.add(new SidebarButton(new ResourceLocation(FTBLibFinals.MOD_ID, "friends_gui"), 995, TextureCoords.getSquareIcon(new ResourceLocation("ftbl", "textures/gui/friendsbutton.png"), 256), null)
    {
        @Override
        public void onClicked(ForgePlayerSP player)
        {
            new GuiInfo(null, new InfoFriendsGUI()).openGui();
        }

        @Override
        protected ITextComponent getDisplayName()
        {
            return new TextComponentString("FriendsGUI");
        }
    });

    public static final ActionButton CLIENT_SETTINGS = ActionButtonRegistry.add(new SidebarButton(new ResourceLocation(FTBLibFinals.MOD_ID, "settings"), 990, GuiIcons.settings, null)
    {
        @Override
        public void onClicked(ForgePlayerSP player)
        {
            new GuiEditConfig(null, ClientConfigRegistry.CONTAINER).openGui();
        }
    });

    public static final ActionButton MY_SERVER_SETTINGS = ActionButtonRegistry.add(new SidebarButton(new ResourceLocation(FTBLibFinals.MOD_ID, "my_server_settings"), 985, GuiIcons.settings_red, true)
    {
        @Override
        public void onClicked(ForgePlayerSP player)
        {
            FTBLibClient.execClientCommand("/ftblib my_settings");
        }
    });

    public static final ActionButton HEAL = ActionButtonRegistry.add(new SidebarButton(new ResourceLocation(FTBLibFinals.MOD_ID, "heal"), 200, GuiIcons.heart, true)
    {
        @Override
        public void onClicked(ForgePlayerSP player)
        {
            FTBLibClient.execClientCommand("/ftb heal");
        }
    });

    public static final ActionButton TOGGLE_GAMEMODE = ActionButtonRegistry.add(new SidebarButton(new ResourceLocation(FTBLibFinals.MOD_ID, "toggle_gamemode"), 195, GuiIcons.toggle_gamemode, true)
    {
        @Override
        public void onClicked(ForgePlayerSP player)
        {
            int i = player.getPlayer().capabilities.isCreativeMode ? 0 : 1;
            FTBLibClient.execClientCommand("/gamemode " + i);
        }
    });

    public static final ActionButton TOGGLE_RAIN = ActionButtonRegistry.add(new SidebarButton(new ResourceLocation(FTBLibFinals.MOD_ID, "toggle_rain"), 190, GuiIcons.toggle_rain, true)
    {
        @Override
        public void onClicked(ForgePlayerSP player)
        {
            FTBLibClient.execClientCommand("/toggledownfall");
        }
    });

    public static final ActionButton SET_DAY = ActionButtonRegistry.add(new SidebarButton(new ResourceLocation(FTBLibFinals.MOD_ID, "set_day"), 185, GuiIcons.toggle_day, true)
    {
        @Override
        public void onClicked(ForgePlayerSP player)
        {
            FTBLibClient.execClientCommand("/time set 6000");
        }
    });

    public static final ActionButton SET_NIGHT = ActionButtonRegistry.add(new SidebarButton(new ResourceLocation(FTBLibFinals.MOD_ID, "set_night"), 180, GuiIcons.toggle_night, true)
    {
        @Override
        public void onClicked(ForgePlayerSP player)
        {
            FTBLibClient.execClientCommand("/time set 18000");
        }
    });

    public static void init()
    {
        GuiScreenRegistry.register(new ResourceLocation(FTBLibFinals.MOD_ID, "notifications"), () -> new GuiNotifications().getWrapper());
        GuiScreenRegistry.register(new ResourceLocation(FTBLibFinals.MOD_ID, "friends_gui"), () -> new GuiInfo(null, new InfoFriendsGUI()).getWrapper());
        GuiScreenRegistry.register(new ResourceLocation(FTBLibFinals.MOD_ID, "client_config"), () -> new GuiEditConfig(null, ClientConfigRegistry.CONTAINER).getWrapper());
    }
}