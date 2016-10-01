package com.ada.todoapp.activities;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.ada.todoapp.R;
import com.ada.todoapp.adapters.ItemAdapter;
import com.ada.todoapp.fragments.ItemDetailFragment;
import com.ada.todoapp.fragments.ItemEditFragment;
import com.ada.todoapp.models.Item;
import com.ada.todoapp.utils.Constants;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<Item> todoItems;
    private ItemAdapter<Item> aToDoAdapter;
    private RecyclerView rvItems;
    private LinearLayoutManager llmLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        populateArrayItems();
        rvItems = (RecyclerView) findViewById(R.id.rvItems);
        llmLayoutManager = new LinearLayoutManager(this);
        rvItems.setLayoutManager(llmLayoutManager);
        rvItems.setAdapter(aToDoAdapter);
    }

    public void populateArrayItems() {
        readItems();
        aToDoAdapter = new ItemAdapter<Item>(
                this,
                todoItems,
                new ItemAdapter.ItemArrayAdapterDelegate() {
                    @Override
                    public boolean onLongClick(int position) {
                        return true;
                    }

                    @Override
                    public void onClick(int position) {
                        Item item = todoItems.get(position);
                        showDetailDialog(item);
                    }
                });
    }

    public void onAddItemButtonClicked(View view) {
        ItemEditFragment fragment = ItemEditFragment.newInstance(
                new ItemEditFragment.EditItemDialogListener() {
                    @Override
                    public void onItemSaved(Parcelable parcel) {
                        onDataChanged(true);
                    }
                },
                getString(R.string.title_item_add),
                new Item());

        fragment.showFragment(getSupportFragmentManager(), null);
    }

    private void showDetailDialog(Item item) {
        ItemDetailFragment detailDialogFragment = ItemDetailFragment.newInstance(
                new ItemDetailFragment.DetailItemDialogListener() {
                    @Override
                    public void onDataChanged() {
                        MainActivity.this.onDataChanged(false);
                    }
                },
                item);
        detailDialogFragment.show(getSupportFragmentManager(), Constants.FRAGMENT_EDIT_ITEM);
    }

    private void onDataChanged(boolean scrollToLastItem) {
        readItems();
        aToDoAdapter.notifyDataSetChanged();
        if (scrollToLastItem) {
            llmLayoutManager.smoothScrollToPosition(rvItems, null, todoItems.size() - 1);
        }
    }

//==== database opperations

    private boolean readItems() {
        try {
            List<Item> items = Item.getAll();
            if (todoItems == null) {
                todoItems = items;
            } else {
                todoItems.clear();
                todoItems.addAll(Item.getAll());
            }
        } catch (Exception e) {
            Toast.makeText(this, R.string.error_read_item, Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

}
