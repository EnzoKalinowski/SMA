package functions;

/**
 * 
 * @brief Representation of a function
 *
 */
public abstract class Function {
	public final double min;
	public final double max;
	public final double delta;
	
	/**
	 * 
	 * @param min
	 * @param max
	 * @param delta
	 */
	public Function(double min, double max, double delta){
		this.min=min;
		this.max=max;
		this.delta=delta;
	}
	
	/**
	 * 
	 * @param x
	 * @return f(x)
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
