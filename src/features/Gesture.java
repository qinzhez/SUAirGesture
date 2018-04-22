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

    private double xRange;
    private double yRange;

    /* Euclid Speed Components */
    public double avg_euclid_speed;
    public double max_euclid_speed;
    public double min_euclid_speed = Double.MAX_VALUE;

    /* X Manhattan Speed Components */
    public double avg_x_manhattan_speed;
    public double max_x_manhattan_speed;
    public double min_x_manhattan_speed = Double.MAX_VALUE;

    /* Y Manhattan Speed Components */
    public double avg_y_manhattan_speed;
    public double max_y_manhattan_speed;
    public double min_y_manhattan_speed = Double.MAX_VALUE;

    public Gesture(final double minX, final double minY, final double maxX, final double maxY,
                   ArrayList<Coordinate> positions) {
        this.minX = minX;
        this.minY = minY;
        this.maxX = maxX;
        this.maxY = maxY;
        this.positions = positions;

        xRange = maxX - minX;
        yRange = maxY - minY;
    }

    /* Populate all Speed Components */
    public void populateSpeed() {
        double x_var = 0;
        double y_var = 0;

        double total_euclid_speed = 0;
        double total_x_manhattan_speed = 0;
        double total_y_manhattan_speed = 0;

        int count = 0;
        Coordinate prev = positions.get(0);

        for (int i = 1; i < positions.size(); ++i) {

            // Deal with invalid Coordinates
            if (prev.getX() == 0 && prev.getY() == 0) {
                prev = positions.get(i);
                continue;
            }

            ++count;
            Coordinate curr = positions.get(i);
            x_var = (Math.abs(curr.getX() - prev.getX())) / xRange;
            y_var = (Math.abs(curr.getY() - prev.getY())) / yRange;

            double curr_euclid = Math.sqrt(x_var * x_var + y_var * y_var);
            total_euclid_speed += curr_euclid;
            max_euclid_speed = Math.max(curr_euclid, max_euclid_speed);
            min_euclid_speed = Math.min(curr_euclid, min_euclid_speed);

            total_x_manhattan_speed += x_var;
            max_x_manhattan_speed = Math.max(x_var, max_x_manhattan_speed);
            min_x_manhattan_speed = Math.min(x_var, min_x_manhattan_speed);

            total_y_manhattan_speed += y_var;
            max_y_manhattan_speed = Math.max(y_var, max_y_manhattan_speed);
            min_y_manhattan_speed = Math.min(y_var, min_y_manhattan_speed);
        }


        avg_euclid_speed = total_euclid_speed / count * 30;
        avg_x_manhattan_speed = total_x_manhattan_speed / count * 30;
        avg_y_manhattan_speed = total_y_manhattan_speed / count * 30;

        max_euclid_speed *= 30;
        max_x_manhattan_speed *= 30;
        max_y_manhattan_speed *= 30;

        min_euclid_speed *= 30;
        min_x_manhattan_speed *= 30;
        min_y_manhattan_speed *= 30;

        System.out.println(String.format("Average Euclid Speed: %.5f.\n", avg_euclid_speed));
        System.out.println(String.format("Average Y Manhattan Speed: %.5f.\n", avg_y_manhattan_speed));

        System.out.println(String.format("Max Euclid Speed: %.5f.\n", max_euclid_speed));
        System.out.println(String.format("Max y Manhattan Speed: %.5f.\n", max_y_manhattan_speed));

        System.out.println(String.format("Min Euclid Speed: %.5f.\n", min_euclid_speed));
        System.out.println(String.format("Min y Manhattan Speed: %.5f.\n", min_y_manhattan_speed));
    }


    public static void main(String[] argv) {
        Gesture gesture = JSONParser.generateGestureObj("json/test_speed");
        gesture.populateSpeed();
    }
}
