package com.feed_the_beast.ftbl.lib;

import com.feed_the_beast.ftbl.lib.util.StringUtils;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Created by LatvianModder on 21.12.2016.
 */
public class IconSet
{
    public final ResourceLocation[] textures;

    public IconSet(String v)
    {
        textures = new ResourceLocation[6];

        Map<String, String> map = StringUtils.parse(StringUtils.TEMP_MAP, v);

        String s = map.get("all");

        if(s != null)
        {
            ResourceLocation tex = new ResourceLocation(s);

            for(int i = 0; i < 6; i++)
            {
                textures[i] = tex;
            }
        }

        for(EnumFacing facing : EnumFacing.VALUES)
        {
            s = map.get(facing.getName());

            if(s != null)
            {
                textures[facing.ordinal()] = new ResourceLocation(s);
            }
        }
    }

    @Nullable
    public ResourceLocation getTexture(EnumFacing face)
    {
        return textures[face.ordinal()];
    }

    public Collection<ResourceLocation> getTextures()
    {
        List<ResourceLocation> list = new ArrayList<>();

        for(int i = 0; i < 6; i++)
        {
            if(textures[i] != null)
            {
                list.add(textures[i]);
            }
        }

        return list;
    }
}