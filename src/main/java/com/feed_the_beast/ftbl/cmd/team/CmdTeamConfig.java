package com.feed_the_beast.ftbl.cmd.team;

import com.feed_the_beast.ftbl.api.IForgePlayer;
import com.feed_the_beast.ftbl.api.IForgeTeam;
import com.feed_the_beast.ftbl.api.config.IConfigContainer;
import com.feed_the_beast.ftbl.lib.cmd.CmdEditConfigBase;
import com.feed_the_beast.ftbl.lib.config.BasicConfigContainer;
import com.feed_the_beast.ftbl.lib.internal.FTBLibLang;
import com.feed_the_beast.ftbl.lib.internal.FTBLibPerms;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;

/**
 * Created by LatvianModder on 20.06.2016.
 */
public class CmdTeamConfig extends CmdEditConfigBase
{
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
        IForgePlayer p = getForgePlayer(ep);
        IForgeTeam team = p.getTeam();

        if(team == null)
        {
            throw FTBLibLang.TEAM_NO_TEAM.commandError();
        }
        else if(!team.hasPermission(p.getProfile().getId(), FTBLibPerms.TEAM_EDIT_SETTINGS))
        {
            throw FTBLibLang.COMMAND_PERMISSION.commandError();
        }

        return new BasicConfigContainer(FTBLibLang.TEAM_CONFIG.textComponent(team.getName()), team.getSettings());
    }
}