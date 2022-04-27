package com.zjl.finalarchitecture.module.toolbox.multilist.adapter;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.zhpan.bannerview.BannerViewPager;
import com.zjl.finalarchitecture.R;
import com.zjl.finalarchitecture.module.toolbox.multilist.entity.ExampleMultiEntity;

import java.util.List;
import java.util.Objects;

/**
 * @author Xiaoc
 * @since 2022-04-27
 *
 * 多样式列表Adapter
 */
public class MultiListAdapter extends BaseMultiItemQuickAdapter<ExampleMultiEntity, BaseViewHolder> {

    private final OnBannerSelectChangedEvent bannerSelectChangedEvent;

    public MultiListAdapter(OnBannerSelectChangedEvent bannerSelectChangedEvent){
        this.bannerSelectChangedEvent = bannerSelectChangedEvent;
        // Banner的内容，我们复用文章的Banner
        addItemType(ExampleMultiEntity.TYPE_BANNER, R.layout.item_article_wrapper_banner);
        // Expand展开折叠的内容
        addItemType(ExampleMultiEntity.TYPE_EXPAND, R.layout.item_multi_list_expand);
        // 普通文本内容，我们复用折叠展开的布局
        addItemType(ExampleMultiEntity.TYPE_OTHER_TEXT, R.layout.item_multi_list_expand_child);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder baseViewHolder, ExampleMultiEntity exampleMultiEntity) {
        switch (exampleMultiEntity.getItemType()){
            case ExampleMultiEntity.TYPE_BANNER:
                // Banner
                handleTypeBanner(baseViewHolder, (ExampleMultiEntity.MultiBannerData) exampleMultiEntity);
                break;
            case ExampleMultiEntity.TYPE_EXPAND:
                // 展开折叠
                handleTypeExpand(baseViewHolder, (ExampleMultiEntity.MultiExpandData) exampleMultiEntity);
                break;
            case ExampleMultiEntity.TYPE_OTHER_TEXT:
                // 文本
                handleTypeText(baseViewHolder, (ExampleMultiEntity.MultiTextData) exampleMultiEntity);
                break;
        }
    }

    @Override
    protected void onItemViewHolderCreated(@NonNull BaseViewHolder viewHolder, int viewType) {
        switch (viewType){
            case ExampleMultiEntity.TYPE_BANNER:
                BannerViewPager<String> bannerViewPager = viewHolder.getView(R.id.article_banner);
                bannerViewPager.setAdapter(new MultiListBannerAdapter());
                bannerViewPager.setIndicatorSliderWidth(10, 10);
                bannerViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
                    @Override
                    public void onPageSelected(int position) {
                        bannerSelectChangedEvent.onBannerSelectChanged(bannerViewPager.getData().get(position));
                    }
                });
                bannerViewPager.create();
                break;
            case ExampleMultiEntity.TYPE_EXPAND:
                RecyclerView rvMultiExpand = viewHolder.getView(R.id.rv_multi_expand);
                rvMultiExpand.setAdapter(new MultiListExpandChildAdapter());
                TextView tvExpand = viewHolder.getView(R.id.tv_expand);
                tvExpand.setOnClickListener(v -> {
                    ExampleMultiEntity.MultiExpandData expandData = (ExampleMultiEntity.MultiExpandData)viewHolder.itemView.getTag();
                    expandData.setExpanded(!expandData.getExpanded());
                    notifyItemChanged(viewHolder.getAbsoluteAdapterPosition());
                });
                break;
        }
    }

    /**
     * 处理Banner数据
     */
    private void handleTypeBanner(BaseViewHolder baseViewHolder, ExampleMultiEntity.MultiBannerData bannerData){
        BannerViewPager<String> bannerViewPager = baseViewHolder.getView(R.id.article_banner);
        bannerViewPager.refreshData(bannerData.getImgUrls());
    }

    /**
     * 处理展开折叠数据
     */
    private void handleTypeExpand(BaseViewHolder baseViewHolder, ExampleMultiEntity.MultiExpandData expandData){
        baseViewHolder.itemView.setTag(expandData);
        RecyclerView rvMultiExpand = baseViewHolder.getView(R.id.rv_multi_expand);

        MultiListExpandChildAdapter adapter = (MultiListExpandChildAdapter) Objects.requireNonNull(rvMultiExpand.getAdapter());
        baseViewHolder.setGone(R.id.tv_expand, expandData.getExpandList().size() <= 3);

        if(expandData.getExpanded()){
            // 如果展开，我们显示全部列表
            baseViewHolder.setText(R.id.tv_expand, getContext().getString(R.string.multi_list_collapse_des));
            adapter.setList(expandData.getExpandList());
        } else {
            baseViewHolder.setText(R.id.tv_expand, getContext().getString(R.string.multi_list_expand_des));
            // 如果没展开，我们只展示3个默认
            List<String> subList = expandData.getExpandList().subList(
                    0, Math.min(3, expandData.getExpandList().size())
            );
            adapter.setList(subList);
        }

    }

    /**
     * 处理普通文本
     */
    private void handleTypeText(BaseViewHolder baseViewHolder, ExampleMultiEntity.MultiTextData textData){
        baseViewHolder.setText(R.id.tv_expand_des, textData.getText());
    }

    public interface OnBannerSelectChangedEvent{
        void onBannerSelectChanged(String imgUrl);
    }

}
