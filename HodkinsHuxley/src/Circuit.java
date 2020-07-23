import org.opensourcephysics.numerics.*;

public class Circuit implements ODE {
    double[] state = new double[5];
    ODESolver solver = new RK45MultiStep(this);
    public double vK = -77, vNa = 50, vL = -54.4, gK = 36, gNa = 120, gL = 0.3, Iext;

    public void setState() {
        state[0] = -60;
        state[1] = 0;
        state[4] = 0;
    }

    public double[] getState() {
        return state;
    }

    public void getRate(double[] state, double[] rate) {
        double alphaN = 0.01D * (state[0] + 55.0D) / (1.0D - Math.exp(-(state[0] + 55.0D) / 10.0D));double betaN = 0.125D * Math.exp(-(state[0] + 65.0D) / 80.0D);
        double alphaM = 0.1D * (state[0] + 40.0D) / (1.0D - Math.exp(-(state[0] + 40.0D) / 10.0D));
        double betaM = 4.0D * Math.exp(-(state[0] + 65.0D) / 18.0D);
        double alphaH = 0.07D * Math.exp(-(state[0] + 65.0D) / 20.0D);
        double betaH = 1.0D / (1.0D + Math.exp(-(state[0] + 35.0D) / 10.0D));

        rate[0] = Iext - (gL * (state[0] - vL) + gNa * Math.pow(state[2], 3) * state[3] * (state[0] - vNa) + gK * Math.pow(state[1], 4) * (state[0] - vK));
        rate[1] = alphaN * (1 - state[1]) - betaN * state[1];
        rate[2] = alphaM * (1 - state[2]) - betaM * state[2];
        rate[3] = alphaH * (1 - state[3]) - betaH * state[3];
        rate[4] = 1;
    }


    public void step() {
        solver.step();
    }
}
