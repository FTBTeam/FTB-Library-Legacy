package com.feed_the_beast.ftblib.lib.gui.misc;

import com.feed_the_beast.ftblib.lib.client.CachedVertexData;
import com.feed_the_beast.ftblib.lib.client.ClientUtils;
import com.feed_the_beast.ftblib.lib.gui.Button;
import com.feed_the_beast.ftblib.lib.gui.GuiBase;
import com.feed_the_beast.ftblib.lib.gui.GuiHelper;
import com.feed_the_beast.ftblib.lib.gui.Panel;
import com.feed_the_beast.ftblib.lib.gui.WidgetLayout;
import com.feed_the_beast.ftblib.lib.icon.Color4I;
import com.feed_the_beast.ftblib.lib.icon.Icon;
import com.feed_the_beast.ftblib.lib.math.MathUtils;
import com.feed_the_beast.ftblib.lib.util.misc.MouseButton;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.math.ChunkPos;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * @author LatvianModder
 */
public class GuiChunkSelectorBase extends GuiBase
{
	protected enum Corner
	{
		BOTTOM_LEFT,
		BOTTOM_RIGHT,
		TOP_LEFT
	}

	public static final int TILE_SIZE = 12;
	private static final CachedVertexData GRID = new CachedVertexData(GL11.GL_LINES, DefaultVertexFormats.POSITION_COLOR);

	static
	{
		GRID.color.set(128, 128, 128, 50);

		for (int x = 0; x <= ChunkSelectorMap.TILES_GUI; x++)
		{
			GRID.pos(x * TILE_SIZE, 0D);
			GRID.pos(x * TILE_SIZE, ChunkSelectorMap.TILES_GUI * TILE_SIZE, 0D);
		}

		for (int y = 0; y <= ChunkSelectorMap.TILES_GUI; y++)
		{
			GRID.pos(0D, y * TILE_SIZE, 0D);
			GRID.pos(ChunkSelectorMap.TILES_GUI * TILE_SIZE, y * TILE_SIZE, 0D);
		}
	}

	public static class MapButton extends Button
	{
		public final GuiChunkSelectorBase gui;
		public final ChunkPos chunkPos;
		public final int index;
		private boolean isSelected = false;

		private MapButton(GuiChunkSelectorBase g, int x, int y, int i)
		{
			super(g, x, y, TILE_SIZE, TILE_SIZE);
			gui = g;
			posX += (i % ChunkSelectorMap.TILES_GUI) * width;
			posY += (i / ChunkSelectorMap.TILES_GUI) * height;
			chunkPos = new ChunkPos(gui.startX + (i % ChunkSelectorMap.TILES_GUI), gui.startZ + (i / ChunkSelectorMap.TILES_GUI));
			index = i;
		}

		@Override
		public void onClicked(MouseButton button)
		{
			GuiHelper.playClickSound();
			gui.currentSelectionMode = gui.getSelectionMode(button);

			if (gui.currentSelectionMode == -1)
			{
				gui.onChunksSelected(Collections.singleton(chunkPos));
			}
		}

		@Override
		public void addMouseOverText(List<String> list)
		{
			gui.addButtonText(this, list);
		}

		@Override
		public void renderWidget()
		{
			int ax = getAX();
			int ay = getAY();

			if (!isSelected && gui.currentSelectionMode != -1 && gui.isMouseOver(this))
			{
				isSelected = true;
			}

			if (isSelected || gui.isMouseOver(this))
			{
				Color4I.WHITE_A[33].draw(ax, ay, TILE_SIZE, TILE_SIZE);
			}
		}
	}

	public final int startX, startZ;
	private final MapButton mapButtons[];
	private final Panel panelButtons;
	public int currentSelectionMode = -1;

	public GuiChunkSelectorBase()
	{
		super(ChunkSelectorMap.TILES_GUI * TILE_SIZE, ChunkSelectorMap.TILES_GUI * TILE_SIZE);

		startX = MathUtils.chunk(ClientUtils.MC.player.posX) - ChunkSelectorMap.TILES_GUI2;
		startZ = MathUtils.chunk(ClientUtils.MC.player.posZ) - ChunkSelectorMap.TILES_GUI2;

		panelButtons = new Panel(this, 0, 0, 16, 0)
		{
			@Override
			public void addWidgets()
			{
				addCornerButtons(panelButtons);
				updateWidgetPositions();
			}

			@Override
			public void updateWidgetPositions()
			{
				align(WidgetLayout.VERTICAL);
			}
		};

		mapButtons = new MapButton[ChunkSelectorMap.TILES_GUI * ChunkSelectorMap.TILES_GUI];

		for (int i = 0; i < mapButtons.length; i++)
		{
			mapButtons[i] = new MapButton(this, 0, 0, i);
		}
	}

