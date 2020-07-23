import org.opensourcephysics.controls.AbstractSimulation;
import org.opensourcephysics.controls.SimulationControl;
import org.opensourcephysics.frames.DisplayFrame;
import org.opensourcephysics.frames.PlotFrame;

public class App extends AbstractSimulation {
    DisplayFrame displayFrame = new DisplayFrame("x", "y", "Pendulum");
    PlotFrame plotFrame = new PlotFrame("t", "theta", "Angle versus Time");
    PlotFrame errorFrame = new PlotFrame("t", "error", "Error over time");
    Pendulum pendulum = new Pendulum();
    double theta, omega, l, e0;

    public void initialize() {
        pendulum.state[2] = 0;
        displayFrame.clearDrawables();

        theta = control.getDouble("initial theta");
        double dtheta = control.getDouble("initial angular velocity");
        omega = control.getDouble("omega squared");
        double dt = control.getDouble("dt");

        theta = theta * (Math.PI / 180D);

        stepsPerDisplay = (int)(0.1 / dt);
        l = 9.8D / omega;
        e0 = 9.8 * 3 * (1 - Math.cos(theta));

        pendulum.setState(omega, theta, dtheta);
        pendulum.setStepSize(dt);
        displayFrame.addDrawable(pendulum);
        displayFrame.setPreferredMinMax(-1, 1, -9.8 / omega - 0.5, 0.5);
        displayFrame.setBounds(0, 0, 550, 700);
        plotFrame.setBounds(450, 0, 400, 400);
        errorFrame.setBounds(850, 0, 400, 400);
        //errorFrame.setPreferredMinMax(0, 8, -0.000001, 0.000082);
    }

    public void doStep() {
        plotFrame.append(0, pendulum.state[2], pendulum.state[0]);
        double pot = 3 * 9.8 * (1 - Math.cos(pendulum.state[0]));
        double kin = 0.5 * Math.pow(l * pendulum.state[1], 2);
        double total = pot + kin;
        errorFrame.append(0, pendulum.state[2], Math.abs((total - e0))/e0);

        pendulum.step();
    }

    public void reset() {
        control.setValue("initial theta", 30);
        control.setValue("initial angular velocity", 0);
        control.setValue("omega squared", 9.8/3);
        control.setValue("dt", 0.01);
    }

    public static void main(String[] args) {
        SimulationControl.createApp(new App());
    }
}
