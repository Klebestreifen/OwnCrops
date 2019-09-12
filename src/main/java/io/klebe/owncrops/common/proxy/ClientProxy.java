package io.klebe.owncrops.common.proxy;

import io.klebe.owncrops.common.AbstractItem;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;

public final class ClientProxy extends CommonProxy {
	
	@Override
	public void registerItemRenderer(Item item, int meta) {
		super.registerItemRenderer(item, meta);
		ModelLoader.setCustomModelResourceLocation(
				item,
				meta,
				new ModelResourceLocation(item.getRegistryName(), "inventory"));
	}
	
	
}
