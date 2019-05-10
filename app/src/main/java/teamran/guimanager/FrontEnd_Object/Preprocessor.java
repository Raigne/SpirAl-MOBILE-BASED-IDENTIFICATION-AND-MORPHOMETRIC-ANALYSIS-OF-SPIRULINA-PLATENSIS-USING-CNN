package teamran.guimanager.FrontEnd_Object;
import org.opencv.android.Utils;
import org.opencv.core.*;
import android.graphics.Bitmap;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import java.util.ArrayList;
import java.util.List;
import java.lang.*;
import static org.opencv.imgproc.Imgproc.RETR_EXTERNAL;

public class Preprocessor {

    List<Mat> channels = new ArrayList<Mat>(3);
    Mat image_ref = new Mat(); /** class attribute used for reference of the image **/
    Mat image_temp = new Mat(); /** temporary image of the class for passing of values **/
    Mat img = new Mat();
    ArrayList<Mat> image_contours = new ArrayList<>(); /**list of contours found in an image/frame **/
    double[][] image_output; /** output of a single image **/
    double[][] padded_image_output; /** output of a padded single image **/
    ArrayList<double[][]> cotours_output = new ArrayList<>(); /** output of contours of image **/
    ArrayList<double[][]> padded_contours_output = new ArrayList<>(); /** out put of a padded contours of image **/
    int num_of_erosion; /** determine the number of time to erode an image **/
    int num_of_dilation; /** determine the number of time to dilate an image **/
    int low_threshold; /** low threshold for canny edge detection, any edges with intensity gradient below low_threshold are sure to be edges **/
    int high_threshold; /** high threshold for canny edge detection, any edges with intensity gradient above high_threshold are sure to be edges **/
    int kernel_size; /** blurring kernel size **/
    double sigma_x; /** gaussian kernel standard deviation in x direction **/
    int new_size_x; /** new size of image in x **/
    int new_size_y; /** new size of image in y **/

    /**
     * function used to take image directory and assign it to attribute image_ref
     * @param image_dir
     * @return none
     */
    public void SetImage(Bitmap bitmap){
        Utils.bitmapToMat(bitmap, image_ref);
    }
    /**
     * a function that binarize this.image_temp and save it in this.image_temp
     * @param lower_val1
     * @param lower_val2
     * @param lower_val3
     * @param higher_val1
     * @param higher_val2
     * @param higher_val3
     */
    public void SetImageAdaptiveThreshHold() {
        Imgproc.adaptiveThreshold(image_ref, image_temp, 255, Imgproc.ADAPTIVE_THRESH_MEAN_C,
                Imgproc.THRESH_BINARY_INV, 15,2);
    }

    public void SetImageBinarize(int lower_val1, int lower_val2, int lower_val3, int higher_val1, int higher_val2, int higher_val3){
        Scalar lowerThreshold = new Scalar(lower_val1,lower_val2,lower_val3);
        Scalar higherThreshold = new Scalar(higher_val1,higher_val2,higher_val3);
        Core.inRange(this.image_temp,lowerThreshold,higherThreshold,this.image_temp);
    }

    /**
     * sets image_temp to image_ref
     */
    public void SetImageOriginal(){
        this.image_temp = this.image_ref;
    }
    /**
     * sets this.image_temp to grayscale
     * @return none
     */
    public void SetImageGRAY(){
        Imgproc.cvtColor(this.image_ref,this.image_temp,Imgproc.COLOR_BGR2GRAY);
    }
    /**
     * sets this.image_temp to LAB colorspace
     * @return none
     */
    public void SetImageLAB(){
        Imgproc.cvtColor(this.image_ref,this.image_temp,Imgproc.COLOR_BGR2Lab);
    }

    /**
     * sets this.image_temp to HSV colorspace
     * @return none
     */
    public void SetImageHSV(){
        Imgproc.cvtColor(this.image_ref,this.image_temp,Imgproc.COLOR_BGR2HSV);
    }

    /**
     * sets this.image_temp to HLS colorspace
     * @return none
     */
    public void SetImageHLS(){
        Imgproc.cvtColor(this.image_ref,this.image_temp,Imgproc.COLOR_BGR2HLS);
    }

    public void SplitImageChannel() {
        Core.split(this.image_temp, channels);
        this.image_temp = channels.get(2);
    }

