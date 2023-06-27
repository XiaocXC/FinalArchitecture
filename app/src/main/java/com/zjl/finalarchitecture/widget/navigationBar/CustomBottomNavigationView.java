package com.zjl.finalarchitecture.widget.navigationBar;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.Px;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.google.android.material.animation.AnimationUtils;
import com.google.android.material.animation.TransformationCallback;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.shape.EdgeTreatment;
import com.google.android.material.shape.MaterialShapeDrawable;
import com.zjl.finalarchitecture.R;
import java.util.List;

/**
 * 自定义BottomNavigationView底部导航栏
 *
 * 该导航栏集成了BottomAppBar相关特性，例如与FloatingActionButton结合形成凹陷/凸起
 * 带凹陷动画，并集成了滑动隐藏功能等内容
 */
public class CustomBottomNavigationView extends BottomNavigationView {

    private final MaterialShapeDrawable materialShapeDrawable;
    private int fabDiameter;
    private float fabMargin;
    private float fabCornerRadius;
    private float fabVerticalOffset;
    private float fabHorizontalOffset;
    private int edgeTreatmentType;
    private boolean isCenter;

    @Nullable
    private ValueAnimator currentFabAnimator;

    public CustomBottomNavigationView(@NonNull Context context) {
        this(context,null);
    }

