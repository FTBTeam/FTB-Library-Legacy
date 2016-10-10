package com.feed_the_beast.ftbl.cmd.team;

import com.feed_the_beast.ftbl.FTBLibIntegrationInternal;
import com.feed_the_beast.ftbl.FTBLibLang;
import com.feed_the_beast.ftbl.api.EnumTeamStatus;
import com.feed_the_beast.ftbl.api.IForgePlayer;
import com.feed_the_beast.ftbl.api.IForgeTeam;
import com.feed_the_beast.ftbl.api.config.IConfigContainer;
import com.feed_the_beast.ftbl.api.config.IConfigTree;
import com.feed_the_beast.ftbl.lib.cmd.CmdEditConfigBase;
import com.feed_the_beast.ftbl.lib.config.ConfigTree;
import com.google.gson.JsonObject;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.ITextComponent;

import javax.annotation.Nullable;

/**
 * Created by LatvianModder on 20.06.2016.
 */
public class CmdTeamConfig extends CmdEditConfigBase
{
    private static class TeamConfigContainer implements IConfigContainer
    {
        private final ITextComponent title;
        private final IConfigTree tree;

        private TeamConfigContainer(IForgeTeam t)
        {
            title = FTBLibLang.TEAM_CONFIG.textComponent(t.getName());
            tree = new ConfigTree();
            t.getSettings(tree);
        }

        @Override
        public IConfigTree getConfigTree()
        {
            return tree;
        }

        @Override
        public ITextComponent getTitle()
        {
            return title;
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
        return "config";
    }

    @Override
    public int getRequiredPermissionLevel()
    {
        return 0;
    }

    @Override
    public IConfigContainer getConfigContainer(ICommandSender sender) throws CommandException
    {
        EntityPlayerMP ep = getCommandSenderAsPlayer(sender);
        IForgePlayer p = FTBLibIntegrationInternal.API.getForgePlayer(ep);
        IForgeTeam team = p.getTeam();

        if(team == null)
        {
            throw FTBLibLang.TEAM_NO_TEAM.commandError();
        }
        else if(!team.hasStatus(p, EnumTeamStatus.OWNER))
        {
            throw FTBLibLang.TEAM_NOT_OWNER.commandError();
        }

        return new TeamConfigContainer(team);
    }
}