    public void SetImageThreshold() {
        Imgproc.threshold(this.image_temp, image_temp, 0, 255, Imgproc.THRESH_BINARY + Imgproc.THRESH_OTSU);
    }

    public void SetImageMedianBlur(int kernel_size) {
        Imgproc.medianBlur(this.image_temp, image_temp, kernel_size);
    }

    /**
     * saves this.image_temp to your repository/Img/test'test_number'.jpg
     * @param test_number
     */
    public void SaveTempImage(int test_number){
     //   Imgcodecs.imwrite("img/test"+test_number+".jpg",this.image_temp);
    }

    /**
     * sets this.image_temp to be dilated to num_of_dilation
     * @param num_of_dilation
     */
    public void SetImageDilated(int num_of_dilation){
        for(int i = 0; i < num_of_dilation; i++){
            Imgproc.dilate(this.image_temp, this.image_temp, new Mat());
        }
    }

    /**
     * sets this.image_temp to be eroded to num_of_erosion
     * @param num_of_erosion
     */
    public void SetImageEroded(int num_of_erosion){
        for(int i=0; i<num_of_erosion; i++){
            Imgproc.erode(this.image_temp, this.image_temp, new Mat());
        }
    }

    /**
     * flips this.image_temp if it is binarize
     * @return none
     */
    public void SetImageFlipped(){
        for(int row=0;row<this.image_temp.rows();row++){
            for(int column=0; column<this.image_temp.cols();column++){
                if(this.image_temp.get(row,column)[0]>0){
                    this.image_temp.put(row,column,0);
                }
                else{
                    this.image_temp.put(row,column,255);
                }
            }
        }
    }

    /**
     * saves contours of this.image_temp to your repository/Img/contours/i.jpg where i is an increasing integer
     * @return none
     */
    public void SaveContours(){
        for(int i=0; i< this.image_contours.size(); i++){
            Mat img = this.image_contours.get(i);
            //Imgcodecs.imwrite("img/contours/i" + i +".jpg",img);
        }
    }

    /**
     * sets contours of this.image_temp to this.contours as a ArrayList<Mat>
     * @return none
     */
    public void SetContours(){
        ArrayList<MatOfPoint> contours = new ArrayList<>();

        Imgproc.findContours(this.image_temp ,contours ,new Mat() ,RETR_EXTERNAL ,Imgproc.CHAIN_APPROX_SIMPLE);
        for(int i=0; i< contours.size(); i++) {
            Rect r = Imgproc.boundingRect(contours.get(i));
            Mat img = this.image_ref.submat(r);
            this.image_contours.add(img);
        }
    }

    /**
     * set this.image_output with the given image_dir as a 3darray
     * @param image_dir
     */
    public void SetImageOutput(String image_dir){
        Mat image;
        //image = Imgcodecs.imread(image_dir);
        //for(int row = 0; row<image.rows();row++){
          //  for(int column = 0; column<image.cols();column++){
            //    this.image_output[row][column] = image.get(row,column)[0];
          //  }
       // }
    }

    /**
     * set this.image_output with the given contour_number as a 3darray
     * @param contour_number
     */
    public void SetImageOutput(int contour_number){
        this.image_output = new double[this.image_contours.get(contour_number).rows()][this.image_contours.get(contour_number).cols()];
        for(int row = 0; row<this.image_contours.get(contour_number).rows();row++){
            for(int column = 0; column<this.image_contours.get(contour_number).cols();column++){
                this.image_output[row][column] = this.image_contours.get(contour_number).get(row,column)[0];
            }
        }
    }

    public void SetImageOutput(){
        this.image_output = new double[this.image_temp.rows()][this.image_temp.cols()];
        for(int row = 0; row<this.image_temp.rows();row++){
            for(int column = 0; column<this.image_temp.cols();column++){
                this.image_output[row][column] = this.image_temp.get(row,column)[0];
            }
        }
    }

    /**
     * get this.image_output
     * @return this.image_output
     */
    public double[][] GetImageOutput(){
        return this.image_output;
    }

    /**
     * sets this.num_of_erosion with num_of_erosion
     * @param num_of_erosion
     */
    public void SetNumOfErosion(int num_of_erosion){
        this.num_of_erosion = num_of_erosion;
    }

    /**
     * sets this.num_of_dilation with num_of_dilation
     * @param num_of_dilation
     */
    public void SetNumOfDilation(int num_of_dilation){
        this.num_of_dilation = num_of_dilation;
    }

