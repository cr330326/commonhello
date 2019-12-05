package com.cryallen.commonlib.adapter.recycler.wrapper;

import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cryallen.commonlib.adapter.recycler.RecyclerViewHolder;
import com.cryallen.commonlib.adapter.recycler.utils.RecyclerWrapperUtils;

/**
 * Created by chenran3 on 2017/12/20.
 */
public class RecyclerEmptyWrapper<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final int ITEM_TYPE_EMPTY = Integer.MAX_VALUE - 1;

    private RecyclerView.Adapter mInnerAdapter;
    private View mEmptyView;
    private int mEmptyLayoutId;


    public RecyclerEmptyWrapper(RecyclerView.Adapter adapter)
    {
        mInnerAdapter = adapter;
    }

    private boolean isEmpty()
    {
        return (mEmptyView != null || mEmptyLayoutId != 0) && mInnerAdapter.getItemCount() == 0;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        if (isEmpty())
        {
            RecyclerViewHolder holder;
            if (mEmptyView != null)
            {
                holder = RecyclerViewHolder.createViewHolder(parent.getContext(), mEmptyView);
            } else
            {
                holder = RecyclerViewHolder.createViewHolder(parent.getContext(), parent, mEmptyLayoutId);
            }
            return holder;
        }
        return mInnerAdapter.onCreateViewHolder(parent, viewType);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView)
    {
        RecyclerWrapperUtils.onAttachedToRecyclerView(mInnerAdapter, recyclerView, new RecyclerWrapperUtils.SpanSizeCallback()
        {
            @Override
            public int getSpanSize(GridLayoutManager gridLayoutManager, GridLayoutManager.SpanSizeLookup oldLookup, int position)
            {
                if (isEmpty())
                {
                    return gridLayoutManager.getSpanCount();
                }
                if (oldLookup != null)
                {
                    return oldLookup.getSpanSize(position);
                }
                return 1;
            }
        });


    }

    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder)
    {
        mInnerAdapter.onViewAttachedToWindow(holder);
        if (isEmpty())
        {
            RecyclerWrapperUtils.setFullSpan(holder);
        }
    }


    @Override
    public int getItemViewType(int position)
    {
        if (isEmpty())
        {
            return ITEM_TYPE_EMPTY;
        }
        return mInnerAdapter.getItemViewType(position);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position)
    {
        if (isEmpty())
        {
            return;
        }
        mInnerAdapter.onBindViewHolder(holder, position);
    }

    @Override
    public int getItemCount()
    {
        if (isEmpty()) return 1;
        return mInnerAdapter.getItemCount();
    }



    public void setEmptyView(View emptyView)
    {
        mEmptyView = emptyView;
    }

    public void setEmptyView(int layoutId)
    {
        mEmptyLayoutId = layoutId;
    }

}
