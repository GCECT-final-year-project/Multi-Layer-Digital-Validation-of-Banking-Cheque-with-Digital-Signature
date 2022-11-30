package com.example;

import net.sourceforge.tess4j.*;
import java.io.*;
import java.util.Arrays;

import javax.imageio.*;
import java.awt.image.*;
import java.awt.*;

//program for extracting text fron image using the tess4j package
//tess4j package is installed by mentioning the tess4j dependencies inside the pom.xml file 
//maven is used for generating and managing this project

public class App {
	public static void main(String[] args) {
		try {
			// Tesseract object for performing OCR
			Tesseract tess = new Tesseract();
			// input image file
			File f = new File("ocr-tess4j/src/main/java/com/example/images/img.png");
			// replace the "data.png" with your image file.

			// path for the already trained data
			tess.setDatapath("ocr-tess4j/src/main/java/com/example/tessdata");

			// text lanuage
			tess.setLanguage("eng");

			// calling doOCR(inputImageFile) method on tesseract object for performing OCR
			// and storing the results in the text String
			String text = tess.doOCR(f);

			// processing the results
			System.out.println(text);
			String[] arr = text.split("\n");

			for (int i = 0; i < arr.length; i++) {

				arr[i] = arr[i].replaceAll("\\s", "");
			}
			System.out.println(Arrays.toString(arr));

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