    /**
     * gets this.num_of_erosion
     * @return this.num_of_erosion
     */
    public int GetNumOfErosion(){
        return this.num_of_erosion;
    }

    /**
     * gets this.num_of_dilation
     * @return this.num_of_dilation
     */
    public int GetNumOfDilation(){
        return this.num_of_dilation;
    }

    /**
     * sets the size of x for resizing of image
     * @param new_size_x
     */
    public void SetNewSizeX(int new_size_x){
        this.new_size_x = new_size_x;
    }

    /**
     * sets the size of y for resizing of image
     * @param new_size_y
     */
    public void SetNewSizeY(int new_size_y){
        this.new_size_y = new_size_y;
    }

    /**
     * gets the size of x for resizing of image
     * @return new_size_x
     */
    public int GetNewSizeX(){
        return this.new_size_x;
    }

    /**
     * gets the size of y for resizing of image
     * @return new_size_y
     */
    public int GetNewSizeY(){
        return this.new_size_y;
    }

    /**
     * sets low_treshold for edge
     * @param low_threshold
     */
    public void SetLowThreshold(int low_threshold){
        this.low_threshold = low_threshold;
    }

    /**
     * sets high_threshold for edge
     * @param high_threshold
     */
    public void SetHighThreshold(int high_threshold){
        this.high_threshold = high_threshold;
    }

    /**
     * gets low_threshold for edge
     * @return low_treshold
     */
    public int GetLowThreshold(){
        return this.low_threshold;
    }

    /**
     * gets high_threshold for edge
     * @return
     */
    public int GetHighTreshold(){
        return  this.high_threshold;
    }

    /**
     * sets kernel_size for edge
     * @param kernel_size
     */
    public void SetKernelSizeEdge(int kernel_size){
        this.kernel_size = kernel_size;
    }

    /**
     * sets sigma_x for blur
     * @param sigma_x
     */
    public void SetSigmaXEdge(double sigma_x){
        this.sigma_x = sigma_x;
    }

    /**
     * gets kernel size for blur
     * @return kernel_size
     */
    public int GetKernelSizeEdge(){
        return this.kernel_size;
    }

    /**
     * gets sigma_x for edge
     * @return sigma_x
     */
    public double GetSigmaXEdge(){
        return this.sigma_x;
    }

    /**
     * set all contours in image_contours to grayscale
     */



    public void SetContoursGray(){
        if(this.image_contours.isEmpty()){
            return;
        }
        else{
            for(int iterator = 0; iterator < this.image_contours.size(); iterator++){
                Imgproc.cvtColor(this.image_contours.get(iterator), this.image_contours.get(iterator), Imgproc.COLOR_BGR2GRAY);
            }
        }
    }

    public void SetContoursBinarize(int lower_val1, int lower_val2, int lower_val3, int higher_val1, int higher_val2, int higher_val3){
        Scalar lowerThreshold = new Scalar(lower_val1,lower_val2,lower_val3);
        Scalar higherThreshold = new Scalar(higher_val1,higher_val2,higher_val3);

        if(this.image_contours.isEmpty()){
            return;
        }
        else{
            for(int iterator = 0; iterator < this.image_contours.size(); iterator++){
                Core.inRange(this.image_contours.get(iterator),lowerThreshold,higherThreshold,this.image_contours.get(iterator));
            }
        }
    }

    /**
     * gets contours_output
     * @return this.contours_output;
     */
    public ArrayList<double[][]> GetContoursOutput(){
        return this.cotours_output;
    }

    /**
     * convert all contours to 2darray and add them to list contours_output
     */
    public void SetContoursOutput(){
        double[][] temp;
        if(this.image_contours.isEmpty()){
            return;
        }
        else{
            for(int iterator = 0; iterator < this.image_contours.size(); iterator++){
                temp = new double[this.image_contours.get(iterator).rows()][this.image_contours.get(iterator).cols()];
                for(int row = 0; row < this.image_contours.get(iterator).rows(); row++) {
                    for (int column = 0; column < this.image_contours.get(iterator).cols(); column++) {
                        temp[row][column] = this.image_contours.get(iterator).get(row,column)[0];
                    }
                }
                this.cotours_output.add(temp);
            }
        }
    }

