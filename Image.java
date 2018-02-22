
import java.util.*;
import java.io.*;
import java.util.*;
import java.awt.*;
import java.awt.image.*;
import javax.swing.*;
import javax.imageio.stream.FileImageInputStream;

// A wrapper class of BufferedImage
// Provide a couple of utility functions such as reading from and writing to PPM file

public class Image
{
  private BufferedImage img;
  private String fileName;			// Input file name
  private int pixelDepth=3;			// pixel depth in byte

  public Image(int w, int h)
  // create an empty image with w(idth) and h(eight)
  {
	fileName = "";
	img = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
	System.out.println("Created an empty image with size " + w + "x" + h);
  }

  public Image(String fileName)
  // Create an image and read the data from the file
  {
	  this.fileName = fileName;
	  readPPM(this.fileName);
	  System.out.println("Created an image from " + this.fileName+ " with size "+getW()+"x"+getH());
  }

  public int getW()
  {
	return img.getWidth();
  }

  public int getH()
  {
	return img.getHeight();
  }

  public int getSize()
  // return the image size in byte
  {
	return getW()*getH()*pixelDepth;
  }

  public void setPixel(int x, int y, byte[] rgb)
  // set byte rgb values at (x,y)
  {
	int pix = 0xff000000 | ((rgb[0] & 0xff) << 16) | ((rgb[1] & 0xff) << 8) | (rgb[2] & 0xff);
	img.setRGB(x,y,pix);
  }

  public void setPixel(int x, int y, int[] irgb)
  // set int rgb values at (x,y)
  {
	byte[] rgb = new byte[3];

	for(int i=0;i<3;i++)
	  rgb[i] = (byte) irgb[i];

	setPixel(x,y,rgb);
  }

  public void getPixel(int x, int y, byte[] rgb)
  // retreive rgb values at (x,y) and store in the byte array
  {
  	int pix = img.getRGB(x,y);

  	rgb[2] = (byte) pix;
  	rgb[1] = (byte)(pix>>8);
  	rgb[0] = (byte)(pix>>16);
  }


  public void getPixel(int x, int y, int[] rgb)
  // retreive rgb values at (x,y) and store in the int array
  {
	int pix = img.getRGB(x,y);

	byte b = (byte) pix;
	byte g = (byte)(pix>>8);
	byte r = (byte)(pix>>16);

    // converts singed byte value (~128-127) to unsigned byte value (0~255)
	rgb[0]= (int) (0xFF & r);
	rgb[1]= (int) (0xFF & g);
	rgb[2]= (int) (0xFF & b);
  }
  
 

  public void displayPixelValue(int x, int y)
  // Display rgb pixel in unsigned byte value (0~255)
  {
	int pix = img.getRGB(x,y);

	byte b = (byte) pix;
	byte g = (byte)(pix>>8);
	byte r = (byte)(pix>>16);

    System.out.println("RGB Pixel value at ("+x+","+y+"):"+(0xFF & r)+","+(0xFF & g)+","+(0xFF & b));
   }

  public void readPPM(String fileName)
  // read a data from a PPM file
  {
	File fIn = null;
	FileImageInputStream fis = null;

	try{
		fIn = new File(fileName);
		fis = new FileImageInputStream(fIn);

		System.out.println("Reading "+fileName+"...");

		// read Identifier
		if(!fis.readLine().equals("P6"))
		{
			System.err.println("This is NOT P6 PPM. Wrong Format.");
			System.exit(0);
		}

		// read Comment line
		String commentString = fis.readLine();

		// read width & height
		String[] WidthHeight = fis.readLine().split(" ");
		int width = Integer.parseInt(WidthHeight[0]);
		int height = Integer.parseInt(WidthHeight[1]);

		// read maximum value
		int maxVal = Integer.parseInt(fis.readLine());

		if(maxVal != 255)
		{
			System.err.println("Max val is not 255");
			System.exit(0);
		}

		// read binary data byte by byte and save it into BufferedImage object
		int x,y;
		byte[] rgb = new byte[3];
		img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

		for(y=0;y<getH();y++)
		{
	  		for(x=0;x<getW();x++)
			{
				rgb[0] = fis.readByte();
				rgb[1] = fis.readByte();
				rgb[2] = fis.readByte();
				setPixel(x, y, rgb);
			}
		}

       	fis.close();

		System.out.println("Read "+fileName+" Successfully.");

	} // try
	catch(Exception e)
	{
		System.err.println(e.getMessage());
	}
  }

