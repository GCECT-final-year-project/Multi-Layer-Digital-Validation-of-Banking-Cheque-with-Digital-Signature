package com.example;

import net.sourceforge.tess4j.*;
import java.io.*;
import java.util.Arrays;

import javax.imageio.*;
import java.awt.image.*;
import java.awt.*;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        try{
			Tesseract tess = new Tesseract();

			File f = new File("ocr-tess4j/src/main/java/com/example/images/img.png");
						// replace the "data.png" with your image file.
			
			tess.setDatapath("ocr-tess4j/src/main/java/com/example/tessdata");
			
			tess.setLanguage("eng");
			
			String text = tess.doOCR(f);
			System.out.println(text);
			String[] arr = text.split("\n");

			for(int i=0; i<arr.length;i++){
				
				arr[i]=arr[i].replaceAll("\\s", "");
			}
			System.out.println(Arrays.toString(arr));

		}catch(Exception e){
			e.printStackTrace();
		}
    }
}
