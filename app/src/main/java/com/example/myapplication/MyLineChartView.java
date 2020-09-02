package com.example.myapplication;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by zhouxu on 2018/12/3.
 */

public class MyLineChartView extends FrameLayout {

    private Path path;
    private ViewHolder viewHolder;

    public enum Model {
        /**
         * 标准模式，此模式能拖动已绘制好的
         */
        standard,

        /**
         * 删除模式，此模式仅能在点击已有位置后直接删除
         */
        delete
    }

    private Model model = Model.standard;

    private int mWidth; // 控件宽度

    private int mHeight; // 控件高度

    private int xyTextSize = 24; //xy轴文字大小

    private int paddingTop = 100;// 默认上下左右的padding

    private int paddingLeft = 160;

    private int paddingRight = 200;

    private int paddingBottom = 60;

    private int textToXYAxisGap = 20; // xy轴的文字距xy线的距离

    private Paint paintWhite, paintBlue, paintText, paintGray;

    private ArrayList<PointBean> points = new ArrayList<>();//保存所有点

    private int backGroundColor = Color.parseColor("#272727"); // view的背景颜色

    DecimalFormat decimalFormat = new DecimalFormat("0.00");
    DecimalFormat decimalFormat2 = new DecimalFormat("0.0");
    private CoordinateBean mX;//横坐标相关数据
    private CoordinateBean mY;//纵坐标相关数据
    private GestureDetector gestureDetector;

    public MyLineChartView(Context context) {
        this(context, null);
    }

    public MyLineChartView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyLineChartView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint();

