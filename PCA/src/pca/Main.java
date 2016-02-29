package pca;

public class Main {

	public static void main(String[] args) {
		SampleReader sampleReader=new SampleReader();
		sampleReader.setBvhFilePath("C:\\Users\\Xuping Fang\\Desktop\\CMPUT496\\BVH files\\01\\X.bvh");
		sampleReader.readCurrentBvhFileAsSample();
		sampleReader.getSampleFrames();
		sampleReader.getStdErrs();
	}

}
