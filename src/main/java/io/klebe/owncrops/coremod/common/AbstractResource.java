package io.klebe.owncrops.coremod.common;

import java.io.IOException;
import java.io.InputStream;

import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourcePack;
import net.minecraft.client.resources.data.IMetadataSection;
import net.minecraft.util.ResourceLocation;

public class AbstractResource implements IResource {
	
	protected InputStream iStream;
	private ResourceLocation location;
	private String packName;
	
	public AbstractResource(String packName, ResourceLocation location) {
		this.location = location;
		this.packName = packName;
	}

	protected void setInputStream(InputStream iStream) {
		this.iStream = iStream;
	}
	
	@Override
	public void close() throws IOException {
		iStream.close();
	}

	@Override
	public ResourceLocation getResourceLocation() {
		return location;
	}

	@Override
	public InputStream getInputStream() {
		return iStream;
	}

	@Override
	public boolean hasMetadata() {
		return false;
	}

	@Override
	public <T extends IMetadataSection> T getMetadata(String sectionName) {
		return null;
	}

	@Override
	public String getResourcePackName() {
		return this.packName;
	}

}
