package com.feed_the_beast.ftbl.cmd.team;

import com.feed_the_beast.ftbl.FTBLibLang;
import com.feed_the_beast.ftbl.api.FTBLibAPI;
import com.feed_the_beast.ftbl.api.IForgePlayer;
import com.feed_the_beast.ftbl.api.IForgeTeam;
import com.feed_the_beast.ftbl.api.cmd.CommandLM;
import com.feed_the_beast.ftbl.api.info.impl.InfoPage;
import com.feed_the_beast.ftbl.api.info.impl.InfoPageTheme;
import com.latmod.lib.util.LMStringUtils;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by LatvianModder on 20.06.2016.
 */
public class CmdListTeams extends CommandLM
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

        InfoPage page = new InfoPage("teams").setTitle(new TextComponentString("Teams")); //TODO: Lang
        page.theme = InfoPageTheme.DARK_NON_UNICODE;

        for(IForgeTeam team : FTBLibAPI.get().getWorld().getTeams())
        {
            InfoPage page1 = page.getSub(team.getName());

            ITextComponent title = new TextComponentString(team.getTitle());
            title.getStyle().setColor(team.getColor().getTextFormatting());
            page1.setTitle(title);

            if(team.getDesc() != null)
            {
                ITextComponent desc = new TextComponentString(team.getDesc());
                desc.getStyle().setItalic(true);
                page1.println(desc);
                page1.println(null);
            }

            page1.println("ID: " + team.getName());
            IForgePlayer owner = team.getOwner();
            page1.println(FTBLibLang.OWNER.textComponent(owner.getProfile().getName()));

            List<String> members = new ArrayList<>();

            for(IForgePlayer player : team.getMembers())
            {
                if(player != owner)
                {
                    members.add(player.getProfile().getName());
                }
            }

            if(!members.isEmpty())
            {
                page1.println(null);
                page1.println("Members:"); //TODO: Lang

                Collections.sort(members, LMStringUtils.IGNORE_CASE_COMPARATOR);

                for(String s : members)
                {
                    page1.println(s);
                }
            }
        }

        page.displayGuide(ep);
    }
}
