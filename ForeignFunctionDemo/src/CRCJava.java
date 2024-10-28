// src/Main.java
import java.lang.foreign.*;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;

public class CRCJava {
    public static void main(String[] args) {
        // Define the library path to load
        Path libraryPath = Path.of("/Users/joviegas/gitrepo/JavaDemos/ForeignFunctionDemo/native/libcrc32c.so");

        try (Arena arena = Arena.ofConfined()) {
            // Obtain a linker for interacting with the native code
            Linker linker = Linker.nativeLinker();

            // Define the function signature: uint32_t crc32c_checksum(const char*)
            FunctionDescriptor crc32cDescriptor = FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS);

            // Look up the symbol (the function) in the library
            SymbolLookup loader = SymbolLookup.libraryLookup(libraryPath, arena);
            MemorySegment crc32cFunc = loader.find("crc32c_checksum").orElseThrow();

            // Link the function to a MethodHandle
            MethodHandle crc32cHandle = linker.downcallHandle(crc32cFunc, crc32cDescriptor);

            // Input string to calculate the checksum for
            String input = "Hello, World!";
            MemorySegment inputSegment = arena.allocateUtf8String(input);

            // Call the native function with the input string
            int checksum = (int) crc32cHandle.invoke(inputSegment);
            System.out.printf("CRC32C checksum: %08X%n", checksum);  // Expected output in hexadecimal
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
}
