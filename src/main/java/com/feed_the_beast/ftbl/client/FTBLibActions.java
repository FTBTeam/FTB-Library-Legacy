package com.feed_the_beast.ftbl.client;

import com.feed_the_beast.ftbl.FTBLibFinals;
import com.feed_the_beast.ftbl.api.ForgePlayerSP;
import com.feed_the_beast.ftbl.api.client.FTBLibClient;
import com.feed_the_beast.ftbl.api.client.gui.GuiIcons;
import com.feed_the_beast.ftbl.api.client.gui.GuiLM;
import com.feed_the_beast.ftbl.api.client.gui.GuiScreenRegistry;
import com.feed_the_beast.ftbl.api.client.gui.guibuttons.ActionButtonRegistry;
import com.feed_the_beast.ftbl.api.client.gui.guibuttons.SidebarButton;
import com.feed_the_beast.ftbl.api.config.ClientConfigRegistry;
import com.feed_the_beast.ftbl.api.notification.ClientNotifications;
import com.feed_the_beast.ftbl.gui.GuiEditConfig;
import com.feed_the_beast.ftbl.gui.friends.InfoFriendsGUI;
import com.feed_the_beast.ftbl.gui.info.GuiInfo;
import com.feed_the_beast.ftbl.util.TextureCoords;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

public class FTBLibActions
{
    @SideOnly(Side.CLIENT)
    public static void init()
    {
        ActionButtonRegistry.add(new ResourceLocation(FTBLibFinals.MOD_ID, "friends_gui"), new SidebarButton(995, TextureCoords.getSquareIcon(new ResourceLocation(FTBLibFinals.MOD_ID, "textures/gui/friendsbutton.png")), null)
        {
            @Override
            public void onClicked(ForgePlayerSP player)
            {
                new GuiInfo(null, new InfoFriendsGUI()).openGui();
            }

            @Override
            @Nullable
            protected ITextComponent getDisplayNameOverride()
            {
                return new TextComponentString("FriendsGUI");
            }

            @Override
            public void postRender(Minecraft mc, double ax, double ay)
            {
                if(!ClientNotifications.Perm.map.isEmpty())
                {
                    String n = String.valueOf(ClientNotifications.Perm.map.size());
                    int nw = mc.fontRendererObj.getStringWidth(n);
                    int width = 16;
                    GlStateManager.color(1F, 0.13F, 0.13F, 0.66F);
                    GuiLM.drawBlankRect(ax + width - nw, ay - 4, nw + 1, 9);
                    GlStateManager.color(1F, 1F, 1F, 1F);
                    mc.fontRendererObj.drawString(n, (int) (ax + width - nw + 1), (int) (ay - 3), 0xFFFFFFFF);
                }
            }
        });

        ActionButtonRegistry.add(new ResourceLocation(FTBLibFinals.MOD_ID, "settings"), new SidebarButton(990, GuiIcons.settings, null)
        {
            @Override
            @SideOnly(Side.CLIENT)
            public void onClicked(ForgePlayerSP player)
            {
                new GuiEditConfig(null, ClientConfigRegistry.CONTAINER).openGui();
            }
        });

        ActionButtonRegistry.add(new ResourceLocation(FTBLibFinals.MOD_ID, "my_server_settings"), new SidebarButton(985, GuiIcons.settings_red, true)
        {
            @Override
            @SideOnly(Side.CLIENT)
            public void onClicked(ForgePlayerSP player)
            {
                FTBLibClient.execClientCommand("/ftb my_settings", false);
            }
        });

        ActionButtonRegistry.add(new ResourceLocation(FTBLibFinals.MOD_ID, "heal"), new SidebarButton(200, GuiIcons.heart, true)
        {
            @Override
            @SideOnly(Side.CLIENT)
            public void onClicked(ForgePlayerSP player)
            {
                FTBLibClient.execClientCommand("/ftb heal", false);
            }
        });

        ActionButtonRegistry.add(new ResourceLocation(FTBLibFinals.MOD_ID, "toggle_gamemode"), new SidebarButton(195, GuiIcons.toggle_gamemode, true)
        {
            @Override
            @SideOnly(Side.CLIENT)
            public void onClicked(ForgePlayerSP player)
            {
                int i = player.getPlayer().capabilities.isCreativeMode ? 0 : 1;
                FTBLibClient.execClientCommand("/gamemode " + i, false);
            }
        });

        ActionButtonRegistry.add(new ResourceLocation(FTBLibFinals.MOD_ID, "toggle_rain"), new SidebarButton(190, GuiIcons.toggle_rain, true)
        {
            @Override
            @SideOnly(Side.CLIENT)
            public void onClicked(ForgePlayerSP player)
            {
                FTBLibClient.execClientCommand("/toggledownfall", false);
            }
        });

        ActionButtonRegistry.add(new ResourceLocation(FTBLibFinals.MOD_ID, "set_day"), new SidebarButton(185, GuiIcons.toggle_day, true)
        {
            @Override
            @SideOnly(Side.CLIENT)
            public void onClicked(ForgePlayerSP player)
            {
                FTBLibClient.execClientCommand("/time set 6000", false);
            }
        });

        ActionButtonRegistry.add(new ResourceLocation(FTBLibFinals.MOD_ID, "set_night"), new SidebarButton(180, GuiIcons.toggle_night, true)
        {
            @Override
            @SideOnly(Side.CLIENT)
            public void onClicked(ForgePlayerSP player)
            {
                FTBLibClient.execClientCommand("/time set 18000", false);
            }
        });

        GuiScreenRegistry.register(new ResourceLocation(FTBLibFinals.MOD_ID, "friends_gui"), () -> new GuiInfo(null, new InfoFriendsGUI()).getWrapper());
        GuiScreenRegistry.register(new ResourceLocation(FTBLibFinals.MOD_ID, "client_config"), () -> new GuiEditConfig(null, ClientConfigRegistry.CONTAINER).getWrapper());
    }
}