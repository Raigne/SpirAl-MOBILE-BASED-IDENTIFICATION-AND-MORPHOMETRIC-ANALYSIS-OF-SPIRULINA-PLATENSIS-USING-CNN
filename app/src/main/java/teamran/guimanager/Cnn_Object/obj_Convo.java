package teamran.guimanager.Cnn_Object;

import java.lang.Math;

public class obj_Convo {

    public double convo_input[][][];
    public double convo_result[][][];
    public double convo_kernel[][][];
    public double relu_result[][][];
    public double convo_bias[];
    public double padding_result[][][];
    public double average_image[][];

    public void ExecuteConvo() {

        int input_column_index = 0;
        int input_row_index = 0;
        int column_position = 0;
        int row_position = 0;
        int image_index = 0;
        int kernel_index = 0;
        int feature_index = 0;
        int feature_count = 8;
        int bias_index = 0;
        int current_kernel = 0;
        int kernel_count = getConvo_kernel().length;
        int convo_dimension = getConvo_input()[0].length - (getConvo_kernel()[0].length - 1);
        double feature_result[][][];
        double pixel_product = 0;
        double pixel_sum = 0;
        double feature_sum = 0;

        feature_result = new double [getConvo_kernel().length][convo_dimension][convo_dimension];

        for(kernel_index = current_kernel; kernel_index < kernel_count; kernel_index++) {
            for(image_index = 0; image_index < getConvo_input().length; image_index++) {
                for(input_column_index = 0; input_column_index < convo_dimension; input_column_index++) {
                    for(input_row_index = 0; input_row_index < convo_dimension; input_row_index++) {
                        for(column_position = 0; column_position < getConvo_kernel()[kernel_index].length; column_position++) {
                            for(row_position = 0; row_position < getConvo_kernel()[kernel_index].length; row_position++) {
                                pixel_product = convo_input[image_index][input_column_index + column_position][input_row_index + row_position] * convo_kernel[kernel_index][column_position][row_position];
                                pixel_sum += pixel_product;
                            }
                        }
                        feature_result[feature_index][input_column_index][input_row_index] = pixel_sum + convo_bias[bias_index];
                        pixel_product = 0;
                        pixel_sum = 0;
                    }
                }
                feature_index++;
                kernel_index++;
            }
            bias_index++;
            kernel_index--;
            current_kernel = kernel_index;
        }

        convo_result = new double [feature_count][convo_dimension][convo_dimension];

        if(kernel_count > 8) {

            image_index = 0;
            feature_index = 0;

            do {

                for(input_column_index = 0; input_column_index < convo_dimension; input_column_index++) {
                    for(input_row_index = 0; input_row_index < convo_dimension; input_row_index++) {
                        feature_sum = feature_result[image_index][input_column_index][input_row_index] + feature_result[image_index + 1][input_column_index][input_row_index] + feature_result[image_index + 2][input_column_index][input_row_index] + feature_result[image_index + 3][input_column_index][input_row_index] + feature_result[image_index + 4][input_column_index][input_row_index] + feature_result[image_index + 5][input_column_index][input_row_index] + feature_result[image_index + 6][input_column_index][input_row_index] + feature_result[image_index + 7][input_column_index][input_row_index];
                        convo_result[feature_index][input_column_index][input_row_index] = feature_sum;
                    }
                }

                image_index += 8;
                feature_index++;

            }while(image_index != kernel_count);
        }

        else {
            for(image_index = 0; image_index < feature_result.length; image_index++) {
                for(input_column_index = 0; input_column_index < convo_dimension; input_column_index++) {
                    for(input_row_index = 0; input_row_index < convo_dimension; input_row_index++) {
                        convo_result[image_index][input_column_index][input_row_index] = feature_result[image_index][input_column_index][input_row_index];
                    }
                }
            }
        }

        setConvo_result(convo_result);
        relu_result = new double[convo_result.length][convo_dimension][convo_dimension];

        for(image_index = 0; image_index < convo_result.length; image_index++) {
            for(input_column_index = 0; input_column_index < convo_dimension; input_column_index++){
                for(input_row_index = 0; input_row_index < convo_dimension; input_row_index++){
                    if(convo_result[image_index][input_column_index][input_row_index] <= 0){
                        relu_result[image_index][input_column_index][input_row_index] = 0;
                    }
                    else{
                        relu_result[image_index][input_column_index][input_row_index] = convo_result[image_index][input_column_index][input_row_index];
                    }
                }
            }
        }

        setRelu_result(relu_result);
    }

    public void ExecutePadding(double[][][]padding_input, int padding_size) {
        int pad_number = padding_size;
        int padding_dimensions = padding_input[0].length + (pad_number * 2);
        int padding_matrix_size = padding_input[0].length;
        int column_position = 0;
        int row_position = 0;
        int feature_index = 0;
        int feature_count = padding_input.length;

        padding_result = new double[feature_count][padding_dimensions][padding_dimensions];

        for(feature_index = 0; feature_index < feature_count; feature_index++) {
            for(column_position = 0; column_position < padding_matrix_size; column_position++) {
                for(row_position = 0; row_position < padding_matrix_size; row_position++) {
                    padding_result[feature_index][column_position + pad_number][row_position + pad_number] = padding_input[feature_index][column_position][row_position];
                }
            }
        }
        setPadding_result(padding_result);
    }

    public double [][][] ExecuteZeroCenterNormalization() {

        double normalized_input [][][] = new double [getConvo_input().length][getConvo_input()[0].length][getConvo_input()[0].length];

        for(int img_count = 0; img_count < getConvo_input().length; img_count++){
            for(int column = 0; column < getConvo_input()[0][0].length; column++){
                for (int row = 0; row < getConvo_input()[0].length; row++){
                    normalized_input[img_count][row][column] = getConvo_input()[img_count][row][column] - this.average_image[row][column];
                    normalized_input[img_count][row][column] = Math.round(normalized_input[img_count][row][column] * 10000D)/10000D;
                }
            }
        }

		/*for(int img_count = 0; img_count < getConvo_input().length; img_count++){
			for(int column = 0; column < getConvo_input()[0][0].length; column++){
				for (int row = 0; row < getConvo_input()[0].length; row++){
					System.out.print(normalized_input[img_count][row][column] + "\t");
				}
				System.out.println("");
			}
		}*/

        return normalized_input;
    }

    public double[][][] getConvo_input() {
        return convo_input;
    }

    public void setConvo_input(double[][][] convo_input) {
        this.convo_input = convo_input;
    }

    public double[][][] getConvo_result() {
        return convo_result;
    }

    public void setConvo_result(double[][][] convo_result) {
        this.convo_result = convo_result;
    }

    public double[][][] getConvo_kernel() {
        return convo_kernel;
    }

    public void setConvo_kernel(double[][][] convo_kernel) {
        this.convo_kernel = convo_kernel;
    }

    public double[] getConvo_bias() {
        return convo_bias;
    }

    public void setConvo_bias(double[] convo_bias) {
        this.convo_bias = convo_bias;
    }

    public double[][][] getRelu_result() {
        return relu_result;
    }

    public void setRelu_result(double[][][] relu_result) {
        this.relu_result = relu_result;
    }

    public double[][][] getPadding_result() {
        return padding_result;
    }

    public void setPadding_result(double[][][] padding_result) {
        this.padding_result = padding_result;
    }

    public void setAverage_image(double[][] average_image){
        this.average_image = average_image;
    }
}