  public void write2PPM(String fileName)
  // wrrite the image data in img to a PPM file
  {
	FileOutputStream fos = null;
	PrintWriter dos = null;

	try{
		fos = new FileOutputStream(fileName);
		dos = new PrintWriter(fos);

		System.out.println("Writing the Image buffer into "+fileName+"...");

		// write header
		dos.print("P6"+"\n");
		dos.print("#CS451"+"\n");
		dos.print(getW() + " "+ getH() +"\n");
		dos.print(255+"\n");
		dos.flush();

		// write data
		int x, y;
		byte[] rgb = new byte[3];
		for(y=0;y<getH();y++)
		{
			for(x=0;x<getW();x++)
			{
				getPixel(x, y, rgb);
				fos.write(rgb[0]);
				fos.write(rgb[1]);
				fos.write(rgb[2]);

			}
			fos.flush();
		}
		dos.close();
		fos.close();

		System.out.println("Wrote into "+fileName+" Successfully.");

	} // try
	catch(Exception e)
	{
		System.err.println(e.getMessage());
	}
  }

  public void display()
  // display the image on the screen
  {
     // Use a label to display the image
      //String title = "Image Name - " + fileName;
      String title = fileName;
      JFrame frame = new JFrame(title);
      JLabel label = new JLabel(new ImageIcon(img));
      frame.add(label, BorderLayout.CENTER);
      frame.pack();
      frame.setVisible(true);
      frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
  }
  
  //New get pixel method which returns integer rgb array
  public int[] getPixel(int x, int y)
  // retreive rgb values at (x,y) and store in the int array
  {
	int[] rgb=new int[3];
	int pix = img.getRGB(x,y);

	byte b = (byte) pix;
	byte g = (byte)(pix>>8);
	byte r = (byte)(pix>>16);

    // converts singed byte value (~128-127) to unsigned byte value (0~255)
	rgb[0]= (int) (0xFF & r);
	rgb[1]= (int) (0xFF & g);
	rgb[2]= (int) (0xFF & b);
	
	return rgb;
  }
  public int getPixels(int x, int y)
  // retreive rgb values at (x,y) and store in the int array
  {
	int pix = img.getRGB(x,y);

	byte b = (byte) pix;
	byte g = (byte)(pix>>8);
	byte r = (byte)(pix>>16);

    // converts singed byte value (~128-127) to unsigned byte value (0~255)
	
	return pix;
  }
  public void setPixel(int x, int y, int pix)
  // set byte rgb values at (x,y)
  {
	
	img.setRGB(x,y,pix);
  }
  
  
  //Conversion of 24 bit to 8 bit gray scale
  public void convertGrayScale() {
	  int[] rgb=new int[3];
	  long gray;
	  for(int y=0;y<getH();y++) {
		  for(int x=0;x<getW();x++) {
			  rgb=getPixel(x,y);
			  gray = Math.round((0.299 * rgb[0] + 0.587 * rgb[1] + 0.114 * rgb[2]));
			  if(gray>=0 && gray<=255) {
				  rgb[0]=(int) (gray);
				  rgb[1]=(int) (gray);
				  rgb[2]=(int) (gray);
				  setPixel(x,y,rgb);
			  }
		  }
	  }
	  
  }
  
