import java.awt.Color;

public class Complex {
	private final double re; // the real part
	private final double im; // the imaginary part
	private final double tolerance = Math.pow(10, -6);

	public static final Complex I = new Complex(0, 1);
	public static final Complex ONE = new Complex(1, 0);
	public static final Complex NEGATIVEONE = new Complex(-1, 0);
	public static final Complex E = new Complex(Math.E);
	public static final Complex PI = new Complex(Math.PI);
	public static final Complex OMEGA = new Complex(-1.0 / 2.0, Math.sqrt(3) / 2);
	public static final Complex TWO = new Complex(2, 0);

	// create a new object with the given real and imaginary parts
	public Complex(double real, double imag) {
		this.re = real;
		this.im = imag;
	}

	// create a new object from a real
	public Complex(double d) {
		this.re = d;
		this.im = 0;
	}

	// polar constructor
	public static Complex ComplexPolar(double abs, double phase) {
		double real = abs * Math.cos(phase);
		double imag = abs * Math.sin(phase);
		return new Complex(real, imag);
	}

	// return a string representation of the invoking Complex object
	public String toString() {
		if (this.im == 0)
			return this.re + "";
		if (re == 0)
			return this.im + "i";
		if (this.im < 0)
			return this.re + " - " + (-this.im) + "i";
		return this.re + " + " + this.im + "i";
	}

	// returns the eisenstein form of a complex number a+bi = c+dw
	public String toStringEisenstein() {
		double b = this.im * 2.0 / Math.sqrt(3.0);
		double a = this.re + (b / 2.0);
		if (b >= 0) {
			return a + " + " + b + "w";
		} else {
			return a + " - " + b + "w";
		}
	}

	// returns the domain mapping color of the complex number
	public Color getColor() {
		double h = phaseDegrees();
		double s = 1;
		double a = 0.5;
		double l = (1 - Math.pow(a, abs()));
		double c = (1 - Math.abs(2 * l - 1));
		double hprime = h / 60;
		double x = c * (1 - Math.abs(hprime % 2 - 1));

		double r1 = 0;
		double g1 = 0;
		double b1 = 0;

		if (0 <= hprime && hprime <= 1) {
			r1 = c;
			g1 = x;
			b1 = 0;
		} else if (1 <= hprime && hprime <= 2) {
			r1 = x;
			g1 = c;
			b1 = 0;
		} else if (2 <= hprime && hprime <= 3) {
			r1 = 0;
			g1 = c;
			b1 = x;
		} else if (3 <= hprime && hprime <= 4) {
			r1 = 0;
			g1 = x;
			b1 = c;
		} else if (4 <= hprime && hprime <= 5) {
			r1 = x;
			g1 = 0;
			b1 = c;
		} else if (5 <= hprime && hprime <= 6) {
			r1 = c;
			g1 = 0;
			b1 = x;
		} else {
			r1 = 0;
			g1 = 0;
			b1 = 0;
		}

		double m = l - c / 2;
		return new Color((float) (r1 + m), (float) (g1 + m), (float) (b1 + m));

	}

	// tests if a double is approximately an integer
	private boolean isInteger(double d) {
		if (Math.abs(d - Math.floor(d)) <= tolerance || Math.abs(d - Math.ceil(d)) <= tolerance) {
			return true;
		}
		return false;
	}

	// tests if a complex number is a Gaussian integer
	public boolean isGaussianInteger() {
		if (isInteger(this.re) && isInteger(this.im)) {
			return true;
		}
		return false;
	}

	// tests if a complex number is an Eisenstein integer
	public boolean isEisensteinInteger() {
		double b = this.im * 2.0 / Math.sqrt(3.0);
		double a = this.re + (b / 2.0);
		if (isInteger(a) && isInteger(b)) {
			return true;
		}
		return false;
	}

	// return abs/modulus/magnitude
	public double abs() {
		return Math.hypot(this.re, this.im);
	}

	// return angle/phase/argument, normalized to be between -pi and pi
	public double phase() {
		return Math.atan2(im, re);
	}

	// return angle/phase in degrees
	public double phaseDegrees() {
		return phase() * 180.0 / Math.PI;
	}

	// return a new Complex object whose value is (this + b)
	public Complex plus(Complex b) {
		Complex a = this; // invoking object
		double real = a.re + b.re;
		double imag = a.im + b.im;
		return new Complex(real, imag);
	}

	// return a new Complex object whose value is (this - b)
	public Complex minus(Complex b) {
		Complex a = this;
		double real = a.re - b.re;
		double imag = a.im - b.im;
		return new Complex(real, imag);
	}

	// return a new Complex object whose value is (this * b)
	public Complex times(Complex b) {
		Complex a = this;
		double real = a.re * b.re - a.im * b.im;
		double imag = a.re * b.im + a.im * b.re;
		return new Complex(real, imag);
	}

	// return a new object whose value is (this * alpha)
	public Complex scale(double alpha) {
		return new Complex(alpha * this.re, alpha * this.im);
	}

	// return a new Complex object whose value is the conjugate of this
	public Complex conjugate() {
		return new Complex(re, -im);
	}

	// return a new Complex object whose value is the reciprocal of this
	public Complex reciprocal() {
		double scale = this.re * this.re + this.im * this.im;
		return new Complex(this.re / scale, -this.im / scale);
	}

	// return the real or imaginary part
	public double re() {
		return this.re;
	}

	public double im() {
		return this.im;
	}

	// return a / b
	public Complex divides(Complex b) {
		Complex a = this;
		return a.times(b.reciprocal());
	}

