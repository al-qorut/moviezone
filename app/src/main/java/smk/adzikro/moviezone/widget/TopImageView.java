package smk.adzikro.moviezone.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by server on 11/13/17.
 */

public class TopImageView extends android.support.v7.widget.AppCompatImageView {


    public TopImageView(Context context) {
        super(context);
    }

    public TopImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public TopImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int width = getMeasuredWidth();
        setMeasuredDimension(width, width);
    }
}
