package software.transfromer.classfiledemo;

// LoggableTransformer.java
import org.objectweb.asm.*;
import java.io.IOException;
import java.nio.file.*;

public class LoggableTransformer {

    public static void main(String[] args) throws IOException {
        Path classPath = Path.of("/Users/joviegas/gitrepo/JavaDemos/target/classes/software/transfromer/classfiledemo/LogableHelloWorld.class");
        byte[] classBytes = Files.readAllBytes(classPath);

        // Create a ClassReader for HelloWorld
        ClassReader reader = new ClassReader(classBytes);
        ClassWriter writer = new ClassWriter(reader, ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);

        // Transform the class by visiting and modifying methods
        reader.accept(new ClassVisitor(Opcodes.ASM9, writer) {
            @Override
            public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
                MethodVisitor originalMethod = super.visitMethod(access, name, descriptor, signature, exceptions);

                // Intercept only methods with @Loggable by injecting print statements
                return new MethodVisitor(Opcodes.ASM9, originalMethod) {
                    boolean isLoggable = false;

                    @Override
                    public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
                        if ("Lsoftware/transfromer/classfiledemo/Loggable;".equals(desc)) {
                            isLoggable = true;
                        }

                        return super.visitAnnotation(desc, visible);
                    }

                    @Override
                    public void visitCode() {
                        if (isLoggable) {
                            // Inject `System.out.println("Entering <methodName>")` at the start
                            super.visitFieldInsn(Opcodes.GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
                            super.visitLdcInsn("Entering " + name);
                            super.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);
                        }
                        super.visitCode();
                    }

                    @Override
                    public void visitInsn(int opcode) {
                        if (isLoggable && (opcode >= Opcodes.IRETURN && opcode <= Opcodes.RETURN)) {
                            // Inject `System.out.println("Exiting <methodName>")` before return
                            super.visitFieldInsn(Opcodes.GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
                            super.visitLdcInsn("Exiting " + name);
                            super.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);
                        }
                        super.visitInsn(opcode);
                    }
                };
            }
        }, 0);

        // Write the modified bytecode back to the file
        Files.write(classPath, writer.toByteArray());
        System.out.println("Instrumentation complete for Loggable methods.");
    }
}
