import java.awt.*;
import java.io.Serializable;

/**
 * Created by bmix1 on 8/14/2017.
 */
public class Move implements Serializable {
    private Point startPosition;
    private Point endPosition;

    public Move(int x1, int y1, int x2, int y2) {
        this.startPosition = new Point(x1, y1);
        this.endPosition = new Point(x2, y2);
    }

    public Point getStartPosition() {
        return this.startPosition;
    }

    public Point getEndPosition() {
        return this.endPosition;
    }

    public static double distance(Point p1, Point p2) {
        return Math.sqrt((p1.x-p2.x)*(p1.x-p2.x) + (p1.y-p2.y)*(p1.y-p2.y));
    }


}