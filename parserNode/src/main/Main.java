package main;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.stream.Collectors;

import sgd.SGD;
import sgd.TrainingExample;
import sgd_implementation.SGD_Helper_Implementation;

public class Main {

	//args[0] => path to trained weights file

	public static void main(String[] args) throws IOException, ClassNotFoundException {

		SGD_Helper_Implementation helper = new SGD_Helper_Implementation();

		HashMap<String, Float> weights = loadWeights(args[0]);
		System.err.println("LOADED WEIGHTS");

		String sentence = args[1];

		String expression = helper.parse(sentence, weights);

		System.out.println(expression);
	}

	//Reads in a dataset of training examples!
	private static ArrayList<TrainingExample<String, LogicalExpression>> parseDataset(String path_to_dataset) throws FileNotFoundException {
		ArrayList<TrainingExample<String, LogicalExpression>> examples = new ArrayList<>();

		//This points to one of the folds in the dataset folder!!
		File file = new File(path_to_dataset);
		Scanner sc = new Scanner(file);
		while (sc.hasNextLine()) {
			String line = sc.nextLine();
			if (line.length() == 0)
				break;
			String[] data = line.split(";");
			examples.add(new TrainingExample<String, LogicalExpression>(data[1].trim(),
					new LogicalExpressionTraining(data[2].trim())));
		}
		return examples;
	}

	private static ArrayList<TrainingExample<String, LogicalExpression>> getExamples() {
		ArrayList<TrainingExample<String, LogicalExpression>> examples = new ArrayList<>();
		examples.add(new TrainingExample<String, LogicalExpression>("put the red block on the green block",
				new LogicalExpressionTraining("Put(On(Red(Block(null)), Green(Block(null))))")));
		examples.add(new TrainingExample<String, LogicalExpression>("put the blue block on the green block",
				new LogicalExpressionTraining("Put(On(Blue(Block(null)), Green(Block(null))))")));
		examples.add(new TrainingExample<String, LogicalExpression>("put the blue block on the red block",
				new LogicalExpressionTraining("Put(On(Blue(Block(null)), Red(Block(null))))")));
		examples.add(new TrainingExample<String, LogicalExpression>("put the blue block on the blue block",
				new LogicalExpressionTraining("Put(On(Blue(Block(null)), Blue(Block(null))))")));
		return examples;
	}

	private static void saveWeights(HashMap<String, Float> w, String fileName) throws IOException {
		FileOutputStream myFileOutputStream = new FileOutputStream(fileName);
		ObjectOutputStream myObjectOutputStream = new ObjectOutputStream(myFileOutputStream);
		myObjectOutputStream.writeObject(w);
		myObjectOutputStream.close();
	}

	private static HashMap<String, Float> loadWeights(String fileName) throws IOException, ClassNotFoundException {
		FileInputStream myFileInputStream = new FileInputStream(fileName);
		ObjectInputStream myObjectInputStream = new ObjectInputStream(myFileInputStream);
		HashMap<String, Float> done = (HashMap<String, Float>) myObjectInputStream.readObject();
		myObjectInputStream.close();
		return done;
	}
}
