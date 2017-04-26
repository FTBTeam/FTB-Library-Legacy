package com.feed_the_beast.ftbl.api.events;

import com.feed_the_beast.ftbl.lib.guide.GuideTitlePage;
import net.minecraft.client.resources.IResourceManager;
import net.minecraftforge.fml.common.eventhandler.Event;

import java.util.Map;
import java.util.function.Function;

public class ClientGuideEvent extends Event
{
    private final Map<String, GuideTitlePage> map;
    private final IResourceManager resourceManager;
    private final Function<String, GuideTitlePage> modGuideProvider;

    public ClientGuideEvent(Map<String, GuideTitlePage> m, IResourceManager r, Function<String, GuideTitlePage> f)
    {
        map = m;
        resourceManager = r;
        modGuideProvider = f;
    }

    public void add(GuideTitlePage page)
    {
        map.put(page.getName(), page);
    }

    public GuideTitlePage getModGuide(String modid)
    {
        return map.computeIfAbsent(modid, modGuideProvider);
    }

    public IResourceManager getResourceManager()
    {
        return resourceManager;
    }
}