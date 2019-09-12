package io.klebe.owncrops.common;

import java.util.List;

import org.apache.logging.log4j.Level;

import io.klebe.owncrops.Index;
import io.klebe.owncrops.OwnCrops;
import io.klebe.owncrops.items.CustomStick;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@EventBusSubscriber
public class EventHandler {
	@SubscribeEvent
	public static void onItemRegister(RegistryEvent.Register<Item> event) {
		OwnCrops.getInstance().log(Level.INFO, "RegistryEvent - Items");
		
		Index registry = OwnCrops.getInstance().getRegistry();
		
		registry.registerItems();
		
		
		for (Item item : registry.getItems()) {
			OwnCrops.getInstance().log(Level.INFO, "register item " + item.getRegistryName());
			event.getRegistry().register(item);
		}
	}
	
	@SubscribeEvent
	public static void onBlockRegister(RegistryEvent.Register<Block> event) {
		OwnCrops.getInstance().log(Level.INFO, "RegistryEvent - Blocks");
		
		Index registry = OwnCrops.getInstance().getRegistry();
		
		registry.registerBlocks();
		
		for (Block block : registry.getBlocks()) {
			OwnCrops.getInstance().log(Level.INFO, "register block " + block.getRegistryName());
			event.getRegistry().register(block);
		}
	}
	
	@SubscribeEvent
	public static void onModelRegister(ModelRegistryEvent event) {
		OwnCrops.getInstance().log(Level.DEBUG, "ModelRegistryEvent");
		
		Index registry = OwnCrops.getInstance().getRegistry();
		
		for(Item item : registry.getItems()) {
			if (item instanceof IHasModel) {
				((IHasModel)item).registerModels();
			}
		}
		
		for(Block block : registry.getBlocks()) {
			if (block instanceof IHasModel) {
				((IHasModel)block).registerModels();
			}
		}
	}
	
}