package io.klebe.owncrops.common;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.swing.text.html.Option;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.logging.log4j.Level;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import io.klebe.owncrops.OwnCrops;
import net.minecraft.client.Minecraft;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.config.Configuration;

public class ConfigHandler{
	
	private List<ItemTemplate> itemsInConfig = new ArrayList<ItemTemplate>();
	private static ConfigHandler config = null;
    protected String defaultConfigResourceLocation = "/assets/" + OwnCrops.MODID + "/defaultconfig.xml";
	
	public static ConfigHandler getConfigHandler() throws ParserConfigurationException, SAXException, IOException {
		if(config == null) {
			config = new ConfigHandler();
		}
		return config;
	}
	
	public class Edible {
		private int amount;
		private float saturation;
		
		public Edible(int _amount, float _saturation) {
			amount = _amount;
			saturation = _saturation;
		}
		
		public int getAmount() {
			return amount;
		}
		
		public float getSaturation() {
			return saturation;
		}
	}
	
	public class Plantable {
		private String cropID;
		private String dropItemID;
		private int dropItemMeta;
		
		public Plantable(String id, String drop, int meta) {
			cropID = id;
			dropItemID = drop;
			dropItemMeta = meta;
		}
		
		public String getCropId() {
			return cropID;
		}
		
		public String getDrop() {
			return dropItemID;
		}
		
		public int getDropMeta() {
			return dropItemMeta;
		}
	}
	
	public class ItemTemplate {
		private String id;
		private Optional<Edible> edible;
		private Optional<Plantable> plantable;
		
		public ItemTemplate(String _id, Optional<Edible> _edible, Optional<Plantable> _plantable) {
			id = _id;
			edible = _edible;
			plantable = _plantable;
		}
		
		public Optional<Plantable> getPlantable(){
			return plantable;
		}
		
		public Optional<Edible> getEdible(){
			return edible;
		}
		
		public String getID() {
			return id;
		}
		
		public void register() {
			LocalRegistry registry = OwnCrops.getInstance().getRegistry();
			Item item = null;
			if(edible.isPresent()) {
				if (plantable.isPresent()) {
					AbstractBlockCrops crop = new AbstractBlockCrops(plantable.get().getCropId(), plantable.get().getDrop(), plantable.get().getDropMeta());
					item = new AbstractItemFoodPlantable(id, edible.get().getAmount(), edible.get().getSaturation(), crop);
					crop.setSeed(item);
					registry.registerBlock(crop);
				} else {
					item = new AbstractItemFood(id, edible.get().getAmount(), edible.get().getSaturation(), false);
				}
			} else {
				if (plantable.isPresent()) {
					AbstractBlockCrops crop = new AbstractBlockCrops(plantable.get().getCropId(), plantable.get().getDrop(), plantable.get().getDropMeta());
					item = new AbstractItemPlantable(id, crop);
					crop.setSeed(item);
					registry.registerBlock(crop);
				} else {
					item = new AbstractItem(id);
				}
			}
			registry.registerItem(item);
		}
	}
	
	private Edible parseEdible(Node edibleNode) {
		int amount = Integer.parseInt(edibleNode.getAttributes().getNamedItem("amount").getNodeValue());
		float saturation = Float.parseFloat(edibleNode.getAttributes().getNamedItem("saturation").getNodeValue());
		
		OwnCrops.getInstance().log(Level.INFO, "edible: " + amount + " - " + saturation);
		
		return new Edible(amount, saturation);
	}
	
	private Plantable parsePlantable(Node plantableNode) {
		String cropID = plantableNode.getAttributes().getNamedItem("id").getNodeValue();
		String dropItemID = plantableNode.getAttributes().getNamedItem("drop-item").getNodeValue();
		int dropItemMeta = Integer.parseInt(plantableNode.getAttributes().getNamedItem("drop-meta").getNodeValue());
		
		OwnCrops.getInstance().log(Level.INFO, "plantable: " + cropID + " - " + dropItemID + " - " + dropItemMeta);
		
		return new Plantable(cropID, dropItemID, dropItemMeta);
	}
	
	private void parseItem(Node item){
		OwnCrops.getInstance().log(Level.INFO, "parsing item");
		
		Optional<Edible> edible = Optional.empty();
		Optional<Plantable> plantable = Optional.empty();
		String id = item.getAttributes().getNamedItem("id").getNodeValue();
		NodeList props = item.getChildNodes();
		
		OwnCrops.getInstance().log(Level.INFO, "id is owncrops:" + id);
		
		for(int i = 0; i<props.getLength(); i++) {
			Node prop = props.item(i);
			if(prop.getNodeName() == "edible") {
				edible = Optional.of(parseEdible(prop));
			} else if (prop.getNodeName() == "plantable") {
				plantable = Optional.of(parsePlantable(prop));
			}
		}
		
		itemsInConfig.add(new ItemTemplate(id, edible, plantable));
	}
	
	private void parseConfig(Node root) {
		OwnCrops.log(Level.INFO, "parsing config");
		NodeList items = root.getChildNodes();
		
		for(int i = 0; i<items.getLength(); i++ ) {
			Node item = items.item(i);
			if(item.getNodeName() == "item") {
				parseItem(item);
			}
		}
	}
	
	private void initFile(File configFile) throws IOException {
		if(!configFile.exists()) {
    		InputStream in = getClass().getResourceAsStream(defaultConfigResourceLocation);
    		byte[] buffer = new byte[in.available()];
    		in.read(buffer);
    		in.close();
    		
    		OutputStream out = new FileOutputStream(configFile);
    		out.write(buffer);
    		out.close();
    	}
	}
	
	protected ConfigHandler() throws ParserConfigurationException, SAXException, IOException {
		File configFile = new File(Minecraft.getMinecraft().mcDataDir.getAbsolutePath() + "/config/" + OwnCrops.MODID + ".xml");
		
		initFile(configFile);
		
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document document = builder.parse(configFile);
		
		NodeList inRoot = document.getChildNodes();
		
		for(int i = 0; i<inRoot.getLength(); i++ ) {
			Node item = inRoot.item(i);
			if (item.getNodeName() == "OwnCrops") {
				parseConfig(item);
			}
		}
	}
	
	public void register() {
		for (ItemTemplate item : itemsInConfig) {
			item.register();
		}
	}
	
	public List<ItemTemplate> getItemsInConfig() {
		return itemsInConfig;
	}
}
