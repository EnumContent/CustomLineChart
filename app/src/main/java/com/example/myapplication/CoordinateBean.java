package com.example.myapplication;

/**
     * 坐标系bean
     */
    class CoordinateBean {
        public float maxValue;//最大值
        public float minValue;//最小值
        public int labelCount;//刻度数量
        public float labelSpaceValue;//每个刻度的间隔值

        public int origin;//原点坐标
        public float minCoordinate;//在坐标系中的最小值
        public float maxCoordinate;//在坐标系中的最大值
        public float labelSpaceCoordinate;//每个刻度的坐标间隔

        @Override
        public String toString() {
            return "CoordinateBean{" +
                    "maxValue=" + maxValue +
                    ", minValue=" + minValue +
                    ", labelCount=" + labelCount +
                    ", labelSpaceValue=" + labelSpaceValue +
                    ", origin=" + origin +
                    ", minCoordinate=" + minCoordinate +
                    ", maxCoordinate=" + maxCoordinate +
                    ", labelSpaceCoordinate=" + labelSpaceCoordinate +
                    '}';
        }

        public void setValue(float maxValue, float minValue, int labelCount) {
            this.maxValue = maxValue;
            this.minValue = minValue;
            this.labelCount = labelCount;
            this.labelSpaceValue = (maxValue - minValue) / (labelCount - 1);
        }

        /**
         * 通过值获取在坐标系中的坐标
         */
        public int getCoordinateByValue(float value) {
            float perCoordinate = labelSpaceCoordinate / labelSpaceValue;//单位值跨度的坐标

            float coordinate = 0;

            if (origin == minCoordinate) {
                coordinate = origin + value * perCoordinate;
            } else {
                coordinate = origin - value * perCoordinate;
            }

            coordinate = getAvailableCoordinate(coordinate);
            return (int) coordinate;
        }

        /**
         * 判断指定坐标是否在坐标系内
         */
        public boolean containCoordinate(float coordinate) {
            return coordinate >= minCoordinate && coordinate <= maxCoordinate;
        }

        public boolean containValue(float value) {
            return value >= minValue && value <= maxValue;
        }


    /**
     * 获取不超出边界的坐标
     * @param coordinate
     * @return
     */
    public float getAvailableCoordinate(float coordinate) {

            if (!containValue(coordinate)) {
                coordinate = Math.min(coordinate, maxCoordinate);
                coordinate = Math.max(coordinate, minCoordinate);
            }
            return coordinate;
        }
        /**
         * 通过坐标系中的坐标获取应该的值
         */
        public float getValueByCoordinate(float coordinate) {
            float perValue = labelSpaceValue / labelSpaceCoordinate;//单位坐标的跨度的值
            float dCoordinate = coordinate - origin;
            float value = 0;
            if (origin == minCoordinate) {
                value = minValue + dCoordinate * perValue;
            } else {
                value = minValue - dCoordinate * perValue;
            }

            value = getAvailableValue(value);
            return value;

        }


    /**
     * 获取不超出边界的值
     * @param value
     * @return
     */
    public float getAvailableValue(float value) {
        if (!containValue(value)) {
            value = Math.min(value, maxValue);
            value = Math.max(value, minValue);
        }
        return value;
    }
}