import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.Graphics2D;
import java.awt.Color;
import java.io.File;
import java.io.IOException;

class class1
{
    //AUSFÜLLEN
    private double T = 5d; // Abstand von der Mitte der beiden Kameras in cm
    private int xlength1_2 = 160; // gesamtlänge der x achse der bilder der linken und rechten Kamera in pixel
    private int ylength1_2 = 120; // gesamtlänge der y achse der bilder der linken und rechten Kamera in pixel
    private double f1_2 = 2.7; // focal length für linke und rechte Kamera in cm
    private double FOVH1_2 = 62; // FOV horizontal für die linke und rechte Kamera
    private int range = 100; //Maximale Distanz in cm
    private int search_radius = xlength1_2 / 4;
    //AUSFÜLLEN



    //distanz
    private double xl; // abstand des pixels zur linken seite in cm für den x-Wert der linken kamera
    private double xr; // abstand des pixels zur linken seite in cm für den x-Wert der rechten kamera

    private double d; // abstand von den beiden pixeln horizontal zueinander(linkes und rechtes bild übereinander gesehen) in cm

    private double alpha1_2; // alpha wert für den x-Wert linke und rechte Kamera
    private double gamma1_2; // gamma wert für den x-Wert linke und rechte Kamera

    private double s1_2; // Größe der Anzeigefläche in der x dimension in cm

    private double Z; // zu berechender z-Wert von 1/2 T aus

    //Bufferedimage
    BufferedImage image_left;
    BufferedImage image_right;

    //Class array
    private Pixelclass[][] pixel = new Pixelclass[xlength1_2][ylength1_2];

    BufferedImage output = new BufferedImage(xlength1_2, ylength1_2, BufferedImage.TYPE_INT_RGB);

    Graphics2D g2d = output.createGraphics();
    

    /**
     * 
     * @param args
     */
    public static void main(String[] args) throws Exception
    {
        System.out.println("Hello World!");
        new Pixelclass();
        new class1();
    }

   

    /**
     * Constructor 
     * @throws IOException
     */
    public class1() throws IOException
    {
        try {
        File input_left = new File("picture_left.jpeg");
        File input_right = new File("picture_right.jpeg");
        
        image_left = ImageIO.read(input_left);
        image_right = ImageIO.read(input_right);
    
        } catch (Exception e) {}
        
        //start Timer
        double startTime = System.nanoTime();



        //array befüllen mit den werten
        befüllen();



        //STARTEN DES ALGORYTHMUS
        getDistanzmap();

        System.out.println("Distanz von Pixel (100|100): "+pixel[100][100].getDistance()); //nur debug, array pixel[][] muss als bild ausgegeben werden
        
        //Output File
        setOutput();


        //end Timer
        double endTime = System.nanoTime(); 
        double duration = (endTime - startTime);  //divide by 1000000 to get milliseconds.
        System.out.println("Time: " + duration / 1000000 + "ms");
    }//constructor



    /**
     * 
     * @return array von x und y mit den z werten (0 0 ist links oben der Pixel) !DONE
     */

    public void getDistanzmap()
    {
        for(int y = 0; y < ylength1_2; y++)
        {
            for(int x = 0; x < xlength1_2; x++)
            {
                pixel[x][y].setDistance( getDistanz( x, getRechtenPixel( x, y) ) ); //ruft die "Ausrechen" Methode mit dem x achse pixel des linken und rechten bildes auf
            }//for2
        }//for1
    }//method

    
    /**
     * Befüllt das array mit den werten und dem pixel (by PhiBr0)   !DONE
     */
    public void befüllen()
    {
        for(int x = 0; x < xlength1_2; x++){
            for(int y = 0; y < ylength1_2; y++){
              pixel[x][y] = new Pixelclass();
              System.out.println("Befuellen: "+x); //DEBUG
              getPixelvalue(x, y, false);
            }//for2
          }//for1
    }//method



