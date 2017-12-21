package com.feed_the_beast.ftblib.lib.gui;

import com.feed_the_beast.ftblib.lib.ClientATHelper;
import com.feed_the_beast.ftblib.lib.client.ClientUtils;
import com.feed_the_beast.ftblib.lib.icon.Color4I;
import com.feed_the_beast.ftblib.lib.util.NetUtils;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiConfirmOpenLink;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
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
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
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
import java.util.Stack;

/**
 * @author LatvianModder
 */
public class GuiHelper
{
	private static class Scissor
	{
		private final int x, y, w, h;

		private Scissor(int _x, int _y, int _w, int _h)
		{
			x = _x;
			y = _y;
			w = Math.max(0, _w);
			h = Math.max(0, _h);
		}

		public Scissor crop(int sx, int sy, int sw, int sh)
		{
			int x0 = Math.max(x, sx);
			int y0 = Math.max(y, sy);
			int x1 = Math.min(x + w, sx + sw);
			int y1 = Math.min(y + h, sy + sh);
			return new Scissor(x0, y0, x1 - x0, y1 - y0);
		}

		public void scissor(ScaledResolution screen)
		{
			int scale = screen.getScaleFactor();
			int sx = x * scale;
			int sy = (screen.getScaledHeight() - (y + h)) * scale;
			int sw = w * scale;
			int sh = h * scale;
			GL11.glScissor(sx, sy, sw, sh);
		}
	}

	private static final Stack<Scissor> SCISSOR = new Stack<>();

	public static void playSound(SoundEvent event, float pitch)
	{
		ClientUtils.MC.getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(event, pitch));
	}

	public static void playClickSound()
	{
		playSound(SoundEvents.UI_BUTTON_CLICK, 1F);
	}

	public static void drawTexturedRect(int x, int y, int w, int h, Color4I col, double u0, double v0, double u1, double v1)
	{
		if (col.isEmpty())
		{
			col = Color4I.WHITE;
		}

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

	public static void addRectToBuffer(BufferBuilder buffer, int x, int y, int w, int h, Color4I col)
	{
		int r = col.redi();
		int g = col.greeni();
		int b = col.bluei();
		int a = col.alphai();
		buffer.pos(x, y + h, 0D).color(r, g, b, a).endVertex();
		buffer.pos(x + w, y + h, 0D).color(r, g, b, a).endVertex();
		buffer.pos(x + w, y, 0D).color(r, g, b, a).endVertex();
		buffer.pos(x, y, 0D).color(r, g, b, a).endVertex();
	}

	public static void addRectToBufferWithUV(BufferBuilder buffer, int x, int y, int w, int h, Color4I col, double u0, double v0, double u1, double v1)
	{
		int r = col.redi();
		int g = col.greeni();
		int b = col.bluei();
		int a = col.alphai();
		buffer.pos(x, y + h, 0D).tex(u0, v1).color(r, g, b, a).endVertex();
		buffer.pos(x + w, y + h, 0D).tex(u1, v1).color(r, g, b, a).endVertex();
		buffer.pos(x + w, y, 0D).tex(u1, v0).color(r, g, b, a).endVertex();
		buffer.pos(x, y, 0D).tex(u0, v0).color(r, g, b, a).endVertex();
	}

	public static void drawHollowRect(int x, int y, int w, int h, Color4I col, boolean roundEdges)
	{
		if (w <= 1 || h <= 1 || col.isEmpty())
		{
			col.draw(x, y, w, h);
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

	public static boolean drawItem(ItemStack stack, double x, double y, double scaleX, double scaleY, boolean renderOverlay, Color4I color)
	{
		if (stack.isEmpty() || !color.isEmpty() && color.alphai() < 100) //TODO: Figure out how to change color
		{
			return false;
		}

		boolean result = true;

		RenderItem renderItem = ClientUtils.MC.getRenderItem();
		renderItem.zLevel = 180F;
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
		GlStateManager.enableBlend();
		GlStateManager.enableTexture2D();

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
		if (SCISSOR.isEmpty())
		{
			GL11.glEnable(GL11.GL_SCISSOR_TEST);
		}

		Scissor scissor = SCISSOR.isEmpty() ? new Scissor(x, y, w, h) : SCISSOR.lastElement().crop(x, y, w, h);
		SCISSOR.push(scissor);
		scissor.scissor(screen);
	}

	public static void popScissor(ScaledResolution screen)
	{
		SCISSOR.pop().scissor(screen);

		if (SCISSOR.isEmpty())
		{
			GL11.glDisable(GL11.GL_SCISSOR_TEST);
		}
	}

	public static void setFixUnicode(boolean enabled)
	{
		TextureManager textureManager = ClientUtils.MC.getTextureManager();
		int mode = enabled ? GL11.GL_LINEAR : GL11.GL_NEAREST;

		for (int i = 0; i < 256; i++)
		{
			ResourceLocation loc = ClientATHelper.getFontUnicodePage(i);

			if (loc != null)
			{
				textureManager.bindTexture(loc);
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