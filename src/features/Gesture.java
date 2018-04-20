package features;

import java.util.ArrayList;

/**
 * Created by JOHNNY on 4/19/18.
 */
public class Gesture {

    private double minX;
    private double minY;
    private double maxX;
    private double maxY;

    private ArrayList<Coordinate> positions;

    public Gesture(final double minX, final double minY, final double maxX, final double maxY,
                   final ArrayList<Coordinate> positions) {
        this.minX = minX;
        this.minY = minY;
        this.maxX = maxX;
        this.maxY = maxY;
        this.positions = positions;
    }
}
