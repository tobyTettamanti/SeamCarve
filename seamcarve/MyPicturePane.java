package seamcarve;

import javafx.scene.layout.BorderPane;
import support.seamcarve.*;

/**
 * This class is your seam carving picture pane.  It is a subclass of PicturePane,
 * an abstract class that takes care of all the drawing, displaying, carving, and
 * updating of seams and images for you.  Your job is to override the abstract
 * method of PicturePane that actually finds the lowest cost seam through
 * the image.
 * <p>
 * See method comments and handouts for specifics on the steps of the seam carving algorithm.
 *
 * @version 01/17/2019
 */

public class MyPicturePane extends PicturePane {


    /**
     * The constructor accepts an image filename as a String and passes
     * it to the superclass for displaying and manipulation.
     *
     * @param pane
     * @param filename
     */
    public MyPicturePane(BorderPane pane, String filename) {
        super(pane, filename);

    }


    /**
     * In this method, you'll implement the dynamic programming algorithm
     * that you learned on the first day of class to find the lowest cost seam from the top
     * of the image to the bottom. BEFORE YOU START make sure you fully understand how the algorithm works
     * and what it's doing.
     * See the handout for some helpful resources and use hours/piazza to clarify conceptual blocks
     * before you attempt to write code.
     * <p>
     * This method returns an array of ints that represents a seam.  This size of this array
     * is the height of the image.  Each entry of the seam array corresponds to one row of the
     * image.  The data in each entry should be the x coordinate of the seam in this row.
     * For example, given the below "image" where s is a seam pixel and - is a non-seam pixel
     * <p>
     * - s - -
     * s - - -
     * - s - -
     * - - s -
     * <p>
     * <p>
     * the following code will properly return a seam:
     * <p>
     * int[] currSeam = new int[4];
     * currSeam[0] = 1;
     * currSeam[1] = 0;
     * currSeam[2] = 1;
     * currSeam[3] = 2;
     * return currSeam;
     * <p>
     * <p>
     * This method is protected so it is accessible to the class MyPicturePane and is not
     * accessible to other classes. PLEASE DO NOT CHANGE THIS!
     *
     * @return the lowest cost seam of the current image
     */

    /*
    This method returns the lowest-cost seam of the given image.
     */
    protected int[] findLowestCostSeam() {
        return findSeamCarve(getPixelValues());
    }

    /*
    This method returns an array of ints representing the indexes of the int of the pixels of an image that should be removed in a seam.
    The method first calculates a table of total costs and then the needed direction to go in order to achieve the seam.
    Then, it follows the direction table to create lowest cost seam.
     */
    private int[] findSeamCarve(int[][] pixelValues) {
        int[][] costArray = new int[this.getPicHeight()][this.getPicWidth()];
        int[][] dirArray = new int[this.getPicHeight()][this.getPicWidth()];
        costArray[this.getPicHeight() - 1] = pixelValues[this.getPicHeight() - 1];
        for (int row = this.getPicHeight() - 2; row >= 0; row--) {
            for (int col = 0; col < this.getPicWidth(); col++) {
                    if (col - 1 > 0 && costArray[row + 1][col - 1] < costArray[row + 1][col]) {
                        if (col + 1 < this.getPicWidth() && costArray[row + 1][col + 1] < costArray[row + 1][col - 1]) { // right is min
                            dirArray[row][col] = 1;
                            costArray[row][col] = costArray[row + 1][col + 1] + pixelValues[row][col];
                        } else { // left is min
                            dirArray[row][col] = -1;
                            costArray[row][col] = costArray[row + 1][col - 1] + pixelValues[row][col];
                        }
                    } else if (col + 1 < this.getPicWidth() && costArray[row + 1][col + 1] < costArray[row + 1][col]) { // right is min
                        dirArray[row][col] = 1;
                        costArray[row][col] = costArray[row + 1][col + 1] + pixelValues[row][col];
                    } else { // middle is min
                        dirArray[row][col] = 0;
                        costArray[row][col] = costArray[row + 1][col] + pixelValues[row][col];
                    }
                }
            }
        int[] seamCarve = new int[this.getPicHeight()];
        seamCarve[0] = argMin(costArray[0]);
        for (int row = 0; row < this.getPicHeight()-1; row++) {
                seamCarve[row + 1] = seamCarve[row] + dirArray[row][seamCarve[row]];
        }
        return seamCarve;
    }

    /*
    This method takes in an array of ints that represent the costs of the seam and returns the INDEX of the smallest cost.
     */
    private int argMin(int[] pixelCosts) {
        int tracker = 0;
        for (int index = 1; index < this.getPicWidth(); index++) {
            if (pixelCosts[index] < pixelCosts[tracker])
                tracker = index;
        }
        return tracker;
    }

    /*
    This method returns a table of the image's height x image's width pixel values.
     */
    private int[][] getPixelValues() {
        int[][] pixelImportance = new int[this.getPicHeight()][this.getPicWidth()];
        for (int row = 0; row < this.getPicHeight(); row++) {
            for (int col = 0; col < this.getPicWidth(); col++) {
                pixelImportance[row][col] = getPixelImportance(row, col);
            }
        }
        return pixelImportance;
    }

    /*
    This method returns the an int representing the importance of the pixel. The higher the value, the more important that pixel is and the less likely that the algorithm
    will remove said pixel. The importance is determined by the disparities in color compared to the average neighboring pixel.
     */
    private int getPixelImportance(int row, int col) {
        int totalImportance = 0;
        int perPixel = 0;
        for (int y = row - 1; y <= row + 1; y++) {
            for (int x = col - 1; x <= col + 1; x++) {
                if (0 <= y && y < this.getPicHeight() && 0 <= x && x < this.getPicWidth()) {
                    int redVar = (int) Math.pow(getColorRed(getPixelColor(row, col)) - getColorRed(getPixelColor(y, x)), 2); // red color variation
                    int blueVar = (int) Math.pow(getColorBlue(getPixelColor(row, col)) - getColorBlue(getPixelColor(y, x)), 2); // blue color variation
                    int greenVar = (int) Math.pow(getColorGreen(getPixelColor(row, col)) - getColorGreen(getPixelColor(y, x)), 2); // green color variation
                    totalImportance += (redVar + blueVar + greenVar);
                    perPixel++;
                }
            }
        }
        return (totalImportance / perPixel);
    }
}
