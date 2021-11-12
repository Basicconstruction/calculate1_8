package parser;

public class DoublePrecision {
    public static double round(double v,int scale){
        return Math.round(v*Math.pow(10,scale))*1.0 / Math.pow(10,scale);
    }
}
