import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
	Boolean lyingDown = false; // enable when load a lying-down model; disable
								// when load a standing model

	// FILE PATH
	final static String FILE_NAME = "C:/Users/BiLL/Desktop/Non-TPoseAnimater/Non-TPoseAnimater/src/animater/walk.bvh";
	final static String OUTPUT_FILE_NAME = "C:/Users/BiLL/Desktop/Non-TPoseAnimater/Non-TPoseAnimater/src/animater/new.bvh";
	final static String TEMPLET_FILE_NAME = "C:/Users/BiLL/Desktop/Non-TPoseAnimater/Non-TPoseAnimater/src/animater/templet.bvh";
	final static Charset ENCODING = StandardCharsets.UTF_8;
	
	// store model's facing direction(which axis is the model facing)
	String face_direction = ""; 

	String face_direction_sign = ""; // store the sign of the facing direction
	static String finalFrame = "";
	
	List<String> test = new ArrayList<String>();
	static List<String> file = new ArrayList<String>();

	public static void main(String... aArgs) throws IOException {
		Main text = new Main();
		String FINALFRAME = text.getPresetFinalFrame(FILE_NAME); // store the final frame after generated
		finalFrame = text.setFinalFramePosition(FINALFRAME);
		// First, detect the facing direction of the model
		text.getFaceDirection(FILE_NAME);

		if (text.lyingDown == true) {
			// transform lying-down model into standing model
			List<String> FixedFile = text.fixLyingdown(FILE_NAME);
			text.writetFile(OUTPUT_FILE_NAME, FixedFile);

			// generate a templet BVH file which contains all the information
			// of the loaded model and generate final frame
			// (the file is named templet.bvh)
			file = text.readAndCreateTemplet(OUTPUT_FILE_NAME);
		} else {
			// generate a templet BVH file which contains all the information
			// of the loaded model and generate final frame
			// (the file is named templet.bvh)
			file = text.readAndCreateTemplet(FILE_NAME);
		}
		
		// Interpolate the final frame and generate the frames between the first
		// frame and the final frame(motion track)
		String mid = text.interpolator("hey");
		file.add(mid);
		
		// generate 250 frames in between the 1st frame and the final frame
		text.loop(mid, 250, text);
		
		// add the final frame into the file
		file.add(finalFrame);
		
		//write all generated frames into templet.bvh
		text.writetFile(TEMPLET_FILE_NAME, file);
	}

	String loop(String mid, int frameNumber, Main text) {
		// create middle frames using interpolator function and recursing loop
		if (frameNumber > 0) {
			frameNumber--;
			file.add(text.interpolator(mid));
			return loop(text.interpolator(mid), frameNumber, text);
		} else
			return mid;
	}

	List<String> getFaceDirection(String aFileName) throws IOException {
		// go through the BVH file and calculate the model's facing direction
		Path path = Paths.get(aFileName);
		List<String> lines = new ArrayList<String>();
		List<String> offset = new ArrayList<String>();

		//read the file and get all the offsets
		try (BufferedReader reader = Files.newBufferedReader(path, ENCODING)) {
			String line = null;
			while ((line = reader.readLine()) != null) {
				log(line);
				lines.add(line);
				if (line.contains("OFFSET")) {
					Scanner scanner = new Scanner(line);
					scanner.findInLine("OFFSET");
					// scanner.useDelimiter(" ");
					while (scanner.hasNext()) {
						String point = scanner.next();
						offset.add(point);
					}
					scanner.close();
				}
			}

		}
		
		//get the direction using the offsets
		String direction = faceDirection(offset);
		face_direction = direction;
		
		//get the direction using the offsets
		String direction_sign = facingDirection(offset);
		face_direction_sign = direction_sign;
		
		System.out.println(direction + " " + direction_sign);
		
		return lines;
	}

	public List<Double> toEuler(double x, double y, double z, double angle) {
		// translate axis angles to Euler angle
		double s = Math.sin(angle);
		double c = Math.cos(angle);
		double t = 1 - c;
		double heading = 0, attitude = 0, bank = 0;
		List<Double> vector = new ArrayList<Double>();

		// Normalize the axis
		double magnitude = Math.sqrt(x * x + y * y + z * z);
		if (magnitude == 0) {

		} else {
			x /= magnitude;
			y /= magnitude;
			z /= magnitude;
		}

		if ((x * y * t + z * s) > 0.998) { // north pole singularity detected
			heading = Math.toDegrees(2 * Math.atan2(x * Math.sin(angle / 2),
					Math.cos(angle / 2)));
			attitude = Math.toDegrees(Math.PI / 2);
			bank = 0;

			vector.add(heading);
			vector.add(attitude);
			vector.add(bank);
			return vector;
		}
		if ((x * y * t + z * s) < -0.998) { // south pole singularity detected
			heading = Math.toDegrees(-2
					* Math.atan2(x * Math.sin(angle / 2), Math.cos(angle / 2)));
			attitude = Math.toDegrees(-Math.PI / 2);
			bank = 0;

			vector.add(heading);
			vector.add(attitude);
			vector.add(bank);
			return vector;
		}

		heading = Math.toDegrees(Math.atan2(y * s - x * z * t, 1
				- (y * y + z * z) * t));
		attitude = Math.toDegrees(Math.asin(x * y * t + z * s));
		bank = Math.toDegrees(Math.atan2(x * s - y * z * t, 1 - (x * x + z * z)
				* t));
		vector.add(heading);
		vector.add(attitude);
		vector.add(bank);
		return vector;

	}

	public double axisAngle(double x, double y, double z, double px, double py,
			double pz) {
		// calculate axis angle between parent offset and child offset
		double angle = 0;
		double magnitude = Math.sqrt(x * x + y * y + z * z);
		double magnitude_p = Math.sqrt(px * px + py * py + pz * pz);

		if ((magnitude == 0) || (magnitude_p == 0)) {

		} else {
			angle = Math.toDegrees(Math.acos((x * px + y * py + z * pz)
					/ (magnitude * magnitude_p)));
		}
		return angle;
	}

	public List<Double> crossProduct(double x, double y, double z, double px,
			double py, double pz, double angle) {
		// using cross product to calculate the rotation axis
		double i = y * pz - z * py;
		double j = x * pz - z * px;
		double k = x * py - y * px;

		List<Double> v = new ArrayList<Double>();
		v.add(i);
		v.add(j);
		v.add(k);
		return v;

	}

	public List<Double> getEulerAngles(double x, double y, double z, double px,
			double py, double pz) {
		// calculate the euler angels between child offset and parent offset
		
		//first, get the angle between parent's offset and child's offset
		double angle = Math.toRadians(axisAngle(x, y, z, px, py, pz));
		
		//then, get cross product vector of these offsets and angle
		List<Double> v = crossProduct(x, y, z, px, py, pz, angle);
		
		//last, transfer the 3 euler angle using the vector
		List<Double> vector = toEuler(v.get(0), v.get(1), v.get(2), angle);
		return vector;
	}

	public final List<Double> eulerToQuaternion(double xx, double yy, double zz) {
		// transfer euler angles to quaternion

		// Assuming the angles are in radiant.
		double heading = Math.toRadians(xx);
		double attitude = Math.toRadians(yy);
		double bank = Math.toRadians(zz);

		double c1 = Math.cos(heading / 2);
		double s1 = Math.sin(heading / 2);
		double c2 = Math.cos(attitude / 2);
		double s2 = Math.sin(attitude / 2);
		double c3 = Math.cos(bank / 2);
		double s3 = Math.sin(bank / 2);
		double c1c2 = c1 * c2;
		double s1s2 = s1 * s2;
		List<Double> quaternion = new ArrayList<Double>();

		double w = c1c2 * c3 - s1s2 * s3;
		double x = c1c2 * s3 + s1s2 * c3;
		double y = s1 * c2 * c3 + c1 * s2 * s3;
		double z = c1 * s2 * c3 - s1 * c2 * s3;

		quaternion.add(x);
		quaternion.add(y);
		quaternion.add(z);
		quaternion.add(w);

		return quaternion;
	}

	public List<Double> quaternionToEuler(List<Double> q1) {
		// Transfer quaternion to euler angles

		List<Double> vector = new ArrayList<Double>();
		double heading = 0, attitude = 0, bank = 0;
		double sqx = q1.get(0) * q1.get(0);
		double sqy = q1.get(1) * q1.get(1);
		double sqz = q1.get(2) * q1.get(2);
		double sqw = q1.get(3) * q1.get(3);
		double unit = sqx + sqy + sqz + sqw; // if normalized is one, otherwise
												// is correction factor
		double test = q1.get(0) * q1.get(1) + q1.get(2) * q1.get(3);
		if (test > 0.499 * unit) { // singularity at north pole
			heading = Math.toDegrees(2 * Math.atan2(q1.get(0), q1.get(3)));
			attitude = Math.toDegrees(Math.PI / 2);
			bank = 0;

			vector.add(heading);
			vector.add(attitude);
			vector.add(bank);
			return vector;
		}
		if (test < -0.499 * unit) { // singularity at south pole
			heading = Math.toDegrees(-2 * Math.atan2(q1.get(0), q1.get(3)));
			attitude = Math.toDegrees(-Math.PI / 2);
			bank = 0;

			vector.add(heading);
			vector.add(attitude);
			vector.add(bank);
			return vector;
		}

		heading = Math.toDegrees(Math.atan2(
				2 * q1.get(1) * q1.get(3) - 2 * q1.get(0) * q1.get(2), sqx
						- sqy - sqz + sqw));
		attitude = Math.toDegrees(Math.asin(2 * test / unit));
		bank = Math.toDegrees(Math.atan2(
				2 * q1.get(0) * q1.get(3) - 2 * q1.get(1) * q1.get(2), -sqx
						+ sqy - sqz + sqw));

		vector.add(heading);
		vector.add(attitude);
		vector.add(bank);

		return vector;
	}

	public List<Double> lerp(List<Double> qa, List<Double> qb, double t) {
		// Linear interpolation

		// quaternion to return
		List<Double> qm = new ArrayList<Double>();

		// calculate Quaternion.
		qm.add(qa.get(0) * t + qb.get(0) * (1 - t));
		qm.add(qa.get(1) * t + qb.get(1) * (1 - t));
		qm.add(qa.get(2) * t + qb.get(2) * (1 - t));
		qm.add(qa.get(3) * t + qb.get(3) * (1 - t));
		return qm;
	}

	public List<Double> slerp(List<Double> qa, List<Double> qb, double t) {
		// spherical linear interpolation

		// quaternion to return
		List<Double> qm = new ArrayList<Double>();
		// Calculate angle between them.
		double cosHalfTheta = qa.get(3) * qb.get(3) + qa.get(0) * qb.get(0)
				+ qa.get(1) * qb.get(1) + qa.get(2) * qb.get(2);
		// if qa=qb or qa=-qb then theta = 0 and we can return qa
		if (Math.abs(cosHalfTheta) >= 1.0) {
			qm.add(qa.get(0));
			qm.add(qa.get(1));
			qm.add(qa.get(2));
			qm.add(qa.get(3));

			return qm;
		}

		// Calculate temporary values.
		double halfTheta = Math.acos(cosHalfTheta);
		double sinHalfTheta = Math.sqrt(1.0 - cosHalfTheta * cosHalfTheta);

		// if theta = 180 degrees then result is not fully defined
		// we could rotate around any axis normal to qa or qb
		if (Math.abs(sinHalfTheta) < 0.001) {
			qm.add(qa.get(0) * 0.5 + qb.get(0) * 0.5);
			qm.add(qa.get(1) * 0.5 + qb.get(1) * 0.5);
			qm.add(qa.get(2) * 0.5 + qb.get(2) * 0.5);
			qm.add(qa.get(3) * 0.5 + qb.get(3) * 0.5);
			return qm;
		}

		double ratioA = Math.sin((1 - t) * halfTheta) / sinHalfTheta;
		double ratioB = Math.sin(t * halfTheta) / sinHalfTheta;
		// calculate Quaternion.
		qm.add(qa.get(0) * ratioA + qb.get(0) * ratioB);
		qm.add(qa.get(1) * ratioA + qb.get(1) * ratioB);
		qm.add(qa.get(2) * ratioA + qb.get(2) * ratioB);
		qm.add(qa.get(3) * ratioA + qb.get(3) * ratioB);
		return qm;
	}

	public String interpolator(String frame) {
		// apply interpolation to the models

		List<List<Double>> q1 = new ArrayList<List<Double>>();
		List<List<Double>> q2 = new ArrayList<List<Double>>();
		List<String> value1 = new ArrayList<String>();
		List<String> value2 = new ArrayList<String>();
		String new_frame = "";
		int flag = 0;
		double heading = 0, attitude = 0, bank = 0;

		// get quaternion of finalframe
		Scanner scanner = new Scanner(finalFrame);
		scanner.useDelimiter(" ");
		while (scanner.hasNext()) {
			String point = scanner.next();
			value1.add(point);
		}
		scanner.close();

		for (int i = 0; i < value1.size(); i += 3) {
			//get the 3 euler angles from the final frame
			heading = Float.parseFloat(value1.get(i));
			attitude = Float.parseFloat(value1.get(i + 1));
			bank = Float.parseFloat(value1.get(i + 2));

			//transfer the 3 euler angles to quaternion
			List<Double> quaternion = eulerToQuaternion(heading, attitude, bank);
			
			//add the quaternion into the list of quaternion
			q1.add(quaternion);
		}
		if (frame == "hey") {
			//this means it enters this function 1st time.
			flag = 1;
		} else {
			// scan the frame (which from the argument) and add into value2
			Scanner scanner2 = new Scanner(frame);
			scanner2.useDelimiter(" ");
			while (scanner2.hasNext()) {
				String point = scanner2.next();
				value2.add(point);
			}
			scanner2.close();

			for (int i = 0; i < value2.size(); i += 3) {
				//get the 3 euler angles from the given frame
				heading = Float.parseFloat(value2.get(i));
				attitude = Float.parseFloat(value2.get(i + 1));
				bank = Float.parseFloat(value2.get(i + 2));

				//transfer the 3 euler angles to quaternion
				List<Double> quaternion = eulerToQuaternion(heading, attitude,
						bank);
				
				//add the quaternion into another list of quaternion
				q2.add(quaternion);
			}
		}

		//zero quaternion
		List<Double> qa = new ArrayList<Double>();
		qa.add((double) 0);
		qa.add((double) 0);
		qa.add((double) 0);
		qa.add((double) 1);
		
		List<Double> new_eulers = new ArrayList<Double>();
		for (int j = 0; j < q1.size(); j++) {
			if (j < 2) {
				new_frame += "0 0 0 ";
			} else {
				if (flag == 1) {
					//using slerp to get a new quaternion and transfer it to euler
					new_eulers = quaternionToEuler(slerp(qa, q1.get(j), 0.03));
				} else {
					//using slerp to get a new quaternion and transfer it to euler
					new_eulers = quaternionToEuler(slerp(q2.get(j), q1.get(j),
							0.03));
				}
				
				//get the new frame 
				new_frame += new_eulers.get(0) + " " + new_eulers.get(1) + " "
						+ new_eulers.get(2) + " ";
			}
		}

		return new_frame;
	}

	List<Double> rotation_x(double x, double y, double z, double angle_old) {
		// calculate offset that rotation by x with an angle
		double x11 = 0, x12 = 0, x13 = 0, x21 = 0, x22 = 0, x23 = 0, x31 = 0, x32 = 0, x33 = 0;
		double vx = 0, vy = 0, vz = 0;
		List<Double> vector = new ArrayList<Double>();
		double angle = Math.toRadians(angle_old);
		x11 = 1;
		x12 = 0;
		x13 = 0;

		x21 = 0;
		x22 = Math.cos(angle);
		x23 = -Math.sin(angle);

		x31 = 0;
		x32 = Math.sin(angle);
		x33 = Math.cos(angle);

		vx = (x11 * x) + (x12 * y) + (x13 * z);
		vy = (x21 * x) + (x22 * y) + (x23 * z);
		vz = (x31 * x) + (x32 * y) + (x33 * z);

		vector.add(vx);
		vector.add(vy);
		vector.add(vz);

		return vector;
	}

	List<Double> rotation_y(double x, double y, double z, double angle_old) {
		// calculate offset that rotation by y with an angle
		double x11 = 0, x12 = 0, x13 = 0, x21 = 0, x22 = 0, x23 = 0, x31 = 0, x32 = 0, x33 = 0;
		double vx = 0, vy = 0, vz = 0;
		List<Double> vector = new ArrayList<Double>();
		double angle = Math.toRadians(angle_old);
		x11 = Math.cos(angle);
		x12 = 0;
		x13 = Math.sin(angle);

		x21 = 0;
		x22 = 1;
		x23 = 0;

		x31 = -Math.sin(angle);
		x32 = 0;
		x33 = Math.cos(angle);

		vx = (x11 * x) + (x12 * y) + (x13 * z);
		vy = (x21 * x) + (x22 * y) + (x23 * z);
		vz = (x31 * x) + (x32 * y) + (x33 * z);

		vector.add(vx);
		vector.add(vy);
		vector.add(vz);

		return vector;
	}

	List<Double> rotation_z(double x, double y, double z, double angle_old) {
		// calculate offset that rotation by z with an angle
		double x11 = 0, x12 = 0, x13 = 0, x21 = 0, x22 = 0, x23 = 0, x31 = 0, x32 = 0, x33 = 0;
		double vx = 0, vy = 0, vz = 0;
		List<Double> vector = new ArrayList<Double>();
		double angle = Math.toRadians(angle_old);

		x11 = Math.cos(angle);
		x12 = -Math.sin(angle);
		x13 = 0;

		x21 = Math.sin(angle);
		x22 = Math.cos(angle);
		x23 = 0;

		x31 = 0;
		x32 = 0;
		x33 = 1;

		vx = (x11 * x) + (x12 * y) + (x13 * z);
		vy = (x21 * x) + (x22 * y) + (x23 * z);
		vz = (x31 * x) + (x32 * y) + (x33 * z);

		vector.add(vx);
		vector.add(vy);
		vector.add(vz);

		return vector;
	}

	List<String> fixLyingdown(String aFileName) throws IOException {
		// make a lying down model "stand up"
		Path path = Paths.get(aFileName);
		float x = 0, y = 0, z = 0;
		List<String> lines = new ArrayList<String>();
		try (BufferedReader reader = Files.newBufferedReader(path, ENCODING)) {
			String line = null;
			while ((line = reader.readLine()) != null) {
				List<String> offset = new ArrayList<String>();
				log(line);

				if (line.contains("OFFSET")) {
					Scanner scanner = new Scanner(line);
					scanner.findInLine("OFFSET");
					// scanner.useDelimiter(" ");
					while (scanner.hasNext()) {
						String point = scanner.next();
						offset.add(point);
					}
					scanner.close();
					x = Float.parseFloat(offset.get(0));
					y = Float.parseFloat(offset.get(2));
					z = Float.parseFloat(offset.get(1));
					line = "OFFSET" + " " + x + " " + y + " " + z;
				}
				lines.add(line);
			}
		}
		return lines;
	}

	void writetFile(String aFileName, List<String> aLines) throws IOException {
		// write a list to a file
		Path path = Paths.get(aFileName);
		try (BufferedWriter writer = Files.newBufferedWriter(path, ENCODING)) {
			for (String line : aLines) {
				writer.write(line);
				writer.newLine();
			}
		}
	}

	void writeLine(String aFileName, String line) throws IOException {
		// write a line to a file
		Path path = Paths.get(aFileName);
		try (BufferedWriter writer = Files.newBufferedWriter(path, ENCODING)) {
			writer.write(line);
		}
	}

	String faceDirection(List<String> offset) {
		// calculate the facing direction using the up leg offset
		float LeftUpLeg_x = Float.parseFloat(offset.get(6));
		float LeftUpLeg_y = Float.parseFloat(offset.get(8));
		float RightUpLeg_x = Float.parseFloat(offset.get(24));
		float RightUpLeg_y = Float.parseFloat(offset.get(26));
		String result;
		float diff = Math.abs(LeftUpLeg_x - RightUpLeg_x)
				- Math.abs(LeftUpLeg_y - RightUpLeg_y);
		if (diff >= 0) {
			result = "y";
		} else {
			result = "x";
		}
		return result;
	}

	String facingDirection(List<String> offset) {
		String result = "";
		float LeftShoulder_x = Float.parseFloat(offset.get(63));
		float LeftShoulder_y = Float.parseFloat(offset.get(65));

		float RightShoulder_x = Float.parseFloat(offset.get(90));
		float RightShoulder_y = Float.parseFloat(offset.get(92));

		float vector_x = RightShoulder_x - LeftShoulder_x;
		float vector_y = RightShoulder_y - LeftShoulder_y;
		if (face_direction == "y") {
			if (vector_x > 0) {
				result = "pos";
			} else {
				result = "neg";
			}
		} else {
			if (vector_y > 0) {
				result = "pos";
			} else {
				result = "neg";
			}
		}

		return result;
	}

	String getPresetFinalFrame(String aFileName) throws IOException {
		Path path = Paths.get(aFileName);
		String frame = "";
		try (BufferedReader reader = Files.newBufferedReader(path, ENCODING)) {
			String line = null;
			while ((line = reader.readLine()) != null) {
				log(line);
				frame = line;
			}

		}
		return frame;
	}

	List<String> readAndCreateTemplet(String aFileName) throws IOException {
		// read the BVH file and create a templet file that contain a final
		// frame of T-pose
		Path path = Paths.get(aFileName);
		List<String> lines = new ArrayList<String>();
		try (BufferedReader reader = Files.newBufferedReader(path, ENCODING)) {
			String line = null;
			while ((line = reader.readLine()) != null) {
				log(line);
				lines.add(line);
			}
		}
		//remove final frame from file
		lines.remove(lines.size() - 1);
		
		//set firstframe position to (0, 0, 0)
		String firstFrame = lines.get(lines.size() - 1);
		List<String> points = new ArrayList<String>();
		Scanner scanner = new Scanner(firstFrame);
		scanner.useDelimiter(" ");
		while (scanner.hasNext()) {
			String point = scanner.next();
			points.add(point);
		}
		scanner.close();
		for(int i = 0; i < 6; i++ ) {
			points.set(i, "0");
		}
		
		String firstFrame_new = "";
		for (String point : points) {
			firstFrame_new += point + " ";
		}
		lines.remove(lines.size() - 1);
		lines.add(firstFrame_new);
		return lines;
	}
	
	
	String setFinalFramePosition(String line) {
		List<String> points = new ArrayList<String>();
		Scanner scanner = new Scanner(line);
		scanner.useDelimiter(" ");
		while (scanner.hasNext()) {
			String point = scanner.next();
			points.add(point);
		}
		scanner.close();
		
		for(int i = 0; i < 6; i++ ) {
			points.set(i, "0");
		}
		
		String newLine = "";
		for (String point : points) {
			newLine += point + " ";
		}
		return newLine;
	}
	private static void log(Object aMsg) {
		// System.out.println(String.valueOf(aMsg));
	}

}