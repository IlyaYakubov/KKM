package kg.printer.kkm.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;

public class ScrollEditText extends android.support.v7.widget.AppCompatEditText {

    private GestureDetector detector;

    public ScrollEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        detector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {

            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                super.onScroll(e1, e2, distanceX, distanceY);
                boolean clampedY = false;
                if (computeVerticalScrollOffset() == 0
                        && distanceY < 0) {
                    clampedY = true;
                }

                int deltaY = computeVerticalScrollRange() - computeVerticalScrollExtent();
                if ((computeVerticalScrollOffset() == deltaY || deltaY < 0)
                        && distanceY > 0) {
                    clampedY = true;
                }
                if (clampedY) {
                    getParent().requestDisallowInterceptTouchEvent(false);
                }
                return true;
            }
        });
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        getParent().requestDisallowInterceptTouchEvent(true);
        return super.dispatchTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event) | detector.onTouchEvent(event);
    }

}