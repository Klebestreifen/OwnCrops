package io.klebe.owncrops.common;

import io.klebe.owncrops.OwnCrops;
import io.klebe.owncrops.items.CustomStick;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class AbstractItem extends Item implements IHasModel{

	public AbstractItem(String unlocalizedName) {
		super();
		super.setUnlocalizedName(unlocalizedName);
		super.setRegistryName(unlocalizedName);
		//super.setCreativeTab()
		
	}
	
	public void registerModels() {
		OwnCrops.getProxy().registerItemRenderer(this, 0);
	}
}
