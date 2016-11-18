package com.feed_the_beast.ftbl.lib.gui.misc;

import com.feed_the_beast.ftbl.lib.client.TextureCoords;
import com.feed_the_beast.ftbl.lib.config.PropertyBool;
import com.feed_the_beast.ftbl.lib.config.PropertyInt;
import com.feed_the_beast.ftbl.lib.internal.FTBLibFinals;
import net.minecraft.util.ResourceLocation;

/**
 * Created by LatvianModder on 11.11.2016.
 */
public class GuiConfigs
{
    public static final int CHUNK_SELECTOR_TILES_TEX = 16;
    public static final int CHUNK_SELECTOR_TILES_GUI = 15;
    public static final double CHUNK_SELECTOR_UV = (double) CHUNK_SELECTOR_TILES_GUI / (double) CHUNK_SELECTOR_TILES_TEX;

    public static final ResourceLocation TEX_ENTITY = FTBLibFinals.get("textures/gui/entity.png");
    public static final ResourceLocation TEX_CHUNK_CLAIMING = FTBLibFinals.get("textures/gui/chunk_selectors.png");
    public static final TextureCoords TEX_FILLED = TextureCoords.fromUV(TEX_CHUNK_CLAIMING, 0D, 0D, 0.5D, 1D);
    public static final TextureCoords TEX_BORDER = TextureCoords.fromUV(TEX_CHUNK_CLAIMING, 0.5D, 0D, 1D, 1D);

    public static final PropertyBool UNICODE = new PropertyBool(true);
    public static final PropertyInt BORDER_WIDTH = new PropertyInt(15).setMin(0).setMax(200);
    public static final PropertyInt BORDER_HEIGHT = new PropertyInt(15).setMin(0).setMax(200);
    public static final PropertyBool ENABLE_CHUNK_SELECTOR_DEPTH = new PropertyBool(true);
}