package com.ada.todoapp.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ada.todoapp.R;
import com.ada.todoapp.models.Item;

import java.util.List;

/**
 * Created by ada on 9/13/16.
 */
public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {

    private final static int [] mPriorityStyles = new int [] {
            R.style.priorityFontLow,
            R.style.priorityFontMedium,
            R.style.priorityFontHigh};

    private List<Item> mItems;
    private ItemArrayAdapterDelegate mDelegate;
    private Context mContext;
    private String[] mPriorities;

    public interface ItemArrayAdapterDelegate {
        boolean onLongClick(int position);
        void onClick(int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvItemName;
        public TextView tvItemPriority;

        public ViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);

            tvItemName = (TextView) itemView.findViewById(R.id.tvItemName);
            tvItemPriority = (TextView) itemView.findViewById(R.id.tvItemPriority);

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    return mDelegate.onLongClick(getAdapterPosition());
                }
            });
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mDelegate.onClick(getAdapterPosition());
                }
            });
        }
    }

    // Pass in the contact array into the constructor
    public ItemAdapter(Context context, List<Item> items, ItemArrayAdapterDelegate delegate) {
        mItems = items;
        mContext = context;
        mDelegate = delegate;
        mPriorities = context.getResources().getStringArray(R.array.array_priorities);
    }

    // Easy access to the context object in the recyclerview
    private Context getContext() {
        return mContext;
    }

    @Override
    public ItemAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View itemRowView = inflater.inflate(R.layout.item_item, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(itemRowView);
        return viewHolder;
    }

    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(ItemAdapter.ViewHolder viewHolder, int position) {
        // Get the data model based on position
        Item item = mItems.get(position);

        // Set item views based on your views and data model
        viewHolder.tvItemName.setText(item.getName());
        viewHolder.tvItemPriority.setText(mPriorities[item.getPriority()]);
        viewHolder.tvItemPriority.setTextAppearance(mContext, mPriorityStyles[item.getPriority()]);
    }

    // Returns the total count of items in the list
    @Override
    public int getItemCount() {
        return mItems.size();
    }
}