  public void thresholdQuantization() {
	  Scanner input=new Scanner(System.in);
	  System.out.println("Values can be 2,4,8,16");
	  System.out.println("Enter the level:");
	  int n=input.nextInt();
	  int[] rgb=new int[3];
	  
	  switch(n) {
	  case 2:for(int y=0;y<getH();y++) {
		  		for(int x=0;x<getW();x++) {
		  			rgb=getPixel(x,y);
			  		if(rgb[0]<=128) {
			  			rgb[0]=rgb[1]=rgb[2]=0;
			  		}
			  		else {
				  rgb[0]=rgb[1]=rgb[2]=255;
			  		}
			  		setPixel(x,y,rgb);
		  		}
	  		 }
	  		break;
	  case 4:for(int y=0;y<getH();y++) {
	  			for(int x=0;x<getW();x++) {
	  				rgb=getPixel(x,y);
	  				if(rgb[0]<=64) {
	  					rgb[0]=rgb[1]=rgb[2]=0;
	  				}
	  				else if(rgb[0]<=128) {
	  					rgb[0]=rgb[1]=rgb[2]=85;
	  				}
	  				else if(rgb[0]<=192) {
	  					rgb[0]=rgb[1]=rgb[2]=170;
	  				}
	  				else {
	  					rgb[0]=rgb[1]=rgb[2]=255;
	  				}
	  				setPixel(x,y,rgb);
	     		}
  		 	}
  			break;
	  case 8:for(int y=0;y<getH();y++) {
				for(int x=0;x<getW();x++) {
					rgb=getPixel(x,y);
					if(rgb[0]<=32) {
						rgb[0]=rgb[1]=rgb[2]=0;
					}
					else if(rgb[0]<=64) {
						rgb[0]=rgb[1]=rgb[2]=48;
					}
					else if(rgb[0]<=96) {
						rgb[0]=rgb[1]=rgb[2]=80;
					}
					else if(rgb[0]<=128) {
						rgb[0]=rgb[1]=rgb[2]=112;
					}
					else if(rgb[0]<=160) {
						rgb[0]=rgb[1]=rgb[2]=144;
					}
					else if(rgb[0]<=192) {
						rgb[0]=rgb[1]=rgb[2]=176;
					}
					else if(rgb[0]<=224) {
						rgb[0]=rgb[1]=rgb[2]=208;
					}
					else {
						rgb[0]=rgb[1]=rgb[2]=255;
					}
					setPixel(x,y,rgb);
				}
	 		}
			break;	
	  case 16:for(int y=0;y<getH();y++) {
				for(int x=0;x<getW();x++) {
					rgb=getPixel(x,y);
					if(rgb[0]<=16) {
						rgb[0]=rgb[1]=rgb[2]=0;
					}
					else if(rgb[0]<=32) {
						rgb[0]=rgb[1]=rgb[2]=24;
					}
					else if(rgb[0]<=48) {
						rgb[0]=rgb[1]=rgb[2]=40;
					}
					else if(rgb[0]<=64) {
						rgb[0]=rgb[1]=rgb[2]=56;
					}
					else if(rgb[0]<=80) {
						rgb[0]=rgb[1]=rgb[2]=72;
					}
					else if(rgb[0]<=96) {
						rgb[0]=rgb[1]=rgb[2]=88;
					}
					else if(rgb[0]<=112) {
						rgb[0]=rgb[1]=rgb[2]=104;
					}
					else if(rgb[0]<=128) {
						rgb[0]=rgb[1]=rgb[2]=120;
					}
					else if(rgb[0]<=144) {
						rgb[0]=rgb[1]=rgb[2]=136;
					}
					else if(rgb[0]<=160) {
						rgb[0]=rgb[1]=rgb[2]=152;
					}
					else if(rgb[0]<=176) {
						rgb[0]=rgb[1]=rgb[2]=168;
					}
					else if(rgb[0]<=192) {
						rgb[0]=rgb[1]=rgb[2]=184;
					}
					else if(rgb[0]<=208) {
						rgb[0]=rgb[1]=rgb[2]=200;
					}
					else if(rgb[0]<=224) {
						rgb[0]=rgb[1]=rgb[2]=216;
					}
					else if(rgb[0]<=240) {
						rgb[0]=rgb[1]=rgb[2]=232;
					}
					else {
						rgb[0]=rgb[1]=rgb[2]=255;
					}
					setPixel(x,y,rgb);
				}
			}
			break;
		default:
			System.out.println("Incorrect option");
	  }
  }
  
