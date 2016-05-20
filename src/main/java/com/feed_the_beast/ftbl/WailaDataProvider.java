package com.feed_the_beast.ftbl;

import com.feed_the_beast.ftbl.api.tile.IInfoTile;
import com.feed_the_beast.ftbl.api.tile.TileInfoDataAccessor;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.IWailaDataProvider;
import mcp.mobius.waila.api.IWailaRegistrar;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;

public class WailaDataProvider implements IWailaDataProvider
{
    private static TileInfoDataAccessor getData(IWailaDataAccessor i)
    {
        TileInfoDataAccessor.inst.player = i.getPlayer();
        TileInfoDataAccessor.inst.world = i.getWorld();
        TileInfoDataAccessor.inst.hit = i.getMOP();
        TileInfoDataAccessor.inst.state = i.getBlockState();
        return TileInfoDataAccessor.inst;
    }

    public static void registerHandlers(IWailaRegistrar i)
    {
        i.registerBodyProvider(new WailaDataProvider(), IInfoTile.class);
        i.registerBodyProvider(new WailaDataProvider(), IInfoTile.Stack.class);
    }

    @Override
    public ItemStack getWailaStack(IWailaDataAccessor data, IWailaConfigHandler config)
    {
        TileEntity te = data.getTileEntity();

        if(te instanceof IInfoTile.Stack)
        {
            return ((IInfoTile.Stack) te).getInfoItem(getData(data));
        }

        return null;
    }

    @Override
    public List<String> getWailaHead(ItemStack is, List<String> l, IWailaDataAccessor data, IWailaConfigHandler config)
    {
        return l;
    }

    @Override
    public List<String> getWailaBody(ItemStack is, List<String> l, IWailaDataAccessor data, IWailaConfigHandler config)
    {
        TileEntity te = data.getTileEntity();

        if(te instanceof IInfoTile.Stack)
        {
            ((IInfoTile) te).getInfo(getData(data), l, false);
        }

        return l;
    }

    @Override
    public List<String> getWailaTail(ItemStack is, List<String> l, IWailaDataAccessor data, IWailaConfigHandler config)
    {
        return l;
    }

    @Override
    public NBTTagCompound getNBTData(EntityPlayerMP entityPlayerMP, TileEntity tileEntity, NBTTagCompound nbtTagCompound, World world, BlockPos blockPos)
    {
        return null;
    }
}