package io.klebe.owncrops.common;

import io.klebe.owncrops.OwnCrops;
import net.minecraft.item.ItemFood;

public class AbstractItemFood extends ItemFood implements IHasModel{

	public AbstractItemFood(String unlocalizedName) {
		this(unlocalizedName, 0, 0, true);
	}
	
	public AbstractItemFood(String unlocalizedName, int amount, float saturation, boolean isWolfFood) {
		this(unlocalizedName, amount, saturation, isWolfFood, false);
	}
	
	public AbstractItemFood(String unlocalizedName, int amount, float saturation, boolean isWolfFood, boolean alwaysEdible) {
		super(amount, saturation, isWolfFood);
		
		super.setUnlocalizedName(unlocalizedName);
		super.setRegistryName(unlocalizedName);
		
		if(alwaysEdible) super.setAlwaysEdible();
	}
	
	public void registerModels() {
		OwnCrops.getProxy().registerItemRenderer(this, 0);
	}
}
