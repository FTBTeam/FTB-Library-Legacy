package com.feed_the_beast.ftbl.client;

import com.feed_the_beast.ftbl.api.CustomSidebarButtonTextEvent;
import com.feed_the_beast.ftbl.api.EventHandler;
import com.feed_the_beast.ftbl.api.FTBLibAPI;
import com.feed_the_beast.ftbl.api.INotification;
import com.feed_the_beast.ftbl.api.ISidebarButton;
import com.feed_the_beast.ftbl.api.ISidebarButtonGroup;
import com.feed_the_beast.ftbl.api_impl.SharedClientData;
import com.feed_the_beast.ftbl.lib.Color4I;
import com.feed_the_beast.ftbl.lib.client.ClientUtils;
import com.feed_the_beast.ftbl.lib.gui.GuiHelper;
import com.feed_the_beast.ftbl.lib.gui.GuiIcons;
import com.feed_the_beast.ftbl.lib.icon.AtlasSpriteProvider;
import com.feed_the_beast.ftbl.lib.icon.IconPresets;
import com.feed_the_beast.ftbl.lib.item.ODItems;
import com.feed_the_beast.ftbl.lib.util.CommonUtils;
import com.feed_the_beast.ftbl.lib.util.StringUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.chat.IChatListener;
import net.minecraft.client.gui.inventory.GuiContainerCreative;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.InventoryEffectRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ChatType;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;
import net.minecraftforge.fml.relauncher.Side;
import org.lwjgl.opengl.GL11;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author LatvianModder
 */
@EventHandler(Side.CLIENT)
public class FTBLibClientEventHandler
{
	private static Temp currentNotification;

	private static final IChatListener CHAT_LISTENER = (type, component) ->
	{
		if (type == ChatType.GAME_INFO)
		{
			if (component instanceof INotification || FTBLibClientConfig.general.replace_vanilla_status_messages)
			{
				ResourceLocation id = component instanceof INotification ? ((INotification) component).getId() : INotification.VANILLA_STATUS;
				Temp.MAP.remove(id);

				if (currentNotification != null && currentNotification.widget.id.equals(id))
				{
					currentNotification = null;
				}

				Temp.MAP.put(id, component);
			}
			else
			{
				ClientUtils.MC.ingameGUI.setOverlayMessage(component.getFormattedText(), false);
			}
		}
	};

	public static class NotificationWidget
	{
		public final ITextComponent notification;
		public final ResourceLocation id;
		public final List<String> text;
		public int width, height;
		public final FontRenderer font;
		public final long timer;

		public NotificationWidget(ITextComponent n, FontRenderer f)
		{
			notification = n;
			id = n instanceof INotification ? ((INotification) n).getId() : INotification.VANILLA_STATUS;
			width = 0;
			font = f;
			text = new ArrayList<>();
			timer = n instanceof INotification ? (((INotification) n).getTimer() & 0xFFL) : 60L;

			for (String s : font.listFormattedStringToWidth(notification.getFormattedText(), new ScaledResolution(ClientUtils.MC).getScaledWidth()))
			{
				for (String line : s.split("\n"))
				{
					if (!line.isEmpty())
					{
						line = StringUtils.trimAllWhitespace(line);
						text.add(line);
						width = Math.max(width, font.getStringWidth(line));
					}
				}
			}

			width += 4;
			height = text.size() * 11;

			if (text.isEmpty())
			{
				width = 20;
				height = 20;
			}
		}
	}

	private static class Temp
	{
		private static final LinkedHashMap<ResourceLocation, ITextComponent> MAP = new LinkedHashMap<>();

		private long endTick;
		private NotificationWidget widget;

		private Temp(ITextComponent n)
		{
			widget = new NotificationWidget(n, ClientUtils.MC.fontRenderer);
			endTick = -1L;
		}

		public boolean render(ScaledResolution screen, float partialTicks)
		{
			long tick = ClientUtils.MC.world.getTotalWorldTime();

			if (endTick == -1L)
			{
				endTick = tick + widget.timer;
			}

			if (tick >= endTick)
			{
				return true;
			}

			int alpha = (int) Math.min(255F, (endTick - tick - partialTicks) * 255F / 20F);

			if (alpha <= 2)
			{
				return true;
			}

			GlStateManager.pushMatrix();
			GlStateManager.translate((int) (screen.getScaledWidth() / 2F), (int) (screen.getScaledHeight() - 68F), 0F);
			GlStateManager.disableDepth();
			GlStateManager.depthMask(false);
			GlStateManager.disableLighting();
			GlStateManager.enableBlend();
			GlStateManager.color(1F, 1F, 1F, 1F);

			int offy = -(widget.text.size() * 11) / 2;

			for (int i = 0; i < widget.text.size(); i++)
			{
				String string = widget.text.get(i);
				widget.font.drawStringWithShadow(string, -widget.font.getStringWidth(string) / 2, offy + i * 11, 0xFFFFFF | (alpha << 24));
			}

			GlStateManager.depthMask(true);
			GlStateManager.color(1F, 1F, 1F, 1F);
			GlStateManager.enableLighting();
			GlStateManager.popMatrix();

			return false;
		}
	}

