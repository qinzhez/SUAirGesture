package features;

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
	 * Write current instance and features to a file
	 *
	 *  File type: csv
	 */
	public void toFile()
	{

	}


	/**
	 * @author qinzhe
	 *
	 * @param path
	 */
	public void fromFile(String path)
	{

	}
}
