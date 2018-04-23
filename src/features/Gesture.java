package features;

import java.util.ArrayList;

import lombok.Getter;
import lombok.Setter;
import utilities.JSONParser;

/**
 * Created by JOHNNY on 4/19/18.
 */
public class Gesture {

    @Getter @Setter private double minX;
    @Getter @Setter private double minY;
    @Getter @Setter private double maxX;
    @Getter @Setter private double maxY;

    @Getter @Setter private ArrayList<Coordinate> positions;

    @Getter @Setter private double xRange;
    @Getter @Setter private double yRange;

    @Getter @Setter private int valid_cnt;


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

    /* Frequency Matrix */
    public double[] matrix_100;
    public double[] matrix_64;
    public double[] matrix_36;
    public double[] matrix_16;

    public Gesture(final double minX, final double minY, final double maxX, final double maxY,
                   final ArrayList<Coordinate> positions) {
        this.minX = minX;
        this.minY = minY;
        this.maxX = maxX;
        this.maxY = maxY;
        this.positions = positions;

        xRange = maxX - minX;
        yRange = maxY - minY;
        valid_cnt = positions.size();

        matrix_100 = new double[100];
        matrix_64 = new double[64];
        matrix_36 = new double[36];
        matrix_16 = new double[16];
    }

    /* Populate all Speed Components */
    public void populateSpeed() {
        double x_var = 0;
        double y_var = 0;

        double total_euclid_speed = 0;
        double total_x_manhattan_speed = 0;
        double total_y_manhattan_speed = 0;

        Coordinate prev = positions.get(0);
        for (int i = 1; i < positions.size(); ++i) {

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


        avg_euclid_speed = total_euclid_speed / valid_cnt * 30;
        avg_x_manhattan_speed = total_x_manhattan_speed / valid_cnt * 30;
        avg_y_manhattan_speed = total_y_manhattan_speed / valid_cnt * 30;

        max_euclid_speed *= 30;
        max_x_manhattan_speed *= 30;
        max_y_manhattan_speed *= 30;

        min_euclid_speed *= 30;
        min_x_manhattan_speed *= 30;
        min_y_manhattan_speed *= 30;

        System.out.println(String.format("X Range: %.5f.\n", xRange));
        System.out.println(String.format("Y Range: %.5f.\n", yRange));

        System.out.println(String.format("Average Euclid Speed: %.5f.\n", avg_euclid_speed));
        System.out.println(String.format("Average Y Manhattan Speed: %.5f.\n", avg_y_manhattan_speed));

        System.out.println(String.format("Max Euclid Speed: %.5f.\n", max_euclid_speed));
        System.out.println(String.format("Max y Manhattan Speed: %.5f.\n", max_y_manhattan_speed));

        System.out.println(String.format("Min Euclid Speed: %.5f.\n", min_euclid_speed));
        System.out.println(String.format("Min y Manhattan Speed: %.5f.\n", min_y_manhattan_speed));
    }

    /* Populate all Frequency Matrix Components */
    public void populateMatrixes() {

        double x_unit_100 = xRange / 9.9;
        double y_unit_100 = yRange / 9.9;

        double x_unit_64 = xRange / 7.9;
        double y_unit_64 = yRange / 7.9;

        double x_unit_36 = xRange / 5.9;
        double y_unit_36 = yRange / 5.9;

        double x_unit_16 = xRange / 3.9;
        double y_unit_16 = yRange / 3.9;

        for (int i = 0; i < positions.size(); ++i) {
            Coordinate curr = positions.get(i);

            double curr_x = curr.getX() - minX;
            double curr_y = curr.getY() - minY;

            int x_count_100 = (int) (curr_x / x_unit_100);
            int y_count_100 = (int) (curr_y / y_unit_100);
            int x_count_64 = (int) (curr_x / x_unit_64);
            int y_count_64 = (int) (curr_y / y_unit_64);
            int x_count_36 = (int) (curr_x / x_unit_36);
            int y_count_36 = (int) (curr_y / y_unit_36);
            int x_count_16 = (int) (curr_x / x_unit_16);
            int y_count_16 = (int) (curr_y / y_unit_16);

            matrix_100[10 * y_count_100 + x_count_100] += 1.0 / valid_cnt;
            matrix_64[8 * y_count_64 + x_count_64] += 1.0 / valid_cnt;
            matrix_36[6 * y_count_36 + x_count_36] += 1.0 / valid_cnt;
            matrix_16[4 * y_count_16 + x_count_16] += 1.0 / valid_cnt;
        }


    }

    // For test only
    public static void main(String[] argv) {
        Gesture gesture = JSONParser.generateGestureObj("json/identity/1");
        gesture.populateMatrixes();
    }
}
