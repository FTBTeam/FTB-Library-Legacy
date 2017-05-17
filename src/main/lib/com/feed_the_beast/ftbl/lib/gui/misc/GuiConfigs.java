package com.feed_the_beast.ftbl.lib.gui.misc;

import com.feed_the_beast.ftbl.lib.client.TextureCoords;
import com.feed_the_beast.ftbl.lib.config.PropertyBool;
import com.feed_the_beast.ftbl.lib.config.PropertyColor;
import com.feed_the_beast.ftbl.lib.config.PropertyInt;
import com.feed_the_beast.ftbl.lib.internal.FTBLibFinals;
import net.minecraft.util.ResourceLocation;

/**
 * @author LatvianModder
 */
public class GuiConfigs
{
    public static final int CHUNK_SELECTOR_TILES_TEX = 16;
    public static final int CHUNK_SELECTOR_TILES_GUI = 15;
    public static final int CHUNK_SELECTOR_TILES_GUI2 = 7;
    public static final double CHUNK_SELECTOR_UV = (double) CHUNK_SELECTOR_TILES_GUI / (double) CHUNK_SELECTOR_TILES_TEX;

    public static final ResourceLocation TEX_ENTITY = FTBLibFinals.get("textures/gui/entity.png");
    public static final ResourceLocation TEX_CHUNK_CLAIMING = FTBLibFinals.get("textures/gui/chunk_selectors.png");
    public static final TextureCoords TEX_FILLED = TextureCoords.fromUV(TEX_CHUNK_CLAIMING, 0D, 0D, 0.5D, 1D);
    public static final TextureCoords TEX_BORDER = TextureCoords.fromUV(TEX_CHUNK_CLAIMING, 0.5D, 0D, 1D, 1D);

    public static final PropertyInt INFO_BORDER_WIDTH = new PropertyInt(15, 0, 200);
    public static final PropertyInt INFO_BORDER_HEIGHT = new PropertyInt(15, 0, 200);
    public static final PropertyColor INFO_BACKGROUND = new PropertyColor(0xFFF7F4DA);
    public static final PropertyColor INFO_TEXT = new PropertyColor(0xFF7B6534);
    public static final PropertyBool ENABLE_CHUNK_SELECTOR_DEPTH = new PropertyBool(true);
}