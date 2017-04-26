package com.feed_the_beast.ftbl.api;

import com.feed_the_beast.ftbl.api.config.IConfigFileProvider;
import com.feed_the_beast.ftbl.api.config.IConfigKey;
import com.feed_the_beast.ftbl.api.config.IConfigValue;
import com.feed_the_beast.ftbl.api.config.IConfigValueProvider;
import com.feed_the_beast.ftbl.api.gui.IContainerProvider;
import com.feed_the_beast.ftbl.api.guide.IGuideTextLineProvider;
import net.minecraft.util.ResourceLocation;

/**
 * Created by LatvianModder on 15.11.2016.
 */
public interface IFTBLibRegistry
{
    void addConfigFileProvider(String id, IConfigFileProvider provider);

    void addConfigValueProvider(String id, IConfigValueProvider provider);

    IConfigKey addConfig(String group, String id, IConfigValue value);

    void addOptionalServerMod(String mod);

    void addNotification(INotification notification);

    void addGuiContainer(ResourceLocation id, IContainerProvider provider);

    void addInfoTextLine(String id, IGuideTextLineProvider provider);

    void addSyncData(String mod, ISyncData data);

    void addUniverseDataProvider(ResourceLocation id, IDataProvider<IUniverse> provider);

    void addPlayerDataProvider(ResourceLocation id, IDataProvider<IForgePlayer> provider);

    void addTeamDataProvider(ResourceLocation id, IDataProvider<IForgeTeam> provider);

    void addRankConfig(String id, IConfigValue defPlayer, IConfigValue defOP);
}