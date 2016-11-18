package com.feed_the_beast.ftbl.cmd.team;

import com.feed_the_beast.ftbl.api.IForgePlayer;
import com.feed_the_beast.ftbl.api.IForgeTeam;
import com.feed_the_beast.ftbl.lib.cmd.CommandLM;
import com.feed_the_beast.ftbl.lib.internal.FTBLibLang;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.server.command.CommandTreeBase;

/**
 * Created by LatvianModder on 11.11.2016.
 */
public class CmdSpecialPerm extends CommandTreeBase
{
    private final String ID, perm, managePerm;

    public CmdSpecialPerm(String id, String p, String mp)
    {
        ID = id;
        perm = p;
        managePerm = mp;

        addSubcommand(new CmdAdd());
        addSubcommand(new CmdRemove());
    }

    @Override
    public String getCommandName()
    {
        return ID;
    }

    @Override
    public int getRequiredPermissionLevel()
    {
        return 0;
    }

    @Override
    public boolean checkPermission(MinecraftServer server, ICommandSender sender)
    {
        return true;
    }

    @Override
    public String getCommandUsage(ICommandSender sender)
    {
        return "/ftb team " + ID + " <add|rem> <player>";
    }

    public class CmdAdd extends CommandLM
    {
        @Override
        public String getCommandName()
        {
            return "add";
        }

        @Override
        public boolean isUsernameIndex(String[] args, int i)
        {
            return i == 0;
        }

        @Override
        public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
        {
            EntityPlayerMP ep = getCommandSenderAsPlayer(sender);
            IForgePlayer p = getForgePlayer(ep);
            IForgeTeam team = p.getTeam();

            if(team == null)
            {
                throw FTBLibLang.TEAM_NO_TEAM.commandError();
            }
            else if(!team.hasPermission(p.getProfile().getId(), managePerm))
            {
                throw FTBLibLang.COMMAND_PERMISSION.commandError();
            }

            checkArgs(args, 1, "<player>");
            IForgePlayer p1 = getForgePlayer(args[0]);

            if(p1.equals(team.getOwner()))
            {
                throw FTBLibLang.TEAM_PERMISSION_OWNER.commandError();
            }
            else if(team.setHasPermission(p1.getProfile().getId(), perm, true))
            {
                FTBLibLang.TEAM_PERMISSION_SET.printChat(sender, perm, args[0], true);
            }
            else
            {
                FTBLibLang.TEAM_PERMISSION_ALREADY_SET.printChat(sender, perm, args[0], true);
            }
        }
    }

    public class CmdRemove extends CommandLM
    {
        @Override
        public String getCommandName()
        {
            return "remove";
        }

        @Override
        public boolean isUsernameIndex(String[] args, int i)
        {
            return i == 0;
        }

        @Override
        public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
        {
            EntityPlayerMP ep = getCommandSenderAsPlayer(sender);
            IForgePlayer p = getForgePlayer(ep);
            IForgeTeam team = p.getTeam();

            if(team == null)
            {
                throw FTBLibLang.TEAM_NO_TEAM.commandError();
            }
            else if(!team.hasPermission(p.getProfile().getId(), managePerm))
            {
                throw FTBLibLang.COMMAND_PERMISSION.commandError();
            }

            checkArgs(args, 1, "<player>");
            IForgePlayer p1 = getForgePlayer(args[0]);

            if(p1.equals(team.getOwner()))
            {
                throw FTBLibLang.TEAM_PERMISSION_OWNER.commandError();
            }
            else if(team.setHasPermission(p1.getProfile().getId(), perm, false))
            {
                FTBLibLang.TEAM_PERMISSION_SET.printChat(sender, perm, args[0], false);
            }
            else
            {
                FTBLibLang.TEAM_PERMISSION_ALREADY_SET.printChat(sender, perm, args[0], false);
            }
        }
    }
}
