//Project to convert a 24 bit image into
//1) 8 bit gray scale
//2)N nevel diffusion
//3))Convert into 8 bit using Uniform Color Quantization

import java.util.Scanner;

public class CS4551_Dave
{
  public static void main(String[] args)
  {
	  Scanner input=new Scanner(System.in);
	// if there is no commandline argument, exit the program
    if(args.length != 1)
    {
      usage();
      System.exit(1);
    }

    System.out.println("--Welcome to Multimedia Software System--");

    System.out.println("Main Menu-----------------------------------\r\n" + 
    		"1. Conversion to Gray-scale Image (24bits->8bits)\r\n" + 
    		"2. Conversion to N-level Image by threshold quantization\r\n" + 
    		"3. Conversion to error diffusion\r\n"+
    		"4. Conversion to 8bit Indexed Color Image using Uniform Color Quantization (24bits->8bits)\r\n" +  
    		"5. Quit\r\n" + 
    		"Please enter the task number [1-5]:");
    
    int n=input.nextInt();
    
    switch(n) {
    case 1:
    	Image img=new Image(args[0]);
    	img.convertGrayScale();
    	img.display();
    	img.write2PPM("output.ppm");
    	break;
    case 2:
    	Image img1=new Image(args[0]);
    	img1.convertGrayScale();
    	img1.thresholdQuantization();
    	img1.display();
    	img1.write2PPM("output.ppm");
    	break;
    case 3:
    	Image img2=new Image(args[0]);
    	img2.convertGrayScale();
    	img2.errorDiffusion();
    	img2.display();
    	img2.write2PPM("output.ppm");
    	break;
    case 4:
    	Image img3=new Image(args[0]);
    	img3.uniformColorQuantization();
    	img3.display();
    	img3.write2PPM("output.ppm");
    	break;
    case 5:
    	System.exit(0);
    	break;
    default:
    	System.out.println("Incorrect input");
    	
    }
    // Create an Image object with the input PPM file name.
    // Display it and write it into another PPM file.

    System.out.println("--Good Bye--");
  }

  public static void usage()
  {
    System.out.println("\nUsage: java CS4551_Main [input_ppm_file]\n");
  }
}