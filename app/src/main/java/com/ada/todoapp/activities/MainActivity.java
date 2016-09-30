package com.ada.todoapp.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.ada.todoapp.R;
import com.ada.todoapp.adapters.ItemAdapter;
import com.ada.todoapp.fragments.ItemDetailFragment;
import com.ada.todoapp.models.Item;
import com.ada.todoapp.utils.Constants;

import java.util.List;

public class MainActivity extends AppCompatActivity implements ItemDetailFragment.DetailItemDialogListener {

    List<Item> todoItems;
    ItemAdapter<Item> aToDoAdapter;
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
        aToDoAdapter = new ItemAdapter<Item>(
                this,
                todoItems,
                new ItemAdapter.ItemArrayAdapterDelegate() {
                    @Override
                    public boolean onLongClick(int position) {
                        //deleteItem(position);
                        //aToDoAdapter.notifyDataSetChanged();
                        return true;
                    }

                    @Override
                    public void onClick(int position) {
                        Item item = todoItems.get(position);
                        showDetailDialog(item);
                    }
                });
    }


    public void onAddItem(View view) {
        showEditDialog(new Item(), getString(R.string.title_item_add));
    }

    private void showEditDialog(Item item, String title) {
        /*
        FragmentManager fm = getSupportFragmentManager();
        ItemEditFragment editNameDialogFragment = ItemEditFragment.newInstance(title, item);
        editNameDialogFragment.setTargetFragment(ItemDetailFragment.newInstance(item), Constants.REQUEST_CODE_ADD);
        editNameDialogFragment.show(fm, Constants.FRAGMENT_EDIT_ITEM);
        */
    }

    private void showDetailDialog(Item item) {
        FragmentManager fm = getSupportFragmentManager();
        ItemDetailFragment detailDialogFragment = ItemDetailFragment.newInstance(item);
        detailDialogFragment.show(fm, Constants.FRAGMENT_EDIT_ITEM);
    }

    @Override
    public void onFinishDetailDialog() {
        readItems();
        aToDoAdapter.notifyDataSetChanged();
        llmLayoutManager.smoothScrollToPosition(rvItems, null, todoItems.size() - 1);
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
