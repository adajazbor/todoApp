package com.ada.todoapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ArrayList<String> todoItems;
    ArrayAdapter<String> aToDoAdapter;
    ListView lvItems;
    EditText etEditText;

    private static final String ITEMS_FILE = "todo.txt";
    private static final int REQUEST_CODE_EDIT = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.title_items_list);
        setContentView(R.layout.activity_main);

        populateArrayItems();
        etEditText = (EditText) findViewById(R.id.etEditText);
        lvItems = (ListView) findViewById(R.id.lvItems);
        lvItems.setAdapter(aToDoAdapter);

        //delete item action
        lvItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                todoItems.remove(position);
                aToDoAdapter.notifyDataSetChanged();
                writeItems();
                return true;
            }
        });

        //edit item action
        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item = todoItems.get(position);
                Intent i = new Intent(MainActivity.this, EditItemActivity.class);
                i.putExtra(Constants.PARAM_POSITION, position);
                i.putExtra(Constants.PARAM_ITEM, item);
                startActivityForResult(i, REQUEST_CODE_EDIT);
            }
        });
    }

    public void populateArrayItems() {
        readItems();
        aToDoAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, todoItems);
    }


    public void onAddItem(View view) {
        String newItem = etEditText.getText().toString();
        todoItems.add(newItem);
        aToDoAdapter.notifyDataSetChanged();
        writeItems();
        etEditText.setText("");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_EDIT && resultCode == RESULT_OK) {
            int position = data.getIntExtra(Constants.PARAM_POSITION, -1);
            String item = data.getStringExtra(Constants.PARAM_ITEM);
            todoItems.set(position, item);
            aToDoAdapter.notifyDataSetChanged();
            writeItems();
            Toast.makeText(this, R.string.info_item_saved, Toast.LENGTH_SHORT).show();
        }

    }

    //TODO do not ignore exception, move method to service layer
    private void readItems() {
        File fileDir = getFilesDir();
        File file = new File(fileDir, ITEMS_FILE);
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            todoItems = new ArrayList<String>(FileUtils.readLines(file));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //TODO do not ignore exceptions, move method to service layer
    private void writeItems() {
        File fileDir = getFilesDir();
        File file = new File(fileDir, ITEMS_FILE);
        try {
            FileUtils.writeLines(file, todoItems);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
