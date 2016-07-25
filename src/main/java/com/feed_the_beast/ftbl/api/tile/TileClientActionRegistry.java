package com.feed_the_beast.ftbl.api.tile;

import com.feed_the_beast.ftbl.FTBLibFinals;
import com.feed_the_beast.ftbl.api.MouseButton;
import com.feed_the_beast.ftbl.api.RegistryBase;
import net.minecraft.util.ResourceLocation;

/**
 * Created by LatvianModder on 14.05.2016.
 */
public class TileClientActionRegistry
{
    public static final RegistryBase<ResourceLocation, ITileClientAction> INSTANCE = new RegistryBase<>(0);
    public static final ResourceLocation BUTTON_PRESSED = new ResourceLocation(FTBLibFinals.MOD_ID, "button");

    public static void init()
    {
        INSTANCE.register(BUTTON_PRESSED, (te, data, player) ->
        {
            if(te instanceof ITileButtonPressed)
            {
                ((ITileButtonPressed) te).handleButton(player, data.getInteger("I"), MouseButton.get(data.getByte("M")), data.getCompoundTag("D"));
            }
        });
    }
}