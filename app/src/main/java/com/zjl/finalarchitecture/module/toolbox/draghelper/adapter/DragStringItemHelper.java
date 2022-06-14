package com.zjl.finalarchitecture.module.toolbox.draghelper.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;

/**
 * @author Xiaoc
 * @since 2022-06-14
 *
 * Item拖拽帮助类，继承自[ItemTouchHelper.Callback]
 */
public class DragStringItemHelper extends ItemTouchHelper.Callback {

    /**
     * 允许上下拖拽
     */
    private static final int DRAG_FLAGS = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
    private static final int SWIPE_FLAGS = 0;

    private final DragStringItemAdapter dragStringItemAdapter;

    @Nullable
    private MaterialCardView dragCardView;

    public DragStringItemHelper(DragStringItemAdapter dragStringItemAdapter) {
        this.dragStringItemAdapter = dragStringItemAdapter;
    }

    @Override
    public int getMovementFlags(
            @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        return makeMovementFlags(DRAG_FLAGS, SWIPE_FLAGS);
    }

    /**
     * 当拖拽某个Item时回调此方法
     * @return 返回true则代表这个位置是可以被替换的，返回false则无法替换该位置，该案例全部可以被拖拽替换
     */
    @Override
    public boolean onMove(
            @NonNull RecyclerView recyclerView,
            @NonNull RecyclerView.ViewHolder viewHolder,
            @NonNull RecyclerView.ViewHolder target) {
        int fromPosition = viewHolder.getAdapterPosition();
        int toPosition = target.getAdapterPosition();

        swapCards(fromPosition, toPosition, dragStringItemAdapter);
        return true;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
    }

    /**
     * 拖拽状态改变
     * 我们根据拖拽状态来改变当前拖拽的视图的UI
     */
    @Override
    public void onSelectedChanged(@Nullable RecyclerView.ViewHolder viewHolder, int actionState) {
        super.onSelectedChanged(viewHolder, actionState);

        // 如果拖拽中，我们把卡片状态更改为拖拽样式
        if (actionState == ItemTouchHelper.ACTION_STATE_DRAG && viewHolder != null) {
            dragCardView = (MaterialCardView) viewHolder.itemView;
            dragCardView.setDragged(true);
        } else if (actionState == ItemTouchHelper.ACTION_STATE_IDLE && dragCardView != null) {
            dragCardView.setDragged(false);
            dragCardView = null;
        }
    }

    /**
     * 交换数据源
     * @param fromPosition 从对应位置position
     * @param toPosition 到对应位置position
     */
    private static void swapCards(int fromPosition, int toPosition, DragStringItemAdapter dragStringItemAdapter) {
        String fromNumber = dragStringItemAdapter.getData().get(fromPosition);
        dragStringItemAdapter.getData().set(fromPosition, dragStringItemAdapter.getData().get(toPosition));
        dragStringItemAdapter.getData().set(toPosition, fromNumber);
        // 告知adapter数据源move了，以便更新视图和动画
        dragStringItemAdapter.notifyItemMoved(fromPosition, toPosition);
    }
}
