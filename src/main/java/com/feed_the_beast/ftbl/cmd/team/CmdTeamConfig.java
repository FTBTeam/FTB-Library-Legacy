package com.feed_the_beast.ftbl.cmd.team;

import com.feed_the_beast.ftbl.FTBLibFinals;
import com.feed_the_beast.ftbl.FTBLibLang;
import com.feed_the_beast.ftbl.api.ForgePlayerMP;
import com.feed_the_beast.ftbl.api.ForgeTeam;
import com.feed_the_beast.ftbl.api.ForgeWorldMP;
import com.feed_the_beast.ftbl.api.cmd.CmdEditConfigBase;
import com.feed_the_beast.ftbl.api.config.ConfigContainer;
import com.feed_the_beast.ftbl.api.config.ConfigGroup;
import com.feed_the_beast.ftbl.net.MessageUpdateTeam;
import com.feed_the_beast.ftbl.util.FTBLib;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
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

        public TeamConfigContainer(ForgeTeam t)
        {
            super(new ResourceLocation(FTBLibFinals.MOD_ID, "team_config"));
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
            return FTBLibLang.team_config.textComponent(team.getID());
        }

        @Override
        public void saveConfig(ICommandSender sender, NBTTagCompound nbt, ConfigGroup config)
        {
            group.loadFromGroup(config);

            for(EntityPlayerMP ep : FTBLib.getServer().getPlayerList().getPlayerList())
            {
                new MessageUpdateTeam(ForgeWorldMP.inst.getPlayer(ep), team).sendTo(null);
            }
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

        return new TeamConfigContainer(team);
    }
}
