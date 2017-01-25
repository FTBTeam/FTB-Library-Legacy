package com.feed_the_beast.ftbl.lib;

import com.feed_the_beast.ftbl.api.config.IConfigValue;
import com.feed_the_beast.ftbl.api.gui.IImageProvider;
import com.feed_the_beast.ftbl.api.gui.ISidebarButton;
import com.feed_the_beast.ftbl.lib.gui.GuiHelper;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class SidebarButton extends FinalIDObject implements ISidebarButton
{
    private final IImageProvider icon;
    private final IConfigValue config;
    private List<String> requiredBefore, requiredAfter;

    public SidebarButton(ResourceLocation id, IImageProvider c, @Nullable IConfigValue b, String dependencies)
    {
        super(id.toString().toLowerCase().replace(':', '.'));
        icon = c;
        config = b;

        if(!dependencies.isEmpty())
        {
            for(String s : dependencies.split(";"))
            {
                int index = s.indexOf(':');

                if(index != -1)
                {
                    switch(s.substring(0, index))
                    {
                        case "before":
                            if(requiredBefore == null)
                            {
                                requiredBefore = new ArrayList<>();
                            }
                            requiredBefore.add(s.substring(index + 1, s.length()));
                            break;

                        case "after":
                            if(requiredAfter == null)
                            {
                                requiredAfter = new ArrayList<>();
                            }
                            requiredAfter.add(s.substring(index + 1, s.length()));
                            break;
                    }
                }
            }
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
    public List<String> requiredBefore()
    {
        return requiredBefore == null ? Collections.emptyList() : requiredBefore;
    }

    @Override
    public List<String> requiredAfter()
    {
        return requiredAfter == null ? Collections.emptyList() : requiredAfter;
    }
}