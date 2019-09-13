package io.klebe.owncrops.coremod.dynamicresources;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import io.klebe.owncrops.coremod.common.AbstractResource;
import net.minecraft.util.ResourceLocation;

public class DynamicItemModelRessource extends AbstractResource {

	protected String id;
	
	protected String generate() {
		return String.format("{\"parent\": \"item/handheld\", \"textures\": { \"layer0\": \"%s:items/%s\"}}", getResourceLocation().getResourceDomain(), id);
	}
	
	protected InputStream createInputStream() {
		return new ByteArrayInputStream(generate().getBytes());
	}
	
	public DynamicItemModelRessource(String packName, ResourceLocation location, String id) {
		super(packName, location);
		
		this.id = id;
		
		super.setInputStream(createInputStream());
	}
}
