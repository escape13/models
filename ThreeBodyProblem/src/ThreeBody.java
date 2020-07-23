import org.opensourcephysics.display.Circle;
import org.opensourcephysics.display.Drawable;
import org.opensourcephysics.display.DrawingPanel;
import org.opensourcephysics.display.Trail;
import org.opensourcephysics.numerics.*;

import java.awt.*;

public class ThreeBody implements ODE, Drawable {
    int n = 3;
    double[] state = new double[13];
    double[] force = new double[6];
    double[] zeros = new double[6];
    ODESolver solver = new Verlet(this);
    Mass mass1 = new Mass(), mass2 = new Mass(), mass3 = new Mass();

    void initialize(double[] initState) {
        System.arraycopy(initState, 0, state, 0, 13);
        mass1.clear();
        mass2.clear();
        mass3.clear();
        mass1.setXY(state[0], state[2]);
        mass2.setXY(state[4], state[6]);
        mass3.setXY(state[8], state[10]);
    }

    public double[] getState() {
        return state;
    }

    public void computeForce(double[] state) {
        System.arraycopy(zeros, 0, force, 0, force.length);
        for(int i = 0; i < n; ++i) {
            for(int j = i + 1; j < n; ++j) {
                double dx = state[i*4] - state[j*4];
                double dy = state[i*4 + 2] - state[j*4 + 2];
                double r2 = dx*dx + dy*dy;
                double r3 = r2 * Math.sqrt(r2);
                double fx = dx/r3;
                double fy = dy/r3;
                force[i*2] -= fx;
                force[i*2 + 1] -= fy;
                force[j*2] += fx;
                force[j*2 + 1] += fy;
            }
        }
    }

    public void getRate(double[] state, double[] rate) {
        computeForce(state);
        for(int i = 0; i < n; ++i) {
            int i4 = i * 4;
            rate[i4] = state[i4 + 1];
            rate[i4 + 1] = force[2 * i];
            rate[i4 + 2] = state[i4 + 3];
            rate[i4 + 3] = force[2 * i + 1];
        }
        rate[state.length - 1] = 1;
    }

    public void setStepSize(double dt) {
        solver.setStepSize(dt);
    }

    public void step() {
        solver.step();
        mass1.setXY(state[0], state[2]);
        mass2.setXY(state[4], state[6]);
        mass3.setXY(state[8], state[10]);
    }

    public void draw(DrawingPanel panel, Graphics g) {
        mass1.draw(panel, g);
        mass2.draw(panel, g);
        mass3.draw(panel, g);
    }

    class Mass extends Circle {
        Trail trail = new Trail();

        public void draw(DrawingPanel panel, Graphics g) {
            trail.draw(panel, g);
            super.draw(panel, g);
        }

        void clear() {
            trail.clear();
        }

        public void setXY(double x, double y) {
            super.setXY(x, y);
            trail.addPoint(x, y);
        }
    }
}
