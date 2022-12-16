package functions;
import functions.Function;

/**
 * 
 * Function inherited class wich represent f(x)= x*x
 *
 */
public class MyFunction extends Function {
	/**
	 * 
	 * @param min  min low bound of the integral
	 * @param max max upper bound of the integral
	 * @param delta delta step of discretization
	 */
	public MyFunction(double min, double max, double delta) {
		super(min, max, delta);
	}

	/**
	 * @param x abscissa
	 * @return f(x)= x*x
	 */
	public double f(double x) {
		return x*x;
	}
}
