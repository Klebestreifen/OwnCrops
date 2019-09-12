package io.klebe.owncrops.common;

import io.klebe.owncrops.OwnCrops;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.IPlantable;

public class AbstractItemFoodPlantable extends AbstractItemFood implements IPlantable {
	
	private AbstractBlockCrops cropToPlant;

	public AbstractItemFoodPlantable(String unlocalizedName, int amount, float saturation, AbstractBlockCrops crop) {
		super(unlocalizedName, amount, saturation, false, false);
		cropToPlant = crop;
	}

	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		super.onItemUse(player, worldIn, pos, hand, facing, hitX, hitY, hitZ);
		
		return Util.onPlantableItemUse(this, player, worldIn, pos, hand, facing, hitX, hitY, hitZ);
	}
	
	@Override
	public EnumPlantType getPlantType(IBlockAccess world, BlockPos pos) {
		return EnumPlantType.Crop;
	}
	
	@Override
	public IBlockState getPlant(IBlockAccess world, BlockPos pos) {
		return cropToPlant.getDefaultState();
	}

}
