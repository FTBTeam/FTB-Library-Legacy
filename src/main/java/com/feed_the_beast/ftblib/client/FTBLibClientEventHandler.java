package com.feed_the_beast.ftblib.client;

import com.feed_the_beast.ftblib.FTBLibConfig;
import com.feed_the_beast.ftblib.events.CustomSidebarButtonTextEvent;
import com.feed_the_beast.ftblib.lib.ClientATHelper;
import com.feed_the_beast.ftblib.lib.EventHandler;
import com.feed_the_beast.ftblib.lib.client.ClientUtils;
import com.feed_the_beast.ftblib.lib.gui.GuiHelper;
import com.feed_the_beast.ftblib.lib.gui.GuiIcons;
import com.feed_the_beast.ftblib.lib.icon.AtlasSpriteIcon;
import com.feed_the_beast.ftblib.lib.icon.Color4I;
import com.feed_the_beast.ftblib.lib.icon.IconPresets;
import com.feed_the_beast.ftblib.lib.util.InvUtils;
import com.feed_the_beast.ftblib.lib.util.StringUtils;
import com.feed_the_beast.ftblib.lib.util.text_components.Notification;
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
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
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
			if (component instanceof Notification || FTBLibClientConfig.general.replace_vanilla_status_messages)
			{
				ResourceLocation id = component instanceof Notification ? ((Notification) component).getId() : Notification.VANILLA_STATUS;
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
			id = n instanceof Notification ? ((Notification) n).getId() : Notification.VANILLA_STATUS;
			width = 0;
			font = f;
			text = new ArrayList<>();
			timer = n instanceof Notification ? (((Notification) n).getTimer() & 0xFFL) : 60L;

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

		private long tick, endTick;
		private NotificationWidget widget;

		private Temp(ITextComponent n)
		{
			widget = new NotificationWidget(n, ClientUtils.MC.fontRenderer);
			tick = endTick = -1L;
		}

		public void render(ScaledResolution screen, float partialTicks)
		{
			if (tick == -1L || tick >= endTick)
			{
				return;
			}

			int alpha = (int) Math.min(255F, (endTick - tick - partialTicks) * 255F / 20F);

			if (alpha <= 2)
			{
				return;
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
			GlStateManager.enableDepth();
		}

		private boolean tick()
		{
			tick = ClientUtils.MC.world.getTotalWorldTime();

			if (endTick == -1L)
			{
				endTick = tick + widget.timer;
			}

			return tick >= endTick || Math.min(255F, (endTick - tick) * 255F / 20F) <= 2F;
		}

		private boolean isImportant()
		{
			return widget.notification instanceof Notification && ((Notification) widget.notification).isImportant();
		}
	}

	@SubscribeEvent
	public static void onConnected(FMLNetworkEvent.ClientConnectedToServerEvent event)
	{
		FTBLibClient.UNIVERSE_UUID = null;
		FTBLibClient.OPTIONAL_SERVER_MODS_CLIENT.clear();
		currentNotification = null;
		Temp.MAP.clear();
		ClientATHelper.getChatListeners().get(ChatType.GAME_INFO).clear();
		ClientATHelper.getChatListeners().get(ChatType.GAME_INFO).add(CHAT_LISTENER);
	}

	@SubscribeEvent
	public static void onTooltip(ItemTooltipEvent event)
	{
		if (FTBLibClientConfig.general.item_ore_names)
		{
			Collection<String> ores = InvUtils.getOreNames(null, event.getItemStack());

			if (!ores.isEmpty())
			{
				event.getToolTip().add(StringUtils.translate("ftblib_client.general.item_ore_names.item_tooltip"));

				for (String or : ores)
				{
					event.getToolTip().add("> " + or);
				}
			}
		}

		if (FTBLibClientConfig.general.item_nbt && event.getItemStack().hasTagCompound())
		{
			event.getToolTip().add(TextFormatting.DARK_GRAY.toString() + TextFormatting.getTextWithoutFormattingCodes(event.getItemStack().getTagCompound().toString()));
		}
	}

	@SubscribeEvent
	public static void onGuiInit(final GuiScreenEvent.InitGuiEvent.Post event)
	{
		if (!(event.getGui() instanceof InventoryEffectRenderer))
		{
			return;
		}

		if (!FTBLibClient.SIDEBAR_BUTTON_GROUPS.isEmpty())
		{
			GuiButtonSidebarGroup renderer = new GuiButtonSidebarGroup();
			event.getButtonList().add(renderer);
			int x, y = 0;
			boolean addedAny;

			for (SidebarButtonGroup group : FTBLibClient.SIDEBAR_BUTTON_GROUPS)
			{
				x = 0;
				addedAny = false;

				for (SidebarButton button : group.getButtons())
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

	@SubscribeEvent
	public static void onClientTick(TickEvent.ClientTickEvent event)
	{
		if (event.phase == TickEvent.Phase.START)
		{
			if (ClientUtils.MC.world == null)
			{
				currentNotification = null;
				Temp.MAP.clear();
			}

			if (currentNotification != null)
			{
				if (currentNotification.tick())
				{
					currentNotification = null;
				}
			}

			if (currentNotification == null && !Temp.MAP.isEmpty())
			{
				currentNotification = new Temp(Temp.MAP.values().iterator().next());
				Temp.MAP.remove(currentNotification.widget.id);
			}
		}
	}

	@SubscribeEvent(priority = EventPriority.HIGHEST, receiveCanceled = true)
	public static void onGameOverlayRender(RenderGameOverlayEvent.Text event)
	{
		if (currentNotification != null && !currentNotification.isImportant())
		{
			currentNotification.render(event.getResolution(), event.getPartialTicks());
			GlStateManager.color(1F, 1F, 1F, 1F);
			GlStateManager.disableLighting();
			GlStateManager.enableBlend();
			GlStateManager.enableTexture2D();
		}
	}

	@SubscribeEvent(priority = EventPriority.HIGHEST, receiveCanceled = true)
	public static void onRenderTick(TickEvent.RenderTickEvent event)
	{
		if (event.phase == TickEvent.Phase.END && currentNotification != null && currentNotification.isImportant())
		{
			currentNotification.render(new ScaledResolution(ClientUtils.MC), event.renderTickTime);
			GlStateManager.color(1F, 1F, 1F, 1F);
			GlStateManager.disableLighting();
			GlStateManager.enableBlend();
			GlStateManager.enableTexture2D();
		}
	}

	/*
	@SubscribeEvent(priority = EventPriority.HIGH)
	public static void onWorldRender(RenderWorldLastEvent event)
	{
	}
	*/

	/*
	@SubscribeEvent
	public static void onBlockHighlightDraw(DrawBlockHighlightEvent event)
	{
	}
	*/

	@SubscribeEvent
	public static void onBeforeTexturesStitched(TextureStitchEvent.Pre event)
	{
		ClientUtils.SPRITE_MAP.clear();

		try
		{
			for (Field field : GuiIcons.class.getDeclaredFields())
			{
				field.setAccessible(true);
				Object o = field.get(null);

				if (o instanceof AtlasSpriteIcon)
				{
					AtlasSpriteIcon a = (AtlasSpriteIcon) o;
					event.getMap().registerSprite(a.name);
					IconPresets.MAP.put(a.name.toString(), a);
				}
			}
		}
		catch (Exception ex)
		{
			if (FTBLibConfig.debugging.print_more_errors)
			{
				ex.printStackTrace();
			}
		}
	}

	private static class GuiButtonSidebar extends GuiButton
	{
		public final int buttonX, buttonY;
		public final SidebarButton button;
		public final String title;

		public GuiButtonSidebar(int x, int y, SidebarButton b)
		{
			super(495830 + x + y * 16, -16, -16, 16, 16, "");
			buttonX = x;
			buttonY = y;
			button = b;
			title = StringUtils.translate(b.getLangKey());
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
			int guiLeft = gui.getGuiLeft();
			int guiTop = gui.getGuiTop();

			if (prevGuiLeft != guiLeft || prevGuiTop != guiTop)
			{
				prevGuiLeft = guiLeft;
				prevGuiTop = guiTop;
			}

			boolean hasPotions = !gui.mc.player.getActivePotionEffects().isEmpty() || (gui instanceof GuiInventory && ((GuiInventory) gui).func_194310_f().isVisible());

			if (hasPotions || FTBLibClientConfig.general.action_buttons.top())
			{
				for (GuiButtonSidebar button : buttons)
				{
					button.x = 2 + button.buttonX * 17;
					button.y = 2 + button.buttonY * 17;
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

				for (int index = 0; index < buttons.size(); index++)
				{
					GuiButtonSidebar button = buttons.get(index);
					button.x = guiLeft + buttonX - (index / 8) * 17;
					button.y = guiTop + buttonY + (index % 8) * 17;
				}
			}
		}

		@Override
		public void drawButton(Minecraft mc, int mx, int my, float partialTicks)
		{
			updateButtonPositions();

			zLevel = 0F;

			GlStateManager.pushMatrix();
			GlStateManager.translate(0, 0, 500);

			FontRenderer font = mc.fontRenderer;

			GlStateManager.enableBlend();
			GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			GlStateManager.color(1F, 1F, 1F, 1F);

			GuiButtonSidebar mouseOver = null;

			for (GuiButtonSidebar b : buttons)
			{
				b.button.getIcon().draw(b.x, b.y, b.width, b.height);

				if (mx >= b.x && my >= b.y && mx < b.x + b.width && my < b.y + b.height)
				{
					mouseOver = b;
					Color4I.WHITE_A[33].draw(b.x, b.y, b.width, b.height);
				}

				if (b.button.hasCustomText())
				{
					CustomSidebarButtonTextEvent event = new CustomSidebarButtonTextEvent(b.button);
					event.post();

					if (!event.getText().isEmpty())
					{
						int nw = font.getStringWidth(event.getText());
						int width = 16;
						Color4I.LIGHT_RED.draw(b.x + width - nw, b.y - 2, nw + 1, 9);
						font.drawString(event.getText(), b.x + width - nw + 1, b.y - 1, 0xFFFFFFFF);
						GlStateManager.color(1F, 1F, 1F, 1F);
					}
				}
			}

			if (mouseOver != null)
			{
				int mx1 = mx - 4;
				int my1 = my - 12;

				int tw = font.getStringWidth(mouseOver.title);

				if (!FTBLibClientConfig.general.action_buttons.top())
				{
					mx1 -= tw + 8;
					my1 += 4;
				}

				if (mx1 < 4)
				{
					mx1 = 4;
				}
				if (my1 < 4)
				{
					my1 = 4;
				}

				GlStateManager.pushMatrix();
				GlStateManager.translate(0, 0, 500);
				GlStateManager.enableBlend();
				GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
				Color4I.DARK_GRAY.draw(mx1 - 3, my1 - 2, tw + 6, 12);
				font.drawString(mouseOver.title, mx1, my1, 0xFFFFFFFF);
				GlStateManager.color(1F, 1F, 1F, 1F);
				GlStateManager.popMatrix();
			}

			GlStateManager.color(1F, 1F, 1F, 1F);
			GlStateManager.popMatrix();
			zLevel = 0F;
		}
	}
}