	@SubscribeEvent
	public static void onConnected(FMLNetworkEvent.ClientConnectedToServerEvent event)
	{
		SharedClientData.INSTANCE.reset();
		currentNotification = null;
		Temp.MAP.clear();
		ClientUtils.MC.ingameGUI.chatListeners.get(ChatType.GAME_INFO).clear();
		ClientUtils.MC.ingameGUI.chatListeners.get(ChatType.GAME_INFO).add(CHAT_LISTENER);
	}

	@SubscribeEvent
	public static void onTooltip(ItemTooltipEvent event)
	{
		if (FTBLibClientConfig.general.item_ore_names)
		{
			Collection<String> ores = ODItems.getOreNames(null, event.getItemStack());

			if (!ores.isEmpty())
			{
				event.getToolTip().add(StringUtils.translate("ftbl_client.config.general.item_ore_names.item_tooltip"));

				for (String or : ores)
				{
					event.getToolTip().add("> " + or);
				}
			}
		}

		if (FTBLibClientConfig.general.item_nbt && event.getItemStack().hasTagCompound())
		{
			event.getToolTip().add(TextFormatting.DARK_GRAY.toString() + event.getItemStack().getTagCompound());
		}
	}

	@SubscribeEvent
	public static void onGuiInit(final GuiScreenEvent.InitGuiEvent.Post event)
	{
		if (!(event.getGui() instanceof InventoryEffectRenderer))
		{
			return;
		}

		List<ISidebarButtonGroup> groups = FTBLibAPI.API.getSidebarButtonGroups();

		if (!groups.isEmpty())
		{
			GuiButtonSidebarGroup renderer = new GuiButtonSidebarGroup();
			event.getButtonList().add(renderer);
			int x, y = 0;
			boolean addedAny;

			for (ISidebarButtonGroup group : groups)
			{
				x = 0;
				addedAny = false;

				for (ISidebarButton button : group.getButtons())
				{
					if (button.isVisible())
					{
						GuiButtonSidebar b = new GuiButtonSidebar(x, y, button);
						event.getButtonList().add(b);
						renderer.buttons.add(b);
						x++;
						addedAny = true;
					}
				}

				if (addedAny)
				{
					y++;
				}
			}

			renderer.updateButtonPositions();
		}
	}

	@SubscribeEvent
	public static void onGuiAction(GuiScreenEvent.ActionPerformedEvent.Post event)
	{
		if (event.getButton() instanceof GuiButtonSidebar)
		{
			GuiHelper.playClickSound();
			(((GuiButtonSidebar) event.getButton()).button).onClicked(GuiScreen.isShiftKeyDown());
		}
	}

	@SubscribeEvent(priority = EventPriority.HIGHEST, receiveCanceled = true)
	public static void onOverlayRender(RenderGameOverlayEvent.Pre event)
	{
		if ((currentNotification != null || !Temp.MAP.isEmpty()) && event.getType() == RenderGameOverlayEvent.ElementType.TEXT)
		{
			if (currentNotification != null)
			{
				if (currentNotification.render(event.getResolution(), event.getPartialTicks()))
				{
					currentNotification = null;
				}

				GlStateManager.color(1F, 1F, 1F, 1F);
				GlStateManager.disableLighting();
				GlStateManager.enableBlend();
				GlStateManager.enableTexture2D();
			}
			else if (!Temp.MAP.isEmpty())
			{
				currentNotification = new Temp(Temp.MAP.values().iterator().next());
				Temp.MAP.remove(currentNotification.widget.id);
			}
		}
	}

	@SubscribeEvent(priority = EventPriority.HIGH)
	public static void onWorldRender(RenderWorldLastEvent event)
	{
		ClientUtils.updateRenderInfo();
	}

	/*
	@SubscribeEvent
	public static void onBlockHighlightDraw(DrawBlockHighlightEvent event)
	{
	}
	*/

	@SubscribeEvent
	public static void onBeforeTexturesStitched(TextureStitchEvent.Pre event)
	{
		AtlasSpriteProvider.SPRITE_MAP.clear();

		try
		{
			for (Field field : GuiIcons.class.getDeclaredFields())
			{
				field.setAccessible(true);
				Object o = field.get(null);

				if (o instanceof AtlasSpriteProvider)
				{
					AtlasSpriteProvider a = (AtlasSpriteProvider) o;
					event.getMap().registerSprite(a.name);
					IconPresets.MAP.put(a.name.toString(), a);
				}
			}
		}
		catch (Exception ex)
		{
			if (CommonUtils.DEV_ENV)
			{
				ex.printStackTrace();
			}
		}
	}

	private static class GuiButtonSidebar extends GuiButton
	{
		public final int buttonX, buttonY;
		public final ISidebarButton button;
		public final String title;

		public GuiButtonSidebar(int x, int y, ISidebarButton b)
		{
			super(495830 + x + y * 16, -16, -16, 16, 16, "");
			buttonX = x;
			buttonY = y;
			button = b;
			title = StringUtils.translate("sidebar_button." + b.getName());
		}

