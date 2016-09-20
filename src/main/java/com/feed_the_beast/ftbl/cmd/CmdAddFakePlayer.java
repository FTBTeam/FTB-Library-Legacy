package com.feed_the_beast.ftbl.cmd;

import com.feed_the_beast.ftbl.FTBLibLang;
import com.feed_the_beast.ftbl.api.cmd.CommandLM;
import com.feed_the_beast.ftbl.api_impl.FTBLibAPI_Impl;
import com.feed_the_beast.ftbl.api_impl.ForgePlayer;
import com.feed_the_beast.ftbl.api_impl.Universe;
import com.latmod.lib.util.LMStringUtils;
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
    public CmdAddFakePlayer()
    {
        super("add_fake_player");
    }

    @Override
    public String getCommandUsage(ICommandSender ics)
    {
        return '/' + commandName + " <player>";
    }

    @Override
    public boolean isUsernameIndex(String[] args, int i)
    {
        return i == 0;
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender ics, String[] args) throws CommandException
    {
        checkArgs(args, 2, "<uuid> <name>");

        UUID id = LMStringUtils.fromString(args[0]);
        if(id == null)
        {
            throw FTBLibLang.RAW.commandError("Invalid UUID!");
        }

        Universe world = FTBLibAPI_Impl.INSTANCE.getUniverse();

        if(world.getPlayer(id) != null || world.getPlayer(args[1]) != null)
        {
            throw FTBLibLang.RAW.commandError("Player already exists!");
        }

        ForgePlayer p = new ForgePlayer(new GameProfile(id, args[1]));
        world.playerMap.put(p.getProfile().getId(), p);

        ics.addChatMessage(new TextComponentString("Fake player " + args[1] + " added!"));
    }
}
