import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;
import org.opensourcephysics.controls.AbstractSimulation;
import org.opensourcephysics.controls.SimulationControl;
import org.opensourcephysics.display.Interactive;
import org.opensourcephysics.display.InteractiveMouseHandler;
import org.opensourcephysics.display.InteractivePanel;
import org.opensourcephysics.frames.PlotFrame;
import org.w3c.dom.events.MouseEvent;

public class App extends AbstractSimulation implements InteractiveMouseHandler{

    Circuit circuit = new Circuit();
    PlotFrame frameMain = new PlotFrame("t", "V", "n vs membrane voltage");
    PlotFrame frameN = new PlotFrame("t", "n", "n vs time");
    PlotFrame frameM = new PlotFrame("t", "m", "m vs time");
    PlotFrame frameH = new PlotFrame("t", "h", "h vs time");
    double I_ext;

    public App() {
        frameMain.setInteractiveMouseHandler(this);
    }

   public void handleMouseAction(InteractivePanel panel, java.awt.event.MouseEvent evt) {
        switch(panel.getMouseAction()) {
            /*case InteractivePanel.MOUSE_CLICKED:
                long startTime = System.currentTimeMillis();
                circuit.Iext = 7;
                while(System.currentTimeMillis() - startTime < 20);

                startTime = System.currentTimeMillis();
                circuit.Iext = 8;
                while(System.currentTimeMillis() - startTime < 40);

                 circuit.Iext = 0;
                break;*/


          case InteractivePanel.MOUSE_PRESSED :
                circuit.Iext = I_ext;
                break;

                case InteractivePanel.MOUSE_RELEASED :
                circuit.Iext = 0;
                break;
        }
    }

    public void initialize() {
        double dt = control.getDouble("dt");
        this.I_ext = control.getDouble("External current (mA)");
        setStepsPerDisplay((int) (0.1 / dt));
        circuit.setState();
        frameMain.clearData();
        frameN.clearData();
        frameM.clearData();
        frameH.clearData();


        frameMain.setBounds(0, 0, 900, 400);
        frameN.setBounds(0, 400, 300, 300);
        frameM.setBounds(300, 400, 300, 300);
        frameH.setBounds(600, 400, 300, 300);
        //frameMain.setPreferredMinMax(20, 70, -75, 40);
    }

    public void doStep() {
        frameMain.append(0, circuit.state[4], circuit.state[0]);
        frameN.append(1, circuit.state[4], circuit.state[1]);
        frameM.append(2, circuit.state[4], circuit.state[2]);
        frameH.append(3, circuit.state[4], circuit.state[3]);
        circuit.step();


    }

    public void reset() {
        control.setValue("dt", 0.01);
        control.setValue("External current (mA)", 5);
        frameMain.clearData();
    }

    public static void main(String[] args) {
        SimulationControl.createApp(new App());
    }
}
