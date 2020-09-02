package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.util.Linkify;
import android.util.SparseArray;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Checkable;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;



public class ViewHolder  {
    private SparseArray<View> mViews;
    private View mConvertView;
    private Context mContext;

    public ViewHolder(Context context, View itemView) {
        mContext = context;
        mConvertView = itemView;
        mViews = new SparseArray<>();
    }


    public static ViewHolder createViewHolder(Context context, View itemView) {
        ViewHolder holder = new ViewHolder(context, itemView);
        return holder;
    }

    public static ViewHolder createViewHolder(Context context,
                                              ViewGroup parent, int layoutId) {
        View itemView = LayoutInflater.from(context).inflate(layoutId, parent,
                false);
        ViewHolder holder = new ViewHolder(context, itemView);
        return holder;
    }

    /**
     * 通过viewId获取控件
     *
     * @param viewId
     * @return
     */
    public <T extends View> T getView(int viewId) {
        View view = mViews.get(viewId);
        if (view == null) {
            view = mConvertView.findViewById(viewId);
            mViews.put(viewId, view);
        }
        return (T) view;
    }

    public View getConvertView() {
        return mConvertView;
    }


    /****以下为辅助方法*****/

    /**
     * 设置TextView的值
     *
     * @param viewId
     * @param text
     * @return
     */
    public ViewHolder setText(int viewId, CharSequence text) {
        TextView textView = getView(viewId);
        if (textView != null) {
            textView.setText(text);
        }
        return this;
    }

    /**
     * 设置TextView的值
     *
     * @param viewId
     * @param text
     * @return
     */
    public ViewHolder setText(int viewId, SpannableString text) {
        TextView textView = getView(viewId);
        if (textView != null) {
            textView.setText(text);
        }
        return this;
    }

    public ViewHolder setText(int viewId, String text) {
        setText(viewId, text, "");
        return this;
    }



    public String getText(int viewId) {
        TextView view = getView(viewId);
        if (view != null) {
            return view.getText().toString();
        }
        return "";
    }

    public ViewHolder setEnable(int viewId, boolean enable) {
        View view = getView(viewId);
        if (view != null) {
            view.setEnabled(enable);
        }
        return this;
    }

    /**
     * //添加删除线
     * txt1.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
     * //在代码中设置加粗
     * txt2.getPaint().setFlags(Paint.FAKE_BOLD_TEXT_FLAG);
     * //添加下划线
     * txt3.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
     *
     * @param viewId
     * @param textFlag
     * @return
     */
    public ViewHolder setTextViewFlag(int viewId, int textFlag) {
        TextView textView = getView(viewId);
        if (textView != null) {
            textView.getPaint().setFlags(textFlag);
        }
        return this;
    }


    public ViewHolder setText(int viewId, String textString, String defaultStr) {
        TextView textView = getView(viewId);
        if (textView != null) {
            if (!TextUtils.isEmpty(textString) && !textString.contains("null")) {
                textView.setText(textString);
            } else {
                textView.setText(defaultStr);
            }
        }
        return this;
    }

    public ViewHolder setText(int viewId, String text, boolean isVsiable) {
        TextView tv = getView(viewId);
        if (!TextUtils.isEmpty(text) && !text.contains("null")) {
            tv.setVisibility(View.VISIBLE);
            tv.setText(text);
        } else {
            if (isVsiable) {
                tv.setVisibility(View.GONE);
            } else {
                tv.setText("");
            }
        }
        return this;
    }

    public ViewHolder setText(int viewId, String text, int textSize) {
        TextView tv = getView(viewId);
        if (!TextUtils.isEmpty(text)) {
            tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
            tv.setText(text);
        } else {
            tv.setText("");
        }
        return this;
    }


    public ViewHolder setMaxLine(int viewId, int line) {
        if (line > 0) {
            TextView tv = getView(viewId);
            tv.setMaxLines(line);
        }
        return this;
    }

    public ViewHolder setImageResource(int viewId, int resId) {
        ImageView view = getView(viewId);
        if (view != null) view.setImageResource(resId);
        return this;
    }


    public ViewHolder setImageBitmap(int viewId, Bitmap bitmap) {
        ImageView view = getView(viewId);
        if (view != null) view.setImageBitmap(bitmap);
        return this;
    }


    public ViewHolder setImageDrawable(int viewId, Drawable drawable) {
        ImageView view = getView(viewId);
        if (view != null) view.setImageDrawable(drawable);
        return this;
    }

    public ViewHolder setBackgroundColor(int viewId, int color) {
        View view = getView(viewId);
        if (view != null) view.setBackgroundColor(view.getResources().getColor(color));
        return this;
    }

    public ViewHolder setBackgroundResource(int viewId, int drawable) {
        View view = getView(viewId);
        if (view != null) view.setBackgroundResource(drawable);
        return this;
    }