  public void errorDiffusion() {
	  int[] rgb=new int[3];
	  Scanner input=new Scanner(System.in);
	  System.out.println("Enter the level:");
	  System.out.println("You can enter 2 or 4");
	  int n=input.nextInt();
	  switch(n) {
	  
	  case 2:for(int y=0;y<getH();y++) {
		  		for(int x=0;x<getW();x++) {
		  			int oldPixel=getPixels(x,y);
		  			rgb=getPixel(x,y);
		  			if(rgb[0]<=128) {
		  				rgb[0]=rgb[1]=rgb[2]=0;
		  			}
		  			else {
		  				rgb[0]=rgb[1]=rgb[2]=255;
		  			}
		  			setPixel(x,y,rgb);
		  			int newPixel=getPixels(x,y);
		  			int error=oldPixel-newPixel;
		  			try {
		  			setPixel(x+1,y,getPixels(x+1,y)+error *7/16);
		  			setPixel(x-1,y+1,getPixels(x-1,y+1)+error *7/16);
		  			setPixel(x,y+1,getPixels(x,y+1)+error *7/16);
		  			setPixel(x+1,y+1,getPixels(x+1,y+1)+error *7/16);
		  			}
		  			catch(ArrayIndexOutOfBoundsException e) {
		  				e.getMessage();
		  			}
		  		}	
	  		}
	  		break;
	  case 4:for(int y=0;y<getH();y++) {
	  			for(int x=0;x<getW();x++) {
	  				int oldPixel=getPixels(x,y);
	  				rgb=getPixel(x,y);
	  				if(rgb[0]<=64) {
	  					rgb[0]=rgb[1]=rgb[2]=0;
	  				}
	  				else if(rgb[0]<=128) {
	  					rgb[0]=rgb[1]=rgb[2]=85;
	  				}
	  				else if(rgb[0]<=192) {
	  					rgb[0]=rgb[1]=rgb[2]=170;
	  				}
	  				else {
	  					rgb[0]=rgb[1]=rgb[2]=255;
	  				}
	  				setPixel(x,y,rgb);
	  				int newPixel=getPixels(x,y);
	  				int error=oldPixel-newPixel;
	  				try {
	  					setPixel(x+1,y,getPixels(x+1,y)+error *7/16);
	  					setPixel(x-1,y+1,getPixels(x-1,y+1)+error *7/16);
	  					setPixel(x,y+1,getPixels(x,y+1)+error *7/16);
	  					setPixel(x+1,y+1,getPixels(x+1,y+1)+error *7/16);
	  				}
	  				catch(ArrayIndexOutOfBoundsException e) {
	  				e.getMessage();
	  				}
	  			}	
  			}
  			break;
  		default:
  			System.out.println("Not proper input");
	  }
	  
  }
  
  public void uniformColorQuantization() {
	  int[][] LUT=new int[256][4];
		 
		 System.out.println("Index  R  G  B");
		 for(int i=0;i<256;i++) {
				 	
					 byte i1=(byte)(i);
					 byte r=(byte)(i1>>5);
					 byte g1=(byte)(i1>>2);
					 byte g2=(byte)(g1<<5);
					 byte g=(byte)(g2>>5);
					 byte b1=(byte)(i1<<6);
					 byte b=(byte)(b1>>6);
					 
					 int rint=(int) (0xFF & r);
					 int gint=(int) (0xFF & g);
					 int bint=(int) (0xFF & b);
					
					 
					 LUT[i][0]=i;
					 LUT[i][1]=(16+rint*32);
					 LUT[i][2]=(16+gint*32);
					 LUT[i][3]=(32+bint*64);
					 
					 System.out.println(LUT[i][0]+"     "+LUT[i][1]+" "+LUT[i][2]+" "+LUT[i][3]);
					 
			 }
		 int index[][]=new int[getW()][getH()];
		 	
			 for(int y=0;y<getH();y++) {
				 for(int x=0;x<getW();x++) {
					int[] rgb= new int[3];
					rgb=getPixel(x,y);
					int minIndex=0;
					int minD=256*256*256;

					for(int i=0;i<256;i++) {
						int dr=rgb[0]-LUT[i][1];
						int dg=rgb[1]-LUT[i][2];
						int db=rgb[2]-LUT[i][3];
						
						int distance=(int) Math.sqrt((dr*dr)+(dg*dg)+(db*db));
						
						if(distance<minD) {
							minD=distance;
							minIndex=i;
						}
					}
					
					index[x][y]=minIndex;
					rgb[0]=minIndex;
					rgb[1]=minIndex;
					rgb[2]=minIndex;	
					
					setPixel(x,y,rgb);	
				 }
				 }
			 
			 
			 write2PPM("[fileName]"+"-index.ppm");
			 
			 for(int y=0;y<getH();y++){
				 for(int x=0;x<getW();x++) {
					 int[] rgb=new int[3];
					 getPixel(x,y,rgb);
					 rgb[0]=LUT[index[x][y]][1];
					 rgb[1]=LUT[index[x][y]][2];
					 rgb[2]=LUT[index[x][y]][3];
					 setPixel(x,y,rgb);
				 }
			 }
			 write2PPM("[fileName]"+"QT8.ppm");
		 
	 }
  }

  // Image class