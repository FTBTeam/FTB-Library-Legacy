package com.feed_the_beast.ftbl.cmd;

import com.feed_the_beast.ftbl.api.FTBLibAPI;
import com.feed_the_beast.ftbl.api.INotification;
import com.feed_the_beast.ftbl.api.cmd.CommandLM;
import com.feed_the_beast.ftbl.api_impl.Notification;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CmdNotify extends CommandLM
{
    public static class CustomNotifications
    {
        private static final Map<ResourceLocation, INotification> MAP = new HashMap<>();

        public void loadFromJson(JsonObject o)
        {
            MAP.clear();

            for(Map.Entry<String, JsonElement> entry : o.entrySet())
            {
                ResourceLocation key = new ResourceLocation(entry.getKey());
                int id = FTBLibAPI.get().getRegistries().notifications().getOrCreateIDFromKey(key);
                Notification n = new Notification(id);

                if(entry.getValue().isJsonObject())
                {
                    //FIXME: JsonObject custom notification loading
                }
                else
                {
                    n.addText(new TextComponentString(entry.getValue().getAsString()));
                }

                MAP.put(key, n);
            }
        }
    }

    public CmdNotify()
    {
        super("notify");
    }

    @Override
    public int getRequiredPermissionLevel()
    {
        return 0;
    }

    @Nonnull
    @Override
    public String getCommandUsage(@Nonnull ICommandSender ics)
    {
        return "/" + commandName + " <player> <id>";
    }

    @Nonnull
    @Override
    public List<String> getTabCompletionOptions(MinecraftServer server, ICommandSender sender, String[] args, BlockPos pos)
    {
        if(args.length == 2)
        {
            return getListOfStringsMatchingLastWord(args, CustomNotifications.MAP.keySet());
        }

        return super.getTabCompletionOptions(server, sender, args, pos);
    }

    @Override
    public boolean isUsernameIndex(String[] args, int i)
    {
        return i == 0;
    }

    @Override
    public void execute(@Nonnull MinecraftServer server, @Nonnull ICommandSender ics, @Nonnull String[] args) throws CommandException
    {
        checkArgs(args, 2, "<player> <id>");
        EntityPlayerMP player = getPlayer(server, ics, args[0]);
        INotification n = CustomNotifications.MAP.get(new ResourceLocation(args[1]));

        if(n == null)
        {
            throw new IllegalArgumentException("Notification '" + args[1] + "' not found!");
        }

        FTBLibAPI.get().sendNotification(player, n);
    }
}