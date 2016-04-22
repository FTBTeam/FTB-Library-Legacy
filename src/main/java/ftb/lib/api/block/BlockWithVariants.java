package ftb.lib.api.block;

import ftb.lib.FTBLib;
import ftb.lib.api.client.FTBLibClient;
import net.minecraft.block.material.*;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.*;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.*;
import net.minecraft.util.IStringSerializable;
import net.minecraftforge.fml.relauncher.*;

import java.util.List;

/**
 * Created by LatvianModder on 07.03.2016.
 */
public abstract class BlockWithVariants<T extends Enum<T> & BlockWithVariants.IVariantEnum> extends BlockLM
{
	public interface IVariantEnum extends IStringSerializable
	{
		int getMetadata();
		MapColor getMapColor();
	}
	
	private PropertyEnum<T> variantProperty;
	private final T[] variants = getVariantType().getEnumConstants();
	
	public BlockWithVariants(Material m)
	{
		super(m);
		variantProperty = getVariantProperty();
	}
	
	public abstract Class<T> getVariantType();
	
	public T getVariantFromMeta(int i)
	{ return (i < 0 || i >= variants.length) ? variants[0] : variants[i]; }
	
	public T getVariant(IBlockState state)
	{ return state.getValue(getVariantProperty()); }
	
	@Override
	public void onPostLoaded()
	{
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void loadModels()
	{
		Item item = getItem();
		
		for(T e : variants)
		{
			FTBLibClient.addItemModel(item, e.getMetadata(), FTBLib.getStateString(createBlockState().getBaseState().withProperty(getVariantProperty(), e)));
		}
	}
	
	@Override
	public String getUnlocalizedName(ItemStack stack)
	{ return getMod().getBlockName(getVariantFromMeta(stack.getMetadata()).getName()); }
	
	@Override
	@SideOnly(Side.CLIENT)
	public void getSubBlocks(Item itemIn, CreativeTabs tab, List<ItemStack> list)
	{
		for(T e : variants)
		{
			list.add(new ItemStack(itemIn, 1, e.getMetadata()));
		}
	}
	
	@Override
	public int damageDropped(IBlockState state)
	{ return state.getValue(getVariantProperty()).getMetadata(); }
	
	@Override
	public IBlockState getStateFromMeta(int meta)
	{ return getDefaultState().withProperty(getVariantProperty(), getVariantFromMeta(meta)); }
	
	@Override
	public MapColor getMapColor(IBlockState state)
	{ return state.getValue(getVariantProperty()).getMapColor(); }
	
	@Override
	public int getMetaFromState(IBlockState state)
	{ return state.getValue(getVariantProperty()).getMetadata(); }
	
	private PropertyEnum<T> getVariantProperty()
	{
		if(variantProperty != null) return variantProperty;
		variantProperty = PropertyEnum.create("variant", getVariantType());
		return variantProperty;
	}
	
	@Override
	protected BlockStateContainer createBlockState()
	{ return new BlockStateContainer(this, getVariantProperty()); }
}
