package com.zjl.finalarchitecture.widget.navigationBar;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.ViewCompat;

import com.google.android.material.behavior.HideBottomViewOnScrollBehavior;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.lang.ref.WeakReference;

/**
 * @author Xiaoc
 * @since 2023-04-11
 *
 * 基于底部导航栏的滑动隐藏及动画适配Behavior
 * 它会做如下操作：
 * 1.检测布局中是否存在FloatingActionButton，如果存在，那么会设置凹陷或者突起
 * 2.当布局滚动时，做动画处理
 **/
public class HideBottomEdgeTreatmentOnScrollBehavior extends HideBottomViewOnScrollBehavior<CustomBottomNavigationView> {

    @NonNull
    private final Rect fabContentRect;
    private WeakReference<CustomBottomNavigationView> viewRef;
    private int originalBottomMargin;

    private final View.OnLayoutChangeListener fabLayoutListener = new View.OnLayoutChangeListener() {
        @Override
        public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
            CustomBottomNavigationView child = viewRef.get();

            if (child == null || !(v instanceof FloatingActionButton)) {
                v.removeOnLayoutChangeListener(this);
                return;
            }

            FloatingActionButton fab = ((FloatingActionButton) v);

            fab.getMeasuredContentRect(fabContentRect);
            int height = fabContentRect.height();

            // 设置Fab的直径进去以适配对应凹陷大小
            child.setFabDiameter(height);

            CoordinatorLayout.LayoutParams fabLayoutParams =
                    (CoordinatorLayout.LayoutParams) v.getLayoutParams();

            if (originalBottomMargin == 0) {
                int bottomShadowPadding = (fab.getMeasuredHeight() - height) / 2;
                int bottomMargin =
                        child.getResources()
                                .getDimensionPixelOffset(com.google.android.material.R.dimen.mtrl_bottomappbar_fab_bottom_margin);
                fabLayoutParams.bottomMargin = bottomMargin - bottomShadowPadding;
            }
        }
    };

    @Override
    public boolean onLayoutChild(@NonNull CoordinatorLayout parent, @NonNull CustomBottomNavigationView child, int layoutDirection) {
        viewRef = new WeakReference<>(child);

        View dependentView = child.findDependentView();

        if (dependentView != null && !ViewCompat.isLaidOut(dependentView)) {
            CoordinatorLayout.LayoutParams fabLayoutParams =
                    (CoordinatorLayout.LayoutParams) dependentView.getLayoutParams();
            fabLayoutParams.anchorGravity = Gravity.CENTER | Gravity.TOP;
            originalBottomMargin = fabLayoutParams.bottomMargin;

            if (dependentView instanceof FloatingActionButton) {
                FloatingActionButton fab = ((FloatingActionButton) dependentView);

                // 设置fab的布局更改监听
                fab.addOnLayoutChangeListener(fabLayoutListener);

                // 确保能正确使用动画效果
                child.addFabAnimationListeners(fab);
            }
        }
        parent.onLayoutChild(child, layoutDirection);
        return super.onLayoutChild(parent, child, layoutDirection);
    }

    @Override
    public boolean onStartNestedScroll(
            @NonNull CoordinatorLayout coordinatorLayout,
            @NonNull CustomBottomNavigationView child,
            @NonNull View directTargetChild,
            @NonNull View target,
            @ViewCompat.ScrollAxis int axes,
            @ViewCompat.NestedScrollType int type) {
        // 开启滑动隐藏后，滑动时会自动隐藏底部导航栏
        return super.onStartNestedScroll(
                coordinatorLayout, child, directTargetChild, target, axes, type);
    }

    public HideBottomEdgeTreatmentOnScrollBehavior() {
        fabContentRect = new Rect();
    }

    public HideBottomEdgeTreatmentOnScrollBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
        fabContentRect = new Rect();
    }
}