        init();
    }


    /**
     * 初始化
     */
    private void init() {
        gestureDetector = new GestureDetector(getContext(), new MyOnGestureListener());
        viewHolder = getView(R.layout.touch);
        viewHolder.setOnClickListener(R.id.delete, new OnClickListener() {//切换到删除模式
            @Override
            public void onClick(View v) {
                model = model == Model.delete ? Model.standard : Model.delete;
                viewHolder.setImageResource(R.id.delete, model == Model.delete ? R.mipmap.delete_on : R.mipmap.delete_off);
                notifyDataChanged();
            }
        });
        viewHolder.setOnClickListener(R.id.add, new OnClickListener() {//显示新增图标

            @Override
            public void onClick(View v) {
                View view = viewHolder.getView(R.id.add_icon);
                viewHolder.setVisible(R.id.add_icon, view.getVisibility() == GONE);
            }
        });
        initAddIcon();
        addView(viewHolder.getConvertView());
        notifyDataChanged();
    }

    /**
     * 初始化Add图标
     */
    private void initAddIcon() {
        View view = viewHolder.getView(R.id.add_icon);
        view.setTranslationY(0);
        view.setTranslationX(0);
        viewHolder.setVisible(R.id.add_icon, false);
    }

    public void setData(ArrayList<PointBean> data) {
        if (data != null) {
            points.clear();
            points.addAll(data);
            notifyDataChanged();
        }
    }

    /**
     * 刷新
     */
    public void notifyDataChanged() {
        for (int i = 0; i < points.size(); i++) {
            PointBean pointBean = points.get(i);
            ViewHolder viewHolder = pointBean.getViewHolder();
            if (viewHolder == null) {
                viewHolder = getView(R.layout.test);
                pointBean.setViewHolder(viewHolder);
                View catchView = viewHolder.getConvertView();
                catchView.setTag(R.id.flag_title, new Integer(i));
                setViewData(pointBean, viewHolder);
                addView(catchView);
            } else {
                setViewData(pointBean, viewHolder);
                postInvalidate();
                requestLayout();
            }
            viewHolder.getConvertView().setTag(R.id.flag_title, new Integer(i));
        }
    }

    /**
     * 填充View的数据 这里进行各种数据判断
     *
     * @param pointBean
     * @param viewHolder
     */
    private void setViewData(PointBean pointBean, ViewHolder viewHolder) {
        viewHolder.setText(R.id.flag_title, decimalFormat2.format(pointBean.getValueY()) + "/" + decimalFormat2.format(pointBean.getValueX()));
        viewHolder.setVisible(R.id.flag_delete, model == Model.delete);
    }

    /**
     * 获取新View
     */
    private ViewHolder getView(int id) {
        return ViewHolder.createViewHolder(getContext(), this, id);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        for (int i = 0; i < getChildCount(); i++) {
            View childView = getChildAt(i);
            ViewGroup.LayoutParams layoutParams = childView.getLayoutParams();
            if (childView.getVisibility() != View.GONE) {
                //将layoutParams转变成为 measureSpec
                int childWidthMeasureSpec = getChildMeasureSpec(widthMeasureSpec, paddingLeft + paddingRight,
                        layoutParams.width);
                int childHeightMeasureSpec = getChildMeasureSpec(heightMeasureSpec, paddingTop + paddingBottom, layoutParams.height);
                childView.measure(childWidthMeasureSpec, childHeightMeasureSpec);
            }
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        for (int count = 0; count < getChildCount(); count++) {
            View childAt = getChildAt(count);
            Object tag = childAt.getTag(R.id.flag_title);//获取做了标记的图标控件
            if (tag instanceof Integer) {
                Integer index = (Integer) tag;
                PointBean pointBean = points.get(index);
                pointBean.initCoordinate(mX, mY);

                int y = (int) pointBean.getCoordinateY();
                int x = (int) pointBean.getCoordinateX();
                int height = childAt.getMeasuredHeight();//
                int width = childAt.getMeasuredWidth();//
                childAt.layout(x - (width / 2), y - height, x + width / 2, y - smallCircleR);
            } else {
                childAt.layout(0, 0, r, b);
            }
        }
    }


    private void initPaint() {
        path = new Path();

        paintWhite = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintWhite.setColor(Color.WHITE);
        paintWhite.setStyle(Paint.Style.STROKE);

        paintGray = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintGray.setColor(Color.parseColor("#666666"));
        paintGray.setStyle(Paint.Style.STROKE);

        paintBlue = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintBlue.setColor(Color.parseColor("#0198cd"));
        paintBlue.setStrokeWidth(3f);
        paintBlue.setStyle(Paint.Style.STROKE);

        paintText = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintText.setColor(Color.WHITE);
        paintText.setTextSize(xyTextSize);
        paintText.setStrokeWidth(2f);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mX = new CoordinateBean();
        mY = new CoordinateBean();
        mWidth = w;
        mHeight = h;

        mX.setValue(24, 0, 13);
        mX.labelSpaceCoordinate = (w - paddingLeft - paddingRight) / (float) (mX.labelCount - 1);
        mX.minCoordinate = mX.origin = paddingLeft;
        mX.maxCoordinate = mWidth - paddingRight;

        mY.setValue(40, 0, 9);
        mY.labelSpaceCoordinate = (h - paddingTop - paddingBottom) / (float) mY.labelCount;//多留一格位置
        mY.maxCoordinate = mY.origin = mHeight - paddingBottom;


        mY.minCoordinate = paddingTop + mY.labelSpaceCoordinate;

        setBackgroundColor(backGroundColor);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawX(canvas);//画X轴
        drawY(canvas);//画Y轴
        drawBrokenLine(canvas);//画折线
    }

    /**
     * 画x轴
     *
     * @param canvas
     */
    private void drawX(Canvas canvas) {
        for (int i = 0; i < mX.labelCount; i++) {
            // x轴上的文字
            String text = decimalFormat.format(mX.minValue + mX.labelSpaceValue * i);
            canvas.drawText(text, mX.origin + i * mX.labelSpaceCoordinate - getTextWidth(paintText, "17.01") / 2,
                    mY.origin + textToXYAxisGap + getTextHeight(paintText, "17.01"), paintText);
        }
    }

    private int smallCircleR = 5;//圆的半径

    /**
     * 画折线
     *
     * @param canvas
     */
    private void drawBrokenLine(Canvas canvas) {
        ArrayList<PointBean> pointBeans = new ArrayList<>(points);
        Collections.sort(pointBeans, new Comparator<PointBean>() {//x轴升序排列
            @Override
            public int compare(PointBean o1, PointBean o2) {
                float v = o1.getValueX() - o2.getValueX();
                if (v > 0) {
                    return 1;
                } else if (v < 0) {
                    return -1;
                } else {
                    return 0;
                }
            }
        });
        path.reset();
        //画折线
        float currentCoordinateY = 0;
        for (int i = 0; i < pointBeans.size(); i++) {
            PointBean pointBean = pointBeans.get(i);
            if (i == 0) {
                currentCoordinateY = pointBean.getCoordinateY();
                path.moveTo(pointBean.getCoordinateX(), currentCoordinateY);
            } else {
                path.lineTo(pointBean.getCoordinateX(), currentCoordinateY);
                currentCoordinateY = pointBean.getCoordinateY();
                path.lineTo(pointBean.getCoordinateX(), currentCoordinateY);
            }
        }
        canvas.drawPath(path, paintBlue);

        //画圆点
        for (int i = 0; i < pointBeans.size(); i++) {
            PointBean pointBean = pointBeans.get(i);
            canvas.drawCircle(pointBean.getCoordinateX(), pointBean.getCoordinateY(), smallCircleR, paintBlue);
        }
    }

    /**
     * 画y轴
     *
     * @param canvas
     */
    private void drawY(Canvas canvas) {
        for (int i = 0; i < mY.labelCount; i++) {
            //画刻度标注
            String title = i == 0 ? "关闭" : String.valueOf(mY.minValue + i * mY.labelSpaceValue);
            canvas.drawText(title, mX.origin - textToXYAxisGap - getTextWidth(paintText, "00.00"),
                    mHeight - paddingBottom - i * mY.labelSpaceCoordinate + getTextHeight(paintText, "00.00") / 2, paintText);
            //画刻度线
            path.reset();
            path.moveTo(mX.origin, mHeight - paddingBottom - i * mY.labelSpaceCoordinate);
            path.lineTo(mWidth - paddingRight, mHeight - paddingBottom - i * mY.labelSpaceCoordinate);
            canvas.drawPath(path, i == 0 ? paintWhite : paintGray);
        }
    }


    private boolean pointAtView(float x, float y, View childAt) {
        if (childAt.getVisibility() == GONE) {
            return false;
        }
        int[] outLocation = new int[2];
        childAt.getLocationOnScreen(outLocation);
        Rect rect = new Rect(outLocation[0], outLocation[1], outLocation[0] + childAt.getWidth(), outLocation[1] + childAt.getHeight());
        return rect.contains((int) x, (int) y);
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {

        return super.onInterceptTouchEvent(event);
    }


    /**
     * 获取文字的宽度
     *
     * @param paint
     * @param text
     * @return
     */
    private int getTextWidth(Paint paint, String text) {
        return (int) paint.measureText(text);
    }

    /**
     * 获取文字的高度
     *
     * @param paint
     * @param text
     * @return
     */
    private int getTextHeight(Paint paint, String text) {
        Rect rect = new Rect();
        paint.getTextBounds(text, 0, text.length(), rect);
        return rect.height();
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:
                touchTagAndPosition = -1;//取消触摸标记
                break;
        }
        return gestureDetector.onTouchEvent(event);
    }

    int touchTagAndPosition = -1;//用于记录触摸到了谁，如果是标签则记录触摸到哪个标签，如果是触摸到新增的图标标签则为-2
    public static final int POSITION_NONE = -1;//默认
    public static final int POSITION_ADD = -2;//触摸到新增图标

    /**
     * 获取触摸到哪个View
     *
     * @param x
     * @param y
     * @return
     */
    private int getTouchPosition(float x, float y) {
        View addView = viewHolder.getView(R.id.add_icon);//是否触摸到添加的图标
        if (pointAtView(x, y, addView)) {
            return POSITION_ADD;
        }

        for (int i = 0; i < getChildCount(); i++) {
            View childAt = getChildAt(i);
            Object tag = childAt.getTag(R.id.flag_title);
            if (tag instanceof Integer) {//如果是tag
                if (pointAtView(x, y, childAt)) {
                    return (int) tag;
                }
            }
        }
        return POSITION_NONE;
    }

    /**
     * 手势事件
     */
    class MyOnGestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onDown(MotionEvent event) { // 按下事件
            float x = event.getRawX();
            float y = event.getRawY();
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    touchTagAndPosition = getTouchPosition(x, y);
                    break;
            }
            return true;
        }

        // 按下停留时间超过瞬时，并且按下时没有松开或拖动，就会执行此方法
        @Override
        public void onShowPress(MotionEvent motionEvent) {
        }

        @Override
        public boolean onSingleTapUp(MotionEvent motionEvent) { // 单击抬起
            int touchPosition = getTouchPosition(motionEvent.getRawX(), motionEvent.getRawY());
            if (model == Model.delete) {//删除模式下删除TAG
                delete(touchPosition);
            }
            return false;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            //  这里的distanceX是e1.getX()-e2.getX()
            float x = e2.getX();
            float y = e2.getY();
            switch (model) {
                case standard://标准模式自由移动
                    if (touchTagAndPosition >= 0) {//移动tag
                        PointBean pointBean = points.get(touchTagAndPosition);
                        pointBean.setCoordinate(pointBean.getCoordinateX() - distanceX, pointBean.getCoordinateY() - distanceY, mX, mY);
                        notifyDataChanged();
                    } else if (touchTagAndPosition == POSITION_ADD) {//添加tag的图标
                        if (mX.containCoordinate(x) && mY.containCoordinate(y)) {
                            int size = addTag(x, y);
                            touchTagAndPosition = size;
                            initAddIcon();
                        } else {
                            View view = viewHolder.getView(R.id.add_icon);
                            view.setTranslationX(view.getTranslationX() - distanceX);
                            view.setTranslationY(view.getTranslationY() - distanceY);
                            postInvalidate();
                        }
                    }
                    break;
            }
            return super.onScroll(e1, e2, distanceX, distanceY);
        }

        @Override
        public void onLongPress(MotionEvent motionEvent) {
        } // 长按事件

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            return false;
        }
    }


    /**
     * 新增tag
     *
     * @param x
     * @param y
     * @return
     */
    private int addTag(float x, float y) {
        int size = points.size();
        PointBean pointBean = new PointBean();
        pointBean.setCoordinate(x, y, mX, mY);
        points.add(pointBean);
        notifyDataChanged();
        return size;
    }

    /**
     * 删除tag
     */
    private void delete(int position) {
        if (position >= 0 && position < points.size()) {
            PointBean pointBean = points.get(position);
            View convertView = pointBean.getViewHolder().getConvertView();
            points.remove(position);//先移除数据，再移除控件
            notifyDataChanged();
            removeView(convertView);
        }
    }


}