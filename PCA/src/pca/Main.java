package pca;

public class Main {

	public static void main(String[] args) {
		SampleReader sampleReader=new SampleReader();
		sampleReader.setBvhFilePath("XXXXXXXX");
		sampleReader.readCurrentBvhFileAsSample();
		sampleReader.getSampleFrames();
		sampleReader.getStdErrs();
	}

}
