package com.feed_the_beast.ftbl.cmd;

import com.feed_the_beast.ftbl.api.cmd.CmdEditConfigBase;
import com.feed_the_beast.ftbl.api.config.ConfigGroup;
import com.feed_the_beast.ftbl.api.config.IConfigContainer;
import com.feed_the_beast.ftbl.api_impl.ForgePlayer;
import com.google.gson.JsonObject;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;

/**
 * Created by LatvianModder on 29.05.2016.
 */
public class CmdMyServerSettings extends CmdEditConfigBase
{
    public static class MyServerSettingsContainer implements IConfigContainer
    {
        public final ForgePlayer player;
        public final ConfigGroup group;

        //new ResourceLocation(FTBLibFinals.MOD_ID, "my_server_settings")
        public MyServerSettingsContainer(ForgePlayer p)
        {
            player = p;
            group = new ConfigGroup();
            p.getSettings(group);
        }

        @Override
        public ConfigGroup createGroup()
        {
            return group;
        }

        @Override
        public ITextComponent getConfigTitle() //TODO: Lang
        {
            return new TextComponentString("My Server Settings");
        }

        @Override
        public void saveConfig(ICommandSender sender, NBTTagCompound nbt, JsonObject json)
        {
            group.loadFromGroup(json);
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