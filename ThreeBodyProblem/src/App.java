import org.opensourcephysics.controls.AbstractSimulation;
import org.opensourcephysics.controls.SimulationControl;
import org.opensourcephysics.frames.PlotFrame;


public class App extends AbstractSimulation {

    PlotFrame frame = new PlotFrame("x", "y", "Three-Body motion");
    ThreeBody threeBody = new ThreeBody();

    public void initialize() {
        double dt = control.getDouble("dt");
        double x1 = control.getDouble("x1");
        double vx1 = control.getDouble("vx1");
        double y1 = control.getDouble("y1");
        double vy1 = control.getDouble("vy1");
        double x2 = control.getDouble("x2");
        double vx2 = control.getDouble("vx2");
        double y2 = control.getDouble("y2");
        double vy2 = control.getDouble("vy2");
        double x3 = control.getDouble("x3");
        double vx3 = control.getDouble("vx3");
        double y3 = control.getDouble("y3");
        double vy3 = control.getDouble("vy3");
        threeBody.initialize(new double[] {x1, vx1, y1, vy1, x2, vx2, y2, vy2, x3, vx3, y3, vy3, 0});
        stepsPerDisplay = (int)(0.1 / dt);
        threeBody.setStepSize(dt);

        frame.setPreferredMinMax(-1.5, 1.5, -1.5, 1.5);
        frame.setSquareAspect(true);
        frame.setBounds(0, 0, 700, 700);
        frame.addDrawable(threeBody);

    }

    public void doStep() {
        threeBody.step();
        frame.setMessage("t = "+decimalFormat.format(threeBody.state[12]));
    }

    public void reset() {
        control.setValue("dt", 0.01);
        control.setValue("x1", 0.97000436);
        control.setValue("vx1", 0.46666203685);
        control.setValue("y1", -0.24308753);
        control.setValue("vy1", 0.43236573);
        control.setValue("x2", -0.97000436);
        control.setValue("vx2", 0.46662036685);
        control.setValue("y2", 0.24308753);
        control.setValue("vy2", 0.432366573);
        control.setValue("x3", 0);
        control.setValue("vx3", -0.93240737);
        control.setValue("y3", 0);
        control.setValue("vy3", -0.86473146);
        control.setValue("t", 0);
    }

    public static void main(String[] args) {
        SimulationControl.createApp(new App());
    }
}
