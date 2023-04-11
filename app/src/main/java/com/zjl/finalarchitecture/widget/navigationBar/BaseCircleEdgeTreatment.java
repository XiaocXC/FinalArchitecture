package com.zjl.finalarchitecture.widget.navigationBar;

import androidx.annotation.FloatRange;

import com.google.android.material.shape.EdgeTreatment;

/**
 * @author Xiaoc
 * @since 2023-04-11
 *
 * 突起凹陷圆形的基类
 **/
abstract public class BaseCircleEdgeTreatment extends EdgeTreatment {

    protected float fabDiameter;
    protected float fabMargin;
    protected float roundedCornerRadius;
    protected float cradleVerticalOffset;
    protected float horizontalOffset;

    /**
     * 凸起
     */
    public static final int BULGE_TYPE = 1;
    /**
     * 凹陷
     */
    public static final int SAG_TYPE = 2;

    private int type;

    public BaseCircleEdgeTreatment(int type){
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    /**
     * 返回当前Fab的直径大小
     */
    public float getFabDiameter() {
        return fabDiameter;
    }

    /**
     * 设置Fab直径
     */
    public void setFabDiameter(float fabDiameter) {
        this.fabDiameter = fabDiameter;
    }

    /** 设置水平偏移量 */
    public void setHorizontalOffset(float horizontalOffset) {
        this.horizontalOffset = horizontalOffset;
    }

    /**
     * 获得水平偏移量
     */
    public float getHorizontalOffset() {
        return horizontalOffset;
    }

    /**
     * 返回Fab的竖直偏移量
     */
    public float getCradleVerticalOffset() {
        return cradleVerticalOffset;
    }

    /**
     * 设置Fab的竖直偏移量
     */
    public void setCradleVerticalOffset(@FloatRange(from = 0f) float cradleVerticalOffset) {
        if (cradleVerticalOffset < 0) {
            throw new IllegalArgumentException("cradleVerticalOffset must be positive.");
        }
        this.cradleVerticalOffset = cradleVerticalOffset;
    }

    public float getFabCradleMargin() {
        return fabMargin;
    }

    public void setFabCradleMargin(float fabMargin) {
        this.fabMargin = fabMargin;
    }

    public float getFabCradleRoundedCornerRadius() {
        return roundedCornerRadius;
    }

    public void setFabCradleRoundedCornerRadius(float roundedCornerRadius) {
        this.roundedCornerRadius = roundedCornerRadius;
    }
}
