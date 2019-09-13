package io.klebe.owncrops.coremod.dynamicresources;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import io.klebe.owncrops.coremod.common.AbstractResource;
import net.minecraft.util.ResourceLocation;

public class DynamicCropBlockStateResource extends AbstractResource {

	protected String id;
	protected int maxAge;
	
	protected String generateAge(int age) {
		return String.format("\"age=%d\": { \"model\": \"%s:%s_%d\" }", age, getResourceLocation().getResourceDomain(), id, age);
	}
	
	protected String generate() {
		String rtn = "{\"variants\": {";
		
		for(int i = 0; i <= maxAge; i++) {
			rtn += generateAge(i); 
			if(i != maxAge) {
				rtn += ", ";
			}
		}
		
		return rtn + " } }";
	}
	
	protected InputStream createInputStream() {
		return new ByteArrayInputStream(generate().getBytes());
	}
	
	public DynamicCropBlockStateResource(String packName, ResourceLocation location, String id, int maxAge) {
		super(packName, location);
		
		this.id = id;
		this.maxAge = maxAge;
		
		super.setInputStream(createInputStream());
	}
}
