package com.zjl.finalarchitecture.module.toolbox.selectRv.adapter;

import android.annotation.SuppressLint;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.zjl.finalarchitecture.R;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Xiaoc
 * @since 2022-07-12
 *
 * 多选内容适配器（Java）
 */
public class JavaSelectMultiAdapter extends BaseQuickAdapter<String, BaseViewHolder>{

    /**
     * 当前已选择的数据集
     */
    private Set<String> currentSelectSet = new HashSet<>();

    /**
     * 设置当前选择的内容
     */
    @SuppressLint("NotifyDataSetChanged")
    public void setCurrentSelectSet(Set<String> currentSelectSet) {
        this.currentSelectSet.clear();
        this.currentSelectSet.addAll(currentSelectSet);
        notifyDataSetChanged();
    }

    public Set<String> getCurrentSelectSet() {
        return currentSelectSet;
    }

    public void setSelectContent(String item, boolean selected, int position){
        // 如果是选中，将其加入到已选数据集，如果是取消选中，将其从里面移除
        if(selected){
            currentSelectSet.add(item);
        } else {
            currentSelectSet.remove(item);
        }
        // 通知对应Item进行局部更新是否选中状态
        notifyItemChanged(position, selected);
    }

    public JavaSelectMultiAdapter() {
        super(R.layout.item_single_or_multi_select);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder baseViewHolder, String s) {
        baseViewHolder.setText(R.id.tv_title, s);
        // 判断该Item是否在已选择的数据集中，更新其选择状态
        handleSelectStatus(baseViewHolder, currentSelectSet.contains(s));
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
    private void handleSelectStatus(BaseViewHolder holder, boolean selected){
        CheckBox cbSelect = holder.getView(R.id.cb_select);
        cbSelect.setChecked(selected);
    }
}
