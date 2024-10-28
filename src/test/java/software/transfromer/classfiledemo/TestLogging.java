package software.transfromer.classfiledemo;

public class TestLogging {
    public static void main(String[] args) {
        LogableHelloWorld hello = new LogableHelloWorld();
        hello.greet();
        hello.add(5, 3);
        hello.noLogMethod();
    }
}