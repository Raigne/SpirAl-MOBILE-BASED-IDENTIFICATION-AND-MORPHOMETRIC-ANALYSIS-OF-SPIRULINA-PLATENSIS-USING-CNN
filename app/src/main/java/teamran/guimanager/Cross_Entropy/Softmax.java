package teamran.guimanager.Cross_Entropy;

public class Softmax {
    public String output;
    public double softmax_input [];
    double max_temp = 0;
    int index_position = 0;
    double sum_of_exp = 0.0;
    double exp_value [] = new double [softmax_input.length];
    public double softmax_output[] = new double [softmax_input.length];
    double cross_entropy_output = 0;

    public void ExecuteBackEnd() {

        for(index_position = 0; index_position < softmax_input.length; index_position++) {
            exp_value[index_position] = Math.exp(softmax_input[index_position]);
            sum_of_exp += exp_value[index_position];
        }
        int pos = 0;
        for(index_position = 0; index_position < exp_value.length; index_position++) {
            softmax_output[index_position] = exp_value [index_position] / sum_of_exp;
            //System.out.println("Softmax: " + softmax_output[index_position]);
            if(softmax_output[index_position] > max_temp) {
                max_temp = softmax_output[index_position];
                pos = index_position;
            }
        }

        if(pos == 0){
            output = "STRESSED";
        }
        else {
            output = "NON-STRESSED";
        }

        cross_entropy_output = -1 * Math.log10(max_temp);
        //log.i("Cross Entropy: " + cross_entropy_output);

    }
}