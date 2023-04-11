package com.zjl.finalarchitecture.widget.navigationBar;

import com.google.android.material.shape.ShapePath;

import org.jetbrains.annotations.NotNull;


public final class BulgeCircleEdgeTreatment extends BaseCircleEdgeTreatment {

    private static final int ARC_QUARTER = 90;
    private static final int ARC_HALF = 180;
    private static final int ANGLE_UP = 270;
    private static final int ANGLE_LEFT = 180;

    public BulgeCircleEdgeTreatment(
            float fabMargin, float roundedCornerRadius, float cradleVerticalOffset) {
        super(BaseCircleEdgeTreatment.BULGE_TYPE);
        this.fabMargin = fabMargin;
        this.roundedCornerRadius = roundedCornerRadius;
        setCradleVerticalOffset(cradleVerticalOffset);
        this.horizontalOffset = 0f;
    }

    @Override
    public void getEdgePath(float length, float center, float interpolation, @NotNull ShapePath shapePath) {
        if (fabDiameter == 0) {
            // 不存在fab直径时不进行弯曲绘制
            shapePath.lineTo(length, 0);
            return;
        }

        float cradleDiameter = fabMargin * 2 + fabDiameter;
        float cradleRadius = cradleDiameter / 2f;
        float roundedCornerOffset = interpolation * roundedCornerRadius;
        float middle = center + horizontalOffset;

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

        shapePath.lineTo(leftRoundedCornerCircleX, 0.0F);
        shapePath.addArc(
                leftRoundedCornerCircleX - roundedCornerOffset,
                -roundedCornerOffset * 2,
                leftRoundedCornerCircleX + roundedCornerOffset,
                0,
                -ANGLE_UP,
                -cornerRadiusArcLength);
        shapePath.addArc(
                middle - cradleRadius,
                -cradleRadius + verticalOffset,
                middle + cradleRadius,
                cradleRadius + verticalOffset,
                ANGLE_LEFT - cutoutArcOffset,
                cutoutArcOffset * 2 + ARC_HALF);
        shapePath.addArc(
                rightRoundedCornerCircleX - roundedCornerOffset,
                -roundedCornerOffset * 2,
                rightRoundedCornerCircleX + roundedCornerOffset,
                0,
                -ANGLE_UP + cornerRadiusArcLength,
                -cornerRadiusArcLength);
        shapePath.lineTo(length, 0.0F);

        // 画出剩下的直线
        shapePath.lineTo(length,0);
    }
}
