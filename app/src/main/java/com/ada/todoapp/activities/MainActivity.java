package com.ada.todoapp.activities;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.ada.todoapp.R;
import com.ada.todoapp.adapters.ItemArrayAdapter;
import com.ada.todoapp.fragments.EditItemDialogFragment;
import com.ada.todoapp.models.Item;

import org.parceler.Parcels;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements EditItemDialogFragment.EditItemDialogListener {

    ArrayList<Item> todoItems;
    ItemArrayAdapter<Item> aToDoAdapter;
    RecyclerView rvItems;
    LinearLayoutManager llmLayoutManager;
    EditText etEditText;

    private static final String ITEMS_FILE = "todo.txt";
    private static final int REQUEST_CODE_EDIT = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        populateArrayItems();

        etEditText = (EditText) findViewById(R.id.etEditText);
        rvItems = (RecyclerView) findViewById(R.id.rvItems);
        llmLayoutManager = new LinearLayoutManager(this);
        rvItems.setLayoutManager(llmLayoutManager);
        rvItems.setAdapter(aToDoAdapter);
    }

    private void showEditDialog(Item item, int position) {
        FragmentManager fm = getSupportFragmentManager();
        EditItemDialogFragment editNameDialogFragment = EditItemDialogFragment.newInstance("Some Title", item, position);
        editNameDialogFragment.show(fm, "fragment_edit_item");
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
                        showEditDialog(item, position);
                    }
                });
    }

    public void onAddItem(View view) {
        Item newItem = new Item("New", etEditText.getText().toString());
        if (writeItem(newItem)) {
            todoItems.add(newItem);
        }
        aToDoAdapter.notifyDataSetChanged();
        llmLayoutManager.smoothScrollToPosition(rvItems, null, todoItems.size() - 1);
        etEditText.setText("");
    }

    private boolean readItems() {
        try {
            todoItems = (ArrayList<Item>) Item.getAll();
        } catch (Exception e) {
            Toast.makeText(this, R.string.errorReadItem, Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    private boolean deleteItem(int position) {
        try {
            todoItems.get(position).delete();
        } catch (Exception e) {
            Toast.makeText(this, R.string.errorItemDelete, Toast.LENGTH_LONG).show();
            return false;
        }
        todoItems.remove(position);
        return true;
    }

    private boolean writeItem(Item item) {
        try {
            Item.save(item);
        } catch (Exception e) {
            Toast.makeText(this, R.string.errorSaveItem, Toast.LENGTH_LONG).show();
            return false;
        }
        Toast.makeText(this, R.string.info_item_saved, Toast.LENGTH_SHORT).show();
        return true;
    }

    @Override
    public void onFinishEditDialog(Parcelable parcel, int position) {
        Item updatedItem = Parcels.unwrap(parcel);
        if (writeItem(updatedItem)) {
            todoItems.set(position, updatedItem);
        }
        aToDoAdapter.notifyDataSetChanged();
    }
}
