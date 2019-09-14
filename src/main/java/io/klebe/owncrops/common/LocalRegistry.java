package io.klebe.owncrops.common;

import java.util.ArrayList;
import java.util.List;
import io.klebe.owncrops.OwnCrops;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class LocalRegistry {
	
	// Items
	private List<Item> items = new ArrayList<Item>();
	
	public boolean registerItem(Item item) {
		return items.add(item);
	}

	
	public List<Item> getItems(){
		return items;
	}
	
	// Blocks
	private List<Block> blocks = new ArrayList<Block>();
	
	public boolean registerBlock(Block block) {
		return blocks.add(block);
	}
	
	public List<Block> getBlocks(){
		return blocks;
	}
}
