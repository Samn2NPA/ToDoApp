package com.project.samn.todoapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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

    private ArrayList<String> toDoItems;
    private ArrayAdapter<String> aTodoAdapter;
    private final int REQUEST_CODE = 000; //use for "StartActivityForResult"
    private static int ITEM_ID;

    ListView lvListItem;
    EditText etAddItem;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        populateArrayItem();

        lvListItem = (ListView) findViewById(R.id.lvListItem);
        etAddItem = (EditText) findViewById(R.id.etAddItem);

        lvListItem.setAdapter(aTodoAdapter);
        lvListItem.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                toDoItems.remove(position);
                aTodoAdapter.notifyDataSetChanged();
                writeItem();
                Toast.makeText(MainActivity.this,"Item Deleted",Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        lvListItem.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String value = adapterView.getItemAtPosition(i).toString();

                ITEM_ID = (int) adapterView.getItemIdAtPosition(i);
                Log.e("ITEM_ID: ", ITEM_ID + "");

                Intent intent = new Intent(MainActivity.this, EditItemActivity.class);
                intent.putExtra("ToDoItem",value);
                startActivityForResult(intent,REQUEST_CODE);
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if(resultCode == RESULT_OK && requestCode == REQUEST_CODE){
            String edited = data.getExtras().getString("editedItem");
            aTodoAdapter.remove(aTodoAdapter.getItem(ITEM_ID));
            aTodoAdapter.insert(edited,ITEM_ID);
            Log.e("Edited ToDoAdapter: ", "");
            writeItem();
            aTodoAdapter.notifyDataSetChanged();
        }
    }

    private void populateArrayItem(){
        readItem();
        aTodoAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, toDoItems);
    }

    private void readItem(){
        File file = getFile();
        try{
            toDoItems = new ArrayList<String>(FileUtils.readLines(file));
        }
        catch (IOException e){
            Log.e("file READ: ",e.toString());
        }
    }

    private void writeItem(){
        File file = getFile();
        try{
           FileUtils.writeLines(file, toDoItems);
        }
        catch (IOException e){
            Log.e("file WRITE: ", e.toString());
        }
    }

    private File getFile(){
        File fileDir = getFilesDir();
        File file = new File(fileDir, "todoList.txt");
        try{
            file.createNewFile();
            return file;
        }
        catch (IOException e)
        {
            Log.e("Create new file: ", e.toString()) ;
            return null;
        }
    }

    public void onAddItem(View view) {
        String input = etAddItem.getText().toString();
        if(input.isEmpty())
        {
            Toast.makeText(this,getResources().getString(R.string.add_item_request).toString(),Toast.LENGTH_SHORT).show();
        }
        else {
            aTodoAdapter.add(input);
            etAddItem.setText("");
            writeItem();
        }
    }
}
