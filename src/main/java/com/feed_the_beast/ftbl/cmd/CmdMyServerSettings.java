package com.feed_the_beast.ftbl.cmd;

import com.feed_the_beast.ftbl.api.cmd.CmdEditConfigBase;
import com.feed_the_beast.ftbl.api.config.IConfigContainer;
import com.feed_the_beast.ftbl.api.config.IConfigTree;
import com.feed_the_beast.ftbl.api.config.impl.ConfigTree;
import com.feed_the_beast.ftbl.api_impl.ForgePlayer;
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
    public static class MyServerSettingsContainer implements IConfigContainer
    {
        public final ForgePlayer player;
        public final IConfigTree tree;

        //new ResourceLocation(FTBLibFinals.MOD_ID, "my_server_settings")
        public MyServerSettingsContainer(ForgePlayer p)
        {
            player = p;
            tree = new ConfigTree();
            p.getSettings(tree);
        }

        @Override
        public IConfigTree createGroup()
        {
            return tree;
        }

        @Override
        public ITextComponent getConfigTitle() //TODO: Lang
        {
            return new TextComponentString("My Server Settings");
        }

        @Override
        public void saveConfig(ICommandSender sender, @Nullable NBTTagCompound nbt, JsonObject json)
        {
            tree.fromJson(json);
        }
    }

    public CmdMyServerSettings()
    {
        super("my_settings");
    }

    @Override
    public int getRequiredPermissionLevel()
    {
        return 0;
    }

    @Override
    public IConfigContainer getConfigContainer(ICommandSender sender) throws CommandException
    {
        return new MyServerSettingsContainer(getForgePlayer(getCommandSenderAsPlayer(sender)));
    }
}