package com.zjl.finalarchitecture.widget.navigationBar;

import androidx.annotation.FloatRange;
import androidx.annotation.NonNull;
import com.google.android.material.shape.ShapePath;

/**
 * @author Xiaoc
 * 顶部形状处理器
 * 基于{@link com.google.android.material.bottomappbar.BottomAppBarTopEdgeTreatment}修改
 * 将部分限制打开
 *
 * 此顶部形状的特点是从边缘线向下的半圆形切口。创造了两个边缘，圆形切口也可以支持垂直偏移
 * 切口可以不是一个完美的半圆，可以是小于180度的形状这个垂直偏移
 */
public class SagCircleEdgeTreatment extends BaseCircleEdgeTreatment implements Cloneable {

    private static final int ARC_QUARTER = 90;
    private static final int ARC_HALF = 180;
    private static final int ANGLE_UP = 270;
    private static final int ANGLE_LEFT = 180;

    /**
     * @param fabMargin Fab与切出的凹陷间的距离
     * @param roundedCornerRadius 切出的凹陷部分的圆角大小
     * @param cradleVerticalOffset Fab的竖直偏移量
     */
    public SagCircleEdgeTreatment(
            float fabMargin, float roundedCornerRadius, float cradleVerticalOffset) {
        super(BaseCircleEdgeTreatment.SAG_TYPE);
        this.fabMargin = fabMargin;
        this.roundedCornerRadius = roundedCornerRadius;
        setCradleVerticalOffset(cradleVerticalOffset);
        this.horizontalOffset = 0f;
    }

    @Override
    public void getEdgePath(
            float length, float center, float interpolation, @NonNull ShapePath shapePath) {
        if (fabDiameter == 0) {
            // 不存在fab直径时不进行弯曲绘制
            shapePath.lineTo(length, 0);
            return;
        }

        float cradleDiameter = fabMargin * 2 + fabDiameter;
        float cradleRadius = cradleDiameter / 2f;
        float roundedCornerOffset = interpolation * roundedCornerRadius;
        float middle = 0;
        if(isCenter){
            middle = center + horizontalOffset;
        } else {
            middle = horizontalOffset;
        }

        // 切口的中心偏移量介于附着时的竖直偏移量和对应比率。
        float verticalOffset =
                interpolation * cradleVerticalOffset + (1 - interpolation) * cradleRadius;
        float verticalOffsetRatio = verticalOffset / cradleRadius;
        if (verticalOffsetRatio >= 1.0f) {
            // Vertical offset is so high that there's no curve to draw in the edge, i.e., the fab is
            // actually above the edge so just draw a straight line.
            shapePath.lineTo(length, 0);
            return; // Early exit.
        }

        // 通过计算两个相邻圆的位置来计算切口的路径。
        // 一个圆代表圆角。如果圆角圆半径为0，则四舍五入。
        // 另一个圆是切口。用毕达哥拉斯定理算出两个相邻圆中心之间的水平距离。
        float distanceBetweenCenters = cradleRadius + roundedCornerOffset;
        float distanceBetweenCentersSquared = distanceBetweenCenters * distanceBetweenCenters;
        float distanceY = verticalOffset + roundedCornerOffset;
        float distanceX = (float) Math.sqrt(distanceBetweenCentersSquared - (distanceY * distanceY));

        // 计算出左右圆角的内容
        float leftRoundedCornerCircleX = middle - distanceX;
        float rightRoundedCornerCircleX = middle + distanceX;

        // 计算出两个圆之间距离
        float cornerRadiusArcLength = (float) Math.toDegrees(Math.atan(distanceX / distanceY));
        float cutoutArcOffset = ARC_QUARTER - cornerRadiusArcLength;

        // 画出开始的直线
        shapePath.lineTo(leftRoundedCornerCircleX,  0);

        // 画出左边圆角
        shapePath.addArc(
                /* left= */ leftRoundedCornerCircleX - roundedCornerOffset,
                /* top= */ 0,
                /* right= */ leftRoundedCornerCircleX + roundedCornerOffset,
                /* bottom= */ roundedCornerOffset * 2,
                /* startAngle= */ ANGLE_UP,
                /* sweepAngle= */ cornerRadiusArcLength);

        // 画出外切圆
        shapePath.addArc(
                /* left= */ middle - cradleRadius,
                /* top= */ -cradleRadius - verticalOffset,
                /* right= */ middle + cradleRadius,
                /* bottom= */ cradleRadius - verticalOffset,
                /* startAngle= */ ANGLE_LEFT - cutoutArcOffset,
                /* sweepAngle= */ cutoutArcOffset * 2 - ARC_HALF);

        // 画出右边圆角
        shapePath.addArc(
                /* left= */ rightRoundedCornerCircleX - roundedCornerOffset,
                /* top= */ 0,
                /* right= */ rightRoundedCornerCircleX + roundedCornerOffset,
                /* bottom= */ roundedCornerOffset * 2,
                /* startAngle= */ ANGLE_UP - cornerRadiusArcLength,
                /* sweepAngle= */ cornerRadiusArcLength);

        // 画出剩下的直线
        shapePath.lineTo(length,0);
    }
}
