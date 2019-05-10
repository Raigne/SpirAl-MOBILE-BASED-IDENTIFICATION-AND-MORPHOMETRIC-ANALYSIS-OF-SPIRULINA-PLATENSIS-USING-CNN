package teamran.guimanager.Ann_Object;

public class Neuron {

    private static final int EXECUTE_LOGSIG = 1; /** static integer that specifies the use of Log Sigmoid Activation Function */
    private static final int EXECUTE_TANSIG = 2; /** static integer that specifies the use of Tan Sigmoid Activation Function */
    private static final int EXECUTE_PURELIN = 3; /** static integer that specifies the use of Pure Linear Activation Function */

    private int num_input = 0; /** private integer attribute to determine total number of inputs for neuron */
    private int selected_activation_function = 0; /** private integer that holds selected activation function is to be executed */
    private double[] input = new double[num_input]; /** private double 1D array which holds the input for neuron */
    private int num_output = 0; /** private integer attribute to determine total number of outputs for neuron */
    private double[] weight = new double[num_input]; /** private double 1D array that holds the weights of the inputs to the neuron */
    private double output = 0; /** private double output that holds the output of the neuron */
    private double bias = 0; /** private double that holds the bias to the neuron */

    public ActivationFcn activation_function = new ActivationFcn();  /** Instantiation of activation function control class */

    public Neuron(){

    }

    public void finalize() throws Throwable {

    }

    public void ExecuteNeuron(){
        for(int index = 0; index < num_input; index++ ){
            output += weight[index] * input[index];
        }

        output = output  + bias;

        switch(selected_activation_function){

            case EXECUTE_LOGSIG:

                output = activation_function.executeLogsig(output);
                break;

            case EXECUTE_TANSIG:
                output = activation_function.executeTansig(output);
                break;

            case EXECUTE_PURELIN:
                output = activation_function.executePurelin(output);
                break;

            default:
                break;
        }

        setOutput(output);
    }

    public double getBias(){
        return this.bias;
    }

    public int getNum_input(){
        return this.num_input;
    }

    public int getNum_output(){
        return this.num_output;
    }

    public double[] getInput(){
        return this.input;
    }

    public double getOutput(){
        return this.output;
    }

    public double[] getWeight(){
        return this.weight;
    }

    public int getActivationFcn(){
        return this.selected_activation_function;
    }

    public void setBias(double bias){
        this.bias = bias;
    }

    public void setInput(double[] input){
        this.input = input;
    }

    public void setOutput(double output){
        this.output = output;
    }

    public void setWeight(double[] weight){
        this.weight = weight;
    }

    public void setNum_input(int num_input){
        this.num_input = num_input;
    }

    public void setNum_output(int num_output){
        this.num_input = num_output;
    }

    public void setActivationFcn(int selected_activation_function){
        this.selected_activation_function = selected_activation_function;
    }
}