    /**
     * @return den dazugehörigen pixel des rechten bildes   !DONE(MAYBE)
     */
    public int getRechtenPixel(int x_input, int y_input)
    {

        float myNumber = pixel[x_input][y_input].getHsb_l(0) + pixel[x_input][y_input].getHsb_l(1) + pixel[x_input][y_input].getHsb_l(2);
        float distance = Math.abs( pixel[0][y_input].getHsb_r(0) + pixel[0][y_input].getHsb_r(1) + pixel[0][y_input].getHsb_r(2) - myNumber);
        int idx = 0;
        if(x_input >= search_radius)
        {
            for(int c = x_input - search_radius; c < x_input; c++)
            {
                float cdistance = Math.abs( pixel[c][y_input].getHsb_r(0) + pixel[c][y_input].getHsb_r(1) + pixel[c][y_input].getHsb_r(2) - myNumber);
                if(cdistance < distance)
                {
                    idx = c;
                    distance = cdistance;
                }
            }
            System.out.println("DEBUG: "+idx);
            return idx;
        }
        else
        {
            for(int c = 0; c < xlength1_2; c++)
            {
                float cdistance = Math.abs( pixel[c][y_input].getHsb_r(0) + pixel[c][y_input].getHsb_r(1) + pixel[c][y_input].getHsb_r(2) - myNumber);
                if(cdistance < distance)
                {
                    idx = c;
                    distance = cdistance;
                }
            }
            System.out.println("DEBUG: "+idx);
            return idx; // die ersten können nicht gefunden werden, da der pixel beim rechten bild links ausserhalb des bildes wäre
        }
    }//method



    /**
     * Ausrechnen des Z wertes  !DONE
     */
    public float getDistanz(int xkoord1, int xkoord2)
    {
        gamma1_2 = 90 - FOVH1_2 / 2; // berechnung von gamma für den x-Wert der linken und rechten Kamera
        System.out.println("gamma1_2 "+gamma1_2);
        
        alpha1_2 = FOVH1_2; // alpha x ist gleich FOV horizontal
        
        s1_2 = 2 * (f1_2 / Math.sin(Math.PI * gamma1_2 / 180) * Math.sin(Math.PI * (alpha1_2 / 2) / 180)); // berechnung der länge der Anzeigefläche in cm
        System.out.println("s1_2 "+s1_2);

        xl = s1_2 / xlength1_2 * xkoord1; // berechnung des abstandes vom pixel zur linken seite der linken kamera in cm
        System.out.println("xl "+xl);
        xr = s1_2 / xlength1_2 * xkoord2; // berechnung des abstandes vom pixel zur linken seite der rechten kamera in cm
        System.out.println("xr "+xr);
       
        d = xl - xr; // abstand der beiden pixel horizontal zueinander in cm
        System.out.println("d "+d);

        Z = f1_2 * T / d; // berechnung der Distanz zum aufgenommenen Objekt in cm

        return (float)Z; // gebe distanz zum objekt aus
    }//method



    /**
     * @return zusammengerechneter wert von dem aufgerufenen pixel  !DONE
     */
    public void getPixelvalue(int x, int y, boolean isLeftPictue)
    {
        if(isLeftPictue)
        {
            Color c_l = new Color(image_left.getRGB(x, y)); //Left
            pixel[x][y].setHsb_l(Color.RGBtoHSB(c_l.getRed(), c_l.getGreen(), c_l.getBlue(), null));
            pixel[x][y].setValue_left(pixel[x][y].getH_l());
        }
        else
        {
            Color c_r = new Color(image_right.getRGB(x, y)); //Right
            pixel[x][y].setHsb_r(Color.RGBtoHSB(c_r.getRed(), c_r.getGreen(), c_r.getBlue(), null));
            pixel[x][y].setValue_right(pixel[x][y].getH_r());
        }
        
    }//method



    /**
     * Save a Image with the Values
     * @throws IOException
     */
    public void setOutput() throws IOException
    {
        for(int y = 0; y < ylength1_2; y++)
        {
            for(int x = 0; x < xlength1_2; x++)
            {
                g2d.setColor(Color.getHSBColor( (pixel[x][y].getDistance()-0)/(range-0) * (1-0) + 0 , 1, 1)); //float von hue muss 0-1 sein mit einer maximalen Distanz von range
                g2d.drawLine(x, y, x, y);
            }
            
        }
        // Disposes of this graphics context and releases any system resources that it is using. 
        g2d.dispose();

        // Save as JPEG
        File file = new File("output_file.jpg");
        ImageIO.write(output, "jpg", file);
    }

}