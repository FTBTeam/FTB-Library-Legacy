package com.feed_the_beast.ftbl.lib;

import com.feed_the_beast.ftbl.api.config.IConfigValue;
import com.feed_the_beast.ftbl.api.gui.IDrawableObject;
import com.feed_the_beast.ftbl.api.gui.IMouseButton;
import com.feed_the_beast.ftbl.lib.client.ImageProvider;
import com.feed_the_beast.ftbl.lib.config.PropertyBool;
import com.feed_the_beast.ftbl.lib.gui.GuiHelper;
import com.feed_the_beast.ftbl.lib.gui.GuiIcons;
import com.feed_the_beast.ftbl.lib.gui.misc.GuiLoading;
import com.feed_the_beast.ftbl.lib.internal.FTBLibIntegrationInternal;
import com.feed_the_beast.ftbl.lib.util.JsonUtils;
import com.feed_the_beast.ftbl.lib.util.LMUtils;
import com.google.gson.JsonObject;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.event.ClickEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SidebarButton extends FinalIDObject
{
    public IDrawableObject icon = ImageProvider.NULL;
    public IConfigValue config = null;
    public final Map<String, Boolean> dependencies = new HashMap<>();
    public final List<String> requiredServerMods = new ArrayList<>();
    private ClickEvent clickEvent;
    public boolean requiresOp, devOnly, hideWithNEI, loadingScreen;

    public SidebarButton(ResourceLocation id)
    {
        super(id.toString().replace(':', '.'));
    }

    public SidebarButton(ResourceLocation id, JsonObject o)
    {
        this(id);

        if(o.has("icon"))
        {
            icon = ImageProvider.get(o.get("icon"));
        }

        if(icon == ImageProvider.NULL)
        {
            icon = GuiIcons.ACCEPT_GRAY;
        }
        if(o.has("dependencies"))
        {
            setDependencies(o.get("dependencies").getAsString());
        }
        if(o.has("click"))
        {
            clickEvent = JsonUtils.deserializeClickEvent(o.get("click"));
        }
        if(o.has("config"))
        {
            config = new PropertyBool(o.get("config").getAsBoolean());
        }

        requiresOp = o.has("requires_op") && o.get("requires_op").getAsBoolean();
        devOnly = o.has("dev_only") && o.get("dev_only").getAsBoolean();
        hideWithNEI = o.has("hide_with_nei") && o.get("hide_with_nei").getAsBoolean();
        loadingScreen = o.has("loading_screen") && o.get("loading_screen").getAsBoolean();
    }

    public void setDependencies(String deps)
    {
        dependencies.clear();

        if(!deps.isEmpty())
        {
            for(String s : deps.split(";"))
            {
                int index = s.indexOf(':');

                if(index != -1)
                {
                    switch(s.substring(0, index))
                    {
                        case "before":
                            dependencies.put(s.substring(index + 1, s.length()), true);
                            break;
                        case "after":
                            dependencies.put(s.substring(index + 1, s.length()), false);
                            break;
                    }
                }
            }
        }
    }

    public void onClicked(IMouseButton button)
    {
        if(clickEvent != null)
        {
            if(loadingScreen)
            {
                new GuiLoading().openGui();
            }

            GuiHelper.onClickEvent(clickEvent);
        }
    }

    public boolean isVisible()
    {
        return !(hideWithNEI && LMUtils.isNEILoaded()) && !(requiresOp && !FTBLibIntegrationInternal.API.getClientData().isClientOP()) && !(!requiredServerMods.isEmpty() && FTBLibIntegrationInternal.API.getClientData().optionalServerMods().containsAll(requiredServerMods));
    }
}