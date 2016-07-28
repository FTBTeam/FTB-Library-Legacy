package com.feed_the_beast.ftbl.api.client.gui;

import com.feed_the_beast.ftbl.FTBLibMod;
import com.feed_the_beast.ftbl.api.RegistryBase;
import com.feed_the_beast.ftbl.net.MessageOpenGui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.Container;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public abstract class GuiHandler
{
    public static final RegistryBase<String, GuiHandler> REGISTRY = new RegistryBase<>(RegistryBase.ALLOW_OVERRIDES);

    public static TileEntity getTileEntity(IBlockAccess world, NBTTagCompound tag)
    {
        if(tag != null)
        {
            int[] pos = tag.getIntArray("Pos");

            if(pos.length == 3)
            {
                return world.getTileEntity(new BlockPos(pos[0], pos[1], pos[2]));
            }
        }

        return null;
    }

    public static NBTTagCompound getTileData(TileEntity te)
    {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setIntArray("Pos", new int[] {te.getPos().getX(), te.getPos().getY(), te.getPos().getZ()});
        return tag;
    }

    public static void openGui(@Nonnull String modID, @Nonnull EntityPlayer ep, int id, @Nullable NBTTagCompound data)
    {
        GuiHandler handler = REGISTRY.get(modID);

        if(ep.worldObj.isRemote)
        {
            FTBLibMod.proxy.openClientGui(handler, ep, id, data, ep.openContainer.windowId);
        }
        else
        {
            Container c = (handler != null) ? handler.getContainer(ep, id, data) : null;

            EntityPlayerMP epM = (EntityPlayerMP) ep;
            epM.getNextWindowId();
            epM.closeContainer();

            if(c != null)
            {
                epM.openContainer = c;
            }

            epM.openContainer.windowId = epM.currentWindowId;
            epM.openContainer.addListener(epM);
            new MessageOpenGui(modID, id, data, epM.currentWindowId).sendTo(epM);
        }
    }

    public abstract Container getContainer(@Nonnull EntityPlayer ep, int id, @Nullable NBTTagCompound data);

    @SideOnly(Side.CLIENT)
    public abstract GuiScreen getGui(@Nonnull EntityPlayer ep, int id, @Nullable NBTTagCompound data);
}