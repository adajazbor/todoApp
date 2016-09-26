package com.ada.todoapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.ada.todoapp.adapter.ItemArrayAdapter;
import com.ada.todoapp.domain.model.Item;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

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
                        Intent i = new Intent(MainActivity.this, EditItemActivity.class);
                        i.putExtra(Constants.PARAM_POSITION, position);
                        i.putExtra(Constants.PARAM_ITEM, item);
                        startActivityForResult(i, REQUEST_CODE_EDIT);
                    }
                });
    }


    public void onAddItem(View view) {
        Item newItem = new Item("New", etEditText.getText().toString());
        writeItem(newItem);
        aToDoAdapter.notifyDataSetChanged();
        llmLayoutManager.smoothScrollToPosition(rvItems, null, todoItems.size() - 1);
        etEditText.setText("");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_EDIT && resultCode == RESULT_OK) {
            int position = data.getIntExtra(Constants.PARAM_POSITION, -1);
            Item updatedItem = data.getParcelableExtra(Constants.PARAM_ITEM);
            /*XXX Do not receive id since I do not have an access to setId() method in
            Parcelable due to activeandroid framework limitation
            todoItems.set(position, item);*/
            Item item = todoItems.get(position);
            item.setName(updatedItem.getName());
            item.setStatus(updatedItem.getStatus());
            writeItem(item);
            aToDoAdapter.notifyDataSetChanged();
        }

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
        } catch (android.database.sqlite.SQLiteConstraintException e1) {
            // TODO somehow on duplicate I see the exception in Android console but this catch is
            // not executed. It looks like activeandroid was catching and ignoring it internally
            Toast.makeText(this, R.string.errorItemNotUniqueName, Toast.LENGTH_LONG).show();
            return false;
        } catch (Exception | Error e) {
            Toast.makeText(this, R.string.errorSaveItem, Toast.LENGTH_LONG).show();
            return false;
        }
        todoItems.add(item);
        Toast.makeText(this, R.string.info_item_saved, Toast.LENGTH_SHORT).show();
        return true;
    }
}
