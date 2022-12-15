package functions;
import functions.Function;

/**
 * 
 * @brief Function inherited class wich represent f(x)= x*x
 *
 */
public class MyFunction extends Function {
	/**
	 * 
	 * @param min
	 * @param max
	 * @param delta
	 */
	public MyFunction(double min, double max, double delta) {
		super(min, max, delta);
	}

	/**
	 * @param x
	 * @return x*x
	 */
	public double f(double x) {
		return x*x;
	}
}
