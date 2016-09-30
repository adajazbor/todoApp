package com.ada.todoapp.activities;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.ada.todoapp.R;
import com.ada.todoapp.adapters.ItemArrayAdapter;
import com.ada.todoapp.fragments.EditItemDialogFragment;
import com.ada.todoapp.models.Item;
import com.ada.todoapp.utils.Constants;

import org.parceler.Parcels;

import java.util.List;

public class MainActivity extends AppCompatActivity implements EditItemDialogFragment.EditItemDialogListener {

    List<Item> todoItems;
    ItemArrayAdapter<Item> aToDoAdapter;
    RecyclerView rvItems;
    LinearLayoutManager llmLayoutManager;

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
        aToDoAdapter = new ItemArrayAdapter<Item>(
                this,
                todoItems,
                new ItemArrayAdapter.ItemArrayAdapterDelegate() {
                    @Override
                    public boolean onLongClick(int position) {
                        deleteItem(position);
                        aToDoAdapter.notifyDataSetChanged();
                        return true;
                    }

                    @Override
                    public void onClick(int position) {
                        Item item = todoItems.get(position);
                        showEditDialog(item, getString(R.string.title_item_edit));
                    }
                });
    }


    public void onAddItem(View view) {
        showEditDialog(new Item(), getString(R.string.title_item_add));
    }

    private void showEditDialog(Item item, String title) {
        FragmentManager fm = getSupportFragmentManager();
        EditItemDialogFragment editNameDialogFragment = EditItemDialogFragment.newInstance(title, item);
        editNameDialogFragment.show(fm, Constants.FRAGMENT_EDIT_ITEM);
    }

    @Override
    public void onFinishEditDialog(Parcelable parcel) {
        Item updatedItem = Parcels.unwrap(parcel);

        if (writeItem(updatedItem)) {
            readItems();
            aToDoAdapter.notifyDataSetChanged();
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

    private boolean deleteItem(int position) {
        try {
            todoItems.get(position).delete();
        } catch (Exception e) {
            Toast.makeText(this, R.string.error_item_delete, Toast.LENGTH_LONG).show();
            return false;
        }
        todoItems.remove(position);
        return true;
    }

    private boolean writeItem(Item item) {
        try {
            Item.save(item);
        } catch (Exception e) {
            Toast.makeText(this, R.string.error_save_item, Toast.LENGTH_LONG).show();
            return false;
        }
        Toast.makeText(this, R.string.info_item_saved, Toast.LENGTH_SHORT).show();
        return true;
    }
}
