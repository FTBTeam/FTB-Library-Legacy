package com.feed_the_beast.ftbl.client;

import com.feed_the_beast.ftbl.FTBLibFinals;
import com.feed_the_beast.ftbl.api.ForgePlayerSP;
import com.feed_the_beast.ftbl.api.client.FTBLibClient;
import com.feed_the_beast.ftbl.api.client.gui.GuiIcons;
import com.feed_the_beast.ftbl.api.client.gui.GuiLM;
import com.feed_the_beast.ftbl.api.client.gui.GuiScreenRegistry;
import com.feed_the_beast.ftbl.api.client.gui.guibuttons.ActionButton;
import com.feed_the_beast.ftbl.api.client.gui.guibuttons.ActionButtonRegistry;
import com.feed_the_beast.ftbl.api.client.gui.guibuttons.PlayerAction;
import com.feed_the_beast.ftbl.api.client.gui.guibuttons.SidebarButton;
import com.feed_the_beast.ftbl.api.config.ClientConfigRegistry;
import com.feed_the_beast.ftbl.api.info.InfoPage;
import com.feed_the_beast.ftbl.api.info.InfoPageTheme;
import com.feed_the_beast.ftbl.api.notification.ClientNotifications;
import com.feed_the_beast.ftbl.gui.GuiEditConfig;
import com.feed_the_beast.ftbl.gui.GuiNotifications;
import com.feed_the_beast.ftbl.gui.friends.InfoFriendsGUI;
import com.feed_the_beast.ftbl.gui.info.GuiInfo;
import com.feed_the_beast.ftbl.util.TextureCoords;
import latmod.lib.LMColor;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class FTBLibActions
{
    public static final ActionButton NOTIFICATIONS = ActionButtonRegistry.add(new SidebarButton(new ResourceLocation(FTBLibFinals.MOD_ID, "notifications"), 1000, GuiIcons.chat, null)
    {
        @Override
        public void onClicked(ForgePlayerSP player)
        {
            FTBLibClient.mc().displayGuiScreen(new GuiNotifications(FTBLibClient.mc().currentScreen));
        }

        @Override
        public String getLangKey()
        {
            return FTBLibModClient.notifications.getFullID();
        }

        @Override
        public boolean isVisibleFor(ForgePlayerSP player)
        {
            return !ClientNotifications.Perm.list.isEmpty();
        }

        @Override
        public void postRender(int ax, int ay, float z)
        {
            String n = String.valueOf(ClientNotifications.Perm.list.size());
            int nw = FTBLibClient.mc().fontRendererObj.getStringWidth(n);
            int width = 16;
            GlStateManager.color(1F, 0.13F, 0.13F, 0.66F);
            GuiLM.drawBlankRect(ax + width - nw, ay - 4, z, nw + 1, 9);
            GlStateManager.color(1F, 1F, 1F, 1F);
            FTBLibClient.mc().fontRendererObj.drawString(n, ax + width - nw + 1, ay - 3, 0xFFFFFFFF);
        }
    });

    public static final ActionButton FRIENDS_GUI = ActionButtonRegistry.add(new SidebarButton(new ResourceLocation(FTBLibFinals.MOD_ID, "friends_gui"), 995, TextureCoords.getSquareIcon(new ResourceLocation("ftbl", "textures/gui/friendsbutton.png"), 256), null)
    {
        @Override
        public void onClicked(ForgePlayerSP player)
        {
            FTBLibClient.mc().displayGuiScreen(new GuiInfo(null, new InfoFriendsGUI()));
        }

        @Override
        public String getDisplayName()
        {
            return "FriendsGUI";
        }
    });

    public static final ActionButton CLIENT_SETTINGS = ActionButtonRegistry.add(new SidebarButton(new ResourceLocation(FTBLibFinals.MOD_ID, "settings"), 990, GuiIcons.settings, null)
    {
        @Override
        public void onClicked(ForgePlayerSP player)
        {
            FTBLibClient.mc().displayGuiScreen(new GuiEditConfig(FTBLibClient.mc().currentScreen, ClientConfigRegistry.provider()));
        }
    });

    public static final ActionButton MY_SERVER_SETTINGS = ActionButtonRegistry.add(new SidebarButton(new ResourceLocation(FTBLibFinals.MOD_ID, "my_server_settings"), 985, GuiIcons.settings_red, true)
    {
        @Override
        public void onClicked(ForgePlayerSP player)
        {
            InfoPage page = new InfoPage("my_server_settings")
            {
                @Override
                public void refreshGui(GuiInfo gui)
                {
                    clear();
                    /*
                    PersonalSettings ps = LMWorldClient.inst.clientPlayer.getSettings();
					
					booleanCommand("chat_links", ps.getMode(PersonalSettings.CHAT_LINKS));
					booleanCommand("render_badge", LMWorldClient.inst.clientPlayer.renderBadge);
					booleanCommand("explosions", ps.getMode(PersonalSettings.EXPLOSIONS));
					booleanCommand("fake_players", ps.getMode(PersonalSettings.FAKE_PLAYERS));
					
					IChatComponent text1 = ps.blocks.lang.chatComponent();
					text1.getChatStyle().setColor(ps.blocks == EnumPrivacyLevel.TEAM ? EnumChatFormatting.BLUE : (ps.blocks == EnumPrivacyLevel.PUBLIC ? EnumChatFormatting.GREEN : EnumChatFormatting.RED));
					InfoExtendedTextLine line = new InfoExtendedTextLine(this, new ChatComponentTranslation("ftbu.player_setting.security_level").appendText(": ").appendSibling(text1));
					line.setClickAction(new ClickAction(ClickActionType.CMD, new JsonPrimitive("lmplayer_settings block_security toggle")));
					text.add(line);
					*/
                }
                
				/*
                private void booleanCommand(String s, boolean current)
				{
					ChatComponentText text1 = new ChatComponentText(Boolean.toString(current));
					text1.getChatStyle().setColor(current ? EnumChatFormatting.GREEN : EnumChatFormatting.RED);
					InfoExtendedTextLine line = new InfoExtendedTextLine(this, new ChatComponentTranslation("ftbu.player_setting." + s).appendText(": ").appendSibling(text1));
					line.setClickAction(new ClickAction(ClickActionType.CMD, new JsonPrimitive("lmplayer_settings " + s + " toggle")));
					text.add(line);
				}
				*/
            };

            page.setTitle(new TextComponentTranslation("player_action." + MY_SERVER_SETTINGS.getID()));
            page.theme = new InfoPageTheme().setBackgroundColor(new LMColor.RGB(30, 30, 30)).setTextColor(new LMColor.RGB(200, 200, 200)).setUseUnicodeFont(false);
            FTBLibClient.mc().displayGuiScreen(new GuiInfo(null, page));
        }
    });

    public static final ActionButton HEAL = ActionButtonRegistry.add(new SidebarButton(new ResourceLocation(FTBLibFinals.MOD_ID, "heal"), 200, GuiIcons.heart, true)
    {
        @Override
        public void onClicked(ForgePlayerSP player)
        {
            FTBLibClient.execClientCommand("/heal");
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

    public static final ActionButton LEAVE_TEAM = ActionButtonRegistry.add(new SidebarButton(new ResourceLocation(FTBLibFinals.MOD_ID, "team_leave"), -1, GuiIcons.remove, false)
    {
        @Override
        public void onClicked(ForgePlayerSP player)
        {
            //TODO: Ask for confirmation
        }

        @Override
        public boolean isVisibleFor(ForgePlayerSP player)
        {
            return super.isVisibleFor(player) && player.hasTeam();
        }
    });

    // Other //

    public static final ActionButton TEAM_INVITE = ActionButtonRegistry.add(new PlayerAction(new ResourceLocation(FTBLibFinals.MOD_ID, "team_invite"), 1, GuiIcons.add)
    {
        @Override
        public void onClicked(ForgePlayerSP player)
        {
        }

        @Override
        public boolean isVisibleFor(ForgePlayerSP player)
        {
            return super.isVisibleFor(player) && (!player.hasTeam() || player.getTeam().getStatus(player).isOwner());
        }
    });

    public static final ActionButton TEAM_DENY_INVITE = ActionButtonRegistry.add(new PlayerAction(new ResourceLocation(FTBLibFinals.MOD_ID, "team_deny_invite"), -1, GuiIcons.remove)
    {
        @Override
        public void onClicked(ForgePlayerSP player)
        {
        }
/*
        @Override
        public boolean isVisibleFor(ForgePlayer self, ForgePlayer other)
        {
            return !self.isFriendRaw(other) && other.isFriendRaw(self);
        }
        */
    });

    public static void init()
    {
        GuiScreenRegistry.register(new ResourceLocation(FTBLibFinals.MOD_ID, "notifications"), new GuiScreenRegistry.Entry()
        {
            @Override
            @SideOnly(Side.CLIENT)
            public GuiScreen openGui(EntityPlayer ep)
            {
                return new GuiNotifications(FTBLibClient.mc().currentScreen);
            }
        });

        GuiScreenRegistry.register(new ResourceLocation(FTBLibFinals.MOD_ID, "friends_gui"), new GuiScreenRegistry.Entry()
        {
            @Override
            @SideOnly(Side.CLIENT)
            public GuiScreen openGui(EntityPlayer ep)
            {
                return new GuiInfo(null, new InfoFriendsGUI());
            }
        });

        GuiScreenRegistry.register(new ResourceLocation(FTBLibFinals.MOD_ID, "client_config"), new GuiScreenRegistry.Entry()
        {
            @Override
            @SideOnly(Side.CLIENT)
            public GuiScreen openGui(EntityPlayer ep)
            {
                return new GuiEditConfig(FTBLibClient.mc().currentScreen, ClientConfigRegistry.provider());
            }
        });
    }
}