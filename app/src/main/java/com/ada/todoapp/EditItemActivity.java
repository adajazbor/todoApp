package com.ada.todoapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

public class EditItemActivity extends AppCompatActivity {

    private String item;
    private Integer position;

    EditText etName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);

        item = getIntent().getStringExtra(Constants.PARAM_ITEM);
        position = getIntent().getIntExtra(Constants.PARAM_POSITION, -1);

        etName = (EditText) findViewById(R.id.etName);
        etName.setText(item);
    }

    //TODO I am wondering why the instruction says that I should save the changes in the MainAction?
    // I think it would be better to save it here.
    public void onSaveItem(View view) {
        String item = etName.getText().toString();
        Intent i = new Intent();
        i.putExtra(Constants.PARAM_ITEM, item);
        i.putExtra(Constants.PARAM_POSITION, position);
        setResult(RESULT_OK, i);
        finish();
    }
}
