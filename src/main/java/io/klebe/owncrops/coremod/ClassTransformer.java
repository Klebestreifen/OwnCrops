package io.klebe.owncrops.coremod;

import net.minecraft.launchwrapper.IClassTransformer;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;
import io.klebe.owncrops.OwnCrops;

public class ClassTransformer implements IClassTransformer
{
	
	@Override
	public byte[] transform(String name, String transformedName, byte[] basicClass) {
		if (transformedName.equals("net.minecraft.client.Minecraft")){
			return patchMinecraft(basicClass);
		}
		return basicClass;
	}

	private byte[] patchMinecraft(byte[] basicClass) {
		ClassNode classNode = new ClassNode();
		ClassReader classReader = new ClassReader(basicClass);
		classReader.accept(classNode, 0);
		OwnCrops.log(Level.INFO, "Found Minecraft Class: " + classNode.name);

		MethodNode init = null;

		for (MethodNode mn : classNode.methods){
			if (mn.name.equals("init")){
				init = mn;
			}
		}

		if (init != null){
			OwnCrops.log(Level.INFO, "Patching 'init'");

			AbstractInsnNode target = init.instructions.get(0);
			
			InsnList toInsert = new InsnList();
			
			toInsert.add(new VarInsnNode(Opcodes.ALOAD, 0));
			toInsert.add(new FieldInsnNode(Opcodes.GETFIELD, "net/minecraft/client/Minecraft", "defaultResourcePacks", "Ljava/util/List;"));
			toInsert.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "io/klebe/owncrops/coremod/FallbackResourceLoader", "create", "()Lnet/minecraft/client/resources/IResourcePack;", false));
			toInsert.add(new MethodInsnNode(Opcodes.INVOKEINTERFACE, "java/util/List", "add", "(Ljava/lang/Object;)Z", true));
			toInsert.add(new InsnNode(Opcodes.POP));
			
			init.instructions.insertBefore(target, toInsert);
			OwnCrops.log(Level.INFO, "Patching 'init' done");
		}

		ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS);
		classNode.accept(writer);

		return writer.toByteArray();
	}
}