		@Override
		public void drawButton(Minecraft mc, int mx, int my, float partialTicks)
		{
		}
	}

	private static class GuiButtonSidebarGroup extends GuiButton
	{
		public final List<GuiButtonSidebar> buttons;
		private int prevGuiLeft = -1, prevGuiTop = -1;

		public GuiButtonSidebarGroup()
		{
			super(495829, -1000, -1000, 0, 0, "");
			buttons = new ArrayList<>();
		}

		public void updateButtonPositions()
		{
			if (!(ClientUtils.MC.currentScreen instanceof InventoryEffectRenderer))
			{
				return;
			}

			InventoryEffectRenderer gui = (InventoryEffectRenderer) ClientUtils.MC.currentScreen;
			int guiLeft = GuiHelper.getGuiX(gui);
			int guiTop = GuiHelper.getGuiY(gui);

			if (prevGuiLeft != guiLeft || prevGuiTop != guiTop)
			{
				prevGuiLeft = guiLeft;
				prevGuiTop = guiTop;
			}

			boolean hasPotions = !gui.mc.player.getActivePotionEffects().isEmpty() || (gui instanceof GuiInventory && ((GuiInventory) gui).recipeBookGui.isVisible());

			if (FTBLibClientConfig.general.action_buttons.top())
			{
				int x = 0;
				int y = 0;

				for (GuiButtonSidebar button : buttons)
				{
					if (hasPotions)
					{
						button.x = 4 + x * 18;
						button.y = 4 + y * 18;

						x++;

						if (x >= 15 || 4 + x * 18 >= gui.height)
						{
							x = 0;
							y++;
						}
					}
					else
					{
						button.x = 4 + button.buttonX * 18;
						button.y = 4 + button.buttonY * 18;
					}
				}
			}
			else
			{
				int buttonX = -17;
				int buttonY = 8;

				if (gui instanceof GuiContainerCreative)
				{
					buttonY = 6;
				}

				if (hasPotions)
				{
					buttonX -= 4;
					buttonY -= 26;
				}

				for (int index = 0; index < buttons.size(); index++)
				{
					GuiButtonSidebar button = buttons.get(index);

					if (hasPotions)
					{
						button.x = guiLeft + buttonX - (index % 8) * 18;
						button.y = guiTop + buttonY - (index / 8) * 18;
					}
					else
					{
						button.x = guiLeft + buttonX - (index / 8) * 18;
						button.y = guiTop + buttonY + (index % 8) * 18;
					}

				}
			}
		}

		@Override
		public void drawButton(Minecraft mc, int mx, int my, float partialTicks)
		{
			//if(creativeContainer != null && creativeContainer.getSelectedTabIndex() != CreativeTabs.tabInventory.getTabIndex())
			//	return;

			updateButtonPositions();

			zLevel = 0F;
			FontRenderer font = mc.fontRenderer;

			GlStateManager.enableBlend();
			GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			GlStateManager.color(1F, 1F, 1F, 1F);

			for (GuiButtonSidebar b : buttons)
			{
				b.button.getIcon().draw(b.x, b.y, b.width, b.height, Color4I.NONE);

				if (mx >= b.x && my >= b.y && mx < b.x + b.width && my < b.y + b.height)
				{
					GuiHelper.drawBlankRect(b.x, b.y, b.width, b.height, Color4I.WHITE_A[33]);
				}
			}

			for (GuiButtonSidebar b : buttons)
			{
				if (b.button.hasCustomText())
				{
					CustomSidebarButtonTextEvent event = new CustomSidebarButtonTextEvent(b.button);
					event.post();

					if (!event.getText().isEmpty())
					{
						int nw = font.getStringWidth(event.getText());
						int width = 16;
						GuiHelper.drawBlankRect(b.x + width - nw, b.y - 4, nw + 1, 9, Color4I.LIGHT_RED);

						font.drawString(event.getText(), b.x + width - nw + 1, b.y - 3, 0xFFFFFFFF);
						GlStateManager.color(1F, 1F, 1F, 1F);
					}
				}

				if (mx >= b.x && my >= b.y && mx < b.x + b.width && my < b.y + b.height)
				{
					GlStateManager.pushMatrix();
					double mx1 = mx - 4D;
					double my1 = my - 12D;

					int tw = font.getStringWidth(b.title);

					if (!FTBLibClientConfig.general.action_buttons.top())
					{
						mx1 -= tw + 8;
						my1 += 4;
					}

					if (mx1 < 4D)
					{
						mx1 = 4D;
					}
					if (my1 < 4D)
					{
						my1 = 4D;
					}

					GlStateManager.translate(mx1, my1, zLevel);

					GlStateManager.enableBlend();
					GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
					GuiHelper.drawBlankRect(-3, -2, tw + 6, 12, Color4I.DARK_GRAY);
					font.drawString(b.title, 0, 0, 0xFFFFFFFF);
					GlStateManager.color(1F, 1F, 1F, 1F);
					GlStateManager.popMatrix();
				}
			}

			GlStateManager.color(1F, 1F, 1F, 1F);
		}
	}
}