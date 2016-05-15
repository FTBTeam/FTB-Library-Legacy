package com.feed_the_beast.ftbl.client;

import com.feed_the_beast.ftbl.api.EnumSelf;
import com.feed_the_beast.ftbl.api.ForgePlayer;
import com.feed_the_beast.ftbl.api.ForgePlayerSP;
import com.feed_the_beast.ftbl.api.ForgeWorldSP;
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
import com.feed_the_beast.ftbl.util.EventBusHelper;
import com.feed_the_beast.ftbl.util.TextureCoords;
import latmod.lib.LMColor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiContainerCreative;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.InventoryEffectRenderer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@SideOnly(Side.CLIENT)
public class FTBLibActions
{
	public static void init()
	{
		EventBusHelper.register(new FTBLibActions());
		
		PlayerActionRegistry.add(notifications);
		PlayerActionRegistry.add(friends_gui);
		PlayerActionRegistry.add(settings);
		PlayerActionRegistry.add(my_server_settings);
		PlayerActionRegistry.add(toggle_gamemode);
		PlayerActionRegistry.add(toggle_rain);
		PlayerActionRegistry.add(toggle_day);
		PlayerActionRegistry.add(toggle_night);
		PlayerActionRegistry.add(heal);
		PlayerActionRegistry.add(toggle_chunk_bounds);
		PlayerActionRegistry.add(toggle_light_values);
		
		PlayerActionRegistry.add(friend_add);
		PlayerActionRegistry.add(friend_remove);
		PlayerActionRegistry.add(friend_deny);
		
		GuiScreenRegistry.register("notifications", ep -> new GuiNotifications(FTBLibClient.mc.currentScreen));
		
		GuiScreenRegistry.register("friends_gui", new GuiScreenRegistry.Entry()
		{
			@Override
			public GuiScreen openGui(EntityPlayer ep)
			{ return new GuiInfo(null, new InfoFriendsGUI()); }
		});
		
		GuiScreenRegistry.register("client_config", new GuiScreenRegistry.Entry()
		{
			@Override
			public GuiScreen openGui(EntityPlayer ep)
			{ return new GuiEditConfig(FTBLibClient.mc.currentScreen, ClientConfigRegistry.provider()); }
		});
	}
	
	public static final PlayerAction notifications = new PlayerAction(EnumSelf.SELF, "ftbl.notifications", 1000, GuiIcons.chat)
	{
		@Override
		public void onClicked(ForgePlayer self, ForgePlayer other)
		{ FTBLibClient.openGui(new GuiNotifications(FTBLibClient.mc.currentScreen)); }
		
		@Override
		public String getDisplayName()
		{ return I18n.translateToLocal(FTBLibModClient.notifications.getFullID()); }
		
		@Override
		public boolean isVisibleFor(ForgePlayer self, ForgePlayer other)
		{ return !ClientNotifications.Perm.list.isEmpty(); }
		
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
	};
	
	public static final PlayerAction friends_gui = new PlayerAction(EnumSelf.SELF, "ftbl.friends_gui", 950, TextureCoords.getSquareIcon(new ResourceLocation("ftbl", "textures/gui/friendsbutton.png"), 256))
	{
		@Override
		public void onClicked(ForgePlayer self, ForgePlayer other)
		{ FTBLibClient.openGui(new GuiInfo(null, new InfoFriendsGUI())); }
		
		@Override
		public String getDisplayName()
		{ return "FriendsGUI"; }
	};
	
	public static final PlayerAction settings = new PlayerAction(EnumSelf.SELF, "ftbl.settings", -1000, GuiIcons.settings)
	{
		@Override
		public void onClicked(ForgePlayer self, ForgePlayer other)
		{ FTBLibClient.openGui(new GuiEditConfig(FTBLibClient.mc.currentScreen, ClientConfigRegistry.provider())); }
	};
	
	public static final PlayerAction my_server_settings = new PlayerAction(EnumSelf.SELF, "ftbu.my_server_settings", -1000, GuiIcons.settings)
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
			