	@Override
	public void onInit()
	{
		ChunkSelectorMap.getMap().resetMap(startX, startZ);
	}

	@Override
	public void addWidgets()
	{
		for (MapButton b : mapButtons)
		{
			add(b);
		}

		add(panelButtons);
		panelButtons.posX = getScreen().getScaledWidth() - 20 - getAX();
		panelButtons.posY = -getAY();
		panelButtons.setHeight(panelButtons.widgets.size() * 20);
	}

	@Override
	public void drawBackground()
	{
		GlStateManager.color(1F, 1F, 1F, 1F);
		Color4I.BLACK.draw(posX - 2, posY - 2, width + 4, height + 4);

		ChunkSelectorMap.getMap().drawMap(this, posX, posY, startX, startZ);

		GlStateManager.color(1F, 1F, 1F, 1F);

		for (MapButton mapButton : mapButtons)
		{
			mapButton.renderWidget();
		}

		GlStateManager.disableTexture2D();
		GlStateManager.glLineWidth(1F);

		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder buffer = tessellator.getBuffer();
		buffer.setTranslation(mapButtons[0].getAX(), mapButtons[0].getAY(), 0D);
		//GlStateManager.color(1F, 1F, 1F, GuiScreen.isCtrlKeyDown() ? 0.2F : 0.7F);
		GlStateManager.color(1F, 1F, 1F, 1F);

		if (!Keyboard.isKeyDown(Keyboard.KEY_TAB))
		{
			drawArea(tessellator, buffer);
		}

		GRID.draw(tessellator, buffer);
		buffer.setTranslation(0D, 0D, 0D);
		GlStateManager.enableTexture2D();
		GlStateManager.color(1F, 1F, 1F, 1F);
	}

	@Override
	public void mouseReleased()
	{
		super.mouseReleased();

		if (currentSelectionMode != -1)
		{
			Collection<ChunkPos> c = new ArrayList<>();

			for (MapButton b : mapButtons)
			{
				if (b.isSelected)
				{
					c.add(b.chunkPos);
					b.isSelected = false;
				}
			}

			onChunksSelected(c);
			currentSelectionMode = -1;
		}
	}

	@Override
	public void drawForeground()
	{
		int lineSpacing = getFontHeight() + 1;
		addCornerText(TEMP_TEXT_LIST, Corner.BOTTOM_RIGHT);

		for (int i = 0; i < TEMP_TEXT_LIST.size(); i++)
		{
			String s = TEMP_TEXT_LIST.get(i);
			drawString(s, getScreen().getScaledWidth() - getStringWidth(s) - 2, getScreen().getScaledHeight() - (TEMP_TEXT_LIST.size() - i) * lineSpacing, SHADOW);
		}

		TEMP_TEXT_LIST.clear();

		addCornerText(TEMP_TEXT_LIST, Corner.BOTTOM_LEFT);

		for (int i = 0; i < TEMP_TEXT_LIST.size(); i++)
		{
			drawString(TEMP_TEXT_LIST.get(i), 2, getScreen().getScaledHeight() - (TEMP_TEXT_LIST.size() - i) * lineSpacing, SHADOW);
		}

		TEMP_TEXT_LIST.clear();

		addCornerText(TEMP_TEXT_LIST, Corner.TOP_LEFT);

		for (int i = 0; i < TEMP_TEXT_LIST.size(); i++)
		{
			drawString(TEMP_TEXT_LIST.get(i), 2, 2 + i * lineSpacing, SHADOW);
		}

		TEMP_TEXT_LIST.clear();

		super.drawForeground();
	}

	public int getSelectionMode(MouseButton button)
	{
		return -1;
	}

	public void onChunksSelected(Collection<ChunkPos> chunks)
	{
	}

	public void drawArea(Tessellator tessellator, BufferBuilder buffer)
	{
	}

	public void addCornerButtons(Panel panel)
	{
	}

	public void addCornerText(List<String> list, Corner corner)
	{
	}

	public void addButtonText(MapButton button, List<String> list)
	{
	}

	@Override
	public Icon getIcon()
	{
		return Icon.EMPTY;
	}
}
