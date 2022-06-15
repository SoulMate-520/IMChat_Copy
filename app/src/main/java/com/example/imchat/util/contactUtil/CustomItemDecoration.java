package com.example.imchat.util.contactUtil;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.TextUtils;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.imchat.R;
import com.example.imchat.bean.ContactBean;
import com.example.imchat.util.DpUtil;

import java.util.List;

/**
 * @author: yzy
 * @date: 2022/6/13 10:50
 * @description: 绘制recycleview的分割线和顶部悬浮字母栏
 * @version: 1.0
 */
public class CustomItemDecoration extends RecyclerView.ItemDecoration {
    private Paint mPaint;
    private List<ContactBean> mBeans;
    private static final int dividerHeight = 80;
    private Context mContext;
    private final Rect mBounds = new Rect();
    private String tag;

    public void setDatas(List<ContactBean> mBeans, String tag) {
        this.mBeans = mBeans;
        this.tag = tag;
    }

    public CustomItemDecoration(Context mContext) {
        this.mContext = mContext;
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setTextAlign(Paint.Align.CENTER);
    }

    @Override
    public void onDraw(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        if (parent.getLayoutManager() == null) {
            return;
        }
        c.save();
        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            int position = params.getViewLayoutPosition();
            if (mBeans == null || mBeans.size() == 0 || mBeans.size() <= position || position < 0) {
                continue;
            }
            if (position == 0) {
                //第一条数据有bar
                drawTitleBar(c, parent, child, mBeans.get(position), tag.indexOf(mBeans.get(position).getIndexTag()));
            } else if (position > 0) {
                if (TextUtils.isEmpty(mBeans.get(position).getIndexTag())) continue;
                //与上一条数据中的tag不同时，该显示bar了
                if (!mBeans.get(position).getIndexTag().equals(mBeans.get(position - 1).getIndexTag())) {
                    drawTitleBar(c, parent, child, mBeans.get(position), tag.indexOf(mBeans.get(position).getIndexTag()));
                }
            }
        }
        c.restore();
    }

    /**
     * 绘制titlebar
     * @param canvas
     * @param parent
     * @param child
     * @param bean
     * @param position
     */
    private void drawTitleBar(Canvas canvas, RecyclerView parent, View child, ContactBean bean, int position) {
        final int left = 0;
        final int right = parent.getWidth();
        //返回一个包含Decoration和Margin在内的Rect
        parent.getDecoratedBoundsWithMargins(child, mBounds);
        final int top = mBounds.top;
        final int bottom = mBounds.top + Math.round(ViewCompat.getTranslationY(child)) + dividerHeight;
        //设置title背景颜色
        mPaint.setColor(ContextCompat.getColor(mContext, R.color.gray_light));
        canvas.drawRect(left, top, right, bottom, mPaint);
        mPaint.setColor(Color.parseColor("#FFFFFFFF"));
        mPaint.setTextSize(40);
        mPaint.setColor(Color.BLACK);
        canvas.drawText(bean.getIndexTag(), DpUtil.dp2px(mContext, 42.5f), bottom - dividerHeight / 3, mPaint);
    }

    @Override
    public void onDrawOver(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        //绘制顶部的悬浮字母
        int position = ((LinearLayoutManager) (parent.getLayoutManager())).findFirstVisibleItemPosition();
        if (mBeans == null || mBeans.size() == 0 || mBeans.size() <= position || position < 0) {
            return;
        }
        final int bottom = parent.getPaddingTop() + dividerHeight;
        mPaint.setColor(ContextCompat.getColor(mContext, R.color.gray_light));
        c.drawRect(parent.getLeft(), parent.getPaddingTop(), parent.getRight() - parent.getPaddingRight(), parent.getPaddingTop() + dividerHeight, mPaint);
//        ColorUtil.setPaintColor(mPaint, tagsStr.indexOf(mBeans.get(position).getIndexTag()));
        mPaint.setColor(Color.parseColor("#FFFFFFFF"));
        mPaint.setTextSize(40);
//        canvas.drawCircle(DpUtil.dp2px(mContext, 42.5f), bottom - dividerHeight / 2, 35, mPaint);
        mPaint.setColor(Color.BLACK);
        c.drawText(mBeans.get(position).getIndexTag(), DpUtil.dp2px(mContext, 42.5f), bottom - dividerHeight / 3, mPaint);
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        int position = parent.getChildAdapterPosition(view);
        super.getItemOffsets(outRect, view, parent, state);
        if (mBeans == null || mBeans.size() == 0 || mBeans.size() <= position || position < 0) {
            super.getItemOffsets(outRect, view, parent, state);
            return;
        }
        if (position == 0) {
            //第一条数据有bar
            outRect.set(0, dividerHeight, 0, 0);
        } else if (position > 0) {
            if (TextUtils.isEmpty(mBeans.get(position).getIndexTag())) return;
            //与上一条数据中的tag不同时，该显示bar了
            if (!mBeans.get(position).getIndexTag().equals(mBeans.get(position - 1).getIndexTag())) {
                outRect.set(0, dividerHeight, 0, 0);
            }
        }
    }

}
