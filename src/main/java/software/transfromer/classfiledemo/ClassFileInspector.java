package software.transfromer.classfiledemo;

// ClassFileInspector.java

import org.objectweb.asm.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class ClassFileInspector {
    public static void main(String[] args) throws IOException {
        Path classFilePath = Path.of("/Users/joviegas/gitrepo/JavaDemos/target/classes/software/transfromer/ClassFileDemo/HelloWorld.class");
        byte[] classBytes = Files.readAllBytes(classFilePath);

        ClassReader reader = new ClassReader(classBytes);
        reader.accept(new ClassVisitor(Opcodes.ASM9) {
            @Override
            public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
                System.out.println("Class: " + name);
                System.out.println("Superclass: " + superName);
            }

            @Override
            public FieldVisitor visitField(int access, String name, String descriptor, String signature, Object value) {
                System.out.println("Field: " + name + ", Descriptor: " + descriptor);
                return super.visitField(access, name, descriptor, signature, value);
            }

            @Override
            public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
                System.out.println("Method: " + name + ", Descriptor: " + descriptor);
                return super.visitMethod(access, name, descriptor, signature, exceptions);
            }
        }, 0);
    }
}
