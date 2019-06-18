import com.sun.javafx.geom.Point2D;

public class Tools2D{
    public static float area2(Point2D a, Point2D b, Point2D c){
        return (a.x-c.x)*(b.y-c.y)-(a.y-c.y)*(b.x-c.x);
    }
}