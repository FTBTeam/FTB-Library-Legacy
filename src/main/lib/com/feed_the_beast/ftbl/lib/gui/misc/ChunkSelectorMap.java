package com.feed_the_beast.ftbl.lib.gui.misc;

import com.feed_the_beast.ftbl.lib.gui.GuiBase;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author LatvianModder
 */
public abstract class ChunkSelectorMap
{
	private static ChunkSelectorMap INSTANCE;

	public static ChunkSelectorMap getMap()
	{
		if (INSTANCE == null)
		{
			INSTANCE = new BuiltinChunkMap();
		}

		return INSTANCE;
	}

	public static void setMap(ChunkSelectorMap map)
	{
		INSTANCE = map;
	}

	public static final int TILES_TEX = 16;
	public static final int TILES_GUI = 15;
	public static final int TILES_GUI2 = TILES_GUI / 2;

	@SideOnly(Side.CLIENT)
	public abstract void resetMap(int startX, int startZ);

	@SideOnly(Side.CLIENT)
	public abstract void drawMap(GuiBase gui, int ax, int ay, int startX, int startZ);
}