package io.klebe.owncrops.coremod.dynamicresources;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import io.klebe.owncrops.coremod.common.AbstractResource;
import net.minecraft.util.ResourceLocation;

public class DynamicCropModelResource extends AbstractResource {

	protected String id;
	protected int age;
	
	protected String generate() {
		return String.format("{\"parent\": \"block/crop\", \"textures\": { \"crop\": \"%s:blocks/%s_%d\"}}", getResourceLocation().getResourceDomain(), id, age);
	}
	
	protected InputStream createInputStream() {
		return new ByteArrayInputStream(generate().getBytes());
	}
	
	public DynamicCropModelResource(String packName, ResourceLocation location, String id, int age) {
		super(packName, location);
		
		this.id = id;
		this.age = age;
		
		super.setInputStream(createInputStream());
	}

}
