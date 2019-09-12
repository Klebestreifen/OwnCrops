package io.klebe.owncrops.common;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;

public class Util {

	public static EnumActionResult onPlantableItemUse(IPlantable seed, EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		ItemStack handStack = player.getHeldItem(hand);
		IBlockState target = worldIn.getBlockState(pos);
		
		if(		facing == EnumFacing.UP && 
				player.canPlayerEdit(pos.offset(facing), facing, handStack) &&
				target.getBlock().canSustainPlant(target, worldIn, pos, EnumFacing.UP, seed) &&
				worldIn.isAirBlock(pos.up())) {
			worldIn.setBlockState(pos.up(), seed.getPlant(worldIn, pos));
			handStack.shrink(1);
			return EnumActionResult.SUCCESS;
		}
		
		return EnumActionResult.FAIL;
	}
}
