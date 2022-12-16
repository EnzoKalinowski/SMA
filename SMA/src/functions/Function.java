package functions;

/**
 * 
 *  Representation of a function
 *
 */
public abstract class Function {
	/**
	 * 
	 */
	public final double min;
	/**
	 * 
	 */
	public final double max;
	/**
	 * 
	 */
	public final double delta;
	
	/**
	 * 
	 * @param min low bound of the integral
	 * @param max upper bound of the integral
	 * @param delta step of discretization
	 */
	public Function(double min, double max, double delta){
		this.min=min;
		this.max=max;
		this.delta=delta;
	}
	
	/**
	 * 
	 * @param x abscissa
	 * @return f(x) ordinate
	 */
	public abstract double f(double x);
	
	/**
	 * 
	 * @return f(x) integral from min to max with a step of delta
	 */
	public double eval() {
		double res=0;
		for(double i=min;i<=max;i+=delta) {
			res+=f(i);
		}
		return res;
		
		
	}
}
