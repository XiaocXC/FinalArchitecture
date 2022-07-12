package com.zjl.finalarchitecture.module.toolbox.selectRv.adapter;

import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.zjl.finalarchitecture.R;

import java.util.List;

/**
 * @author Xiaoc
 * @since 2022-07-12
 *
 * 单选内容适配器（Java）
 */
public class JavaSelectSingleAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    private int currentSelectPosition = RecyclerView.NO_POSITION;

    public void setCurrentSelectPosition(int currentSelectPosition) {
        // 通知更新前一个内容取消勾选
        String lastItem = getItemOrNull(this.currentSelectPosition);
        if(lastItem != null){
            notifyItemChanged(this.currentSelectPosition, false);
        }

        this.currentSelectPosition = currentSelectPosition;

        // 通知更新当前选中的状态
        String currentItem = getItemOrNull(this.currentSelectPosition);
        if(currentItem != null){
            notifyItemChanged(this.currentSelectPosition, true);
        }
    }

    public int getCurrentSelectPosition() {
        return currentSelectPosition;
    }

    public JavaSelectSingleAdapter() {
        super(R.layout.item_single_or_multi_select);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder baseViewHolder, String s) {
        baseViewHolder.setText(R.id.tv_title, s);
        handleSelectStatus(baseViewHolder, baseViewHolder.getAbsoluteAdapterPosition() == currentSelectPosition);
    }

    /**
     * 局部更新，
     * 当调用 notifyItemChanged(pos, payloads) 方法时会调用到此处
     * 我们将payloads取出然后无损更新视图
     * @param payloads 额外参数值
     */
    @Override
    protected void convert(@NonNull BaseViewHolder holder, String item, @NonNull List<?> payloads) {
        if(payloads.isEmpty()){
            return;
        }
        for(Object o: payloads){
            boolean selected = (boolean)o;
            handleSelectStatus(holder, selected);
        }
    }

    @Override
    protected void onItemViewHolderCreated(@NonNull BaseViewHolder viewHolder, int viewType) {
        CheckBox cbSelect = viewHolder.getView(R.id.cb_select);
        // 禁用掉CheckBox自己的点击效果
        cbSelect.setClickable(false);
    }

    /**
     * 更新选中状态
     */
    private void handleSelectStatus(@NonNull BaseViewHolder baseViewHolder, boolean selected){
        CheckBox cbSelect = baseViewHolder.getView(R.id.cb_select);
        cbSelect.setChecked(selected);
    }
}
