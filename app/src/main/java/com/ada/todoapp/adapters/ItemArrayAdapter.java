package com.ada.todoapp.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ada.todoapp.R;
import com.ada.todoapp.models.Item;
import com.ada.todoapp.utils.Utils;

import java.util.List;

/**
 * Created by ada on 9/13/16.
 */
public class ItemArrayAdapter<I> extends RecyclerView.Adapter<ItemArrayAdapter.ViewHolder> {

    public interface ItemArrayAdapterDelegate {
        boolean onLongClick(int position);
        void onClick(int position);
    }

    private List<Item> mItems;
    private Context mContext;
    private ItemArrayAdapterDelegate mDelegate;

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvItemStatus;
        public TextView tvItemName;
        public TextView tvItemPriority;
        public TextView tvItemDue;

        public ViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);

            tvItemStatus = (TextView) itemView.findViewById(R.id.tvItemStatus);
            tvItemName = (TextView) itemView.findViewById(R.id.tvItemName);
            tvItemPriority = (TextView) itemView.findViewById(R.id.tvItemPriority);
            tvItemDue = (TextView) itemView.findViewById(R.id.tvItemDue);

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
    public ItemArrayAdapter(Context context, List<Item> items, ItemArrayAdapterDelegate delegate) {
        mItems = items;
        mContext = context;
        mDelegate = delegate;
    }

    // Easy access to the context object in the recyclerview
    private Context getContext() {
        return mContext;
    }

    @Override
    public ItemArrayAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
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
    public void onBindViewHolder(ItemArrayAdapter.ViewHolder viewHolder, int position) {
        // Get the data model based on position
        Item item = mItems.get(position);

        // Set item views based on your views and data model
        viewHolder.tvItemName.setText(item.getName());
        viewHolder.tvItemStatus.setText(item.getStatus());
        viewHolder.tvItemPriority.setText(item.getPriority());
        viewHolder.tvItemDue.setText(Utils.formatDay(item.getDueDate()));
    }

    // Returns the total count of items in the list
    @Override
    public int getItemCount() {
        return mItems.size();
    }
}
