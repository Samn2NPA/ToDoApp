package com.project.samn.todoapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class EditItemActivity extends AppCompatActivity {

    EditText edtEditText;
    Button btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);

        edtEditText = (EditText) findViewById(R.id.edtEditText);
        btnSave = (Button) findViewById(R.id.btnSave);

        String ToDoItem = getIntent().getStringExtra("ToDoItem");
        edtEditText.setText(ToDoItem);
        Log.e("Second Activity (get): ",ToDoItem);
    }


    public void SaveItemEdited(View view){
        Intent data = new Intent();
        data.putExtra("editedItem",edtEditText.getText().toString());
        setResult(RESULT_OK,data);

        Log.e("Second Activity (put): ", data.getExtras().getString("editedItem"));
        finish();
    }

    public void CancelItemEdit(View view){
        finish();
    }
}