    public ViewHolder setBackgroundRes(int viewId, int backgroundRes) {
        View view = getView(viewId);
        if (view != null) view.setBackgroundResource(backgroundRes);
        return this;
    }

    public ViewHolder setBackgroundRes(int viewId, Drawable drawable) {
        View view = getView(viewId);
        if (view != null && drawable != null) view.setBackground(drawable);
        return this;
    }

    public ViewHolder setTextColor(int viewId, int textColor) {
        TextView view = getView(viewId);
        if (view != null) view.setTextColor(view.getResources().getColor(textColor));
        return this;
    }

    public ViewHolder setTextColorRes(int viewId, int textColorRes) {
        TextView view = getView(viewId);
        if (view != null) view.setTextColor(mContext.getResources().getColor(textColorRes));
        return this;
    }

    @SuppressLint("NewApi")
    public ViewHolder setAlpha(int viewId, float value) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            getView(viewId).setAlpha(value);
        } else {
            // Pre-honeycomb hack to set Alpha value
            AlphaAnimation alpha = new AlphaAnimation(value, value);
            alpha.setDuration(0);
            alpha.setFillAfter(true);
            getView(viewId).startAnimation(alpha);
        }
        return this;
    }

    public ViewHolder setVisible(int viewId, boolean visible) {
        View view = getView(viewId);
        if (view != null) {
            int oldVisibilty = view.getVisibility();
            int newVisibility = visible ? View.VISIBLE : View.GONE;
            if (oldVisibilty != newVisibility) {
                view.setVisibility(newVisibility);
            }
        }
        return this;
    }

    public ViewHolder setVisible(int viewId, int visible) {
        View view = getView(viewId);
        if (view != null) view.setVisibility(visible);
        return this;
    }

    public ViewHolder linkify(int viewId) {
        TextView view = getView(viewId);
        Linkify.addLinks(view, Linkify.ALL);
        return this;
    }

    public ViewHolder setTypeface(Typeface typeface, int... viewIds) {
        for (int viewId : viewIds) {
            TextView view = getView(viewId);
            view.setTypeface(typeface);
            view.setPaintFlags(view.getPaintFlags() | Paint.SUBPIXEL_TEXT_FLAG);
        }
        return this;
    }

    public ViewHolder setProgress(int viewId, int progress) {
        ProgressBar view = getView(viewId);
        if (view == null)
            return this;
        view.setProgress(progress);
        return this;
    }

    public ViewHolder setProgress(int viewId, int progress, int max) {
        ProgressBar view = getView(viewId);
        if (view == null)
            return this;
        view.setMax(max);
        view.setProgress(progress);
        return this;
    }

    public ViewHolder setMax(int viewId, int max) {
        ProgressBar view = getView(viewId);
        if (view == null)
            return this;
        view.setMax(max);
        return this;
    }

    public ViewHolder setRating(int viewId, float rating) {
        RatingBar view = getView(viewId);
        if (view == null)
            return this;
        view.setRating(rating);
        return this;
    }

    public ViewHolder setRating(int viewId, float rating, int max) {
        RatingBar view = getView(viewId);
        if (view == null)
            return this;
        view.setMax(max);
        view.setRating(rating);
        return this;
    }

    public ViewHolder setTag(int viewId, Object tag) {
        View view = getView(viewId);
        if (view != null)
            view.setTag(tag);
        return this;
    }

    Object tag;

    public ViewHolder setTag(Object tag) {
        this.tag = tag;
        return this;
    }

    public Object getTag() {
        return tag;
    }

    public ViewHolder setTag(int viewId, int key, Object tag) {
        View view = getView(viewId);
        if (view != null)
            view.setTag(key, tag);
        return this;
    }

    public ViewHolder setChecked(int viewId, boolean checked) {
        Checkable view = getView(viewId);
        if (view != null)
            view.setChecked(checked);
        return this;
    }

    /**
     * 关于事件的
     */
    public ViewHolder setOnClickListener(int viewId, View.OnClickListener listener) {
        View view = getView(viewId);
        if (view != null)
            view.setOnClickListener(listener);
        return this;
    }

    public ViewHolder setOnTouchListener(int viewId, View.OnTouchListener listener) {

        View view = getView(viewId);
        if (view != null)
            view.setOnTouchListener(listener);
        return this;
    }

    public ViewHolder setOnLongClickListener(int viewId, View.OnLongClickListener listener) {
        View view = getView(viewId);
        if (view != null)
            view.setOnLongClickListener(listener);
        return this;
    }


    public ViewHolder setInVisible(int viewId, boolean invisible) {
        View view = getView(viewId);
        if (view != null)
            view.setVisibility(invisible ? View.INVISIBLE : View.VISIBLE);
        return this;
    }
}
