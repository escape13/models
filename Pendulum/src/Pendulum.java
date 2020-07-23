import org.opensourcephysics.display.Drawable;
import org.opensourcephysics.display.DrawingPanel;
import org.opensourcephysics.numerics.*;

import java.awt.*;

public class Pendulum implements ODE, Drawable {
    int pixRadius = 6;
    double omega;
    double[] state = new double[] {0, 0, 0}; // alpha, d(alpha)/dt, t
    ODESolver solver = new Verlet(this);

    public double[] getState() {
        return state;
    }

    public void getRate(double[] state, double[] rate) {
        rate[0] = state[1];
        rate[1] = -omega * Math.sin(state[0]);
        rate[2] = 1;
    }

    public void setState(double omega, double theta, double dtheta) {
        this.omega = omega;
        state[0] = theta;
        state[1] = dtheta;
    }

    public void setStepSize(double dt) {
        solver.setStepSize(dt);
    }

    public void step() {
        solver.step();
    }

    public void draw(DrawingPanel panel, Graphics g) {
        int xpivot = panel.xToPix(0);
        int ypivot = panel.yToPix(0);
        int xpix = panel.xToPix(9.8D / omega * Math.sin(state[0]));
        int ypix = panel.yToPix(-9.8D / omega * Math.cos(state[0]));
        g.setColor(Color.BLACK);
        g.drawLine(xpivot, ypivot, xpix, ypix);
        g.setColor(Color.RED);
        g.fillOval(xpix - pixRadius, ypix - pixRadius, 2*pixRadius, 2*pixRadius);

    }
}
