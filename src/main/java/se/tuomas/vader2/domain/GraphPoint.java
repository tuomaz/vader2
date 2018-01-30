package se.tuomas.vader2.domain;

import java.util.Date;

public class GraphPoint {
    Object[] point = new Object[2];

    public GraphPoint(float y, Date x) {
        this.point[0] = x;
        this.point[1] = y;
    }

    public Object[] getPoint() {
        return point;
    }
}
