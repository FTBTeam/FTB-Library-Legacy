package com.feed_the_beast.ftbl.api_impl;

import com.feed_the_beast.ftbl.api.config.IConfigValue;
import com.feed_the_beast.ftbl.api.gui.ISidebarButton;
import com.feed_the_beast.ftbl.lib.AsmData;
import com.feed_the_beast.ftbl.lib.ResourceLocationComparator;
import com.feed_the_beast.ftbl.lib.config.ConfigKey;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by LatvianModder on 25.10.2016.
 */
public enum FTBLibClientRegistries
{
    INSTANCE;

    public static final Comparator<ISidebarButton> SIDEBAR_BUTTON_COMPARATOR = (o1, o2) ->
    {
        int i = Integer.compare(o2.getPriority(), o1.getPriority());

        if(i == 0)
        {
            i = ResourceLocationComparator.INSTANCE.compare(o1.getID(), o2.getID());
        }

        return i;
    };

    public final Map<ResourceLocation, ISidebarButton> SIDEBAR_BUTTONS = new HashMap<>();

    public void init(AsmData asmData)
    {
        asmData.findRegistryObjects(ISidebarButton.class, false, (obj, field, id) -> SIDEBAR_BUTTONS.put(obj.getID(), obj));

        for(ISidebarButton button : FTBLibClientRegistries.INSTANCE.SIDEBAR_BUTTONS.values())
        {
            IConfigValue value = button.getConfig();

            if(value != null)
            {
                FTBLibRegistries.INSTANCE.CLIENT_CONFIG.add(new ConfigKey(button.getPath(), value.copy()), value);
            }
        }
    }

    public List<ISidebarButton> getSidebarButtons(boolean ignoreConfig)
    {
        List<ISidebarButton> l = new ArrayList<>();

        SIDEBAR_BUTTONS.forEach((key, value) ->
        {
            if(value.isVisible())
            {
                if(ignoreConfig || value.getConfig() == null || value.getConfig().getBoolean())
                {
                    l.add(value);
                }
            }
        });

        return l;
    }
}
