package features;

import java.util.ArrayList;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by JOHNNY on 4/19/18.
 */
public class Gesture {

	@Getter @Setter private double minX;
	@Getter @Setter private double minY;
	@Getter @Setter private double maxX;
	@Getter @Setter private double maxY;

	@Getter @Setter private ArrayList<Coordinate> positions;

    public Gesture(final double minX, final double minY, final double maxX, final double maxY,
                   final ArrayList<Coordinate> positions) {
        this.minX = minX;
        this.minY = minY;
        this.maxX = maxX;
        this.maxY = maxY;
        this.positions = positions;
    }

}
