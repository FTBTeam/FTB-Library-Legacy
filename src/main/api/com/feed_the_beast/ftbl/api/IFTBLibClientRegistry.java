package com.feed_the_beast.ftbl.api;

import com.feed_the_beast.ftbl.api.config.IConfigKey;
import com.feed_the_beast.ftbl.api.config.IConfigValue;
import com.feed_the_beast.ftbl.api.gui.IGuiProvider;
import com.feed_the_beast.ftbl.api.gui.ISidebarButton;
import com.feed_the_beast.ftbl.lib.config.ConfigKey;
import net.minecraft.util.ResourceLocation;

/**
 * Created by LatvianModder on 15.11.2016.
 */
public interface IFTBLibClientRegistry
{
    void addClientConfig(IConfigKey key, IConfigValue value);

    default ConfigKey addClientConfig(String mod, String id, IConfigValue value, byte... flags)
    {
        ConfigKey key = new ConfigKey(mod.toLowerCase() + '.' + id, value.copy());

        for(byte b : flags)
        {
            key.addFlags(b);
        }

        addClientConfig(key, value);
        return key;
    }

    void addGui(ResourceLocation id, IGuiProvider provider);

    void addSidebarButton(ISidebarButton provider);
}