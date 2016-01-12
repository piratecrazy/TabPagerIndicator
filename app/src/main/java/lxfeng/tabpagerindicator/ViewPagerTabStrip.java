
package lxfeng.tabpagerindicator;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

public class ViewPagerTabStrip extends LinearLayout {
    private int mSelectedUnderlineThickness;
    private final Paint mSelectedUnderlinePaint;

    private int mIndexForSelection;
    private float mSelectionOffset;
    private int mUnderlineColor;
    private int mBackgroundColor;

    public ViewPagerTabStrip(Context context) {
        this(context, null);
    }

    public ViewPagerTabStrip(Context context, AttributeSet attrs) {
        super(context, attrs);

        final Resources res = context.getResources();

        mSelectedUnderlineThickness =
                res.getDimensionPixelSize(R.dimen.tab_selected_underline_height);
        mUnderlineColor = res.getColor(R.color.tab_selected_underline_color);
        mBackgroundColor = res.getColor(R.color.actionbar_background_color);

        mSelectedUnderlinePaint = new Paint();
        mSelectedUnderlinePaint.setColor(mUnderlineColor);

        setBackgroundColor(mBackgroundColor);
        setWillNotDraw(false);
    }

    @Override
    public void setBackgroundColor(int color) {
        super.setBackgroundColor(color);
    }

    public void setUnderlineColor(int underlineColor){
        mUnderlineColor = underlineColor;
        mSelectedUnderlinePaint.setColor(mUnderlineColor);
    }

    public void setSelectedUnderlineThickness(int size){
        mSelectedUnderlineThickness = size;
    }

    /**
     * Notifies this view that view pager has been scrolled. We save the tab index
     * and selection offset for interpolating the position and width of selection
     * underline.
     */
    void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        mIndexForSelection = position;
        mSelectionOffset = positionOffset;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int childCount = getChildCount();

        // Thick colored underline below the current selection
        if (childCount > 0) {
            View selectedTitle = getChildAt(mIndexForSelection);
            int selectedLeft = selectedTitle.getLeft();
            int selectedRight = selectedTitle.getRight();
            final boolean isRtl = isRtl();
            final boolean hasNextTab = isRtl ? mIndexForSelection > 0
                    : (mIndexForSelection < (getChildCount() - 1));
            if ((mSelectionOffset > 0.0f) && hasNextTab) {
                // Draw the selection partway between the tabs
                View nextTitle = getChildAt(mIndexForSelection + (isRtl ? -1 : 1));
                int nextLeft = nextTitle.getLeft();
                int nextRight = nextTitle.getRight();

                selectedLeft = (int) (mSelectionOffset * nextLeft +
                        (1.0f - mSelectionOffset) * selectedLeft);
                selectedRight = (int) (mSelectionOffset * nextRight +
                        (1.0f - mSelectionOffset) * selectedRight);
            }

            int height = getHeight();
            canvas.drawRect(selectedLeft, height - mSelectedUnderlineThickness,
                    selectedRight, height, mSelectedUnderlinePaint);
        }
    }

    private boolean isRtl() {
        return getLayoutDirection() == View.LAYOUT_DIRECTION_RTL;
    }
}