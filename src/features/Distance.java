package features;

import java.util.ArrayList;
import java.util.Arrays;

import lombok.*;

/**
 *
 * @author qinzhe
 *
 *         Distance class is for calculate feature about distance in each
 *         instance
 */

public class Distance {
	@Getter
	private Double[] attributes;
	@Setter
	private Gesture gesture;

	/**
	 * Copy gesture for analysis
	 *
	 * @param g
	 * @throws InterruptedException
	 */
	public Distance(Gesture g) throws InterruptedException {
		this.gesture = new Gesture(g.getMinX(), g.getMinY(), g.getMaxX(), g.getMaxY(),
				new ArrayList<Coordinate>(g.getPositions()));
		attributes = new Double[15];
		calculate();
	}

	@Override
	public String toString() {
		ArrayList<Double> attris = new ArrayList<Double>(Arrays.asList(attributes));
		if (attris.isEmpty())
			return null;

		String str = new String();
		str += "STD to Vertical Central Line,STD to Horizontal Central Line,Ratio A1,"
				+ "Ratio A2,Ratio A3,Ratio A4,Ratio A5,"
				+ "Large Drift from Max Horizontally,Large Drift from Max Vertically,"
				+ "Small Drift from Max Horizontally,Small Drift from Max Vertically,"
				+ "Large Drift from Average Horizontally,Large Drift from Average Vertically,"
				+ "Small Drift from Average Horizontally,Small Drift from Average Vertically,";

		for (Double attr : attributes) {
			str += attr + ",";
		}
		return str;
	}

	/**
	 * Start to feature calculate
	 *
	 * @throws InterruptedException
	 */
	public void calculate() throws InterruptedException {
		checkData();
		ArrayList<Thread> threadList = new ArrayList<Thread>();
		threadList.add(new Thread(new Runnable() {
			@Override
			public void run() {
				centralSTD();
			}
		}));

		threadList.add(new Thread(new Runnable() {
			@Override
			public void run() {
				ratio();
			}
		}));

		threadList.add(new Thread(new Runnable() {
			@Override
			public void run() {
				drift();
			}
		}));

		for (Thread thread : threadList) {
			thread.start();
		}
		for (Thread thread : threadList) {
			thread.join();
		}
	}

	/**
	 * Remove all invalid points
	 */
	private void checkData() {
		double maxX = Double.MIN_VALUE, maxY = Double.MIN_VALUE;
		double minX = Double.MAX_VALUE, minY = Double.MAX_VALUE;
		gesture.getPositions().removeIf(s -> s.getX() == 0 && s.getY() == 0);
		for (Coordinate coor : gesture.getPositions()) {
			if (coor.getX() > maxX)
				maxX = coor.getX();
			if (coor.getY() > maxY)
				maxY = coor.getY();
			if (coor.getX() < minX)
				minX = coor.getX();
			if (coor.getY() < minY)
				minY = coor.getY();
		}
		if (minX < gesture.getMinX())
			gesture.setMinX(minX);
		if (minY < gesture.getMinY())
			gesture.setMinY(minY);
		if (maxX > gesture.getMaxX())
			gesture.setMaxX(maxX);
		if (maxY > gesture.getMaxY())
			gesture.setMaxY(maxY);
	}

	private void centralSTD() {
		double horizon = 0;
		double vertical = 0;
		int count = gesture.getPositions().size();
		double uX = (gesture.getMaxX() + gesture.getMinX()) / 2;
		double uY = (gesture.getMaxY() + gesture.getMinY()) / 2;
		for (Coordinate co : gesture.getPositions()) {
			horizon += Math.pow((co.getX() - uX), 2);
			vertical += Math.pow((co.getY() - uY), 2);
		}
		horizon = Math.sqrt(horizon / count);
		vertical = Math.sqrt(vertical / count);
		attributes[0] = vertical;
		attributes[1] = horizon;
	}

	private void ratio() {
		int splitIndex = gesture.getPositions().size() / 5;
		double maxX = Double.MIN_VALUE, maxY = Double.MIN_VALUE;
		double minX = Double.MAX_VALUE, minY = Double.MAX_VALUE;
		int area = 1;

		for (int index = 0; index < gesture.getPositions().size(); index++) {
			if (index != 0 && area < 5 && index % splitIndex == 0) {
				double ratio = (maxX - minX) / (maxY - minY);
				attributes[area + 1] = ratio;
				area++;
				maxX = Double.MIN_VALUE;
				maxY = Double.MIN_VALUE;
				minX = Double.MAX_VALUE;
				minY = Double.MAX_VALUE;
			}

			Coordinate cur = gesture.getPositions().get(index);
			if (cur.getX() > maxX)
				maxX = cur.getX();
			if (cur.getX() < minX)
				minX = cur.getX();
			if (cur.getY() > maxY)
				maxY = cur.getY();
			if (cur.getY() < minY)
				minY = cur.getY();
		}
		double ratio = (maxX - minX) / (maxY - minY);
		attributes[area + 1] = ratio;
	}

	private void drift() {
		double LH = 0, LV = 0, SH = 0, SV = 0;
		double uX = 0, uY = 0;
		for (Coordinate cur : gesture.getPositions()) {
			uX += cur.getX();
			uY += cur.getY();
		}
		uX = uX / gesture.getPositions().size();
		uY = uY / gesture.getPositions().size();

		for (Coordinate cur : gesture.getPositions()) {
			LH += cur.getX() * cur.getX() / (gesture.getMaxX() - gesture.getMinX());
			LV += cur.getY() * cur.getY() / (gesture.getMaxY() - gesture.getMinY());
			SH += cur.getX() * Math.abs(1 - cur.getX() / (gesture.getMaxX() - gesture.getMinX()));
			SV += cur.getY() * Math.abs(1 - cur.getY() / (gesture.getMaxY() - gesture.getMinY()));
		}

		attributes[7]= LH;
		attributes[8]= LV;
		attributes[9]= SH;
		attributes[10]=SV;

		for (Coordinate cur : gesture.getPositions()) {
			LH += cur.getX() * cur.getX() / uX;
			LV += cur.getY() * cur.getY() / uY;
			SH += cur.getX() * Math.abs(1 - cur.getX() / uX);
			SV += cur.getY() * Math.abs(1 - cur.getY() / uY);
		}
		attributes[11]= LH;
		attributes[12]= LV;
		attributes[13]= SH;
		attributes[14]= SV;
	}
}
