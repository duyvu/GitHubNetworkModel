package utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Iterator;
import java.util.StringTokenizer;
import java.util.TreeSet;

public class ArrayMethods {

	public static void resetIntegerArray(int[] array) {
		for (int i = 0; i < array.length; i++)
			array[i] = 0;
	}

	public static void resetDoubleArray(double[] array) {
		for (int i = 0; i < array.length; i++)
			array[i] = 0;
	}

	public static void resetIntegerMatrix(int[][] array) {
		for (int i = 0; i < array.length; i++)
			for (int j = 0; j < array[0].length; j++)
				array[i][j] = 0;
	}

	public static void resetDoubleMatrix(double[][] array) {
		for (int i = 0; i < array.length; i++)
			for (int j = 0; j < array[0].length; j++)
				array[i][j] = 0;
	}

	public static void copyIntegerArray(int[] copyFrom, int[] copyTo) {
		for (int i = 0; i < copyFrom.length; i++)
			copyTo[i] = copyFrom[i];
	}

	public static void copyDoubleArray(double[] copyFrom, double[] copyTo) {
		for (int i = 0; i < copyFrom.length; i++)
			copyTo[i] = copyFrom[i];
	}

	public static void copyDoubleMatrix(double[][] copyFrom, double[][] copyTo) {
		for (int i = 0; i < copyFrom.length; i++)
			for (int j = 0; j < copyFrom[0].length; j++)
				copyTo[i][j] = copyFrom[i][j];
	}

	public static void printIntegerArray(int[] array, String delim,
			PrintStream outputStream) {
		for (int i = 0; i < array.length; i++)
			outputStream.print(array[i] + delim);
		outputStream.println();
	}

	public static void printIntegerMatrix(int[][] matrix, String delim,
			PrintStream outputStream) {
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix[0].length; j++)
				outputStream.print(matrix[i][j] + delim);
			outputStream.println();
		}
	}

	public static void printDoubleArray(double[] array, String delim,
			PrintStream outputStream) {
		for (int i = 0; i < array.length; i++)
			outputStream.print(array[i] + delim);
		outputStream.println();
	}

	public static void printDoubleMatrix(double[][] matrix, String delim,
			PrintStream outputStream) {
		for (int i = 0; i < matrix.length; i++) {
			outputStream.println(i + ": ");
			for (int j = 0; j < matrix[0].length; j++)
				outputStream.print(matrix[i][j] + delim);
			outputStream.println();
		}
	}

	public static void printDoubleMatrix(double[][] matrix, String delim,
			int numRows, PrintStream outputStream) {
		for (int i = 0; i < numRows; i++) {
			outputStream.println(i + ": ");
			for (int j = 0; j < matrix[0].length; j++)
				outputStream.print(matrix[i][j] + delim);
			outputStream.println();
		}
	}

	public static void printDouble3DMatrix(double[][][] matrix, String delim,
			PrintStream outputStream) {
		for (int i = 0; i < matrix.length; i++)
			for (int j = 0; j < matrix[0].length; j++) {
				for (int p = 0; p < matrix[0][0].length; p++)
					outputStream.print(matrix[i][j][p] + delim);
				outputStream.println();
			}
	}

	public static void readMatrix(BufferedReader reader, String dataDelim,
			double[][] covariates) {
		try {
			for (int i = 0; i < covariates.length; i++) {
				StringTokenizer tokenizer = new StringTokenizer(reader
						.readLine().trim(), dataDelim);
				for (int j = 0; j < covariates[0].length; j++)
					covariates[i][j] = Double.parseDouble(tokenizer.nextToken()
							.trim());
			}
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(-1);
		}
	}

	public static void read3DMatrix(BufferedReader reader, String dataDelim,
			double[][][] covariates) {
		try {
			for (int i = 0; i < covariates.length; i++)
				for (int j = 0; j < covariates[0].length; j++) {
					StringTokenizer tokenizer = new StringTokenizer(reader
							.readLine().trim(), dataDelim);
					for (int p = 0; p < covariates[0][0].length; p++)
						covariates[i][j][p] = Double.parseDouble(tokenizer
								.nextToken().trim());
				}
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(-1);
		}
	}

	public static void reflexDoubleMatrix(double[][] matrix) {
		if (matrix.length == matrix[0].length)
			for (int i = 0; i < matrix.length; i++)
				for (int j = 0; j < i; j++)
					matrix[j][i] = matrix[i][j];
		else {
			System.out.println("Matrix to reflex is not symmetric!!!");
			System.exit(-1);
		}
	}

	public static String toStringIntegerSet(TreeSet<Integer> set) {
		String results = "";
		for (Iterator<Integer> it = set.iterator(); it.hasNext();)
			results += "," + it.next();
		return results;
	}
	
}
