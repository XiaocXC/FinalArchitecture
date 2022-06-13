package com.zjl.finalarchitecture.widget.treeview;

import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.LinearLayout;
import android.widget.ListAdapter;

import com.zjl.finalarchitecture.R;

/**
 * Adapter used to feed the table view.
 * 
 * @param <T>
 *            class for ID of the tree
 */
public abstract class AbstractTreeViewAdapter<T> extends BaseAdapter implements
        ListAdapter {

    private static final String TAG = AbstractTreeViewAdapter.class
            .getSimpleName();

    protected final TreeStateManager<T> treeStateManager;
    private final int numberOfLevels;
    private final LayoutInflater layoutInflater;

    private final TreeViewList treeViewList;

    private int indentWidth = 0;
    private int indicatorGravity = 0;
    private Drawable collapsedDrawable;
    private Drawable expandedDrawable;
    private Drawable indicatorBackgroundDrawable;
    private Drawable rowBackgroundDrawable;

    private final OnClickListener indicatorClickListener = new OnClickListener() {
        @Override
        public void onClick(final View v) {
            @SuppressWarnings("unchecked")
            final T id = (T) v.getTag();
            expandCollapse(treeViewList, id, v);
        }
    };

    private boolean collapsible;
    private final Context context;

    public Context getContext() {
        return context;
    }

    protected TreeStateManager<T> getManager() {
        return treeStateManager;
    }

    protected void expandCollapse(AdapterView< ? > parent, final T id, View view) {
        final TreeNodeInfo<T> info = treeStateManager.getNodeInfo(id);
        if (!info.isWithChildren()) {
            // ignore - no default action
            return;
        }
        if (info.isExpanded()) {
            treeStateManager.collapseChildren(id);
        } else {
            treeStateManager.expandDirectChildren(id);
//            treeViewList.post(new Runnable() {
//                @Override
//                public void run() {
//                    List<T> children = treeStateManager.getChildren(id);
//                    int startPosition = parent.getPositionForView(view) + 1;
//                    int lastPosition = startPosition + children.size() - 1;
//
//                    for(int i = startPosition; i <= lastPosition; i++){
//                        View childView = parent.getChildAt(i);
//                        if(childView == null){
//                            return;
//                        }
////                        childView.measure(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//                        final int targetHeight = 161;
//
//                        childView.getLayoutParams().height = 0;
//
//                        Animation a = new Animation() {
//                            @Override
//                            protected void applyTransformation(float interpolatedTime, Transformation t) {
//                                childView.getLayoutParams().height = interpolatedTime == 1
//                                        ? LinearLayout.LayoutParams.WRAP_CONTENT
//                                        : (int) (targetHeight * interpolatedTime);
//                                childView.requestLayout();
//                            }
//
//                            @Override
//                            public boolean willChangeBounds() {
//                                return true;
//                            }
//                        };
//
//                        a.setAnimationListener(new Animation.AnimationListener() {
//                            @Override
//                            public void onAnimationStart(Animation animation) {
//                                childView.setVisibility(View.VISIBLE);
//                            }
//
//                            @Override
//                            public void onAnimationEnd(Animation animation) {
//
//                            }
//
//                            @Override
//                            public void onAnimationRepeat(Animation animation) {
//
//                            }
//                        });
//                        // 1dp/ms
//                        a.setDuration(1000);
//                        childView.startAnimation(a);
//                    }
//                }
//            });

        }
    }

    private void calculateIndentWidth() {
        if (expandedDrawable != null) {
            indentWidth = Math.max(getIndentWidth(),
                    expandedDrawable.getIntrinsicWidth());
        }
        if (collapsedDrawable != null) {
            indentWidth = Math.max(getIndentWidth(),
                    collapsedDrawable.getIntrinsicWidth());
        }
    }

    public AbstractTreeViewAdapter(final Context context,
                                   TreeViewList treeViewList,
            final TreeStateManager<T> treeStateManager, final int numberOfLevels) {
        this.context = context;
        this.treeViewList = treeViewList;
        this.treeStateManager = treeStateManager;
        this.layoutInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.numberOfLevels = numberOfLevels;
        this.collapsedDrawable = null;
        this.expandedDrawable = null;
        this.rowBackgroundDrawable = null;
        this.indicatorBackgroundDrawable = null;
    }

    @Override
    public void registerDataSetObserver(final DataSetObserver observer) {
        treeStateManager.registerDataSetObserver(observer);
    }

    @Override
    public void unregisterDataSetObserver(final DataSetObserver observer) {
        treeStateManager.unregisterDataSetObserver(observer);
    }

    @Override
    public int getCount() {
        return treeStateManager.getVisibleCount();
    }

    @Override
    public Object getItem(final int position) {
        return getTreeId(position);
    }

    public T getTreeId(final int position) {
        return treeStateManager.getVisibleList().get(position);
    }

    public TreeNodeInfo<T> getTreeNodeInfo(final int position) {
        return treeStateManager.getNodeInfo(getTreeId(position));
    }

    @Override
    public boolean hasStableIds() { // NOPMD
        return true;
    }

    @Override
    public int getItemViewType(final int position) {
        return getTreeNodeInfo(position).getLevel();
    }

    @Override
    public int getViewTypeCount() {
        return numberOfLevels;
    }

    @Override
    public boolean isEmpty() {
        return getCount() == 0;
    }

    @Override
    public boolean areAllItemsEnabled() { // NOPMD
        return true;
    }

    @Override
    public boolean isEnabled(final int position) { // NOPMD
        return true;
    }

    protected int getTreeListItemWrapperId() {
        return R.layout.tree_list_item_wrapper;
    }

    @Override
    public final View getView(final int position, final View convertView,
            final ViewGroup parent) {
        Log.d(TAG, "Creating a view based on " + convertView
                + " with position " + position);
        final TreeNodeInfo<T> nodeInfo = getTreeNodeInfo(position);
        if (convertView == null) {
            Log.d(TAG, "Creating the view a new");
            final LinearLayout layout = (LinearLayout) layoutInflater.inflate(
                    getTreeListItemWrapperId(), null);
            return populateTreeItem(layout, getNewChildView(nodeInfo),
                    nodeInfo, true);
        } else {
            Log.d(TAG, "Reusing the view");
            final LinearLayout linear = (LinearLayout) convertView;
            final FrameLayout frameLayout = (FrameLayout) linear
                    .findViewById(R.id.treeview_list_item_frame);
            final View childView = frameLayout.getChildAt(0);
            updateView(childView, nodeInfo);
            return populateTreeItem(linear, childView, nodeInfo, false);
        }
    }

    /**
     * Called when new view is to be created.
     * 
     * @param treeNodeInfo
     *            node info
     * @return view that should be displayed as tree content
     */
    public abstract View getNewChildView(TreeNodeInfo<T> treeNodeInfo);

    /**
     * Called when new view is going to be reused. You should update the view
     * and fill it in with the data required to display the new information. You
     * can also create a new view, which will mean that the old view will not be
     * reused.
     * 
     * @param view
     *            view that should be updated with the new values
     * @param treeNodeInfo
     *            node info used to populate the view
     * @return view to used as row indented content
     */
    public abstract View updateView(View view, TreeNodeInfo<T> treeNodeInfo);

    /**
     * Retrieves background drawable for the node.
     * 
     * @param treeNodeInfo
     *            node info
     * @return drawable returned as background for the whole row. Might be null,
     *         then default background is used
     */
    public Drawable getBackgroundDrawable(final TreeNodeInfo<T> treeNodeInfo) { // NOPMD
        return null;
    }

    private Drawable getDrawableOrDefaultBackground(final Drawable r) {
        if (r == null) {
            return context.getResources()
                    .getDrawable(R.drawable.list_selector_background).mutate();
        } else {
            return r;
        }
    }

    public final LinearLayout populateTreeItem(final LinearLayout layout,
            final View childView, final TreeNodeInfo<T> nodeInfo,
            final boolean newChildView) {
//        final Drawable individualRowDrawable = getBackgroundDrawable(nodeInfo);
//        layout.setBackgroundDrawable(individualRowDrawable == null ? getDrawableOrDefaultBackground(rowBackgroundDrawable)
//                : individualRowDrawable);
        final LinearLayout.LayoutParams indicatorLayoutParams = new LinearLayout.LayoutParams(
                calculateIndentation(nodeInfo), LayoutParams.MATCH_PARENT);
        final LinearLayout indicatorLayout = (LinearLayout) layout
                .findViewById(R.id.treeview_list_item_image_layout);
        indicatorLayout.setGravity(indicatorGravity);
        indicatorLayout.setLayoutParams(indicatorLayoutParams);
        layout.setTag(nodeInfo.getId());
        final FrameLayout frameLayout = (FrameLayout) layout
                .findViewById(R.id.treeview_list_item_frame);
        final LayoutParams childParams = new LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        if (newChildView) {
            frameLayout.addView(childView, childParams);
        }
        frameLayout.setTag(nodeInfo.getId());
        return layout;
    }

    protected int calculateIndentation(final TreeNodeInfo<T> nodeInfo) {
        return getIndentWidth() * (nodeInfo.getLevel() + (collapsible ? 1 : 0));
    }

    protected Drawable getDrawable(final TreeNodeInfo<T> nodeInfo) {
        if (!nodeInfo.isWithChildren() || !collapsible) {
            return getDrawableOrDefaultBackground(indicatorBackgroundDrawable);
        }
        if (nodeInfo.isExpanded()) {
            return expandedDrawable;
        } else {
            return collapsedDrawable;
        }
    }

    public void setIndicatorGravity(final int indicatorGravity) {
        this.indicatorGravity = indicatorGravity;
    }

    public void setCollapsedDrawable(final Drawable collapsedDrawable) {
        this.collapsedDrawable = collapsedDrawable;
        calculateIndentWidth();
    }

    public void setExpandedDrawable(final Drawable expandedDrawable) {
        this.expandedDrawable = expandedDrawable;
        calculateIndentWidth();
    }

    public void setIndentWidth(final int indentWidth) {
        this.indentWidth = indentWidth;
        calculateIndentWidth();
    }

    public void setRowBackgroundDrawable(final Drawable rowBackgroundDrawable) {
        this.rowBackgroundDrawable = rowBackgroundDrawable;
    }

    public void setIndicatorBackgroundDrawable(
            final Drawable indicatorBackgroundDrawable) {
        this.indicatorBackgroundDrawable = indicatorBackgroundDrawable;
    }

    public void setCollapsible(final boolean collapsible) {
        this.collapsible = collapsible;
    }

    public void refresh() {
        treeStateManager.refresh();
    }

    private int getIndentWidth() {
        return indentWidth;
    }

    @SuppressWarnings("unchecked")
    public void handleItemClick(final View view, final Object id) {
        expandCollapse(treeViewList, (T) id, view);
    }

}
