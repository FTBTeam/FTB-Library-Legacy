package com.feed_the_beast.ftbl.client;

import com.feed_the_beast.ftbl.FTBLibFinals;
import com.feed_the_beast.ftbl.api.EnumSelf;
import com.feed_the_beast.ftbl.api.ForgePlayer;
import com.feed_the_beast.ftbl.api.PlayerAction;
import com.feed_the_beast.ftbl.api.client.FTBLibClient;
import com.feed_the_beast.ftbl.api.config.ClientConfigRegistry;
import com.feed_the_beast.ftbl.api.gui.GuiIcons;
import com.feed_the_beast.ftbl.api.gui.GuiLM;
import com.feed_the_beast.ftbl.api.gui.GuiScreenRegistry;
import com.feed_the_beast.ftbl.api.gui.PlayerActionRegistry;
import com.feed_the_beast.ftbl.api.info.InfoPage;
import com.feed_the_beast.ftbl.api.info.InfoPageTheme;
import com.feed_the_beast.ftbl.api.notification.ClientNotifications;
import com.feed_the_beast.ftbl.gui.GuiEditConfig;
import com.feed_the_beast.ftbl.gui.GuiNotifications;
import com.feed_the_beast.ftbl.gui.friends.InfoFriendsGUI;
import com.feed_the_beast.ftbl.gui.info.GuiInfo;
import com.feed_the_beast.ftbl.net.MessageModifyFriends;
import com.feed_the_beast.ftbl.util.TextureCoords;
import latmod.lib.LMColor;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class FTBLibActions
{
    public static final PlayerAction NOTIFICATIONS = PlayerActionRegistry.add(new PlayerAction(EnumSelf.SELF, new ResourceLocation(FTBLibFinals.MOD_ID, "notifications"), 1000, GuiIcons.chat)
    {
        @Override
        public void onClicked(ForgePlayer self, ForgePlayer other)
        {
            FTBLibClient.openGui(new GuiNotifications(FTBLibClient.mc.currentScreen));
        }

        @Override
        public String getDisplayName()
        {
            return I18n.translateToLocal(FTBLibModClient.notifications.getFullID());
        }

        @Override
        public boolean isVisibleFor(ForgePlayer self, ForgePlayer other)
        {
            return !ClientNotifications.Perm.list.isEmpty();
        }

        @Override
        public void postRender(int ax, int ay, double z)
        {
            String n = String.valueOf(ClientNotifications.Perm.list.size());
            int nw = FTBLibClient.mc.fontRendererObj.getStringWidth(n);
            int width = 16;
            GlStateManager.color(1F, 0.13F, 0.13F, 0.66F);
            GuiLM.drawBlankRect(ax + width - nw, ay - 4, z, nw + 1, 9);
            GlStateManager.color(1F, 1F, 1F, 1F);
            FTBLibClient.mc.fontRendererObj.drawString(n, ax + width - nw + 1, ay - 3, 0xFFFFFFFF);
        }
    });
    public static final PlayerAction FRIENDS_GUI = PlayerActionRegistry.add(new PlayerAction(EnumSelf.SELF, new ResourceLocation(FTBLibFinals.MOD_ID, "friends_gui"), 995, TextureCoords.getSquareIcon(new ResourceLocation("ftbl", "textures/gui/friendsbutton.png"), 256))
    {
        @Override
        public void onClicked(ForgePlayer self, ForgePlayer other)
        {
            FTBLibClient.openGui(new GuiInfo(null, new InfoFriendsGUI()));
        }

        @Override
        public String getDisplayName()
        {
            return "FriendsGUI";
        }
    });
    public static final PlayerAction CLIENT_SETTINGS = PlayerActionRegistry.add(new PlayerAction(EnumSelf.SELF, new ResourceLocation(FTBLibFinals.MOD_ID, "settings"), 990, GuiIcons.settings)
    {
        @Override
        public void onClicked(ForgePlayer self, ForgePlayer other)
        {
            FTBLibClient.openGui(new GuiEditConfig(FTBLibClient.mc.currentScreen, ClientConfigRegistry.provider()));
        }
    });
    public static final PlayerAction MY_SERVER_SETTINGS = PlayerActionRegistry.add(new PlayerAction(EnumSelf.SELF, new ResourceLocation(FTBLibFinals.MOD_ID, "my_server_settings"), 985, GuiIcons.settings)
    {
        @Override
        public void onClicked(ForgePlayer self, ForgePlayer other)
        {
            InfoPage page = new InfoPage("my_server_settings")
            {
                @Override
                public void refreshGui(GuiInfo gui)
                {
                    clear();
                    /*
                    PersonalSettings ps = LMWorldClient.inst.clientPlayer.getSettings();
					
					booleanCommand("chat_links", ps.get(PersonalSettings.CHAT_LINKS));
					booleanCommand("render_badge", LMWorldClient.inst.clientPlayer.renderBadge);
					booleanCommand("explosions", ps.get(PersonalSettings.EXPLOSIONS));
					booleanCommand("fake_players", ps.get(PersonalSettings.FAKE_PLAYERS));
					
					IChatComponent text1 = ps.blocks.lang.chatComponent();
					text1.getChatStyle().setColor(ps.blocks == PrivacyLevel.FRIENDS ? EnumChatFormatting.BLUE : (ps.blocks == PrivacyLevel.PUBLIC ? EnumChatFormatting.GREEN : EnumChatFormatting.RED));
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
            FTBLibClient.openGui(new GuiInfo(null, page));
        }

        @Override
        public Boolean configDefault()
        {
            return Boolean.TRUE;
        }
    });
    public static final PlayerAction HEAL = PlayerActionRegistry.add(new PlayerAction(EnumSelf.SELF, new ResourceLocation(FTBLibFinals.MOD_ID, "heal"), 200, GuiIcons.heart)
    {
        @Override
        public void onClicked(ForgePlayer self, ForgePlayer other)
        {
            FTBLibClient.execClientCommand("/heal");
        }

        @Override
        public Boolean configDefault()
        {
            return Boolean.TRUE;
        }
    });
    public static final PlayerAction TOGGLE_GAMEMODE = PlayerActionRegistry.add(new PlayerAction(EnumSelf.SELF, new ResourceLocation(FTBLibFinals.MOD_ID, "toggle_gamemode"), 195, GuiIcons.toggle_gamemode)
    {
        @Override
        public void onClicked(ForgePlayer self, ForgePlayer other)
        {
            int i = self.getPlayer().capabilities.isCreativeMode ? 0 : 1;
            FTBLibClient.execClientCommand("/gamemode " + i);
        }

        @Override
        public Boolean configDefault()
        {
            return Boolean.TRUE;
        }
    });
    public static final PlayerAction TOGGLE_RAIN = PlayerActionRegistry.add(new PlayerAction(EnumSelf.SELF, new ResourceLocation(FTBLibFinals.MOD_ID, "toggle_rain"), 190, GuiIcons.toggle_rain)
    {
        @Override
        public void onClicked(ForgePlayer self, ForgePlayer other)
        {
            FTBLibClient.execClientCommand("/toggledownfall");
        }

        @Override
        public Boolean configDefault()
        {
            return Boolean.TRUE;
        }
    });
    public static final PlayerAction SET_DAY = PlayerActionRegistry.add(new PlayerAction(EnumSelf.SELF, new ResourceLocation(FTBLibFinals.MOD_ID, "set_day"), 185, GuiIcons.toggle_day)
    {
        @Override
        public void onClicked(ForgePlayer self, ForgePlayer other)
        {
            FTBLibClient.execClientCommand("/time set 6000");
        }

        @Override
        public Boolean configDefault()
        {
            return Boolean.TRUE;
        }
    });
    public static final PlayerAction SET_NIGHT = PlayerActionRegistry.add(new PlayerAction(EnumSelf.SELF, new ResourceLocation(FTBLibFinals.MOD_ID, "set_night"), 180, GuiIcons.toggle_night)
    {
        @Override
        public void onClicked(ForgePlayer self, ForgePlayer other)
        {
            FTBLibClient.execClientCommand("/time set 18000");
        }

        @Override
        public Boolean configDefault()
        {
            return Boolean.TRUE;
        }
    });
    public static final PlayerAction TOGGLE_CHUNK_BOUNDS = PlayerActionRegistry.add(new PlayerAction(EnumSelf.SELF, new ResourceLocation(FTBLibFinals.MOD_ID, "toggle_chunk_bounds"), 100, GuiIcons.toggle_chunk_bounds)
    {
        @Override
        public void onClicked(ForgePlayer self, ForgePlayer other)
        {
            FTBLibRenderHandler.renderChunkBounds = !FTBLibRenderHandler.renderChunkBounds;
        }

        @Override
        public Boolean configDefault()
        {
            return Boolean.TRUE;
        }
    });
    public static final PlayerAction TOGGLE_LIGHT_VALUES = PlayerActionRegistry.add(new PlayerAction(EnumSelf.SELF, new ResourceLocation(FTBLibFinals.MOD_ID, "toggle_light_values"), 90, GuiIcons.toggle_light_values)
    {
        @Override
        public void onClicked(ForgePlayer self, ForgePlayer other)
        {
            FTBLibRenderHandler.toggleLightLevel();
        }

        @Override
        public Boolean configDefault()
        {
            return Boolean.TRUE;
        }
    });
    public static final PlayerAction ADD_FRIEND = PlayerActionRegistry.add(new PlayerAction(EnumSelf.OTHER, new ResourceLocation(FTBLibFinals.MOD_ID, "add_friend"), 1, GuiIcons.add)
    {
        @Override
        public void onClicked(ForgePlayer self, ForgePlayer other)
        {
            new MessageModifyFriends(MessageModifyFriends.ADD, other.getProfile().getId()).sendToServer();
        }

        @Override
        public boolean isVisibleFor(ForgePlayer self, ForgePlayer other)
        {
            return !self.isFriendRaw(other);
        }
    });

    // Other //
    public static final PlayerAction REMOVE_FRIEND = PlayerActionRegistry.add(new PlayerAction(EnumSelf.OTHER, new ResourceLocation(FTBLibFinals.MOD_ID, "rem_friend"), -1, GuiIcons.remove)
    {
        @Override
        public void onClicked(ForgePlayer self, ForgePlayer other)
        {
            new MessageModifyFriends(MessageModifyFriends.REMOVE, other.getProfile().getId()).sendToServer();
        }

        @Override
        public boolean isVisibleFor(ForgePlayer self, ForgePlayer other)
        {
            return self.isFriendRaw(other);
        }
    });
    public static final PlayerAction DENY_FRIEND = PlayerActionRegistry.add(new PlayerAction(EnumSelf.OTHER, new ResourceLocation(FTBLibFinals.MOD_ID, "deny_friend"), -1, GuiIcons.remove)
    {
        @Override
        public void onClicked(ForgePlayer self, ForgePlayer other)
        {
            new MessageModifyFriends(MessageModifyFriends.DENY, other.getProfile().getId()).sendToServer();
        }

        @Override
        public boolean isVisibleFor(ForgePlayer self, ForgePlayer other)
        {
            return !self.isFriendRaw(other) && other.isFriendRaw(self);
        }
    });

    public static void init()
    {
        GuiScreenRegistry.register(new ResourceLocation(FTBLibFinals.MOD_ID, "notifications"), new GuiScreenRegistry.Entry()
        {
            @Override
            @SideOnly(Side.CLIENT)
            public GuiScreen openGui(EntityPlayer ep)
            {
                return new GuiNotifications(FTBLibClient.mc.currentScreen);
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
                return new GuiEditConfig(FTBLibClient.mc.currentScreen, ClientConfigRegistry.provider());
            }
        });
    }
}