//package deteksikendaraan

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.lang.Math;

/**
 *
 * @author Irrevaldy
 */
public final class Point
{
    private final double x;    // x-coordinate
    private final double y;    // y-coordinate

    // random point
    //public Point() {
      //  x = StdRandom.uniform(0.0, 1.0);
       // y = StdRandom.uniform(0.0, 1.0);
    //}

    // point initialized from parameters
    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getl()
    {
        return x;
    }

    public double getlo()
    {
        return y;
    }

    // accessor methods
    public double x() { return x; }
    public double y() { return y; }
    public double r() { return Math.sqrt(x*x + y*y); }
    public double theta() { return Math.atan2(y, x); }

    // Euclidean distance between this point and that point
    public double distanceTo(Point that) {
        double dx = this.x - that.x;
        double dy = this.y - that.y;
        return Math.sqrt(dx*dx + dy*dy);
    }

    // return a string representation of this point
    public String toString() {
        return "(" + x + ", " + y + ")";
    }

    public double getAngle(Point target)
    {
        double ax = Math.cos(target.x) * Math.sin(this.y - target.y);
        double by = (Math.cos(this.x) * Math.sin(target.x)) - Math.sin(this.x) * Math.cos(target.y) * Math.cos(this.y - target.y); ;

       //double ax = target.x - this.x;
       //double by = target.y - this.y;

       return (double) Math.toDegrees(Math.atan2(by, ax));
    }

    public double haversine(Point target)
    {
        double R = 6372.8; // In kilometers

        double dLat = Math.toRadians(target.x - this.x);
        double dLon = Math.toRadians(target.y - this.y);
        double lat1 = Math.toRadians(this.x);
        double lat2 = Math.toRadians(target.x);

        double a = Math.pow(Math.sin(dLat / 2),2) + Math.pow(Math.sin(dLon / 2),2) * Math.cos(lat1) * Math.cos(lat2);
        double c = 2 * Math.asin(Math.sqrt(a));
        return R * c;
    }

}
