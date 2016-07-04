package com.feed_the_beast.ftbl.cmd;

import com.feed_the_beast.ftbl.FTBLibLang;
import com.feed_the_beast.ftbl.api.ForgePlayerMP;
import com.feed_the_beast.ftbl.api.ForgeWorldMP;
import com.feed_the_beast.ftbl.api.cmd.CommandLM;
import com.latmod.lib.util.LMUtils;
import com.mojang.authlib.GameProfile;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;

import javax.annotation.Nonnull;
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

    @Nonnull
    @Override
    public String getCommandUsage(@Nonnull ICommandSender ics)
    {
        return '/' + commandName + " <player>";
    }

    @Override
    public boolean isUsernameIndex(String[] args, int i)
    {
        return i == 0;
    }

    @Override
    public void execute(@Nonnull MinecraftServer server, @Nonnull ICommandSender ics, @Nonnull String[] args) throws CommandException
    {
        checkArgs(args, 2, "<uuid> <name>");

        UUID id = LMUtils.fromString(args[0]);
        if(id == null)
        {
            throw FTBLibLang.raw.commandError("Invalid UUID!");
        }

        if(ForgeWorldMP.inst.getPlayer(id) != null || ForgeWorldMP.inst.getPlayer(args[1]) != null)
        {
            throw FTBLibLang.raw.commandError("Player already exists!");
        }

        ForgePlayerMP p = new ForgePlayerMP(new GameProfile(id, args[1]));
        ForgeWorldMP.inst.playerMap.put(p.getProfile().getId(), p);

        ics.addChatMessage(new TextComponentString("Fake player " + args[1] + " added!"));
    }
}