    public CustomBottomNavigationView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, com.google.android.material.R.attr.bottomNavigationStyle);
    }

    public CustomBottomNavigationView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CustomBottomNavigationView);
        fabMargin = a.getDimensionPixelOffset(R.styleable.CustomBottomNavigationView_fabCradleMargin,12);
        fabCornerRadius = a.getDimensionPixelOffset(R.styleable.CustomBottomNavigationView_fabCradleRoundedCornerRadius,0);
        fabVerticalOffset = a.getDimensionPixelOffset(R.styleable.CustomBottomNavigationView_fabCradleVerticalOffset,0);
        fabDiameter = a.getDimensionPixelOffset(R.styleable.CustomBottomNavigationView_fabDiameter,0);
        isCenter = a.getBoolean(R.styleable.CustomBottomNavigationView_isCenter, true);
        int edgeTreatmentTypeInner = a.getInt(R.styleable.CustomBottomNavigationView_edgeTreatmentType, 0);

        a.recycle();

        // 得到BottomNavigationView的背景
        materialShapeDrawable = (MaterialShapeDrawable)getBackground();

        setEdgeTreatmentType(edgeTreatmentTypeInner);
    }

    /**
     * 获得顶部凹陷Treatment实例
     * @return TopEdgeTreatment
     */
    @Nullable
    private BaseCircleEdgeTreatment getTopEdgeTreatment() {
        EdgeTreatment edgeTreatment = materialShapeDrawable.getShapeAppearanceModel().getTopEdge();
        if(edgeTreatment instanceof BaseCircleEdgeTreatment){
            return (BaseCircleEdgeTreatment) edgeTreatment;
        }
        return null;
    }

    /**
     * 设置FloatingActionButton的直径，用于生成凹陷/突起图形
     */
    public boolean setFabDiameter(@Px int diameter) {
        this.fabDiameter = diameter;
        BaseCircleEdgeTreatment edgeTreatment = getTopEdgeTreatment();
        if(edgeTreatment == null){
            return false;
        }
        if(diameter != edgeTreatment.getFabDiameter()){
            edgeTreatment.setFabDiameter(diameter);
            materialShapeDrawable.invalidateSelf();
            return true;
        }
        return false;
    }

    public int getFabDiameter() {
        return fabDiameter;
    }

    public boolean setFabMargin(float fabMargin) {
        this.fabMargin = fabMargin;
        BaseCircleEdgeTreatment edgeTreatment = getTopEdgeTreatment();
        if(edgeTreatment == null){
            return false;
        }
        edgeTreatment.setFabCradleMargin(fabMargin);
        materialShapeDrawable.invalidateSelf();
        return true;
    }

    public float getFabMargin() {
        return fabMargin;
    }

    public boolean setFabCornerRadius(float fabCornerRadius) {
        this.fabCornerRadius = fabCornerRadius;
        BaseCircleEdgeTreatment edgeTreatment = getTopEdgeTreatment();
        if(edgeTreatment == null){
            return false;
        }
        edgeTreatment.setFabCradleRoundedCornerRadius(fabCornerRadius);
        materialShapeDrawable.invalidateSelf();
        return true;
    }

    public float getFabCornerRadius() {
        return fabCornerRadius;
    }

    public boolean setFabVerticalOffset(float fabVerticalOffset) {
        this.fabVerticalOffset = fabVerticalOffset;
        BaseCircleEdgeTreatment edgeTreatment = getTopEdgeTreatment();
        if(edgeTreatment == null){
            return false;
        }
        edgeTreatment.setCradleVerticalOffset(fabVerticalOffset);
        materialShapeDrawable.invalidateSelf();
        return true;
    }

    /**
     * 设置凸起or凹陷
     * 注意凹陷需要布局的裁剪功能失效 clipToPadding = false
     * @param type 凸起或凹陷效果
     */
    public void setEdgeTreatmentType(int type){
        if(type == edgeTreatmentType){
            return;
        }
        this.edgeTreatmentType = type;
        BaseCircleEdgeTreatment edgeTreatment;
        if(edgeTreatmentType == BaseCircleEdgeTreatment.BULGE_TYPE){
            edgeTreatment = new BulgeCircleEdgeTreatment(fabMargin, fabCornerRadius, fabVerticalOffset);
        } else {
            edgeTreatment = new SagCircleEdgeTreatment(fabMargin, fabCornerRadius, fabVerticalOffset);
        }
        edgeTreatment.setCenter(isCenter);
        edgeTreatment.setHorizontalOffset(fabHorizontalOffset);
        // 设置到MaterialShapeDrawable
        materialShapeDrawable.setShapeAppearanceModel(materialShapeDrawable
                .getShapeAppearanceModel().toBuilder().setTopEdge(edgeTreatment)
                .build());
        setFabDiameter(fabDiameter);
    }

    public int getEdgeTreatmentType() {
        return edgeTreatmentType;
    }

    /**
     * Fab按钮转变回调
     */
    @NonNull
    TransformationCallback<FloatingActionButton> fabTransformationCallback =
            new TransformationCallback<FloatingActionButton>() {
                @Override
                public void onScaleChanged(@NonNull FloatingActionButton fab) {
                    // 如果Fab大小发生变化
                    materialShapeDrawable.setInterpolation(
                            fab.getVisibility() == View.VISIBLE ? fab.getScaleY() : 0);
                }

                @Override
                public void onTranslationChanged(@NonNull FloatingActionButton fab) {
                    float horizontalOffset = fab.getTranslationX();
                    BaseCircleEdgeTreatment edgeTreatment = getTopEdgeTreatment();
                    if(edgeTreatment == null){
                        return;
                    }
                    if (edgeTreatment.getHorizontalOffset() != horizontalOffset) {
                        edgeTreatment.setHorizontalOffset(horizontalOffset);
                        materialShapeDrawable.invalidateSelf();
                    }
                    // 如果Fab的位置改变，改变其导航栏的缺省位置.
                    float verticalOffset = Math.max(0, -fab.getTranslationY());
                    if (edgeTreatment.getCradleVerticalOffset() != verticalOffset) {
                        edgeTreatment.setCradleVerticalOffset(verticalOffset);
                        materialShapeDrawable.invalidateSelf();
                    }

                    materialShapeDrawable.setInterpolation(
                            fab.getVisibility() == View.VISIBLE ? fab.getScaleY() : 0);
                }
            };

    void addFabAnimationListeners(@NonNull FloatingActionButton fab) {
        fab.addTransformationCallback(fabTransformationCallback);
    }

    public void setEdgeTreatmentHorizontalOffset(float horizontalOffset){
        setEdgeTreatmentHorizontalOffset(horizontalOffset, true);
    }

    @SuppressLint("RestrictedApi")
    public void setEdgeTreatmentHorizontalOffset(float horizontalOffset, boolean animate){
        this.fabHorizontalOffset = horizontalOffset;
        BaseCircleEdgeTreatment edgeTreatment = getTopEdgeTreatment();
        if(edgeTreatment == null){
            return;
        }
        if (edgeTreatment.getHorizontalOffset() != horizontalOffset) {
            // 使用动画
            if(animate){
                int lastFabDiameter = fabDiameter;
                animateChildTo(this, fabDiameter, 0, 75, AnimationUtils.LINEAR_OUT_SLOW_IN_INTERPOLATOR, new AnimateEndCallback() {
                    @Override
                    public void animateEnd() {
                        edgeTreatment.setHorizontalOffset(horizontalOffset);
                        materialShapeDrawable.invalidateSelf();

                        animateChildTo(CustomBottomNavigationView.this, 0, lastFabDiameter, 125, AnimationUtils.FAST_OUT_LINEAR_IN_INTERPOLATOR, null);
                    }
                });
            } else {
                edgeTreatment.setHorizontalOffset(horizontalOffset);
                materialShapeDrawable.invalidateSelf();
            }
        }

    }

    public boolean isCenter() {
        return isCenter;
    }

    /**
     * 设置凹陷是否基于中心
     */
    public boolean setCenter(boolean center) {
        isCenter = center;
        BaseCircleEdgeTreatment edgeTreatment = getTopEdgeTreatment();
        if(edgeTreatment == null){
            return false;
        }
        edgeTreatment.setCenter(isCenter);
        materialShapeDrawable.invalidateSelf();
        return true;
    }

    @Nullable
    private FloatingActionButton findDependentFab() {
        View view = findDependentView();
        return view instanceof FloatingActionButton ? (FloatingActionButton) view : null;
    }

    /**
     * 找到依赖该GatherBottomNavigationView的组件
     * 支持FloatingActionButton、ExtendedFloatingActionButton
     * @return View
     */
    @Nullable
    View findDependentView() {
        if (!(getParent() instanceof CoordinatorLayout)) {
            // 如果不是CoordinatorLayout布局，则不存在依赖的Fab
            return null;
        }

        List<View> dependents = ((CoordinatorLayout) getParent()).getDependents(this);
        for (View v : dependents) {
            if (v instanceof FloatingActionButton || v instanceof ExtendedFloatingActionButton) {
                return v;
            }
        }
        return null;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        if(isCenter){
            return;
        }
        // 计算一下水平偏移量
        int totalSize = getMenu().size();
        int totalWidth = w;

        int itemWidth = totalWidth / totalSize;
        int currentIndex = -1;
        for(int i = 0; i < getMenu().size(); i++){
            if(getMenu().getItem(i).getItemId() == getSelectedItemId()){
                currentIndex = i;
                break;
            }
        }

        float defaultFirstHorizontalOffset = (itemWidth * currentIndex + itemWidth * (currentIndex + 1f)) / 2;
        setEdgeTreatmentHorizontalOffset(defaultFirstHorizontalOffset);

    }

    private void animateChildTo(
            @NonNull CustomBottomNavigationView child,
            int fromValue, int toValue, long duration, TimeInterpolator interpolator, AnimateEndCallback callback) {
        if(currentFabAnimator != null){
            currentFabAnimator.cancel();
        }
        currentFabAnimator = ValueAnimator.ofInt(fromValue, toValue)
                .setDuration(duration);
        currentFabAnimator.setInterpolator(interpolator);
        currentFabAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                child.setFabDiameter((int)animation.getAnimatedValue());
            }
        });
        currentFabAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                currentFabAnimator = null;
                if(callback != null){
                    callback.animateEnd();
                }
            }
        });
        currentFabAnimator.start();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if(currentFabAnimator != null){
            currentFabAnimator.cancel();
            currentFabAnimator = null;
        }
    }

    private interface AnimateEndCallback{
        void animateEnd();
    }
}
