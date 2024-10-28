import java.lang.foreign.*;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.nio.file.Path;

public class Main {
    public static void main(String[] args) {
        // Define the library path to load
        Path libraryPath = Path.of("/Users/joviegas/gitrepo/JavaDemos/ForeignFunctionDemo/native/libmathlib.so");

        try (Arena arena = Arena.ofConfined()) {
            // Obtain a linker for interacting with the native code
            Linker linker = Linker.nativeLinker();

            // Define the method signature: int add(int, int)
            FunctionDescriptor addDescriptor = FunctionDescriptor.of(ValueLayout.JAVA_INT,
                    ValueLayout.JAVA_INT,
                    ValueLayout.JAVA_INT);

            // Look up the symbol (the function) in the library
            SymbolLookup loader = SymbolLookup.libraryLookup(libraryPath, arena);
            MemorySegment addFunc = loader.find("add").orElseThrow();

            // Link the function to a MethodHandle
            MethodHandle addHandle = linker.downcallHandle(addFunc, addDescriptor);

            // Call the native function with arguments
            int result = (int) addHandle.invoke(5, 3);
            System.out.println("Result of addition: " + result);  // Expected output: Result of addition: 8
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
}
