package io.klebe.owncrops;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;
import org.xml.sax.SAXException;

import io.klebe.owncrops.common.ConfigHandler;
import io.klebe.owncrops.common.LocalRegistry;
import io.klebe.owncrops.common.proxy.CommonProxy;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(	modid = OwnCrops.MODID,
		name = OwnCrops.NAME,
		version = OwnCrops.VERSION)
public final class OwnCrops {
    public static final String MODID = "owncrops";
    public static final String NAME = "OwnCrops";
    public static final String VERSION = "0.0.1";
    
    private Logger logger = null;
    private Index registry = new Index();
    private ConfigHandler config = null;
    private ResourceLocation defaultConfigResourceLocation = new ResourceLocation(MODID + ":defaultconfig.xml");
    
    @Instance
    private static OwnCrops instance;
    
    @SidedProxy(
    		clientSide = "io.klebe.owncrops.common.proxy.ClientProxy",
    		serverSide = "io.klebe.owncrops.common.proxy.CommonProxy")
    private static CommonProxy proxy;
    
    public static CommonProxy getProxy() {
    	return proxy;
    }
    
    public static OwnCrops getInstance() {
    	return instance;
    }
    
    public boolean log(Level level, String msg) {
    	boolean condition = logger != null;
    	if(condition) {
    		logger.log(level, msg);
    	}
    	return condition;
    }
    
    public Index getRegistry() {
    	return registry;
    }
    
    public ConfigHandler getConfig() {
    	return config;
    }
    
    @EventHandler
    public void preInit(FMLPreInitializationEvent event) throws Exception{
    	event.getModLog().log(Level.INFO, "pre init " + MODID);
        this.logger = event.getModLog();
        
        initConfig(event);
    }
    
    private void initConfig(FMLPreInitializationEvent event) throws Exception {
    	File configFile = new File(event.getModConfigurationDirectory() + "/" + MODID + ".xml");
    	if(!configFile.exists()) {
    		InputStream in = Minecraft.getMinecraft().getResourceManager().getResource(defaultConfigResourceLocation).getInputStream();
    		byte[] buffer = new byte[in.available()];
    		in.read(buffer);
    		in.close();
    		
    		OutputStream out = new FileOutputStream(configFile);
    		out.write(buffer);
    		out.close();
    	}
    	config = new ConfigHandler(configFile);
    }

}
