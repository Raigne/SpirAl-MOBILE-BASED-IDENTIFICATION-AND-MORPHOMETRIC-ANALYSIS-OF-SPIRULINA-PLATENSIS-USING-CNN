package teamran.guimanager.Ann_Object;
import java.lang.Math;

public class ActivationFcn {

    /**
     * ActivationFcn Class applies the Activation Functions,
     * such as LogSig, TanSig, PureLin to the Neurons.
     */

    private double output; /** */

    /**
     * Activation Function Constructor
     * The constructor is not used in this class
     */
    public ActivationFcn(){

    }

    public void finalize() throws Throwable {

    }

    /**
     * double function for applying Log Sigmoid Activation Function
     * @param input
     */
    public double executeLogsig(double input){
        this.output = 1 / (1 + Math.exp(input*-1)); // LogSig Activation Function
        return this.output; // returns output after applying activation function
    }

    /**
     * double function for applying Pure Linear Activation Function
     * @param input
     */
    public double executePurelin(double input){
        this.output = input; //! PureLin Activation Function
        return this.output; // returns output after applying activation function
    }

    /**
     * double function for applying Tan Sigmoid Activation Function
     * @param input
     */
    public double executeTansig(double input){
        this.output = Math.exp(input) - (Math.exp(input*-1)) / Math.exp(input) + (Math.exp(input*-1)); //! TanSig Activation Function
        return this.output; // returns output after applying activation function
    }

    public void initActivation(){

    }
}