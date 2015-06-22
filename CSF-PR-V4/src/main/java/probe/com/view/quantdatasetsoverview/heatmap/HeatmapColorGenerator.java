/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package probe.com.view.quantdatasetsoverview.heatmap;

/**
 *
 * @author Yehia Farag
 */
public class HeatmapColorGenerator {

    private final double max;
    private final double min;

    public HeatmapColorGenerator(double max, double min) {

        this.max = max;
        this.min = min;
    }

    public String getColor(double value) {
        if(value == 0)
            return "RGB("+255+","+255+","+255+")";
        double n = (value)/max;
        //color 1 red =203  green = 220 blue = 255; //color 2 35 42/247
//        int R = (int) (35 * n + 203 * (1 - n));
//        int G = (int) (42 * n + 220  * (1 - n));
//        int B = (int) (247 * n + 255 * (1 - n));
        
        int R =49;
        int G = (int) (46.0+(146.0-(146.0*n))); //(223 * (100 - n)) / 100 ;
        int B =229;//(int) (255 * (100 - n)) / 100 ; //(int) (255 * n) / 100;

        return "RGB("+R+","+G+","+B+")";
    }
}
