package com.feed_the_beast.ftbl.cmd;

import com.feed_the_beast.ftbl.FTBLibFinals;
import com.feed_the_beast.ftbl.FTBLibLang;
import com.feed_the_beast.ftbl.api.ForgePlayer;
import com.feed_the_beast.ftbl.api.ForgePlayerMP;
import com.feed_the_beast.ftbl.api.ForgeTeam;
import com.feed_the_beast.ftbl.api.ForgeWorldMP;
import com.feed_the_beast.ftbl.api.cmd.CommandLM;
import com.feed_the_beast.ftbl.api.cmd.CommandSubBase;
import com.feed_the_beast.ftbl.api.config.ConfigContainer;
import com.feed_the_beast.ftbl.api.config.ConfigGroup;
import com.feed_the_beast.ftbl.api.events.ForgeTeamEvent;
import com.feed_the_beast.ftbl.api.info.InfoPage;
import com.feed_the_beast.ftbl.net.MessageUpdateTeam;
import com.feed_the_beast.ftbl.util.FTBLib;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.common.MinecraftForge;

/**
 * Created by LatvianModder on 29.05.2016.
 */
public class CmdTeam extends CommandSubBase
{
    public static class CmdCreate extends CommandLM
    {
        public CmdCreate()
        {
            super("create");
        }

        @Override
        public int getRequiredPermissionLevel()
        {
            return 0;
        }

        @Override
        public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
        {
            EntityPlayerMP ep = getCommandSenderAsPlayer(sender);
            ForgePlayerMP p = ForgePlayerMP.get(ep);

            if(p.hasTeam())
            {
                throw FTBLibLang.team_must_leave.commandError();
            }

            ForgeTeam team = ForgeWorldMP.inst.newTeam();
            team.changeOwner(p);
            ForgeWorldMP.inst.teams.put(team.teamID, team);

            MinecraftForge.EVENT_BUS.post(new ForgeTeamEvent.Created(team));
            MinecraftForge.EVENT_BUS.post(new ForgeTeamEvent.PlayerJoined(team, p));

            new MessageUpdateTeam(p, team).sendTo(null);

            FTBLibLang.team_created.printChat(sender, String.valueOf(team.teamID));
        }
    }

    public static class CmdLeave extends CommandLM
    {
        public CmdLeave()
        {
            super("leave");
        }

        @Override
        public int getRequiredPermissionLevel()
        {
            return 0;
        }

        @Override
        public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
        {
            EntityPlayerMP ep = getCommandSenderAsPlayer(sender);
            ForgePlayerMP p = ForgePlayerMP.get(ep);

            if(!p.hasTeam())
            {
                throw FTBLibLang.team_no_team.commandError();
            }

            ForgeTeam team = p.getTeam();

            if(team.getMembers().size() == 1)
            {
                int teamID = team.teamID;
                MinecraftForge.EVENT_BUS.post(new ForgeTeamEvent.Deleted(team));
                team.removePlayer(p);
                ForgeWorldMP.inst.teams.remove(teamID);
                new MessageUpdateTeam(teamID).sendTo(null);

                FTBLibLang.team_member_left.printChat(sender, p.getProfile().getName());
                FTBLibLang.team_deleted.printChat(sender, team.getTitle());
            }
            else
            {
                if(team.getStatus(p).isOwner())
                {
                    throw FTBLibLang.team_must_transfer_ownership.commandError();
                }

                team.removePlayer(p);
                FTBLibLang.team_member_left.printChat(sender, p.getProfile().getName());
            }

            p.sendUpdate();
        }
    }

    public static class CmdKick extends CommandLM
    {
        public CmdKick()
        {
            super("kick");
        }

        @Override
        public int getRequiredPermissionLevel()
        {
            return 0;
        }

