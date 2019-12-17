package com.app.facerecog;

import org.bytedeco.javacpp.Loader;
import org.bytedeco.javacpp.opencv_java;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;

import java.io.*;
import java.util.ArrayList;
import org.opencv.face.EigenFaceRecognizer;


public class Test {


    public static void main(String[] args) {

        System.out.printf("java.library.path: %s%n",
                System.getProperty("java.library.path"));
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        Loader.load(opencv_java.class);


        //File[] imageFiles = root.listFiles(imgFilter);

        String csvFilePath="D:\\yalefaces\\csv.csv";
        ArrayList<Mat> images=new ArrayList<Mat>();
        ArrayList<Integer> labels=new ArrayList<Integer>();
        readCSV(csvFilePath,images,labels);

        Mat testSample=images.get(images.size()-1);
        Integer testLabel=labels.get(images.size()-1);
        images.remove(images.size()-1);
        labels.remove(labels.size()-1);
        MatOfInt labelsMat=new MatOfInt();
        labelsMat.fromList(labels);
        EigenFaceRecognizer efr=EigenFaceRecognizer.create();
        System.out.println("Starting training...");
        efr.train(images, labelsMat);

        int[] outLabel=new int[1];
        double[] outConf=new double[1];
        System.out.println("Starting Prediction...");
        efr.predict(testSample,outLabel,outConf);

        System.out.println("***Predicted label is "+outLabel[0]+".***");

        System.out.println("***Actual label is "+testLabel+".***");
        System.out.println("***Confidence value is "+outConf[0]+".***");


/*        Mat labels = new Mat(imageFiles.length, 1, CvType.CV_32SC1);
        IntBuffer labelsBuf = labels.createBuffer();

        int counter = 0;

        for (File image : imageFiles) {
            Mat img = Imgcodecs.imread(image.getAbsolutePath(), IMREAD_GRAYSCALE);

            int label = Integer.parseInt(image.getName().split("\\-")[0]);

            images.put(counter, img);

            labelsBuf.put(counter, label);

            counter++;
        }

        FaceRecognizer faceRecognizer = FisherFaceRecognizer.create();
        // FaceRecognizer faceRecognizer = EigenFaceRecognizer.create();
        // FaceRecognizer faceRecognizer = LBPHFaceRecognizer.create();

        faceRecognizer.train(images, labels);

        IntPointer label = new IntPointer(1);
        DoublePointer confidence = new DoublePointer(1);
        faceRecognizer.predict(testImage, label, confidence);
        int predictedLabel = label.get(0);

        System.out.println("Predicted label: " + predictedLabel);*/
    }
    private static void readCSV(String csvFilePath2, ArrayList<Mat> images, ArrayList<Integer> labels)  {
        BufferedReader br;
        try {
            br = new BufferedReader(new FileReader(csvFilePath2));
            File file=new File("D:\\subject01.png");
            if(file.canRead()){
                System.out.println("can read");
                System.out.println(file.getAbsolutePath()+file.getName());
            }
            String line;
            while((line=br.readLine())!=null){
                String[] tokens=line.split("\\;");
                Mat readImage=Imgcodecs.imread("D:\\subject01.png",0);
                if(readImage.empty()){
                    System.out.println("empty");
                }
                images.add(readImage);
                labels.add(Integer.parseInt(tokens[1]));
                System.out.print(tokens[0]+";");System.out.println(tokens[1]);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (NumberFormatException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
