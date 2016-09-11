package com.feed_the_beast.ftbl.client;

import com.feed_the_beast.ftbl.FTBLibFinals;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import net.minecraft.util.ResourceLocation;

import java.io.IOException;
import java.util.Arrays;

/**
 * Created by LatvianModder on 06.09.2016.
 */
public class FTBLibColors implements IResourceManagerReloadListener
{
    public static final ResourceLocation COLORS_PNG = new ResourceLocation(FTBLibFinals.MOD_ID, "textures/colors_128.png");
    public static final int WIDTH = 16;
    public static final int HEIGHT = 8;
    private static final int[] COLORS = new int[WIDTH * HEIGHT];

    @Override
    public void onResourceManagerReload(IResourceManager resourceManager)
    {
        Arrays.fill(COLORS, 0);

        try
        {
            int[] imageData = TextureUtil.readImageData(resourceManager, COLORS_PNG);

            if(imageData.length == COLORS.length)
            {
                System.arraycopy(imageData, 0, COLORS, 0, COLORS.length);
            }
        }
        catch(IOException ex)
        {
        }
    }

    public static int get(byte col)
    {
        return col >= 0 ? COLORS[col] : 0;
    }
}