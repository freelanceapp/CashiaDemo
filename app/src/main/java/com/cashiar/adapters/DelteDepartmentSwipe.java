package com.cashiar.adapters;

import android.content.Context;
import android.graphics.Canvas;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

public class DelteDepartmentSwipe extends ItemTouchHelper.SimpleCallback {

    private SwipeListener listener;
    private Context context;

    public DelteDepartmentSwipe(Context context, int dragDirs, int swipeDirs, SwipeListener listener) {
        super(dragDirs, swipeDirs);

        this.listener = listener;
        this.context = context;
    }

    @Override
    public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

        CategoriesAdapter.CategoryViewholder holder = ( CategoriesAdapter.CategoryViewholder) viewHolder;
        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {

            if (dX > 0) {
                holder.binding.llRight.setVisibility(View.VISIBLE);
                holder.binding.llLeft.setVisibility(View.GONE);
            } else {
                holder.binding.llRight.setVisibility(View.GONE);
                holder.binding.llLeft.setVisibility(View.VISIBLE);
            }

        }

        getDefaultUIUtil().onDraw(c, recyclerView, holder.binding.consForeground, dX, dY, actionState, isCurrentlyActive);

    }

    @Override
    public void onChildDrawOver(@NonNull Canvas c, @NonNull RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        CategoriesAdapter.CategoryViewholder holder = ( CategoriesAdapter.CategoryViewholder) viewHolder;

        getDefaultUIUtil().onDrawOver(c, recyclerView, holder.binding.consForeground, dX, dY, actionState, isCurrentlyActive);

    }

    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {


        if (viewHolder != null) {
            CategoriesAdapter.CategoryViewholder holder = ( CategoriesAdapter.CategoryViewholder) viewHolder;

            getDefaultUIUtil().onSelected(holder.binding.consForeground);


        }


    }

    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        if (viewHolder != null) {
            CategoriesAdapter.CategoryViewholder holder = ( CategoriesAdapter.CategoryViewholder) viewHolder;

            getDefaultUIUtil().clearView(holder.binding.consForeground);


        }
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        if (listener != null) {
            listener.onSwipe(viewHolder.getAdapterPosition(), direction);

        }


    }

    public interface SwipeListener {
        void onSwipe(int pos, int dir);
    }
}
