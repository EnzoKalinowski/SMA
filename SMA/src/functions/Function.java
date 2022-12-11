package functions;

public abstract class Function {
	public final double min;
	public final double max;
	public final double delta;
	
	public Function(double min, double max, double delta){
		this.min=min;
		this.max=max;
		this.delta=delta;
	}
	
	public abstract double f(double x);
	
	public double eval() {
		double res=0;
		for(double i=min;i<=max;i+=delta) {
			res+=f(i);
		}
		return res;
		
	}
}