			page.setTitle(new TextComponentTranslation("player_action.ftbu.my_server_settings"));
			page.theme = new InfoPageTheme().setBackgroundColor(new LMColor.RGB(30, 30, 30)).setTextColor(new LMColor.RGB(200, 200, 200)).setUseUnicodeFont(false);
			FTBLibClient.openGui(new GuiInfo(null, page));
		}
		
		@Override
		public Boolean configDefault()
		{ return Boolean.FALSE; }
	};
	
	public static final PlayerAction toggle_gamemode = new PlayerAction(EnumSelf.SELF, "ftbl.toggle_gamemode", -10, GuiIcons.toggle_gamemode)
	{
		@Override
		public void onClicked(ForgePlayer self, ForgePlayer other)
		{
			int i = self.getPlayer().capabilities.isCreativeMode ? 0 : 1;
			FTBLibClient.execClientCommand("/gamemode " + i);
		}
		
		@Override
		public Boolean configDefault()
		{ return Boolean.TRUE; }
	};
	
	public static final PlayerAction toggle_rain = new PlayerAction(EnumSelf.SELF, "ftbl.toggle_rain", -11, GuiIcons.toggle_rain)
	{
		@Override
		public void onClicked(ForgePlayer self, ForgePlayer other)
		{ FTBLibClient.execClientCommand("/toggledownfall"); }
		
		@Override
		public Boolean configDefault()
		{ return Boolean.TRUE; }
	};
	
	public static final PlayerAction toggle_day = new PlayerAction(EnumSelf.SELF, "ftbl.toggle_day", -12, GuiIcons.toggle_day)
	{
		@Override
		public void onClicked(ForgePlayer self, ForgePlayer other)
		{ FTBLibClient.execClientCommand("/time set 6000"); }
		
		@Override
		public Boolean configDefault()
		{ return Boolean.TRUE; }
	};
	
	public static final PlayerAction toggle_night = new PlayerAction(EnumSelf.SELF, "ftbl.toggle_night", -13, GuiIcons.toggle_night)
	{
		@Override
		public void onClicked(ForgePlayer self, ForgePlayer other)
		{ FTBLibClient.execClientCommand("/time set 18000"); }
		
		@Override
		public Boolean configDefault()
		{ return Boolean.TRUE; }
	};
	
	public static final PlayerAction heal = new PlayerAction(EnumSelf.SELF, "ftbl.heal", -16, GuiIcons.heart)
	{
		@Override
		public void onClicked(ForgePlayer self, ForgePlayer other)
		{ FTBLibClient.execClientCommand("/heal"); }
		
		@Override
		public Boolean configDefault()
		{ return Boolean.TRUE; }
	};
	
	public static final PlayerAction toggle_chunk_bounds = new PlayerAction(EnumSelf.SELF, "ftbl.toggle_chunk_bounds", -17, GuiIcons.toggle_chunk_bounds)
	{
		@Override
		public void onClicked(ForgePlayer self, ForgePlayer other)
		{ FTBLibRenderHandler.renderChunkBounds = !FTBLibRenderHandler.renderChunkBounds; }
		
		@Override
		public Boolean configDefault()
		{ return Boolean.TRUE; }
	};
	
	public static final PlayerAction toggle_light_values = new PlayerAction(EnumSelf.SELF, "ftbl.toggle_light_values", -18, GuiIcons.toggle_light_values)
	{
		@Override
		public void onClicked(ForgePlayer self, ForgePlayer other)
		{ FTBLibRenderHandler.toggleLightLevel(); }
		
		@Override
		public Boolean configDefault()
		{ return Boolean.TRUE; }
	};
	
	// Other //
	
	public static final PlayerAction friend_add = new PlayerAction(EnumSelf.OTHER, "ftbul.add_friend", 1, GuiIcons.add)
	{
		@Override
		public void onClicked(ForgePlayer self, ForgePlayer other)
		{ new MessageModifyFriends(MessageModifyFriends.ADD, other.getProfile().getId()).sendToServer(); }
		
		@Override
		public boolean isVisibleFor(ForgePlayer self, ForgePlayer other)
		{ return !self.isFriendRaw(other); }
	};
	
	public static final PlayerAction friend_remove = new PlayerAction(EnumSelf.OTHER, "ftbl.rem_friend", -1, GuiIcons.remove)
	{
		@Override
		public void onClicked(ForgePlayer self, ForgePlayer other)
		{ new MessageModifyFriends(MessageModifyFriends.REMOVE, other.getProfile().getId()).sendToServer(); }
		
		@Override
		public boolean isVisibleFor(ForgePlayer self, ForgePlayer other)
		{ return self.isFriendRaw(other); }
	};
	
	public static final PlayerAction friend_deny = new PlayerAction(EnumSelf.OTHER, "ftbl.deny_friend", -1, GuiIcons.remove)
	{
		@Override
		public void onClicked(ForgePlayer self, ForgePlayer other)
		{ new MessageModifyFriends(MessageModifyFriends.DENY, other.getProfile().getId()).sendToServer(); }
		
		@Override
		public boolean isVisibleFor(ForgePlayer self, ForgePlayer other)
		{ return !self.isFriendRaw(other) && other.isFriendRaw(self); }
	};
	
	@SubscribeEvent
	public void guiInitEvent(final GuiScreenEvent.InitGuiEvent.Post e)
	{
		if(!FTBLibClient.isIngame()) { return; }
		
		if(e.getGui() instanceof InventoryEffectRenderer)
		{
			ForgePlayerSP p = ForgeWorldSP.inst.clientPlayer;
			List<PlayerAction> buttons = PlayerActionRegistry.getPlayerActions(EnumSelf.SELF, p, p, false, false);
			
			if(!buttons.isEmpty())
			{
				Collections.sort(buttons);
				
				ButtonInvLMRenderer renderer = new ButtonInvLMRenderer(495830, e.getGui());
				e.getButtonList().add(renderer);
				
				if(FTBLibModClient.action_buttons_on_top.getAsBoolean())
				{
					for(int i = 0; i < buttons.size(); i++)
					{
						PlayerAction a = buttons.get(i);
						int x = i % 4;
						int y = i / 4;
						ButtonInvLM b = new ButtonInvLM(495830 + i, a, 4 + x * 18, 4 + y * 18);
						e.getButtonList().add(b);
						renderer.buttons.add(b);
					}
				}
				else
				{
					int xSize = 176;
					int ySize = 166;
					int buttonX = -17;
					int buttonY = 8;
					
					if(e.getGui() instanceof GuiContainerCreative)
					{
						xSize = 195;
						ySize = 136;
						buttonY = 6;
					}
					boolean hasPotions = !e.getGui().mc.thePlayer.getActivePotionEffects().isEmpty();
					if(hasPotions)
					{
						buttonX -= 4;
						buttonY -= 26;
					}
					
					int guiLeft = (e.getGui().width - xSize) / 2;
					int guiTop = (e.getGui().height - ySize) / 2;
					
					if(hasPotions) { guiLeft += 60; }
					
					for(int i = 0; i < buttons.size(); i++)
					{
						PlayerAction a = buttons.get(i);
						ButtonInvLM b;
						
						if(hasPotions)
						{
							int x = i % 8;
							int y = i / 8;
							b = new ButtonInvLM(495830 + i, a, guiLeft + buttonX - 18 * x, guiTop + buttonY - y * 18);
						}
						else
						{
							int x = i / 8;
							int y = i % 8;
							b = new ButtonInvLM(495830 + i, a, guiLeft + buttonX - 18 * x, guiTop + buttonY + 18 * y);
						}
						
						e.getButtonList().add(b);
						renderer.buttons.add(b);
					}
				}
			}
		}
	}
	
	@SubscribeEvent
	public void guiActionEvent(GuiScreenEvent.ActionPerformedEvent.Post e)
	{
		if(e.getButton() instanceof ButtonInvLM)
		{
			PlayerAction b = ((ButtonInvLM) e.getButton()).action;
			ForgePlayer p = ForgeWorldSP.inst.clientPlayer;
			b.onClicked(p, p);
		}
	}
	
	private static class ButtonInvLM extends GuiButton
	{
		public final PlayerAction action;
		
		public ButtonInvLM(int id, PlayerAction b, int x, int y)
		{
			super(id, x, y, 16, 16, "");
			action = b;
		}
		
		@Override
		public void drawButton(Minecraft mc, int mx, int my)
		{
		}
	}
	
	private static class ButtonInvLMRenderer extends GuiButton
	{
		public final List<ButtonInvLM> buttons;
		private final GuiContainerCreative creativeContainer;
		
		public ButtonInvLMRenderer(int id, GuiScreen g)
		{
			super(id, -1000, -1000, 0, 0, "");
			buttons = new ArrayList<>();
			creativeContainer = (g instanceof GuiContainerCreative) ? (GuiContainerCreative) g : null;
		}
		
		@Override
		public void drawButton(Minecraft mc, int mx, int my)
		{
			//if(creativeContainer != null && creativeContainer.getSelectedTabIndex() != CreativeTabs.tabInventory.getTabIndex())
			//	return;
			
			zLevel = 0F;
			
			GlStateManager.enableBlend();
			GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			
			GlStateManager.color(1F, 1F, 1F, 1F);
			
			for(ButtonInvLM b : buttons)
			{
				b.action.render(b.xPosition, b.yPosition, zLevel);
				
				if(mx >= b.xPosition && my >= b.yPosition && mx < b.xPosition + b.width && my < b.yPosition + b.height)
				{
					GlStateManager.color(1F, 1F, 1F, 0.3F);
					GuiLM.drawBlankRect(b.xPosition, b.yPosition, 0D, b.width, b.height);
					GlStateManager.color(1F, 1F, 1F, 1F);
				}
			}
			
			for(ButtonInvLM b : buttons)
			{
				b.action.postRender(b.xPosition, b.yPosition, 0D);
				
				if(mx >= b.xPosition && my >= b.yPosition && mx < b.xPosition + b.width && my < b.yPosition + b.height)
				{
					GlStateManager.pushMatrix();
					double mx1 = mx - 4D;
					double my1 = my - 12D;
					
					String s = b.action.getDisplayName();
					int tw = FTBLibClient.mc.fontRendererObj.getStringWidth(s);
					
					if(!FTBLibModClient.action_buttons_on_top.getAsBoolean())
					{
						mx1 -= tw + 8;
						my1 += 4;
					}
					
					if(mx1 < 4D) { mx1 = 4D; }
					if(my1 < 4D) { my1 = 4D; }
					
					GlStateManager.translate(mx1, my1, zLevel);
					
					GlStateManager.enableBlend();
					GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
					GlStateManager.color(0.13F, 0.13F, 0.13F, 1F);
					GuiLM.drawBlankRect(-3, -2, zLevel, tw + 6, 12);
					GlStateManager.color(1F, 1F, 1F, 1F);
					FTBLibClient.mc.fontRendererObj.drawString(s, 0, 0, 0xFFFFFFFF);
					GlStateManager.popMatrix();
				}
			}
			
			GlStateManager.color(1F, 1F, 1F, 1F);
		}
	}
}