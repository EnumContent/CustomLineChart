package com.example.myapplication;

public class PointBean {
    private float valueY;
    private float valueX;

    private float coordinateY;
    private float coordinateX;

    private ViewHolder viewHolder;

    public PointBean(float valueY, float valueX) {
        this.valueY = valueY;
        this.valueX = valueX;
    }

    public PointBean() {
    }

    public void setCoordinate(float coordinateX, float coordinateY, CoordinateBean x, CoordinateBean y) {
        if (x != null && y != null) {
            this.coordinateX = x.getAvailableCoordinate(coordinateX);
            this.coordinateY = y.getAvailableCoordinate(coordinateY);
            this.valueX = x.getValueByCoordinate(coordinateX);
            this.valueY = y.getValueByCoordinate(coordinateY);
        }
    }

    /**
     * 计算坐标
     *
     * @param x
     * @param y
     */
    public void initCoordinate(CoordinateBean x, CoordinateBean y) {
        if (coordinateX == 0 && x != null)
            this.coordinateX = x.getCoordinateByValue(valueX);
        if (coordinateY == 0 && y != null)
            this.coordinateY = y.getCoordinateByValue(valueY);
    }

    public float getValueY() {
        return valueY;
    }

    public float getValueX() {
        return valueX;
    }

    public float getCoordinateY() {
        return coordinateY;
    }

    public float getCoordinateX() {
        return coordinateX;
    }

    public ViewHolder getViewHolder() {
        return viewHolder;
    }

    public void setViewHolder(ViewHolder viewHolder) {
        this.viewHolder = viewHolder;
    }
}
