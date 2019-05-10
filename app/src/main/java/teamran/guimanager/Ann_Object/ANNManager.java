package teamran.guimanager.Ann_Object;

public class ANNManager {

    private static final int ANN_MANAGER_INPUT_LAYER_STATE = 1; /** private static integer that specifies the input layer state*/
    private static final int ANN_MANAGER_OUTPUT_LAYER_STATE = 2; /** private static integer that specifies the output layer state */
    private static final int ANN_MANAGER_FINAL_STATE = 3; /** private static integer that specifies the final state flag */

    private static final int EXECUTE_LOGSIG = 1; /** private static integer that specifies the use of Log Sigmoid activation function*/
    private static final int EXECUTE_TANSIG = 2; /** private static integer that specifies the use of Tan Sigmoid activation function*/
    private static final int EXECUTE_PURELIN = 3; /** private static integer that specifies the use of Pure Linear activation function*/

    private static final int NUM_OF_LAYERS = 2; /** private static integer that specifies the total number of layers*/
    private static final int NUM_OF_INPUTS = 32; /** private static integer that specifies the total number of inputs to be taken by the neuron object*/
    private static final int NUM_OF_CLASSIFICATION = 2;

    public double[] input = new double[NUM_OF_INPUTS]; /** double 1D array that holds the input*/

    double[] output_layer_neuron1_weight1_temp_1 = new double[input.length]; /** double 1D array that holds the weights for ezch input*/
    double[] output_layer_neuron2_weight2_temp_1 = new double[input.length]; /** double 1D array that holds the weights of the next layer inputs of the next state*/

    public double[] previous_layer_out = new double[NUM_OF_CLASSIFICATION]; /** double 1D array that holds the output of the previous layer and serve as input for the next state */


    Neuron output_layer_neuron1_a = new Neuron(); /** Instantiation of first Neuron object*/
    Neuron output_layer_neuron2_b = new Neuron(); /** Instantiation of second Neuron object*/

    public int ann_manager_state = ANN_MANAGER_INPUT_LAYER_STATE; /** integer attribute that specifies the current layer of the state machine and initial declaration of the initial layer*/

    public void AnnManagerInit(){

        output_layer_neuron1_weight1_temp_1 = new double[]{ 0.0460,    0.0136,    0.1009,   0.1093,   -0.1493,  -0.0578,   -0.0820,   -0.1220,    0.2315,    0.0904,   -0.2496,	-0.1825,    0.2678,    0.5008,    0.2676,    0.1195,    0.1242,   -0.0575,   -0.3827,   -0.2592,   -0.0259,    0.0174, -0.0389,   -0.0029,   -0.1113,   -0.1308,   -0.2775,    0.1970,    0.1148,   -0.2227,   -0.2007,   -0.1481};
        output_layer_neuron2_weight2_temp_1 = new double[]{-0.0550,   -0.0214,   -0.0905,   -0.1009,    0.1521,    0.0414,    0.0766,    0.1275,   -0.2196,   -0.0925,    0.2369,	0.1598,   -0.2534,   -0.5140,   -0.2563,   -0.1215,   -0.1339,    0.0648,    0.3826,    0.2534,    0.0364,   -0.0054, 0.0239,   -0.0027,    0.1046,    0.1436,    0.2672,   -0.2033,   -0.1263,    0.2184,    0.2054,    0.1337};

        output_layer_neuron1_a.setInput(this.input);
        output_layer_neuron1_a.setNum_input(this.input.length);
        output_layer_neuron1_a.setWeight(this.output_layer_neuron1_weight1_temp_1);
        output_layer_neuron1_a.setBias(-0.1037);
        output_layer_neuron1_a.setActivationFcn(EXECUTE_PURELIN);

        output_layer_neuron2_b.setInput(this.input);
        output_layer_neuron2_b.setNum_input(this.input.length);
        output_layer_neuron2_b.setWeight(this.output_layer_neuron2_weight2_temp_1);
        output_layer_neuron2_b.setBias(0.1037);
        output_layer_neuron2_b.setActivationFcn(EXECUTE_PURELIN);

    }

    public void ExecuteAnnManager() {

        int state_index = 0;  /** integer attribute which determines which state the  */

        do{
            switch(ann_manager_state) {

                case ANN_MANAGER_INPUT_LAYER_STATE:

                    output_layer_neuron1_a.ExecuteNeuron();

                    output_layer_neuron2_b.ExecuteNeuron();

                    ann_manager_state = ANN_MANAGER_OUTPUT_LAYER_STATE;

                    break;

                case ANN_MANAGER_OUTPUT_LAYER_STATE:

                    previous_layer_out[0] = output_layer_neuron1_a.getOutput();

                    previous_layer_out[1] = output_layer_neuron2_b.getOutput();

                    //System.out.println("\n" + previous_layer_out[0] + " " + previous_layer_out[1] + "\n");

                    ann_manager_state = ANN_MANAGER_FINAL_STATE;

                    break;

                default:

                    ann_manager_state = ANN_MANAGER_INPUT_LAYER_STATE;

                    break;
            }

            state_index++;

        }while(state_index < NUM_OF_LAYERS);


    }
}