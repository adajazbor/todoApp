package com.ada.todoapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import com.ada.todoapp.domain.model.Item;

import org.parceler.Parcels;

public class EditItemActivity extends AppCompatActivity {

    private Item item;
    private Integer position;

    private EditText etName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);

        item = Parcels.unwrap(getIntent().getParcelableExtra(Constants.PARAM_ITEM));
        position = getIntent().getIntExtra(Constants.PARAM_POSITION, -1);

        etName = (EditText) findViewById(R.id.etName);
        etName.setText(item.getName());
    }

    public void onSaveItem(View view) {
        String itemName = etName.getText().toString();
        item.setName(itemName);
        item.setStatus("Updated");
        Intent i = new Intent();
        i.putExtra(Constants.PARAM_ITEM, Parcels.wrap(item));
        i.putExtra(Constants.PARAM_POSITION, position);
        setResult(RESULT_OK, i);
        finish();
    }
}
