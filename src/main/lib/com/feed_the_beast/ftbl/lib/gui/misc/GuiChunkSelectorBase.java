package com.feed_the_beast.ftbl.lib.gui.misc;

import com.feed_the_beast.ftbl.lib.Color4I;
import com.feed_the_beast.ftbl.lib.MouseButton;
import com.feed_the_beast.ftbl.lib.client.CachedVertexData;
import com.feed_the_beast.ftbl.lib.client.ClientUtils;
import com.feed_the_beast.ftbl.lib.gui.Button;
import com.feed_the_beast.ftbl.lib.gui.GuiBase;
import com.feed_the_beast.ftbl.lib.gui.GuiHelper;
import com.feed_the_beast.ftbl.lib.gui.Panel;
import com.feed_the_beast.ftbl.lib.math.MathUtils;
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

	public class MapButton extends Button
	{
		public final ChunkPos chunkPos;
		public final int index;
		private boolean isSelected = false;

		private MapButton(int x, int y, int i)
		{
			super(x, y, TILE_SIZE, TILE_SIZE);
			posX += (i % ChunkSelectorMap.TILES_GUI) * width;
			posY += (i / ChunkSelectorMap.TILES_GUI) * height;
			chunkPos = new ChunkPos(startX + (i % ChunkSelectorMap.TILES_GUI), startZ + (i / ChunkSelectorMap.TILES_GUI));
			index = i;
		}

		@Override
		public void onClicked(GuiBase gui, MouseButton button)
		{
			GuiHelper.playClickSound();
			currentSelectionMode = getSelectionMode(button);

			if (currentSelectionMode == -1)
			{
				onChunksSelected(Collections.singleton(chunkPos));
			}
		}

		@Override
		public void addMouseOverText(GuiBase gui, List<String> list)
		{
			addButtonText(this, list);
		}

		@Override
		public void renderWidget(GuiBase gui)
		{
			int ax = getAX();
			int ay = getAY();

			if (!isSelected && currentSelectionMode != -1 && isMouseOver(this))
			{
				isSelected = true;
			}

			if (isSelected || gui.isMouseOver(this))
			{
				GuiHelper.drawBlankRect(ax, ay, TILE_SIZE, TILE_SIZE, Color4I.WHITE_A[33]);
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

		panelButtons = new Panel(0, 0, 16, 0)
		{
			@Override
			public void addWidgets()
			{
				addCornerButtons(panelButtons);
			}
		};

		mapButtons = new MapButton[ChunkSelectorMap.TILES_GUI * ChunkSelectorMap.TILES_GUI];

		for (int i = 0; i < mapButtons.length; i++)
		{
			mapButtons[i] = new MapButton(0, 0, i);
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
		panelButtons.posX = getScreen().getScaledWidth() - 16 - posX;
		panelButtons.posY = -posY;
		panelButtons.setHeight(panelButtons.widgets.size() * 16);
	}

	@Override
	public void drawBackground()
	{
		GlStateManager.color(1F, 1F, 1F, 1F);
		GuiHelper.drawBlankRect(posX - 2, posY - 2, width + 4, height + 4, Color4I.BLACK);

		ChunkSelectorMap.getMap().drawMap(this, posX, posY, startX, startZ);

		GlStateManager.color(1F, 1F, 1F, 1F);

		for (MapButton mapButton : mapButtons)
		{
			mapButton.renderWidget(this);
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
	public void mouseReleased(GuiBase gui)
	{
		super.mouseReleased(gui);

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
		int lineSpacing = getFont().FONT_HEIGHT + 1;
		addCornerText(TEMP_TEXT_LIST, Corner.BOTTOM_RIGHT);

		for (int i = 0; i < TEMP_TEXT_LIST.size(); i++)
		{
			String s = TEMP_TEXT_LIST.get(i);
			getFont().drawStringWithShadow(s, getScreen().getScaledWidth() - getFont().getStringWidth(s) - 2, getScreen().getScaledHeight() - (TEMP_TEXT_LIST.size() - i) * lineSpacing, 0xFFFFFFFF);
		}

		TEMP_TEXT_LIST.clear();

		addCornerText(TEMP_TEXT_LIST, Corner.BOTTOM_LEFT);

		for (int i = 0; i < TEMP_TEXT_LIST.size(); i++)
		{
			getFont().drawStringWithShadow(TEMP_TEXT_LIST.get(i), 2, getScreen().getScaledHeight() - (TEMP_TEXT_LIST.size() - i) * lineSpacing, 0xFFFFFFFF);
		}

		TEMP_TEXT_LIST.clear();

		addCornerText(TEMP_TEXT_LIST, Corner.TOP_LEFT);

		for (int i = 0; i < TEMP_TEXT_LIST.size(); i++)
		{
			getFont().drawStringWithShadow(TEMP_TEXT_LIST.get(i), 2, 2 + i * lineSpacing, 0xFFFFFFFF);
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
}
