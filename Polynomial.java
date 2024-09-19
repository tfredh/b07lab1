/*d. Develop class Polynomial as follows:
i. It has one field representing the coefficients of the polynomial using an array of
double. A polynomial is assumed to have the form 𝑎 ! + 𝑎" 𝑥 " + ⋯ + 𝑎 #$" 𝑥 #$" .
For example, the polynomial 6 − 2𝑥 + 5𝑥 % would be represented using the
array [6, -2, 0, 5]
ii. It has a no-argument constructor that sets the polynomial to zero (i.e. the
corresponding array would be [0])
iii. It has a constructor that takes an array of double as an argument and sets the
coefficients accordingly
iv. It has a method named add that takes one argument of type Polynomial and
returns the polynomial resulting from adding the calling object and the argument
v. It has a method named evaluate that takes one argument of type double
representing a value of x and evaluates the polynomial accordingly. For example,
if the polynomial is 6 − 2𝑥 + 5𝑥 % and evaluate(-1) is invoked, the result should
be 3.
vi. It has a method named hasRoot that takes one argument of type double and
determines whether this value is a root of the polynomial or not. Note that a root
is a value of x for which the polynomial evaluates to zero.
*/

public class Polynomial {
    private double[] coefficients;

    Polynomial() {
        coefficients = new double[] { 0 };
    }

    Polynomial(double[] coefficients) {
        this.coefficients = coefficients.clone();
    }

    public double[] getCoefficients() {
        return this.coefficients;
    }

    public Polynomial add(Polynomial p) {
        final double[] P_COEFFICIENTS = p.getCoefficients();
        int largestDegree = Math.max(this.coefficients.length, P_COEFFICIENTS.length);

        double[] newCoefficients = new double[largestDegree];
        for (int i = 0; i < largestDegree; i++) {
            double oldCoeff = this.coefficients.length > i ? this.coefficients[i] : 0d;
            double pCoeff = P_COEFFICIENTS.length > i ? P_COEFFICIENTS[i] : 0d;

            newCoefficients[i] = oldCoeff + pCoeff;
        }

        return new Polynomial(newCoefficients);
    }

    public double evaluate(double x) {
        double total = 0d;

        for (int i = 0; i < this.coefficients.length; i++) {
            total += this.coefficients[i] * (double) Math.pow(x, i);
        }

        return total;
    }

    public boolean hasRoot(double x) {
        return evaluate(x) == 0;
    }
}