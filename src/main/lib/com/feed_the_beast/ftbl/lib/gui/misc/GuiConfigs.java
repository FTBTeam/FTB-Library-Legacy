package com.feed_the_beast.ftbl.lib.gui.misc;

import com.feed_the_beast.ftbl.api.gui.IDrawableObject;
import com.feed_the_beast.ftbl.lib.client.ImageProvider;
import com.feed_the_beast.ftbl.lib.config.PropertyBool;
import com.feed_the_beast.ftbl.lib.config.PropertyColor;
import com.feed_the_beast.ftbl.lib.config.PropertyInt;
import com.feed_the_beast.ftbl.lib.internal.FTBLibFinals;

/**
 * @author LatvianModder
 */
public class GuiConfigs
{
    public static final int CHUNK_SELECTOR_TILES_TEX = 16;
    public static final int CHUNK_SELECTOR_TILES_GUI = 15;
    public static final int CHUNK_SELECTOR_TILES_GUI2 = 7;
    public static final double CHUNK_SELECTOR_UV = (double) CHUNK_SELECTOR_TILES_GUI / (double) CHUNK_SELECTOR_TILES_TEX;

    public static final IDrawableObject TEX_ENTITY = ImageProvider.get(FTBLibFinals.MOD_ID + ":textures/gui/entity.png");

    public static final PropertyInt INFO_BORDER_WIDTH = new PropertyInt(15, 0, 200);
    public static final PropertyInt INFO_BORDER_HEIGHT = new PropertyInt(15, 0, 200);
    public static final PropertyColor INFO_BACKGROUND = new PropertyColor(0xFFF7F4DA);
    public static final PropertyColor INFO_TEXT = new PropertyColor(0xFF7B6534);
    public static final PropertyBool ENABLE_CHUNK_SELECTOR_DEPTH = new PropertyBool(true);
}