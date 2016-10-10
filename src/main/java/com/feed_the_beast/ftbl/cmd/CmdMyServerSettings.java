package com.feed_the_beast.ftbl.cmd;

import com.feed_the_beast.ftbl.FTBLibIntegrationInternal;
import com.feed_the_beast.ftbl.api.IForgePlayer;
import com.feed_the_beast.ftbl.api.config.IConfigContainer;
import com.feed_the_beast.ftbl.api.config.IConfigTree;
import com.feed_the_beast.ftbl.lib.cmd.CmdEditConfigBase;
import com.feed_the_beast.ftbl.lib.config.ConfigTree;
import com.google.gson.JsonObject;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;

import javax.annotation.Nullable;

/**
 * Created by LatvianModder on 29.05.2016.
 */
public class CmdMyServerSettings extends CmdEditConfigBase
{
    private static class MyServerSettingsContainer implements IConfigContainer
    {
        public final IConfigTree tree;

        private MyServerSettingsContainer(IForgePlayer p)
        {
            tree = new ConfigTree();
            p.getSettings(tree);
        }

        @Override
        public IConfigTree getConfigTree()
        {
            return tree;
        }

        @Override
        public ITextComponent getTitle() //TODO: Lang
        {
            return new TextComponentString("My Server Settings");
        }

        @Override
        public void saveConfig(ICommandSender sender, @Nullable NBTTagCompound nbt, JsonObject json)
        {
            tree.fromJson(json);
        }
    }

    @Override
    public String getCommandName()
    {
        return "my_settings";
    }

    @Override
    public int getRequiredPermissionLevel()
    {
        return 0;
    }

    @Override
    public IConfigContainer getConfigContainer(ICommandSender sender) throws CommandException
    {
        return new MyServerSettingsContainer(FTBLibIntegrationInternal.API.getForgePlayer(getCommandSenderAsPlayer(sender)));
    }
}