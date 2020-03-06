package frc.robot.util;

import java.util.ArrayList;

/**
 * SimpleMovingAverage is designed to find the average of a dynamically updating dataset. The constructor
 * defines the amount of past points you want to include in the moving average.
 * @author Ethan Snyder
 * @author Jordan Bancino
 */
public class SimpleMovingAverage {
    private int dataSampleSize;
    private ArrayList<Number> pointList= new ArrayList<>();
    private Number[] data;

    public SimpleMovingAverage(int dataSampleSize) {
        this.dataSampleSize = dataSampleSize;
    }

    /**
     * Adds a variable amount of numbers to the number array.
     * @param points A number to be added to the array.
     */
    public void add(Number... points) {
        data = new Number[dataSampleSize];
        /** Iterate through array, add each to ArrayList. */
        for (Number point : points) {
            pointList.add(point);
            /** If the amount of elements in the ArrayList exceeds the index size for the desired sample size... */
            if (pointList.size() > dataSampleSize - 1) {
                /** Move last ten elements to an array, clean the old one, and replace the old one with new points. */
                System.arraycopy(pointList.toArray(), pointList.size() - dataSampleSize, data, 0, dataSampleSize);
                pointList.clear();
                for (int i = 0; i < data.length; i++) {
                    pointList.add(data[i]);
                }
            }
        }
    }

    /**
     * Returns the average of most recent additions to the data set.
     */
    public Number get() {
        /** If every index is not full, return null. */
        if (data[data.length - 1] == null) {
            return null;
        }
        /** Find the sum and the array length for averaging. */
        int count = data.length;
        double sum = 0;
        /** Iterates through data set and sums all elements. */
        for (int i = 0; i < data.length; i++) {
            sum += (double) data[i];
        }
        return sum / count;
    }
}