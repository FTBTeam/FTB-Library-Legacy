package com.feed_the_beast.ftbl.lib.util;

import com.feed_the_beast.ftbl.lib.Color4I;
import com.feed_the_beast.ftbl.lib.client.ParticleColoredDust;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.world.World;

/**
 * @author LatvianModder
 */
public class UtilsClient extends UtilsCommon
{
    private static final Object[] NO_OBJECTS = { };

    @Override
    String translate(String key)
    {
        return I18n.format(key, NO_OBJECTS);
    }

    @Override
    String translate(String key, Object... objects)
    {
        return I18n.format(key, objects);
    }

    @Override
    boolean canTranslate(String key)
    {
        return I18n.hasKey(key);
    }

    @Override
    public void spawnDust(World w, double x, double y, double z, Color4I col)
    {
        Minecraft.getMinecraft().effectRenderer.addEffect(new ParticleColoredDust(w, x, y, z, col.redf(), col.greenf(), col.bluef(), col.alphaf()));
    }
}