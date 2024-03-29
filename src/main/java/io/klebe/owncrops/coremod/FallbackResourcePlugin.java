package io.klebe.owncrops.coremod;

import java.util.Map;

import org.apache.logging.log4j.Level;

import io.klebe.owncrops.OwnCrops;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;

@IFMLLoadingPlugin.MCVersion("1.12.2")
@IFMLLoadingPlugin.SortingIndex(900)
public class FallbackResourcePlugin implements IFMLLoadingPlugin
{
	private static boolean IN_MCP = false;

	@Override
	public String[] getASMTransformerClass()
	{
		return new String[] { ClassTransformer.class.getName() };
	}

	@Override
	public String getModContainerClass()
	{
		return null;
	}

	@Override
	public String getSetupClass()
	{
		return null;
	}

	@Override
	public void injectData(Map<String, Object> data)
	{
		IN_MCP = !(Boolean) data.get("runtimeDeobfuscationEnabled");
	}

	@Override
	public String getAccessTransformerClass()
	{
		return null;
	}
	
	public static boolean inMCP() {
		return IN_MCP;
	}

}