package com.feed_the_beast.ftbl.lib.gui;

import com.feed_the_beast.ftbl.api.gui.IGuiWrapper;
import com.feed_the_beast.ftbl.lib.Color4I;
import com.feed_the_beast.ftbl.lib.client.ClientUtils;
import com.feed_the_beast.ftbl.lib.util.NetUtils;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiConfirmOpenLink;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.HoverEvent;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nullable;
import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author LatvianModder
 */
public class GuiHelper
{
	private final static int MAX_SCISSOR = 16;
	private static final int SCISSOR_X[] = new int[MAX_SCISSOR];
	private static final int SCISSOR_Y[] = new int[MAX_SCISSOR];
	private static final int SCISSOR_W[] = new int[MAX_SCISSOR];
	private static final int SCISSOR_H[] = new int[MAX_SCISSOR];
	private static int scissorIndex = -1;

	public static int getGuiWidth(GuiContainer gui)
	{
		return gui.xSize;
	}

	public static int getGuiHeight(GuiContainer gui)
	{
		return gui.ySize;
	}

	public static int getGuiX(GuiContainer gui)
	{
		return gui.guiLeft;
	}

	public static int getGuiY(GuiContainer gui)
	{
		return gui.guiTop;
	}

	public static void playClickSound()
	{
		ClientUtils.MC.getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1F));
	}

	public static void drawTexturedRect(int x, int y, int w, int h, Color4I col, double u0, double v0, double u1, double v1)
	{
		if (u0 == u1 || v0 == v1)
		{
			Tessellator tessellator = Tessellator.getInstance();
			BufferBuilder buffer = tessellator.getBuffer();
			buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);
			addRectToBuffer(buffer, x, y, w, h, col);
			tessellator.draw();
		}
		else
		{
			Tessellator tessellator = Tessellator.getInstance();
			BufferBuilder buffer = tessellator.getBuffer();
			buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR);
			addRectToBufferWithUV(buffer, x, y, w, h, col, u0, v0, u1, v1);
			tessellator.draw();
		}
	}

	public static void drawBlankRect(int x, int y, int w, int h, Color4I col)
	{
		if (w <= 0 || h <= 0 || !col.hasColor())
		{
			return;
		}

		GlStateManager.disableTexture2D();
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder buffer = tessellator.getBuffer();
		buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);
		addRectToBuffer(buffer, x, y, w, h, col);
		tessellator.draw();
		GlStateManager.enableTexture2D();
	}

	private static void addRectToBuffer(BufferBuilder buffer, int x, int y, int w, int h, Color4I col)
	{
		buffer.pos(x, y + h, 0D).color(col.redi(), col.greeni(), col.bluei(), col.alphai()).endVertex();
		buffer.pos(x + w, y + h, 0D).color(col.redi(), col.greeni(), col.bluei(), col.alphai()).endVertex();
		buffer.pos(x + w, y, 0D).color(col.redi(), col.greeni(), col.bluei(), col.alphai()).endVertex();
		buffer.pos(x, y, 0D).color(col.redi(), col.greeni(), col.bluei(), col.alphai()).endVertex();
	}

	private static void addRectToBufferWithUV(BufferBuilder buffer, int x, int y, int w, int h, Color4I col, double u0, double v0, double u1, double v1)
	{
		buffer.pos(x, y + h, 0D).tex(u0, v1).color(col.redi(), col.greeni(), col.bluei(), col.alphai()).endVertex();
		buffer.pos(x + w, y + h, 0D).tex(u1, v1).color(col.redi(), col.greeni(), col.bluei(), col.alphai()).endVertex();
		buffer.pos(x + w, y, 0D).tex(u1, v0).color(col.redi(), col.greeni(), col.bluei(), col.alphai()).endVertex();
		buffer.pos(x, y, 0D).tex(u0, v0).color(col.redi(), col.greeni(), col.bluei(), col.alphai()).endVertex();
	}

	public static void drawHollowRect(int x, int y, int w, int h, Color4I col, boolean roundEdges)
	{
		if (w <= 1 || h <= 1 || !col.hasColor())
		{
			drawBlankRect(x, y, w, h, col);
			return;
		}

		GlStateManager.disableTexture2D();
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder buffer = tessellator.getBuffer();
		buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);

		addRectToBuffer(buffer, x, y + 1, 1, h - 2, col);
		addRectToBuffer(buffer, x + w - 1, y + 1, 1, h - 2, col);

		if (roundEdges)
		{
			addRectToBuffer(buffer, x + 1, y, w - 2, 1, col);
			addRectToBuffer(buffer, x + 1, y + h - 1, w - 2, 1, col);
		}
		else
		{
			addRectToBuffer(buffer, x, y, w, 1, col);
			addRectToBuffer(buffer, x, y + h - 1, w, 1, col);
		}

		tessellator.draw();
		GlStateManager.enableTexture2D();
	}

	public static void drawCenteredString(FontRenderer font, String txt, float x, float y, Color4I color)
	{
		font.drawString(txt, (int) (x - font.getStringWidth(txt) / 2F), (int) (y - font.FONT_HEIGHT / 2F), color.rgba(), false);
	}

	public static boolean drawItem(ItemStack stack, double x, double y, double scaleX, double scaleY, boolean renderOverlay, Color4I color)
	{
		if (stack.isEmpty() || color.hasColor() && color.alphai() < 100) //TODO: Figure out how to change color
		{
			return false;
		}

		boolean result = true;

		RenderItem renderItem = ClientUtils.MC.getRenderItem();
		renderItem.zLevel = 200F;
		GlStateManager.pushMatrix();
		GlStateManager.translate(x, y, 32D);

		if (scaleX != 1D || scaleY != 1D)
		{
			GlStateManager.scale(scaleX, scaleY, 1D);
		}

		GlStateManager.enableLighting();
		RenderHelper.enableGUIStandardItemLighting();
		GlStateManager.enableRescaleNormal();
		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240F, 240F);
		GlStateManager.color(1F, 1F, 1F, 1F);

		try
		{
			renderItem.renderItemAndEffectIntoGUI(stack, 0, 0);

			if (renderOverlay)
			{
				FontRenderer font = stack.getItem().getFontRenderer(stack);

				if (font == null)
				{
					font = ClientUtils.MC.fontRenderer;
				}

				renderItem.renderItemOverlayIntoGUI(font, stack, 0, 0, null);
			}
		}
		catch (Exception ex)
		{
			result = false;
		}

		GlStateManager.popMatrix();
		renderItem.zLevel = 0F;
		return result;
	}

	public static boolean drawItem(ItemStack stack, double x, double y, boolean renderOverlay, Color4I color)
	{
		return drawItem(stack, x, y, 1D, 1D, renderOverlay, color);
	}

	public static void pushScissor(ScaledResolution screen, int x, int y, int w, int h)
	{
		pushScissor(screen, x, y, w, h, false);
	}

	public static void pushScissor(ScaledResolution screen, int x, int y, int w, int h, boolean ingorePrevious)
	{
		if (scissorIndex == -1)
		{
			GL11.glEnable(GL11.GL_SCISSOR_TEST);
		}
		else if (!ingorePrevious)
		{
			//FIXME
		}

		scissorIndex++;

		if (scissorIndex < MAX_SCISSOR)
		{
			int scale = screen.getScaleFactor();
			SCISSOR_X[scissorIndex] = x * scale;
			SCISSOR_Y[scissorIndex] = (screen.getScaledHeight() - y - h) * scale;
			SCISSOR_W[scissorIndex] = Math.max(w, 1) * scale;
			SCISSOR_H[scissorIndex] = Math.max(h, 1) * scale;
			GL11.glScissor(SCISSOR_X[scissorIndex], SCISSOR_Y[scissorIndex], SCISSOR_W[scissorIndex], SCISSOR_H[scissorIndex]);
		}
	}

	public static void popScissor()
	{
		scissorIndex--;

		if (scissorIndex == -1)
		{
			GL11.glDisable(GL11.GL_SCISSOR_TEST);
		}
		else if (scissorIndex < MAX_SCISSOR)
		{
			GL11.glScissor(SCISSOR_X[scissorIndex], SCISSOR_Y[scissorIndex], SCISSOR_W[scissorIndex], SCISSOR_H[scissorIndex]);
		}
	}

	public static void setFixUnicode(boolean enabled)
	{
		TextureManager textureManager = ClientUtils.MC.getTextureManager();
		int mode = enabled ? GL11.GL_LINEAR : GL11.GL_NEAREST;

		for (int i = 0; i < 256; i++)
		{
			if (FontRenderer.UNICODE_PAGE_LOCATIONS[i] != null)
			{
				textureManager.bindTexture(FontRenderer.UNICODE_PAGE_LOCATIONS[i]);
				GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, mode);
				GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, mode);
			}
		}
	}

	public static boolean onClickEvent(@Nullable ClickEvent clickEvent)
	{
		if (clickEvent == null)
		{
			return false;
		}

		switch (clickEvent.getAction())
		{
			case OPEN_URL:
			{
				try
				{
					final URI uri = new URI(clickEvent.getValue());
					String s = uri.getScheme();

					if (s == null)
					{
						throw new URISyntaxException(clickEvent.getValue(), "Missing protocol");
					}
					if (!s.toLowerCase().contains("http") && !s.toLowerCase().contains("https"))
					{
						throw new URISyntaxException(clickEvent.getValue(), "Unsupported protocol: " + s.toLowerCase());
					}

					if (ClientUtils.MC.gameSettings.chatLinksPrompt)
					{
						final GuiScreen currentScreen = ClientUtils.MC.currentScreen;

						ClientUtils.MC.displayGuiScreen(new GuiConfirmOpenLink((result, id) ->
						{
							if (result)
							{
								try
								{
									NetUtils.openURI(uri);
								}
								catch (Exception ex)
								{
									ex.printStackTrace();
								}
							}
							ClientUtils.MC.displayGuiScreen(currentScreen);
						}, clickEvent.getValue(), 0, false));
					}
					else
					{
						NetUtils.openURI(uri);
					}

					return true;
				}
				catch (Exception ex)
				{
					ex.printStackTrace();
				}

				return false;
			}
			case OPEN_FILE:
			{
				try
				{
					NetUtils.openURI((new File(clickEvent.getValue())).toURI());
					return true;
				}
				catch (Exception ex)
				{
					ex.printStackTrace();
				}

				return false;
			}
			case SUGGEST_COMMAND:
			{
				//FIXME
				return true;
			}
			case RUN_COMMAND:
			{
				ClientUtils.execClientCommand(clickEvent.getValue(), false);
				return true;
			}
			case CHANGE_PAGE:
			{
				if (ClientUtils.MC.currentScreen instanceof IGuiWrapper && ((IGuiWrapper) ClientUtils.MC.currentScreen).getWrappedGui().changePage(clickEvent.getValue()))
				{
					return true;
				}
			}
		}

		return false;
	}

	public static class PositionedTextData
	{
		public final int posX, posY;
		public final int width, height;
		public final ClickEvent clickEvent;
		public final HoverEvent hoverEvent;
		public final String insertion;

		public PositionedTextData(int x, int y, int w, int h, Style s)
		{
			posX = x;
			posY = y;
			width = w;
			height = h;
			clickEvent = s.getClickEvent();
			hoverEvent = s.getHoverEvent();
			insertion = s.getInsertion();
		}
	}

	//TODO: Improve me to fix occasional offset
	public static List<PositionedTextData> createDataFrom(ITextComponent component, FontRenderer font, int width)
	{
		if (width <= 0 || component.getUnformattedText().isEmpty())
		{
			return Collections.emptyList();
		}

		List<PositionedTextData> list = new ArrayList<>();

		int line = 0;
		int currentWidth = 0;

		for (ITextComponent t : component.createCopy())
		{
			String text = t.getUnformattedComponentText();
			int textWidth = font.getStringWidth(text);

			while (textWidth > 0)
			{
				int w = textWidth;
				if (w > width - currentWidth)
				{
					w = width - currentWidth;
				}

				list.add(new PositionedTextData(currentWidth, line * 10, w, 10, t.getStyle()));

				currentWidth += w;
				textWidth -= w;

				if (currentWidth >= width)
				{
					currentWidth = 0;
					line++;
				}
			}
		}

		return list;
	}
}