package com.feed_the_beast.ftbl.api;

import com.feed_the_beast.ftbl.api.config.IConfigKey;
import com.feed_the_beast.ftbl.api.config.IConfigValue;
import com.feed_the_beast.ftbl.api.gui.IGuiProvider;
import net.minecraft.util.ResourceLocation;

/**
 * Created by LatvianModder on 15.11.2016.
 */
public interface IFTBLibClientRegistry
{
    IConfigKey addClientConfig(String group, String id, IConfigValue value);

    void addGui(ResourceLocation id, IGuiProvider provider);
}