package teamran.guimanager.Cnn_Object;

public class obj_Pooling {
    public double pooling_input[][][];
    public double pooling_output[][][];
    public int stride = 2;
    public int hop = 2;
    public int padding = 0;
    public boolean is_max_pooling = true;

    public void ExecutePooling() {

        int row = 0;
        int column = 0;
        int pooling_output_row = 0;
        int pooling_output_column = 0;
        double[][][] pooling_result_final = new double[0][0][0];



        if(this.is_max_pooling){
            double current_pixel = 0;
            double max_pixel = 0;
            if(this.padding > 0){
                for(int inputindex = 0; inputindex < this.pooling_input.length; inputindex++){
                    double[][] padded_input = new double[this.pooling_input[inputindex].length+this.padding*2][this.pooling_input[inputindex][0].length+this.padding*2];
                    int row_difference = padded_input.length - this.pooling_input[inputindex].length;
                    int column_difference = padded_input[0].length - this.pooling_input[inputindex][0].length;
                    row = 0;
                    column = 0;
                    for(int pad_row = 0; pad_row < padded_input.length; pad_row++){
                        for(int pad_column = 0; pad_column < padded_input[0].length; pad_column++){
                            if(pad_row >= row_difference/2 && pad_row <= row_difference/2 + this.pooling_input[inputindex].length - 1){
                                if(pad_column >= column_difference/2 && pad_column <= column_difference/2 + this.pooling_input[inputindex][0].length - 1){
                                    padded_input[pad_row][pad_column] = this.pooling_input[inputindex][row][column];
                                    column++;
                                    if(column >= this.pooling_input[inputindex][0].length){
                                        column = 0;
                                        row++;
                                    }
                                }
                            }
                        }
                    }
                    this.pooling_input[inputindex] = padded_input;
                }
                double[][][] pooling_result_temp = new double[this.pooling_input.length][this.pooling_input[0].length/this.stride][this.pooling_input[0][0].length/this.stride];
                for(int inputindex = 0; inputindex < this.pooling_input.length; inputindex++){
                    while(this.pooling_input[inputindex].length % this.stride != 0){
                        double[][] temp_2d_array  = new double[this.pooling_input[inputindex].length-1][this.pooling_input[inputindex].length-1];
                        for(row = 0; row < temp_2d_array.length; row++){
                            for(column = 0; column <temp_2d_array[0].length; column++){
                                temp_2d_array[row][column] = this.pooling_input[inputindex][row][column];
                            }
                        }
                        this.pooling_input[inputindex] = temp_2d_array;
                    }
                    for(row = 0; row < this.pooling_input[inputindex].length; row+=hop){
                        for (column = 0; column < this.pooling_input[inputindex][0].length; column+=hop){
                            for(int stride_row = 0; stride_row < this.stride; stride_row++){
                                for(int stride_column = 0; stride_column < this.stride; stride_column++){
                                    current_pixel = this.pooling_input[inputindex][row + stride_row][column + stride_column];
                                    if(current_pixel > max_pixel){
                                        max_pixel = current_pixel;
                                    }
                                }
                            }
                            pooling_result_temp[inputindex][pooling_output_row][pooling_output_column] = max_pixel;
                            max_pixel = 0;
                            pooling_output_column++;
                        }
                        pooling_output_column = 0;
                        pooling_output_row++;
                    }
                    pooling_output_row = 0;
                }
                pooling_result_final = pooling_result_temp;
            }
            else if(this.padding == 0){
                double[][][] pooling_result_temp = new double[this.pooling_input.length][this.pooling_input[0].length/this.stride][this.pooling_input[0][0].length/this.stride];
                for(int inputindex = 0; inputindex < this.pooling_input.length; inputindex++){
                    while(this.pooling_input[inputindex].length % this.stride != 0){
                        double[][] temp_2d_array  = new double[this.pooling_input[inputindex].length-1][this.pooling_input[inputindex].length-1];
                        for(row = 0; row < temp_2d_array.length; row++){
                            for(column = 0; column <temp_2d_array[0].length; column++){
                                temp_2d_array[row][column] = this.pooling_input[inputindex][row][column];
                            }
                        }
                        this.pooling_input[inputindex] = temp_2d_array;
                    }
                    for(row = 0; row < this.pooling_input[inputindex].length; row+=hop){
                        for (column = 0; column < this.pooling_input[inputindex][0].length; column+=hop){
                            for(int stride_row = 0; stride_row < this.stride; stride_row++){
                                for(int stride_column = 0; stride_column < this.stride; stride_column++){
                                    current_pixel = this.pooling_input[inputindex][row + stride_row][column + stride_column];
                                    if(current_pixel > max_pixel){
                                        max_pixel = current_pixel;
                                    }
                                }
                            }
                            pooling_result_temp[inputindex][pooling_output_row][pooling_output_column] = max_pixel;
                            max_pixel = 0;
                            pooling_output_column++;
                        }
                        pooling_output_column = 0;
                        pooling_output_row++;
                    }
                    pooling_output_row = 0;
                }
                pooling_result_final = pooling_result_temp;
            }
        }
        else if(!this.is_max_pooling){
            double sum_pixel = 0;
            double num_of_pixel = 0;

            if(this.padding > 0){
                for(int inputindex = 0; inputindex < this.pooling_input.length; inputindex++){
                    double[][] padded_input = new double[this.pooling_input[inputindex].length+this.padding*2][this.pooling_input[inputindex][0].length+this.padding*2];
                    int row_difference = padded_input.length - this.pooling_input[inputindex].length;
                    int column_difference = padded_input[0].length - this.pooling_input[inputindex][0].length;
                    row = 0;
                    column = 0;
                    for(int pad_row = 0; pad_row < padded_input.length; pad_row++){
                        for(int pad_column = 0; pad_column < padded_input[0].length; pad_column++){
                            if(pad_row >= row_difference/2 && pad_row <= row_difference/2 + this.pooling_input[inputindex].length - 1){
                                if(pad_column >= column_difference/2 && pad_column <= column_difference/2 + this.pooling_input[inputindex][0].length - 1){
                                    padded_input[pad_row][pad_column] = this.pooling_input[inputindex][row][column];
                                    column++;
                                    if(column >= this.pooling_input[inputindex][0].length){
                                        column = 0;
                                        row++;
                                    }
                                }
                            }
                        }
                    }
                    this.pooling_input[inputindex] = padded_input;
                }
                double[][][] pooling_result_temp = new double[this.pooling_input.length][this.pooling_input[0].length/this.stride][this.pooling_input[0][0].length/this.stride];
                for(int inputindex = 0; inputindex < this.pooling_input.length; inputindex++){
                    while(this.pooling_input[inputindex].length % this.stride != 0){
                        double[][] temp_2d_array  = new double[this.pooling_input[inputindex].length-1][this.pooling_input[inputindex].length-1];
                        for(row = 0; row < temp_2d_array.length; row++){
                            for(column = 0; column <temp_2d_array[0].length; column++){
                                temp_2d_array[row][column] = this.pooling_input[inputindex][row][column];
                            }
                        }
                        this.pooling_input[inputindex] = temp_2d_array;
                    }
                    for(row = 0; row < this.pooling_input[inputindex].length; row+=hop){
                        for (column = 0; column < this.pooling_input[inputindex][0].length; column+=hop){
                            for(int stride_row = 0; stride_row < this.stride; stride_row++){
                                for(int stride_column = 0; stride_column < this.stride; stride_column++){
                                    sum_pixel += this.pooling_input[inputindex][row + stride_row][column + stride_column];
                                    num_of_pixel += 1;
                                }
                            }
                            pooling_result_temp[inputindex][pooling_output_row][pooling_output_column] = sum_pixel/num_of_pixel;
                            pooling_output_column++;
                        }
                        pooling_output_column = 0;
                        pooling_output_row++;
                    }
                    pooling_output_row = 0;
                    sum_pixel = 0;
                    num_of_pixel = 0;
                }
                pooling_result_final = pooling_result_temp;
            }
            else if(this.padding == 0){
                double[][][] pooling_result_temp = new double[this.pooling_input.length][this.pooling_input[0].length/this.stride][this.pooling_input[0][0].length/this.stride];
                for(int inputindex = 0; inputindex < this.pooling_input.length; inputindex++){
                    while(this.pooling_input[inputindex].length % this.stride != 0){
                        double[][] temp_2d_array  = new double[this.pooling_input[inputindex].length-1][this.pooling_input[inputindex].length-1];
                        for(row = 0; row < temp_2d_array.length; row++){
                            for(column = 0; column <temp_2d_array[0].length; column++){
                                temp_2d_array[row][column] = this.pooling_input[inputindex][row][column];
                            }
                        }
                        this.pooling_input[inputindex] = temp_2d_array;
                    }
                    for(row = 0; row < this.pooling_input[inputindex].length; row+=hop){
                        for (column = 0; column < this.pooling_input[inputindex][0].length; column+=hop){
                            for(int stride_row = 0; stride_row < this.stride; stride_row++){
                                for(int stride_column = 0; stride_column < this.stride; stride_column++){
                                    sum_pixel += this.pooling_input[inputindex][row + stride_row][column + stride_column];
                                    num_of_pixel += 1;
                                }
                            }
                            pooling_result_temp[inputindex][pooling_output_row][pooling_output_column] = sum_pixel/num_of_pixel;
                            pooling_output_column++;
                        }
                        pooling_output_column = 0;
                        pooling_output_row++;
                    }
                    pooling_output_row = 0;
                    sum_pixel = 0;
                    num_of_pixel = 0;
                }
                pooling_result_final = pooling_result_temp;
            }
        }
        setPooling_output(pooling_result_final);
    }


    public void setPooling_stride(int stride){
        if(stride > 1){
            this.stride = stride;
            this.hop = stride;
        }
        else{
            this.stride = 2;
        }
    }

    public void setPooling_padding(int pad_size){
        if(pad_size > 0) {
            this.padding = pad_size;
        }
        else{
            this.padding = 0;
        }
    }

    public double[][][] getPooling_input(){
        return pooling_input;
    }

    public void setPooling_input(double[][][] pooling_input){
        this.pooling_input = pooling_input;
    }

    public double[][][] getPooling_output(){
        return pooling_output;
    }

    public void setPooling_output(double[][][] pooling_output){
        this.pooling_output = pooling_output;
    }

    public void setPoolingType(boolean is_max_pooling){
        if(is_max_pooling){
            this.is_max_pooling = true;
        }
        else if(!is_max_pooling){
            this.is_max_pooling = false;
        }
    }
}