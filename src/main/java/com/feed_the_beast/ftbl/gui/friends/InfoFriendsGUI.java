package com.feed_the_beast.ftbl.gui.friends;

import com.feed_the_beast.ftbl.api.info.impl.InfoPage;
import com.feed_the_beast.ftbl.api.info.impl.InfoPageTheme;
import com.feed_the_beast.ftbl.client.ClientNotifications;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Created by LatvianModder on 23.03.2016.
 */
@SideOnly(Side.CLIENT)
public class InfoFriendsGUI extends InfoPage
{
    public InfoFriendsGUI()
    {
        super("friends_gui");
        setTitle(new TextComponentString("FriendsGUI"));
        theme = InfoPageTheme.DARK_NON_UNICODE;

        if(!ClientNotifications.Perm.map.isEmpty())
        {
            addSub(new InfoNotificationsPage());
        }
        
        /*

        List<ForgePlayer> tempPlayerList = new ArrayList<>();
        tempPlayerList.addAll(ForgeWorldSP.inst.playerMap.values());

        tempPlayerList.remove(ForgeWorldSP.inst.clientPlayer);

        //if(FTBUClient.sort_friends_az.getMode()) Collections.sort(tempPlayerList, LMPNameComparator.instance);
        //else Collections.sort(tempPlayerList, new LMPStatusComparator(LMWorldClient.inst.clientPlayer));

        Collections.sort(tempPlayerList, ForgePlayerNameComparator.INSTANCE);

        addSub(ForgeWorldSP.inst.clientPlayer.getProfile().getName(), new InfoFriendsGUISelfPage());

        for(ForgePlayer p : tempPlayerList)
        {
            addSub(p.getProfile().getName(), new InfoFriendsGUIPage(p.toSP()));
        }
        */
    }
}