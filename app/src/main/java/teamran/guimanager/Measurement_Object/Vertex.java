package teamran.guimanager.Measurement_Object;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import java.lang.Math;
import android.graphics.Bitmap;

public class Vertex {
    List<Mat> channels = new ArrayList<Mat>(3);
    private Mat image_ref = new Mat();
    private Mat image_temp = new Mat();
    private static final int MAX_THRESHOLD = 100;
    private int maxCorners = 10;
    private Random rng = new Random(12345);

    public Vertex(Bitmap bitmap) {

        Utils.bitmapToMat(bitmap, image_ref);
        if (image_ref.empty()) {
            System.err.println("Cannot read image");
            System.exit(0);
        }
        //Imgproc.cvtColor(src, srcGray, Imgproc.COLOR_BGR2GRAY);
        ArrayList<MatOfPoint> contours = new ArrayList<>();
        ArrayList<Double> contour_list = new ArrayList<>();
        Imgproc.cvtColor(this.image_ref,this.image_temp,Imgproc.COLOR_BGR2HSV);
        Core.split(this.image_temp, channels);
        this.image_temp = channels.get(2);
        Imgproc.threshold(this.image_temp, image_temp, 0, 255, Imgproc.THRESH_BINARY + Imgproc.THRESH_OTSU);
        Imgproc.medianBlur(this.image_temp, image_temp, 3);
        Imgproc.findContours(image_temp, contours, new Mat(), Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE);
        Imgproc.drawContours(image_ref, contours, -1, new Scalar(255,255,255), 1);

        // Create and set up the window.
        // Image img = HighGui.toBufferedImage(image_ref);
        // Use the content pane's default BorderLayout. No need for
        // setLayout(new BorderLayout());
        // Display the window
        update();
    }

    private void update() {

        int sum = 0;
        double micrometer = 0.49;
        double angle = 0;
        double prod = 0;

        maxCorners = Math.max(maxCorners, 1);
        MatOfPoint corners = new MatOfPoint();
        double qualityLevel = 0.01;
        double minDistance = 10;
        int blockSize = 3, gradientSize = 3;
        boolean useHarrisDetector = false;
        double k = 0.04;
        Mat copy = image_ref.clone();
        Imgproc.goodFeaturesToTrack(image_temp, corners, maxCorners, qualityLevel, minDistance, new Mat(), blockSize, useHarrisDetector, k);
        System.out.println("** Number of corners detected: " + corners.rows());
        int[] cornersData = new int[(int) (corners.total() * corners.channels())];
        int[] finalData = new int[(int) (corners.total() * corners.channels())];
        int [] arr = new int[cornersData.length / 2];
        double[] distance = new double[cornersData.length - 1];
        double[] distance2 = new double[3];
        corners.get(0, 0, cornersData);
        int radius = 1;

        int len = arr.length;
        sort(arr, 0, len - 1);
        System.out.println("");

        for(int i = 0; i < corners.rows(); i++) {
            System.out.println("Values:	" + arr[i]);
        }

        for(int j = 0; j < corners.rows(); j++) {
            for(int i = 0; i < corners.rows(); i++) {
                if(arr[j] == cornersData[i * 2]) {
                    finalData[j * 2] = arr[j];
                    finalData[j * 2 + 1] = cornersData[i * 2 + 1];
                }
            }
        }

        System.out.println("");

        for(int i = 0; i < corners.rows(); i++) {
            System.out.println("Values:	" + finalData[i * 2] + "\t" + finalData[i* 2 + 1]);
        }

        int a1 = finalData[0];
        int b1 = finalData[1];
        int a2 = finalData[finalData.length - 2];
        int b2 = finalData[finalData.length - 1];

        double dis = Math.sqrt((Math.pow((a2 - a1), 2)) + (Math.pow((b2 - b1), 2)));
        double leng = dis * micrometer;

        System.out.println("\n"+ "Length = " + leng);

        /*for(int i = 0; i < corners.rows() / 2; i++) {
        	int x = 0;
        	int x2 = 0;
        	int y = 0;
        	int y2 = 0;

        	x = finalData[i *2];
        	x2 = finalData[(i + 1) * 2];
        	y = finalData[i * 2];
        	y2 = finalData[(i + 1) * 2];

        	distance[i] = Math.sqrt((Math.pow((x2 - x), 2)) + (Math.pow((y2 - y), 2)));
        }

        for(int i = 0; i < corners.rows() / 2; i++) {
        	sum += distance[i];
        }

        prod = micrometer * sum;
    	*/

        int c1 = finalData[0];
        int d1 = finalData[1];
        int c2 = finalData[2];
        int d2 = finalData[3];

        double helix = Math.sqrt((Math.pow((c2 - c1), 2)) + (Math.pow((d2 - d1), 2))) * micrometer;

        System.out.println("\n"+ "Helix = " + helix);

        int x1 = finalData[0];
        int y1 = finalData[1];

        int x2 = finalData[2];
        int y2 = finalData[3];

        int x3 = finalData[4];
        int y3 = finalData[5];

        double a = 0;
        double b = 0;
        double c = 0;

        a = Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2);
        b = Math.pow(x3 - x2, 2) + Math.pow(y3 - y2, 2);
        c = Math.pow(x3 - x1, 2) + Math.pow(y3 - y1, 2);

        angle = Math.acos((a + b - c) / Math.sqrt(4 * a * b) );

        System.out.println("\n" + "Polarity = " + angle);

        int area = 0;
        Mat image;
        double image_output [][] = new double [image_temp.cols()][image_temp.rows()];

        //String image_dir = pathname;
        image_output = new double[image_temp.rows()][this.image_temp.cols()];
        for(int row = 0; row<this.image_temp.rows();row++){
            for(int column = 0; column < image_temp.cols();column++){
                image_output[row][column] = image_temp.get(row,column)[0];
            }
        }

        for(int i = 0; i < image_temp.cols(); i++) {
            for(int j = 0; j < image_temp.rows(); j++) {
                if(image_output[j][i] == 0) {
                    area++;
                }
            }
        }

        double sagot = 0;

        sagot = (dis / area) * micrometer;
        System.out.println("\n" + "Width = " + sagot);

    }

    public int partition(int arr[], int low, int high)
    {
        int pivot = arr[high];
        int i = (low-1); // index of smaller element
        for (int j=low; j<high; j++)
        {
            // If current element is smaller than or
            // equal to pivot
            if (arr[j] <= pivot)
            {
                i++;
                // swap arr[i] and arr[j]
                int temp = arr[i];
                arr[i] = arr[j];
                arr[j] = temp;
            }
        }

        // swap arr[i+1] and arr[high] (or pivot)
        int temp = arr[i+1];
        arr[i+1] = arr[high];
        arr[high] = temp;

        return i+1;
    }


    /* The main function that implements QuickSort()
      arr[] --> Array to be sorted,
      low  --> Starting index,
      high  --> Ending index */
    public void sort(int arr[], int low, int high)
    {
        if (low < high)
        {
            /* pi is partitioning index, arr[pi] is
              now at right place */
            int pi = partition(arr, low, high);

            // Recursively sort elements before
            // partition and after partition
            sort(arr, low, pi-1);
            sort(arr, pi+1, high);
        }
    }

    public static void main(String[] args) {
        // Load the native OpenCV library
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        // Schedule a job for the event dispatch thread:
        // creating and showing this application's GUI.
        Vertex.main(args);
    }
}