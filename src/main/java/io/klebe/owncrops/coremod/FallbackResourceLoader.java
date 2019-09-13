package io.klebe.owncrops.coremod;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.logging.log4j.Level;
import org.xml.sax.SAXException;

import akka.event.Logging.Debug;
import io.klebe.owncrops.OwnCrops;
import io.klebe.owncrops.common.ConfigHandler;
import io.klebe.owncrops.common.ConfigHandler.ItemTemplate;
import io.klebe.owncrops.coremod.common.AbstractResource;
import io.klebe.owncrops.coremod.dynamicresources.DynamicCropBlockStateResource;
import io.klebe.owncrops.coremod.dynamicresources.DynamicCropModelResource;
import io.klebe.owncrops.coremod.dynamicresources.DynamicItemModelRessource;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourcePack;
import net.minecraft.client.resources.SimpleResource;
import net.minecraft.client.resources.data.IMetadataSection;
import net.minecraft.client.resources.data.MetadataSerializer;
import net.minecraft.util.ResourceLocation;

public class FallbackResourceLoader implements IResourcePack {
	
	public static final String PACK_NAME = "OwnCropsFallbackResources"; 
	
	protected HashMap<String, AbstractResource> resources = new HashMap<String, AbstractResource>(); 
	
	public FallbackResourceLoader() throws ParserConfigurationException, SAXException, IOException {
		for(ItemTemplate item : ConfigHandler.getConfigHandler().getItemsInConfig()) {
			// Create Item Model
			String itemId = item.getID();
			String itemModelPath = String.format("models/item/%s.json", itemId);
			resources.put(itemModelPath, new DynamicItemModelRessource(
					getPackName(), 
					new ResourceLocation(OwnCrops.MODID, itemModelPath), 
					itemId));
			
			if(item.getPlantable().isPresent()) { // Crop?
				
				// Create Blockstate
				int maxAge = 7;
				String cropId = item.getPlantable().get().getCropId();
				String statePath = String.format("blockstates/%s.json", cropId);
				resources.put(statePath, new DynamicCropBlockStateResource(
						getPackName(),
						new ResourceLocation(OwnCrops.MODID, statePath),
						cropId,
						maxAge));
				
				// Create Models
				for(int age = 0; age <= maxAge; age++) {
					String modelPath = String.format("models/block/%s_%d.json", cropId, age);
					resources.put(modelPath, new DynamicCropModelResource(
							getPackName(),
							new ResourceLocation(OwnCrops.MODID, modelPath),
							cropId,
							age));
				}
			}
		}
	}
	
	// Called by the patched Minecraft class.
	public static IResourcePack create() throws ParserConfigurationException, SAXException, IOException {
		return new FallbackResourceLoader();
	}

	@Override
	public InputStream getInputStream(ResourceLocation location) throws IOException {
		if(resourceExists(location)) {
			return resources.get(location.getResourcePath()).getInputStream();
		}
		return null;
	}

	@Override
	public boolean resourceExists(ResourceLocation location) {
		if(!location.getResourceDomain().equals(OwnCrops.MODID)) {
			return false;
		} else if(resources.containsKey(location.getResourcePath())) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public Set<String> getResourceDomains() {
		HashSet<String> rtn = new HashSet<String>();
		rtn.add(OwnCrops.MODID);
		return rtn;
	}

	@Override
	public <T extends IMetadataSection> T getPackMetadata(MetadataSerializer metadataSerializer, String metadataSectionName) throws IOException {
		return null;
	}

	@Override
	public BufferedImage getPackImage() throws IOException {
		return null;
	}

	@Override
	public String getPackName() {
		return PACK_NAME;
	}

}
