/*
Replace the array representing the coefficients by two arrays: one representing the non-
zero coefficients (of type double) and another one representing the corresponding
exponents (of type int). For example, the polynomial 6 − 2𝑥 + 5𝑥 ! would be represented
using the arrays [6, -2, 5] and [0, 1, 3]
b. Update the existing methods accordingly
c. Add a method named multiply that takes one argument of type Polynomial and returns
the polynomial resulting from multiplying the calling object and the argument. The
resulting polynomial should not contain redundant exponents.
d. Add a constructor that takes one argument of type File and initializes the polynomial
based on the contents of the file. You can assume that the file contains one line with no
Fall 2024
whitespaces representing a valid polynomial. For example: the line 5-3x2+7x8
corresponds to the polynomial 5 − 3𝑥 " + 7𝑥 #
Hint: you might want to use the following methods: split of the String class, parseInt of
the Integer class, and parseDouble of the Double class
e. Add a method named saveToFile that takes one argument of type String representing a
file name and saves the polynomial in textual format in the corresponding file (similar to
the format used in part d)
f. You can add any supplementary classes/methods you might find useful
*/

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Polynomial {
    private double[] coefficients;
    private int[] exponents;

    Polynomial() {
        this.coefficients = new double[] { 0 };
        this.exponents = new int[] { 0 };
    }

    Polynomial(double[] coefficients, int[] exponents) {
        this.coefficients = coefficients.clone();
        this.exponents = exponents.clone();
    }

    Polynomial(File file) {
        try {
            Scanner scanner = new Scanner(file);

            if (scanner.hasNextLine()) {
                String polynomialStr = scanner.nextLine().trim().replaceAll(" ", "");
                System.out.println(polynomialStr);

                // Create the polynomial
                polynomialStr = polynomialStr.replaceAll("(?=[+-])", " ");
                String[] parts = polynomialStr.split(" ");

                ArrayList<Double> newCoeffs = new ArrayList<>();
                ArrayList<Integer> newExps = new ArrayList<>();
                for (String part : parts) {
                    part = part.trim();

                    if (!part.contains("x")) {
                        newCoeffs.add(Double.parseDouble(part));
                        newExps.add(0);

                        // System.out.print("\"" + newCoeffs.get(0) + "\"");
                        // System.out.print(" ");
                        // System.out.print("\"" + newExps.get(0) + "\"");
                        // System.out.println();
                        continue;
                    }

                    String[] termItems = part.split("x");

                    String newCoeffStr = termItems[0].trim();
                    if (newCoeffStr.isEmpty() || newCoeffStr.equals("+")) {
                        newCoeffs.add(1.0d);
                    } else if (newCoeffStr.equals("-")) {
                        newCoeffs.add(-1.0d);
                    } else {
                        newCoeffs.add(Double.parseDouble(newCoeffStr));
                    }

                    // If there is no exponent term like 5x => [5]
                    String newExpStr = termItems.length > 1 ? termItems[1].replace("^", "").trim() : "";
                    if (newExpStr.isEmpty()) {
                        newExps.add(1);
                    } else {
                        newExps.add(Integer.parseInt(newExpStr));
                    }

                    // System.out.print("\"" + newCoeffStr + "\"");
                    // System.out.print(" ");
                    // System.out.print("\"" + newExpStr + "\"");
                    // System.out.println();
                }

                this.coefficients = newCoeffs.stream().mapToDouble(Double::doubleValue).toArray();
                this.exponents = newExps.stream().mapToInt(Integer::intValue).toArray();
            }
            scanner.close();
        } catch (FileNotFoundException exp) {
            System.out.println("File not found");
        }
    }

    public double[] getCoefficients() {
        return this.coefficients;
    }

    public int[] getExponents() {
        return this.exponents;
    }

    public Polynomial add(Polynomial p) {
        final double[] pCoefficients = p.getCoefficients();
        final int[] pExponents = p.getExponents();

        ArrayList<Double> newCoeffs = new ArrayList<>();
        ArrayList<Integer> newExps = new ArrayList<>();

        int i = 0, j = 0;
        while (i < this.exponents.length && j < pExponents.length) {
            if (this.exponents[i] > pExponents[j]) {
                newCoeffs.add(this.coefficients[i]);
                newExps.add(this.exponents[i]);
                i += 1;
            } else if (this.exponents[i] < pExponents[j]) {
                newCoeffs.add(pCoefficients[j]);
                newExps.add(pExponents[j]);
                j += 1;
            } else {
                // When they (exponents) are equal

                double sumCoeff = this.coefficients[i] + pCoefficients[j];
                if (sumCoeff != 0) {
                    newCoeffs.add(sumCoeff);
                    newExps.add(this.exponents[i]);
                }
                i += 1;
                j += 1;
            }
        }
        if (i == this.coefficients.length) {
            // This means that this.coefficients ran out of values first

            while (j < pCoefficients.length) {
                newCoeffs.add(pCoefficients[j]);
                newExps.add(pExponents[j]);
                j += 1;
            }
        } else if (j == pCoefficients.length) {
            while (i < this.coefficients.length) {
                newCoeffs.add(this.coefficients[i]);
                newExps.add(this.exponents[i]);
                i += 1;
            }
        }

        return new Polynomial(newCoeffs.stream().mapToDouble(Double::doubleValue).toArray(),
                newExps.stream().mapToInt(Integer::intValue).toArray());
    }

    public double evaluate(double x) {
        double total = 0d;

        for (int i = 0; i < this.coefficients.length; i++) {
            total += this.coefficients[i] * Math.pow(x, this.exponents[i]);
        }

        return total;
    }

    public boolean hasRoot(double x) {
        return evaluate(x) == 0;
    }

    public Polynomial multiply(Polynomial p) {
        final double[] pCoefficients = p.getCoefficients();
        final int[] pExponents = p.getExponents();

        ArrayList<Double> newCoeffs = new ArrayList<>();
        ArrayList<Integer> newExps = new ArrayList<>();

        for (int i = 0; i < this.coefficients.length; i++) {
            for (int j = 0; j < pCoefficients.length; j++) {
                double newCoeff = this.coefficients[i] * pCoefficients[j];
                int newExp = this.exponents[i] + pExponents[j];

                boolean combined = false;
                for (int k = 0; k < newExps.size(); k++) {
                    if (newExp == newExps.get(k)) {
                        newCoeffs.set(k, newCoeffs.get(k) + newCoeff);
                        combined = true;
                        break;
                    }
                }
                if (!combined) {
                    newCoeffs.add(newCoeff);
                    newExps.add(newExp);
                }
            }
        }

        return new Polynomial(newCoeffs.stream().mapToDouble(Double::doubleValue).toArray(),
                newExps.stream().mapToInt(Integer::intValue).toArray());
    }

    public void saveToFile(String fileName) {
        try {
            FileWriter writer = new FileWriter(fileName);
            // writer.write("");

            writer.append(String.valueOf(this.coefficients[0]));
            for (int i = 1; i < this.coefficients.length; i++) {
                if (this.coefficients[i] == 0) {
                    // *Should never happen
                    continue;
                } else if (this.coefficients[i] > 0) {
                    writer.append("+");
                }
                // No need to add "-" if this.coefficients[i] < 0 since it is already
                // represented in the coefficient "-5"

                writer.append(String.format("%fx%d", this.coefficients[i], this.exponents[i]));
            }

            writer.close();
        } catch (IOException e) {
            System.out.println("An error occurred.");
        }
    }
}