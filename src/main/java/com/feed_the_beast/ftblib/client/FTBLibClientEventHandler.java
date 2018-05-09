package com.feed_the_beast.ftblib.client;

import com.feed_the_beast.ftblib.FTBLib;
import com.feed_the_beast.ftblib.FTBLibConfig;
import com.feed_the_beast.ftblib.events.CustomSidebarButtonTextEvent;
import com.feed_the_beast.ftblib.events.client.CustomClickEvent;
import com.feed_the_beast.ftblib.lib.ClientATHelper;
import com.feed_the_beast.ftblib.lib.EventHandler;
import com.feed_the_beast.ftblib.lib.client.ClientUtils;
import com.feed_the_beast.ftblib.lib.gui.GuiIcons;
import com.feed_the_beast.ftblib.lib.icon.AtlasSpriteIcon;
import com.feed_the_beast.ftblib.lib.icon.Color4I;
import com.feed_the_beast.ftblib.lib.icon.IconPresets;
import com.feed_the_beast.ftblib.lib.math.MathUtils;
import com.feed_the_beast.ftblib.lib.util.InvUtils;
import com.feed_the_beast.ftblib.lib.util.StringUtils;
import com.feed_the_beast.ftblib.lib.util.text_components.Notification;
import com.feed_the_beast.ftblib.net.MessageAdminPanelGui;
import com.feed_the_beast.ftblib.net.MessageMyTeamGui;
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
import net.minecraft.client.resources.I18n;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagByteArray;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagIntArray;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ChatType;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;
import net.minecraftforge.fml.relauncher.Side;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nullable;
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
	private static double sidebarButtonScale = 0D;

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
				event.getToolTip().add(I18n.format("ftblib_client.general.item_ore_names.item_tooltip"));

				for (String or : ores)
				{
					event.getToolTip().add("> " + or);
				}
			}
		}

		if (FTBLibClientConfig.general.item_nbt && event.getItemStack().hasTagCompound() && GuiScreen.isShiftKeyDown())
		{
			event.getToolTip().add(getNBTString(new StringBuilder(), event.getItemStack().getTagCompound(), 0).toString());
		}
	}

	private static final TextFormatting[] COLORS = {TextFormatting.BLUE, TextFormatting.DARK_GREEN, TextFormatting.YELLOW, TextFormatting.RED};

	private static StringBuilder getNBTString(StringBuilder builder, @Nullable NBTBase nbt, int level)
	{
		if (nbt == null)
		{
			return builder.append(TextFormatting.DARK_GRAY).append("null");
		}

		switch (nbt.getId())
		{
			case Constants.NBT.TAG_END:
				return builder.append(TextFormatting.DARK_GRAY).append("null");
			case Constants.NBT.TAG_BYTE:
			case Constants.NBT.TAG_SHORT:
			case Constants.NBT.TAG_INT:
			case Constants.NBT.TAG_LONG:
			case Constants.NBT.TAG_ANY_NUMERIC:
			case Constants.NBT.TAG_FLOAT:
			case Constants.NBT.TAG_DOUBLE:
				return builder.append(TextFormatting.GRAY).append(nbt.toString());
			case Constants.NBT.TAG_STRING:
				return builder.append(TextFormatting.GRAY).append(nbt.toString());
			case Constants.NBT.TAG_LIST:
			{
				NBTTagList list = (NBTTagList) nbt;
				builder.append(COLORS[level % COLORS.length]).append('[');

				for (int i = 0; i < list.tagCount(); i++)
				{
					if (i > 0)
					{
						builder.append(TextFormatting.DARK_GRAY).append(',').append(' ');
					}

					getNBTString(builder, list.get(i), level + 1);
				}

				return builder.append(COLORS[level % COLORS.length]).append(']');
			}
			case Constants.NBT.TAG_COMPOUND:
			{
				NBTTagCompound map = (NBTTagCompound) nbt;
				builder.append(COLORS[level % COLORS.length]).append('{');

				boolean first = true;

				for (String key : map.getKeySet())
				{
					if (first)
					{
						first = false;
					}
					else
					{
						builder.append(TextFormatting.DARK_GRAY).append(',').append(' ');
					}

					builder.append(TextFormatting.DARK_GRAY).append(key).append(':').append(' ');
					getNBTString(builder, map.getTag(key), level + 1);
				}

				return builder.append(COLORS[level % COLORS.length]).append('}');
			}
			case Constants.NBT.TAG_BYTE_ARRAY:
			{
				NBTTagByteArray list = (NBTTagByteArray) nbt;
				builder.append(COLORS[level % COLORS.length]).append('[');

				for (int i = 0; i < list.getByteArray().length; i++)
				{
					if (i > 0)
					{
						builder.append(TextFormatting.DARK_GRAY).append(',').append(' ');
					}

					builder.append(TextFormatting.GRAY).append(list.getByteArray()[i]);
				}

				return builder.append(COLORS[level % COLORS.length]).append(']');
			}
			case Constants.NBT.TAG_INT_ARRAY:
			{
				NBTTagIntArray list = (NBTTagIntArray) nbt;
				builder.append(COLORS[level % COLORS.length]).append('[');

				for (int i = 0; i < list.getIntArray().length; i++)
				{
					if (i > 0)
					{
						builder.append(TextFormatting.DARK_GRAY).append(',').append(' ');
					}

					builder.append(TextFormatting.GRAY).append(list.getIntArray()[i]);
				}

				return builder.append(COLORS[level % COLORS.length]).append(']');
			}
			default:
				return builder.append(TextFormatting.GRAY).append(nbt.toString());
		}
	}

	@SubscribeEvent
	public static void onGuiInit(final GuiScreenEvent.InitGuiEvent.Post event)
	{
		//sidebarButtonScale = 0D;

		if (FTBLibClientConfig.general.action_buttons != EnumSidebarButtonPlacement.DISABLED && event.getGui() instanceof InventoryEffectRenderer && !FTBLibClient.SIDEBAR_BUTTON_GROUPS.isEmpty())
		{
			event.getButtonList().add(new GuiButtonSidebarGroup((InventoryEffectRenderer) event.getGui()));
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

	@SubscribeEvent
	public static void onDebugInfoEvent(RenderGameOverlayEvent.Text event)
	{
		if (FTBLibClientConfig.general.debug_helper && !ClientUtils.MC.gameSettings.showDebugInfo && Keyboard.isKeyDown(Keyboard.KEY_F3))
		{
			event.getLeft().add(I18n.format("debug.help.help"));
		}
	}

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

	@SubscribeEvent
	public static void onCustomClick(CustomClickEvent event)
	{
		if (event.getID().getResourceDomain().equals(FTBLib.MOD_ID))
		{
			switch (event.getID().getResourcePath())
			{
				case "client_config_gui":
					new GuiClientConfig().openGui();
					break;
				case "my_team_gui":
					new MessageMyTeamGui().sendToServer();
					break;
				case "admin_panel_gui":
					new MessageAdminPanelGui().sendToServer();
					break;
			}

			event.setCanceled(true);
		}
	}

	private static class GuiButtonSidebar
	{
		public final int buttonX, buttonY;
		public final SidebarButton button;
		public int x, y;

		public GuiButtonSidebar(int x, int y, SidebarButton b)
		{
			buttonX = x;
			buttonY = y;
			button = b;
		}
	}

	private static class GuiButtonSidebarGroup extends GuiButton
	{
		private final InventoryEffectRenderer gui;
		public final List<GuiButtonSidebar> buttons;
		private GuiButtonSidebar mouseOver;

		public GuiButtonSidebarGroup(InventoryEffectRenderer g)
		{
			super(495829, 0, 0, 0, 0, "");
			gui = g;
			buttons = new ArrayList<>();
		}

		@Override
		public void drawButton(Minecraft mc, int mx, int my, float partialTicks)
		{
			buttons.clear();
			mouseOver = null;
			int rx, ry = 0;
			boolean addedAny;
			boolean top = FTBLibClientConfig.general.action_buttons.top() || !gui.mc.player.getActivePotionEffects().isEmpty() || (gui instanceof GuiInventory && ((GuiInventory) gui).func_194310_f().isVisible());

			for (SidebarButtonGroup group : FTBLibClient.SIDEBAR_BUTTON_GROUPS)
			{
				rx = 0;
				addedAny = false;

				for (SidebarButton button : group.getButtons())
				{
					if (button.isVisible())
					{
						buttons.add(new GuiButtonSidebar(rx, ry, button));
						rx++;
						addedAny = true;
					}
				}

				if (addedAny)
				{
					ry++;
				}
			}

			int guiLeft = gui.getGuiLeft();
			int guiTop = gui.getGuiTop();

			if (top)
			{
				for (GuiButtonSidebar button : buttons)
				{
					if (FTBLibClientConfig.general.collapse_sidebar_buttons)
					{
						button.x = 4 + button.buttonX * 17;
						button.y = 4 + button.buttonY * 17;
					}
					else
					{
						button.x = 1 + button.buttonX * 17;
						button.y = 1 + button.buttonY * 17;
					}
				}
			}
			else
			{
				int offsetY = 8;

				if (gui instanceof GuiContainerCreative)
				{
					offsetY = 6;
				}

				for (GuiButtonSidebar button : buttons)
				{
					button.x = guiLeft - 18 - button.buttonY * 17;
					button.y = guiTop + offsetY + button.buttonX * 17;
				}
			}

			x = Integer.MAX_VALUE;
			y = Integer.MAX_VALUE;
			int maxX = Integer.MIN_VALUE;
			int maxY = Integer.MIN_VALUE;

			for (GuiButtonSidebar b : buttons)
			{
				if (b.x >= 0 && b.y >= 0)
				{
					x = Math.min(x, b.x);
					y = Math.min(y, b.y);
					maxX = Math.max(maxX, b.x + 16);
					maxY = Math.max(maxY, b.y + 16);
				}

				if (mx >= b.x && my >= b.y && mx < b.x + 16 && my < b.y + 16)
				{
					mouseOver = b;
				}
			}

			x -= 2;
			y -= 2;
			maxX += 2;
			maxY += 2;

			width = maxX - x;
			height = maxY - y;

			if (sidebarButtonScale <= 0D)
			{
				if (mx >= x && my >= y && mx < x + 16 && my < y + 16)
				{
					sidebarButtonScale = 0.01D;
				}
			}

			if (mx < x || my < y || mx >= x + width || my >= y + height)
			{
				sidebarButtonScale -= partialTicks * 0.3D * FTBLibClientConfig.general.sidebar_button_collapse_speed;

				if (sidebarButtonScale < 0D)
				{
					sidebarButtonScale = 0D;
				}
			}
			else if (sidebarButtonScale > 0D)
			{
				sidebarButtonScale += partialTicks * 0.3D * FTBLibClientConfig.general.sidebar_button_collapse_speed;

				if (sidebarButtonScale > 1D)
				{
					sidebarButtonScale = 1D;
				}
			}

			if (!FTBLibClientConfig.general.collapse_sidebar_buttons)
			{
				sidebarButtonScale = 1D;
			}

			zLevel = 0F;

			GlStateManager.pushMatrix();
			GlStateManager.translate(0, 0, 500);

			FontRenderer font = mc.fontRenderer;

			GlStateManager.enableBlend();
			GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			GlStateManager.color(1F, 1F, 1F, 1F);

			GlStateManager.pushMatrix();

			if (sidebarButtonScale < 1D)
			{
				double scale = Math.min(16D / MathUtils.lerp(width, 16D, sidebarButtonScale), 16D / MathUtils.lerp(height, 16D, sidebarButtonScale));
				GlStateManager.scale(scale, scale, 1D);
			}

			if (FTBLibClientConfig.general.collapse_sidebar_buttons)
			{
				int alpha = (int) MathUtils.lerp(50D, 100D, sidebarButtonScale);
				Color4I.GRAY.withAlpha(alpha).draw(x, y, width, height);
				Color4I.DARK_GRAY.withAlpha(alpha).draw(x + 1, y + 1, width - 2, height - 2);
			}

			int alpha255 = (int) MathUtils.lerp(80D, 255D, sidebarButtonScale);

			for (GuiButtonSidebar b : buttons)
			{
				b.button.getIcon().draw(b.x, b.y, 16, 16, Color4I.WHITE.withAlpha(alpha255));

				if (sidebarButtonScale >= 1D && b == mouseOver)
				{
					Color4I.WHITE.withAlpha(33).draw(b.x, b.y, 16, 16);
				}

				if (sidebarButtonScale >= 1D && b.button.hasCustomText())
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

			if (mouseOver != null && sidebarButtonScale >= 1D)
			{
				int mx1 = mx - 4;
				int my1 = my - 12;

				String title = I18n.format(mouseOver.button.getLangKey());

				int tw = font.getStringWidth(title);

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
				font.drawString(title, mx1, my1, 0xFFFFFFFF);
				GlStateManager.color(1F, 1F, 1F, 1F);
				GlStateManager.popMatrix();
			}

			GlStateManager.color(1F, 1F, 1F, 1F);
			GlStateManager.popMatrix();
			GlStateManager.popMatrix();
			zLevel = 0F;
		}

		@Override
		public boolean mousePressed(Minecraft mc, int mx, int my)
		{
			if (super.mousePressed(mc, mx, my))
			{
				if (sidebarButtonScale >= 1D && mouseOver != null)
				{
					mouseOver.button.onClicked(GuiScreen.isShiftKeyDown());

					if (!(ClientUtils.MC.currentScreen instanceof InventoryEffectRenderer))
					{
						sidebarButtonScale = 0D;
					}
				}

				return true;
			}

			return false;
		}
	}
}