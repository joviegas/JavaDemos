
## GCC

To compile the shared library, navigate to the `native/` directory and use `gcc`:

```bash
cd native/
gcc -shared -o libcrc32c.so -fPIC crc32c.c
```

### Compile

Compile the Java code with `javac` using Java 21 preview features:

```bash
javac --enable-preview --release 21 -d out src/CRCJava.java
```

### Run

Run the compiled Java program with native access enabled:

```bash
java --enable-preview --enable-native-access=ALL-UNNAMED -cp out CRCJava
```
