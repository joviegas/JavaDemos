package software.transfromer.classfiledemo;

// Loggable.java

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Loggable {}
