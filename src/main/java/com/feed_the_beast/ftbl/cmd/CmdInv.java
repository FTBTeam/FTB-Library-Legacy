package com.feed_the_beast.ftbl.cmd;

import com.feed_the_beast.ftbl.FTBLibLang;
import com.feed_the_beast.ftbl.api.cmd.CommandLM;
import com.feed_the_beast.ftbl.api.cmd.CommandSubBase;
import com.feed_the_beast.ftbl.api.item.LMInvUtils;
import com.feed_the_beast.ftbl.util.FTBLib;
import com.feed_the_beast.ftbl.util.LMNBTUtils;
import com.feed_the_beast.ftbl.util.OtherMods;
import com.latmod.lib.util.LMUtils;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.Loader;

import javax.annotation.Nonnull;
import java.io.File;

public class CmdInv extends CommandSubBase
{
    public static class CmdSave extends CommandLM
    {
        public CmdSave(String s)
        {
            super(s);
        }

        @Nonnull
        @Override
        public String getCommandUsage(@Nonnull ICommandSender ics)
        {
            return '/' + commandName + " <player> <file_id>";
        }

        @Override
        public boolean isUsernameIndex(String[] args, int i)
        {
            return i == 0;
        }

        @Override
        public void execute(@Nonnull MinecraftServer server, @Nonnull ICommandSender ics, @Nonnull String[] args) throws CommandException
        {
            checkArgs(args, 2);
            EntityPlayerMP ep = getPlayer(server, ics, args[0]);
            File file = new File(FTBLib.folderLocal, "ftbu/playerinvs/" + LMUtils.fromUUID(ep.getGameProfile().getId()) + "_" + args[1].toLowerCase() + ".dat");

            try
            {
                onInvCmd(file, ep);
            }
            catch(Exception e)
            {
                if(FTBLib.DEV_ENV)
                {
                    e.printStackTrace();
                }
                throw FTBLibLang.raw.commandError("Failed to load inventory!");
            }
        }

        protected void onInvCmd(File file, EntityPlayerMP ep) throws Exception
        {
            NBTTagCompound tag = new NBTTagCompound();
            LMInvUtils.writeItemsToNBT(ep.inventory, tag, "Inventory");

            if(Loader.isModLoaded(OtherMods.BAUBLES))
            {
                LMInvUtils.writeItemsToNBT(LMInvUtils.getBaubles(ep), tag, "Baubles");
            }

            LMNBTUtils.writeTag(file, tag);
        }
    }

    public static class CmdLoad extends CmdSave
    {
        public CmdLoad(String s)
        {
            super(s);
        }

        @Override
        protected void onInvCmd(File file, EntityPlayerMP ep) throws Exception
        {
            NBTTagCompound tag = LMNBTUtils.readTag(file);

            LMInvUtils.readItemsFromNBT(ep.inventory, tag, "Inventory");

            if(Loader.isModLoaded(OtherMods.BAUBLES))
            {
                LMInvUtils.readItemsFromNBT(LMInvUtils.getBaubles(ep), tag, "Baubles");
            }
        }
    }

    public static class CmdList extends CommandLM
    {
        public CmdList(String s)
        {
            super(s);
        }

        @Override
        public void execute(@Nonnull MinecraftServer server, @Nonnull ICommandSender ics, @Nonnull String[] args) throws CommandException
        {
        }
    }

    public CmdInv()
    {
        super("ftb_inv");
    }
}