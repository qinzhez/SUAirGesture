package utilities;

import features.Coordinate;
import features.Gesture;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Scanner;

/**
 * Created by JOHNNY on 4/19/18.
 *
 * Define the JSON Parser for the position file.
 */
public class JSONParser {

    private final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    public static String dataDir = "./data/";

    public JSONParser() {
        // Setup the logger
        try {
            SAGLogger.setup();
            LOGGER.setLevel(Level.SEVERE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<File> getJSONFiles(String subDir) {
        File folder = new File(dataDir + subDir);
        File[] listOfFiles = folder.listFiles();

        ArrayList<File> jsonList = new ArrayList<>();
        for (File file : listOfFiles) {
            if (file.getName().endsWith("json")) {
                System.out.println(file.getName());
                jsonList.add(file);
            }
        }

        LOGGER.info(String.format("%d JSON files in Folder \"%s\" are obtained.\n", jsonList.size(), subDir));
        return jsonList;
    }

    public static Gesture generateGestureObj(String subDir) {
        ArrayList<File> jsonList = getJSONFiles(subDir);

        double minX = Double.MAX_VALUE;
        double minY = Double.MAX_VALUE;
        double maxX = Double.MIN_VALUE;
        double maxY = Double.MIN_VALUE;
        ArrayList<Coordinate> positions = new ArrayList<>();

        for (File file : jsonList) {
            Coordinate coor = generateCoordinate(file);
            if (coor.getX() == 0 && coor.getY() == 0) {
                continue;
            }
            minX = Math.min(minX, coor.getX());
            minY = Math.min(minY, coor.getY());

            maxX = Math.max(maxX, coor.getX());
            maxY = Math.max(maxY, coor.getY());
            positions.add(coor);
        }

        LOGGER.info(String.format("Successfully Generated Gesture with %d positions for Folder \"%s\".\n",
                positions.size(), subDir));
        return new Gesture(minX, minY, maxX, maxY, positions);
    }

    public static Coordinate generateCoordinate(File file) {
        StringBuffer JSON = new StringBuffer();

        try {
            Scanner sc = new Scanner(file);
            while (sc.hasNextLine()) {
                JSON.append(sc.nextLine());
            }
        } catch (FileNotFoundException e) {
            LOGGER.severe(String.format("File %s Not Found.\n", file.getName()));
            e.printStackTrace();
        }

        JSONObject obj = new JSONObject(JSON.toString());
        obj.getFloat("version");
        JSONArray people = obj.getJSONArray("people");
        JSONArray right_hand_arr = people.getJSONObject(0).getJSONArray("hand_right_keypoints_2d");
        double x = right_hand_arr.getDouble(21);
        double y = right_hand_arr.getDouble(22);

        LOGGER.info(String.format("%s\nX: %.2f, Y:%.2f\n", file.getName(), x, y));
        return new Coordinate(x, y);
    }

    // For test only
    public static void main(String[] argv) {
        JSONParser.generateGestureObj("json/A/1");
    }
}
