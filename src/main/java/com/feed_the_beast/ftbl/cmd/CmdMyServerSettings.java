package com.feed_the_beast.ftbl.cmd;

import com.feed_the_beast.ftbl.FTBLibFinals;
import com.feed_the_beast.ftbl.api.ForgePlayerMP;
import com.feed_the_beast.ftbl.api.cmd.CmdEditConfigBase;
import com.feed_the_beast.ftbl.api.config.ConfigContainer;
import com.feed_the_beast.ftbl.api.config.ConfigGroup;
import com.google.gson.JsonObject;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;

/**
 * Created by LatvianModder on 29.05.2016.
 */
public class CmdMyServerSettings extends CmdEditConfigBase
{
    public static class MyServerSettingsContainer extends ConfigContainer
    {
        public final ForgePlayerMP player;
        public final ConfigGroup group;

        public MyServerSettingsContainer(ForgePlayerMP p)
        {
            super(new ResourceLocation(FTBLibFinals.MOD_ID, "my_server_settings"));
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
    public ConfigContainer getConfigContainer(ICommandSender sender) throws CommandException
    {
        EntityPlayerMP ep = getCommandSenderAsPlayer(sender);
        ForgePlayerMP p = ForgePlayerMP.get(ep);
        return new MyServerSettingsContainer(p);
    }
}