package functions;

public class MyFunction extends Function {
	
	public MyFunction(double min, double max, double delta) {
		super(min, max, delta);
	}

	public double f(double x) {
		return x*x;
	}
}