    /**
     * set image_temp to edge given low and high threshold use only if grayscale colorspace
     * @param low_threshold
     * @param high_threshold
     */
    public void SetImageEdge(int low_threshold, int high_threshold){
        Imgproc.Canny(this.image_temp, this.image_temp, low_threshold, high_threshold);
    }

    /**
     * set image_temp to blur given kernel_size and sigma_x using Gaussian Blur
     * @param kernel_size
     * @param sigma_x
     */
    public void SetImageBlur(int kernel_size, double sigma_x){
        Imgproc.GaussianBlur(this.image_temp, this.image_temp, new Size(kernel_size, kernel_size), sigma_x);
    }



    /**
     * set the new size of an image
     * @param new_x
     * @param new_y
     */
    public void SetImageSize(int new_x, int new_y){
        Size size = new Size(new_x, new_y);
        Imgproc.resize(this.image_ref, this.image_ref, size);
    }

    /**
     * sets the this.contours_output to a given padded contours
     * @param padded_image_size
     */
    public void SetOutputContoursPadded(int padded_image_size){
        int row_difference;
        int column_difference;
        for(int iteration = 0; iteration < this.cotours_output.size(); iteration++){

            ArrayList<Double> temp_image = new ArrayList<>();
            for(int row = 0; row < this.cotours_output.get(iteration).length; row++){
                for(int column = 0; column < this.cotours_output.get(iteration)[0].length; column++){
                    temp_image.add(this.cotours_output.get(iteration)[row][column]);
                }
            }

            double[][] padded_image = new double[padded_image_size][padded_image_size];
            int temp_val = 0;
            row_difference = padded_image.length - this.cotours_output.get(iteration).length;
            column_difference = padded_image[0].length - this.cotours_output.get(iteration)[0].length;

            for(int pad_row = 0; pad_row < padded_image.length; pad_row++){
                for(int pad_column = 0; pad_column < padded_image[0].length; pad_column++){
                    if(pad_row >= row_difference/2 && pad_row <= row_difference/2 + this.cotours_output.get(iteration).length - 1){
                        if(pad_column >= column_difference/2 && pad_column <= column_difference/2 + this.cotours_output.get(iteration)[0].length - 1){
                            padded_image[pad_row][pad_column] = temp_image.get(temp_val);
                            temp_val++;

                        }
                    }
                }
            }
            this.padded_contours_output.add(padded_image);
        }
    }

    /**
     * gets the output of a padded contours
     * @return this.padded_contours_output
     */
    public ArrayList<double[][]> GetPaddedContoursOutput(){
        return this.padded_contours_output;
    }

    /**
     * sets the this.image_output to a given padded image
     * @param padded_image_size
     */
    public void SetOutputImagePadded(int padded_image_size){
        int row_difference;
        int column_difference;

        ArrayList<Double> temp_image = new ArrayList<>();
        for(int row = 0; row < this.image_output.length; row++){
            for(int column = 0; column < this.image_output[0].length; column++){
                temp_image.add(this.image_output[row][column]);
            }
        }

        double[][] padded_image = new double[padded_image_size][padded_image_size];
        int temp_val = 0;
        row_difference = padded_image.length - this.image_output.length;
        column_difference = padded_image[0].length - this.image_output[0].length;

        for(int pad_row = 0; pad_row < padded_image.length; pad_row++){
            for(int pad_column = 0; pad_column < padded_image[0].length; pad_column++){
                if(pad_row >= row_difference/2 && pad_row <= row_difference/2 + this.image_output.length - 1){
                    if(pad_column >= column_difference/2 && pad_column <= column_difference/2 + this.image_output[0].length - 1){
                        padded_image[pad_row][pad_column] = temp_image.get(temp_val);
                        temp_val++;
                    }
                }
            }
        }
        this.padded_image_output = padded_image;
    }

    /**
     * gets output of a padded single image
     * @return this.padded_image_output
     */
    public double[][] GetPaddedImageOutput(){
        return this.padded_image_output;
    }

    /**
     * saves the padded image for checking
     */
    public void SavePaddedImage(){
        Mat new_mat = new Mat(300, 300, CvType.CV_8UC1);
        for(int row = 0; row < this.padded_image_output.length; row++){
            for(int column = 0; column < this.padded_image_output[0].length; column++){
                new_mat.put(row, column, this.padded_image_output[row][column]);
            }
        }
   //     Imgcodecs.imwrite("Img/Test.jpg", new_mat);
    }
}
