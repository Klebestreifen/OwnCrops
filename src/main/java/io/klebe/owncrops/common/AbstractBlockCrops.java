package io.klebe.owncrops.common;

import java.util.List;

import org.apache.logging.log4j.Level;

import io.klebe.owncrops.OwnCrops;
import net.minecraft.block.Block;
import net.minecraft.block.BlockCrops;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class AbstractBlockCrops extends BlockCrops{
	
	private Item seed = null;
	private Item drop = null;
	private String dropID = null;
	private int dropMeta = 0;
	
	public AbstractBlockCrops(String unlocalizedName, String dropID, int dropMeta) {
		super();
		super.setUnlocalizedName(unlocalizedName);
		super.setRegistryName(unlocalizedName);
		this.dropID = dropID;
		this.dropMeta = dropMeta;
	}
	
	public void setSeed(Item seed) {
		this.seed = seed;
	}
	
	public double getBBHeight(IBlockState state) {
		switch(super.getAge(state)) {
		case 0: return 0.125D;
		case 1: return 0.250D;
		case 2: return 0.375D;
		case 3: return 0.500D;
		case 4: return 0.625D;
		case 5: return 0.750D;
		case 6: return 0.875D;
		case 7: return 1.000D;
		}
		return 1.0D;
	}
	
	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		boolean parent = super.onBlockActivated(worldIn, pos, state, playerIn, hand, facing, hitX, hitY, hitZ);
		
		ItemStack handStack = playerIn.getHeldItem(hand);
		
		// If not remote and parent doesen't do anything and player can edit and crop is mature ... 
		if(		(!worldIn.isRemote) &&
				(!parent) &&
				playerIn.canPlayerEdit(pos.offset(facing), facing, handStack) &&
				super.isMaxAge(state)) { // then do:
			
			// get Drops
			NonNullList<ItemStack> drops = NonNullList.create();
			getDrops(drops, worldIn, pos, state, 0);
			
			// now drop this shit
			boolean replanted = false;
			for (ItemStack is : drops) {
				if((!replanted) && (is.getItem() == getSeed())) { // replant with first seed
					// replant
					worldIn.setBlockState(pos, this.withAge(0));
					replanted = true;
					
					continue; // without droping
				}
				// drop
				if(is.getItem() == getCrop()) {
					worldIn.spawnEntity(new EntityItem(worldIn, ((double)pos.getX())+0.5D, (double)pos.getY(), ((double)pos.getZ())+0.5D, new ItemStack(is.getItem(), is.getCount(), dropMeta)));
				} else {
					worldIn.spawnEntity(new EntityItem(worldIn, ((double)pos.getX())+0.5D, (double)pos.getY(), ((double)pos.getZ())+0.5D, is));
				}
			}
			return true;		
		}
		
		return false;
	}
	
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, getBBHeight(state), 1.0D);
	}
	
	@Override
	public Item getSeed() {
		return seed;
	}
	
	@Override
	public Item getCrop() {
		if(drop == null) {
			drop = Item.getByNameOrId(dropID);
		}
		return drop;
	}
}
