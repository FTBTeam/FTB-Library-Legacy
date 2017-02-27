package com.feed_the_beast.ftbl.cmd;

import com.feed_the_beast.ftbl.api_impl.ForgePlayer;
import com.feed_the_beast.ftbl.api_impl.Universe;
import com.feed_the_beast.ftbl.lib.cmd.CommandLM;
import com.feed_the_beast.ftbl.lib.internal.FTBLibLang;
import com.feed_the_beast.ftbl.lib.util.LMStringUtils;
import com.mojang.authlib.GameProfile;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;

import java.util.UUID;

/**
 * Created by LatvianModder on 04.03.2016.
 */
public class CmdAddFakePlayer extends CommandLM
{
    @Override
    public String getCommandName()
    {
        return "add_fake_player";
    }

    @Override
    public String getCommandUsage(ICommandSender ics)
    {
        return '/' + getCommandName() + " <player>";
    }

    @Override
    public boolean isUsernameIndex(String[] args, int i)
    {
        return i == 0;
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
    {
        checkArgs(args, 2, "<uuid> <name>");

        UUID id = LMStringUtils.fromString(args[0]);
        if(id == null)
        {
            throw FTBLibLang.RAW.commandError("Invalid UUID!");
        }

        if(Universe.INSTANCE.getPlayer(id) != null || Universe.INSTANCE.getPlayer(args[1]) != null)
        {
            throw FTBLibLang.RAW.commandError("Player already exists!");
        }

        ForgePlayer p = new ForgePlayer(new GameProfile(id, args[1]));
        Universe.INSTANCE.playerMap.put(p.getId(), p);

        sender.addChatMessage(new TextComponentString("Fake player " + args[1] + " added!"));
    }
}