        @Override
        public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
        {
            EntityPlayerMP ep = getCommandSenderAsPlayer(sender);
            ForgePlayerMP p = ForgePlayerMP.get(ep);

            if(!p.hasTeam())
            {
                throw FTBLibLang.team_no_team.commandError();
            }

            ForgeTeam team = p.getTeam();

            if(!team.getStatus(p).isOwner())
            {
                throw FTBLibLang.team_not_owner.commandError();
            }

            checkArgs(args, 1);

            ForgePlayerMP p1 = ForgePlayerMP.get(args[0]);

            if(p1.getTeamID() != team.teamID)
            {
                throw FTBLibLang.team_not_member.commandError();
            }

            if(team.getMembers().size() > 1 && !p1.equalsPlayer(p))
            {
                MinecraftForge.EVENT_BUS.post(new ForgeTeamEvent.Deleted(team));
                team.removePlayer(p);
                ForgeWorldMP.inst.teams.remove(team.teamID);
                new MessageUpdateTeam(team.teamID).sendTo(null);

                FTBLibLang.team_member_left.printChat(sender, p.getProfile().getName());
                FTBLibLang.team_deleted.printChat(sender, team.getTitle());
            }
            else
            {
                throw FTBLibLang.team_must_transfer_ownership.commandError();
            }

            p.sendUpdate();
        }
    }

    public static class CmdTransferOwnership extends CommandLM
    {
        public CmdTransferOwnership()
        {
            super("transfer_ownership");
        }

        @Override
        public int getRequiredPermissionLevel()
        {
            return 0;
        }

        @Override
        public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
        {
            EntityPlayerMP ep = getCommandSenderAsPlayer(sender);
            ForgePlayerMP p = ForgePlayerMP.get(ep);

            if(!p.hasTeam())
            {
                throw FTBLibLang.team_no_team.commandError();
            }

            ForgeTeam team = p.getTeam();

            if(!team.getStatus(p).isOwner())
            {
                throw FTBLibLang.team_not_owner.commandError();
            }

            checkArgs(args, 1);

            ForgePlayerMP p1 = ForgePlayerMP.get(args[0]);

            if(p1.getTeamID() != p.getTeamID())
            {
                throw FTBLibLang.team_not_owner.commandError();
            }

            team.changeOwner(p1);
            FTBLibLang.team_transfered_ownership.printChat(sender, p1.getProfile().getName());

            new MessageUpdateTeam(p, team).sendTo(null);
        }
    }

    public static class CmdConfig extends CmdEditConfigBase
    {
        public CmdConfig()
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
            ForgePlayerMP p = ForgePlayerMP.get(ep);

            if(!p.hasTeam())
            {
                throw FTBLibLang.team_no_team.commandError();
            }

            final ForgeTeam team = p.getTeam();

            if(!team.getStatus(p).isOwner())
            {
                throw FTBLibLang.team_not_owner.commandError();
            }

            final ConfigGroup group = new ConfigGroup();
            team.getSettings(group);

            return new ConfigContainer(new ResourceLocation(FTBLibFinals.MOD_ID, "team_config"))
            {
                @Override
                public ConfigGroup createGroup()
                {
                    return group;
                }

                @Override
                public ITextComponent getConfigTitle() //TODO: Lang
                {
                    return new TextComponentString("Team Config");
                }

                @Override
                public void saveConfig(EntityPlayer player, NBTTagCompound nbt, ConfigGroup config)
                {
                    group.loadFromGroup(config, false);

                    for(EntityPlayerMP ep : FTBLib.getServer().getPlayerList().getPlayerList())
                    {
                        new MessageUpdateTeam(ForgeWorldMP.inst.getPlayer(ep), team).sendTo(null);
                    }
                }
            };
        }
    }

    public static class CmdListTeams extends CommandLM
    {
        public CmdListTeams()
        {
            super("list");
        }

        @Override
        public int getRequiredPermissionLevel()
        {
            return 0;
        }

        @Override
        public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
        {
            EntityPlayerMP ep = getCommandSenderAsPlayer(sender);

            InfoPage page = new InfoPage("teams");

            for(ForgeTeam team : ForgeWorldMP.inst.teams.valueCollection())
            {
                InfoPage page1 = page.getSub(String.valueOf(team.teamID));

                ITextComponent title = new TextComponentString(team.getTitle());
                title.getStyle().setColor(team.getColor().textFormatting);
                page1.setTitle(title);

                if(team.getDesc() != null)
                {
                    page1.printlnText(team.getDesc());
                    page1.text.add(null);
                }

                for(ForgePlayer player : team.getMembers())
                {
                    page1.printlnText(player.getProfile().getName());
                }
            }

            page.displayGuide(ep);
        }
    }

    public CmdTeam()
    {
        super("team");

        add(new CmdCreate());
        add(new CmdLeave());
        add(new CmdConfig());
        add(new CmdTransferOwnership());
        add(new CmdListTeams());
        add(new CmdKick());
    }

    @Override
    public int getRequiredPermissionLevel()
    {
        return 0;
    }
}