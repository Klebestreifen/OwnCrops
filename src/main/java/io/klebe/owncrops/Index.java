package io.klebe.owncrops;

import io.klebe.owncrops.common.AbstractBlockCrops;
import io.klebe.owncrops.common.ICanCreateItem;
import io.klebe.owncrops.common.LocalRegistry;
import io.klebe.owncrops.items.CustomStick;
import net.minecraft.block.Block;
import net.minecraft.item.Item;

public class Index extends LocalRegistry{
	
	private boolean registerItemsCalled = false;
	public void registerItems() {
		if(!registerItemsCalled) {
			
			/* Items here */{
				//registerItem(ITEM_CUSTOM_STICK);
				//registerItem(ITEM_RICE);
			
			}
			
			// Register Block-Items
			registerBlocks();
			for(Block block : super.getBlocks()) {
				if(block instanceof ICanCreateItem) {
					registerItem(((ICanCreateItem)block).createItem());
				}
			}
		}
		registerItemsCalled = true;
	}
	
	private boolean registerBlocksCalled = false;
	public void registerBlocks() {
		if(!registerBlocksCalled) {
			
			// Blocks here
			//registerBlock(CROP_RICE);
			
			
		}
		registerBlocksCalled = true;
	}

}
