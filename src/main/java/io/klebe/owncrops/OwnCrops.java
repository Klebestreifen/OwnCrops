package io.klebe.owncrops;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
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
		version = OwnCrops.VERSION,
		dependencies = OwnCrops.DEPENDENCIES)
public final class OwnCrops {
    public static final String MODID = "owncrops";
    public static final String NAME = "Own Crops";
    public static final String VERSION = "0.1.1";
    public static final String DEPENDENCIES = "";
    
    private static Logger logger = null;
    private Index registry = new Index();
    
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
    
    public static void log(Level level, String msg) {
    	if(logger == null) {
    		logger = LogManager.getLogger("owncrops");
    	}
    	logger.log(level, msg);
    }
    
    public Index getRegistry() {
    	return registry;
    }

    
    @EventHandler
    public void preInit(FMLPreInitializationEvent event) throws Exception{
    	log(Level.INFO, "pre init " + MODID);
    	ConfigHandler.getConfigHandler().register();
    }

}
