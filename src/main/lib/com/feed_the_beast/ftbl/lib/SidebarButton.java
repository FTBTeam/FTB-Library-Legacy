package com.feed_the_beast.ftbl.lib;

import com.feed_the_beast.ftbl.api.config.IConfigValue;
import com.feed_the_beast.ftbl.api.gui.IImageProvider;
import com.feed_the_beast.ftbl.api.gui.IMouseButton;
import com.feed_the_beast.ftbl.api.gui.ISidebarButton;
import com.feed_the_beast.ftbl.lib.client.ImageProvider;
import com.feed_the_beast.ftbl.lib.gui.GuiHelper;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class SidebarButton extends FinalIDObject implements ISidebarButton
{
    private final IImageProvider icon;
    private final IConfigValue config;
    private Map<String, Boolean> deps;

    public SidebarButton(ResourceLocation id, IImageProvider c, @Nullable IConfigValue b, String dependencies)
    {
        super(id.toString().replace(':', '.'));
        icon = c;
        config = b;

        if(!dependencies.isEmpty())
        {
            deps = new HashMap<>();

            for(String s : dependencies.split(";"))
            {
                int index = s.indexOf(':');

                if(index != -1)
                {
                    switch(s.substring(0, index))
                    {
                        case "before":
                            deps.put(s.substring(index + 1, s.length()), true);
                            break;
                        case "after":
                            deps.put(s.substring(index + 1, s.length()), false);
                            break;
                    }
                }
            }
        }

        if(deps == null || deps.isEmpty())
        {
            deps = Collections.emptyMap();
        }
    }

    @Override
    public IImageProvider getIcon()
    {
        return icon;
    }

    @Override
    @Nullable
    public IConfigValue getConfig()
    {
        return config;
    }

    @Override
    public void render(int ax, int ay)
    {
        GuiHelper.render(icon, ax, ay, 16, 16);
    }

    @Override
    public Map<String, Boolean> getDependencies()
    {
        return deps;
    }

    public static class Dummy extends SidebarButton
    {
        public Dummy(ResourceLocation id)
        {
            super(id, ImageProvider.NULL, null, "");
        }

        @Override
        public void onClicked(IMouseButton button)
        {
        }
    }
}