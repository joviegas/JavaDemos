package software.transfromer.classfiledemo;

public class LogableHelloWorld {
    @Loggable
    public void greet() {
        System.out.println("Hello, World!");
    }

    @Loggable
    public int add(int a, int b) {
        return a + b;
    }

    public void noLogMethod() {
        System.out.println("This method is not logged.");
    }
}
