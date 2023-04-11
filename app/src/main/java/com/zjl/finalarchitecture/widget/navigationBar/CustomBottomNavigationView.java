package com.zjl.finalarchitecture.widget.navigationBar;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.Px;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

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
    private int edgeTreatmentType;

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

    public float getFabDiameter() {
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
}
