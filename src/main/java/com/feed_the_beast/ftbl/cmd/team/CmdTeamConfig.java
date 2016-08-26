package com.feed_the_beast.ftbl.cmd.team;

import com.feed_the_beast.ftbl.FTBLibLang;
import com.feed_the_beast.ftbl.api.cmd.CmdEditConfigBase;
import com.feed_the_beast.ftbl.api.config.ConfigContainer;
import com.feed_the_beast.ftbl.api.config.ConfigGroup;
import com.feed_the_beast.ftbl.api_impl.ForgePlayer;
import com.feed_the_beast.ftbl.api_impl.ForgeTeam;
import com.google.gson.JsonObject;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.ITextComponent;

/**
 * Created by LatvianModder on 20.06.2016.
 */
public class CmdTeamConfig extends CmdEditConfigBase
{
    public static class TeamConfigContainer extends ConfigContainer
    {
        public final ForgeTeam team;
        public final ConfigGroup group;

        //new ResourceLocation(FTBLibFinals.MOD_ID, "team_config")
        public TeamConfigContainer(ForgeTeam t)
        {
            team = t;
            group = new ConfigGroup();
            team.getSettings(group);
        }

        @Override
        public ConfigGroup createGroup()
        {
            return group;
        }

        @Override
        public ITextComponent getConfigTitle()
        {
            return FTBLibLang.team_config.textComponent(team.getName());
        }

        @Override
        public void saveConfig(ICommandSender sender, NBTTagCompound nbt, JsonObject json)
        {
            group.loadFromGroup(json);
        }
    }

    public CmdTeamConfig()
    {
        super("config");
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
        ForgePlayer p = getForgePlayer(ep);
        ForgeTeam team = p.getTeam();

        if(team == null)
        {
            throw FTBLibLang.team_no_team.commandError();
        }
        else if(!team.getStatus(p).isOwner())
        {
            throw FTBLibLang.team_not_owner.commandError();
        }

        return new TeamConfigContainer(team);
    }
}
