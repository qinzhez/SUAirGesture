package features;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;

import utilities.JSONParser;

public class Warehouse {
	private Gesture gesture;
	private Distance fDistance;

	public Warehouse(Gesture g) throws InterruptedException {
		gesture = g;
		fDistance = new Distance(gesture);
	}

	/**
	 * @author qinzhe
	 *
	 *         Write current instance and features to a file
	 *
	 *         File type: csv
	 */
	public void toFile() {
		Path path = Paths.get("data/out.csv");
		ArrayList<Double> speed = gesture.getSpeed();
		ArrayList<Double> distance =  new ArrayList<Double>(Arrays.asList(fDistance.getAttributes()));
		String data = "";
		for(Double att:speed) {
			data += att+",";
		}
		for(Double att:distance) {
			data+= att+",";
		}
		data += "\n";

		try {
			Files.write(path, data.getBytes(), StandardOpenOption.APPEND);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * @author qinzhe
	 *
	 * @param path
	 */
	public void fromFile(String path) {

	}

	public static void main(String[] argv) {
		String[] suffix = new String[5];
		suffix[0] = "";
		suffix[1] = "_2";
		suffix[2] = "_3";
		suffix[3] = "_4";
		suffix[4] = "_5";

		Path path = Paths.get("data/out.csv");
		String header = "avg_euclid_speed,max_euclid_speed,min_euclid_speed,"
				+ "avg_x_manhattan_speed,max_x_manhattan_speed,min_x_manhattan_speed,"
				+ "avg_y_manhattan_speed,max_y_manhattan_speed,min_y_manhattan_speed,"
				+ "STD to Vertical Central Line,STD to Horizontal Central Line,Ratio A1,"
				+ "Ratio A2,Ratio A3,Ratio A4,Ratio A5,"
				+ "Large Drift from Max Horizontally,Large Drift from Max Vertically,"
				+ "Small Drift from Max Horizontally,Small Drift from Max Vertically,"
				+ "Large Drift from Average Horizontally,Large Drift from Average Vertically,"
				+ "Small Drift from Average Horizontally,Small Drift from Average Vertically\n";
		try {
			Files.write(path, header.getBytes(), StandardOpenOption.APPEND);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		for (int i = 1; i < 6; i++) {
			Gesture gesture = null;
			if(i != 4)
				for(int j=0;j<5;j++) {
					gesture = JSONParser.generateGestureObj("json/identity/"+i+suffix[j]);
					gesture.populateSpeed();
					try {
						Warehouse wh = new Warehouse(gesture);
						wh.toFile();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			else {
				gesture = JSONParser.generateGestureObj("json/identity/"+i);
				gesture.populateSpeed();
				try {
					Warehouse wh = new Warehouse(gesture);
					wh.toFile();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}
	}
}