	// return a^b
	public Complex pow(Complex b) {
		Complex a = this;
		Complex f1 = new Complex(Math.pow(Math.E, -1 * b.im * a.phase()));
		Complex f2 = new Complex(Math.pow(a.abs(), b.re));
		Complex f3 = ComplexPolar(1, Math.log(Math.pow(a.abs(), b.im)));
		Complex f4 = ComplexPolar(1, b.re * a.phase());
		return f1.times(f2).times(f3).times(f4);
	}

	// return a new Complex object whose value is the complex exponential of a
	public static Complex exp(Complex a) {
		return E.pow(a);
	}

	// return a new Complex object whose value is the complex sine of this
	public static Complex sin(Complex a) {
		return exp(a.times(I)).minus(exp(a.times(NEGATIVEONE).times(I))).divides(I.times(TWO));
	}

	// return a new Complex object whose value is the complex cosine of a
	public static Complex cos(Complex a) {
		return exp(a.times(I)).plus(exp(a.times(NEGATIVEONE).times(I))).divides(TWO);
	}

	// return a new Complex object whose value is the complex tangent of a
	public static Complex tan(Complex a) {
		return sin(a).divides(cos(a));
	}

	// return a new Complex object whose value is the complex cosecant of a
	public static Complex csc(Complex a) {
		return ONE.divides(sin(a));
	}

	// return a new Complex object whose value is the complex secant of a
	public static Complex sec(Complex a) {
		return ONE.divides(cos(a));
	}

	// return a new Complex object whose value is the complex cotangent of a
	public static Complex cot(Complex a) {
		return ONE.divides(tan(a));
	}

	// return a new complex object whose value is the complex logarithm of a
	public static Complex ln(Complex a) {
		return new Complex(Math.log(a.abs())).plus(new Complex(a.phase()).times(I));
	}

	// returns a new complex object whose value is the complex arcsin of a
	public static Complex arcsin(Complex a) {
		Complex p1 = I.times(NEGATIVEONE);
		Complex p2 = ONE.minus(a.pow(TWO));
		p2 = p2.pow(new Complex(0.5));
		p2 = p2.plus(I.times(a));
		p2 = ln(p2);
		return p1.times(p2);
	}

	// returns a new complex object whose value is the complex arccos of a
	public static Complex arccos(Complex a) {
		return PI.divides(TWO).minus(arcsin(a));
	}

	// returns a new complex object whose value is the complex arctan of a
	public static Complex arctan(Complex a) {
		Complex p1 = I.divides(TWO);
		Complex p2 = ONE.minus(I.times(a));
		p2 = p2.divides(ONE.plus(I.times(a)));
		p2 = ln(p2);
		return p1.times(p2);
	}

	// returns a new complex object whose value is the complex arccsc of a
	public static Complex arccsc(Complex a) {
		return arcsin(ONE.divides(a));
	}

	// returns a new complex object whose value is the complex arcsec of a
	public static Complex arcsec(Complex a) {
		return arccos(ONE.divides(a));
	}

	// returns a new complex object whose value is the complex arccot of a
	public static Complex arccot(Complex a) {
		return arctan(ONE.divides(a));
	}

	// returns a new complex object whose value is the complex sinh of a
	public static Complex sinh(Complex a) {
		return E.pow(a).minus(E.pow(a.times(NEGATIVEONE))).divides(TWO);
	}

	// returns a new complex object whose value is the complex cosh of a
	public static Complex cosh(Complex a) {
		return E.pow(a).plus(E.pow(a.times(NEGATIVEONE))).divides(TWO);
	}

	// returns a new complex object whose value is the complex tanh of a
	public static Complex tanh(Complex a) {
		return sinh(a).divides(cosh(a));
	}

	// returns a new complex object whose value is the complex csch of a
	public static Complex csch(Complex a) {
		return ONE.divides(sinh(a));
	}

	// returns a new complex object whose value is the complex sech of a
	public static Complex sech(Complex a) {
		return ONE.divides(cosh(a));
	}

	// returns a new complex object whose value is the complex coth of a
	public static Complex coth(Complex a) {
		return ONE.divides(tanh(a));
	}

	// returns a new complex number whose value is the complex arcsinh of a
	public static Complex arcsinh(Complex a) {
		Complex p1 = a.pow(TWO).plus(ONE);
		p1 = p1.pow(new Complex(0.5));
		p1 = p1.plus(a);
		p1 = ln(p1);
		return p1;
	}

	// returns a new complex number whose value is the complex arccosh of a
	public static Complex arccosh(Complex a) {
		Complex p1 = a.pow(TWO).minus(ONE);
		p1 = p1.pow(new Complex(0.5));
		p1 = p1.plus(a);
		p1 = ln(p1);
		return p1;
	}

	// returns a new complex number whose value is the complex arctanh of a
	public static Complex arctanh(Complex a) {
		Complex p1 = new Complex(0.5);
		Complex p2 = a.plus(ONE);
		p2 = p2.divides(ONE.minus(a));
		p2 = ln(p2);
		return p1.times(p2);
	}

	// returns a new complex number whose value is the complex arccsch of a
	public static Complex arccsch(Complex a) {
		return arcsinh(ONE.divides(a));
	}

	// returns a new complex number whose value is the complex arcsech of a
	public static Complex arcsech(Complex a) {
		return arccosh(ONE.divides(a));
	}

	// returns a new complex number whose value is the complex arccothh of a
	public static Complex arccoth(Complex a) {
		return arctanh(ONE.divides(a));
	}

	// See Section 3.3.
	public boolean equals(Object x) {
		if (x == null)
			return false;
		if (this.getClass() != x.getClass())
			return false;
		Complex that = (Complex) x;
		return (this.re == that.re) && (this.im == that.im);
	}

}