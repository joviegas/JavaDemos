package software.transfromer.incubator;

import jdk.incubator.vector.*;
import java.util.random.RandomGenerator;




public class VectorizedArrayAddition {

    private static final VectorSpecies<Float> SPECIES = FloatVector.SPECIES_128;
    static {
        System.setProperty("add-modules", "jdk.incubator.vector");
    }

    public static void main(String[] args) {


        // Define the size of the arrays

        int size = 4 * SPECIES.length() ;

        float[] arrayA = new float[size];
        float[] arrayB = new float[size];

        RandomGenerator random = RandomGenerator.getDefault();
        for (int i = 0; i < size; i++) {
            arrayA[i] = random.nextFloat();
            arrayB[i] = random.nextFloat();
        }

        float[] result = new float[size];

        int i = 0;
        for (; i < SPECIES.loopBound(size); i+= SPECIES.length()){
            FloatVector vectorA = FloatVector.fromArray(SPECIES, arrayA, i);
            FloatVector vectorB = FloatVector.fromArray(SPECIES, arrayB, i);
            FloatVector resultVector = vectorA.add(vectorB);

            resultVector.intoArray(result, i);
        }


        System.out.println("Array A: ");
        printArray(arrayA);
        System.out.println("Array B: ");
        printArray(arrayB);
        System.out.println("Result (A + B): ");
        printArray(result);




    }

    // Helper method to print an array
    private static void printArray(float[] array) {
        for (float value : array) {
            System.out.printf("%.4f ", value);
        }
        System.out.println();
    }
}
