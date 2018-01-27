package com.example.surinderpalsinghsidhu.contactlist;
import android.app.SearchManager;
import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Intent;
/**
 * Created by surinderpal singh on 2017-10-29.
 */
/*Creating class named MainActivity that inherits inbuilt class AppCompatACtivity which further implements inbuilt interface ListAdapter for implementation of Listview*/
public class MainActivity extends AppCompatActivity implements android.widget.ListAdapter {

    java.util.ArrayList<Contacts> dataModel; /*Creating array of objects of Contacts class*/
    DatabaseHelper dbHelper;   //Reference variable of DatabaseHelper class
    android.widget.ListView lstContacts;  //Defining Listview variable
    SearchView searchView;  // Defining searchview 

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);  //Linking xml file with MainActivity
        /*linking variables of Listview,SEarchView etc through ids given in xml file*/
        lstContacts = (android.widget.ListView) findViewById(R.id.lstContacts);
        SearchManager manager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) findViewById(R.id.searchview);
        dbHelper = new DatabaseHelper(getApplicationContext());
        dataModel = dbHelper.pullContacts();
        lstContacts.setAdapter(this);  //Setting Adapter to Listview in within class

        /*Function to perform action on the click on Listview*/
        lstContacts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Contacts cont = (Contacts) getItem(position);

        /*Creating intent to send Id and control to Contact_View activity*/
        Intent intent = new Intent(getApplicationContext(), Contact_View.class);
        intent.putExtra("ID", cont.getContactId());
        startActivity(intent);
            }
        });
        searchView.setSearchableInfo(manager.getSearchableInfo(getComponentName()));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {    //This Method Called When Search Button Is Pressed On Action
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            public boolean onQueryTextChange(String s) {                //This Method Is Called after Every Letter Typed In Search View
            dataModel = dbHelper.searchData(dbHelper.getReadableDatabase(), s);     //Call To searchData Method Of DatabaseHelper Class To Perform On SqLite Database
            lstContacts.setAdapter(MainActivity.this);    //Setting Adapter on lsContacts Object
                return false;
            }
        });

        }

        /*Method which is called on the click of save button*/
        public void addContact(android.view.View view) {

        android.content.Intent intent = new android.content.Intent(this, Add_Edit_Contact.class);
        startActivity(intent);
        }

    @Override
        protected void onResume() {
        dataModel = dbHelper.pullContacts();
        lstContacts.setAdapter(this);
        super.onResume();
        }

    
        /*Method to close the process*/
        public void close(android.view.View view) {
        int pid = android.os.Process.myPid();
        android.os.Process.killProcess(pid);
        System.exit(0);
        }

    @Override
        public boolean areAllItemsEnabled() {
        return false;
    }

    @Override
        public boolean isEnabled(int i) {
        return true;
    }

    @Override
        public void registerDataSetObserver(DataSetObserver dataSetObserver) {
        }

    @Override
        public void unregisterDataSetObserver(DataSetObserver dataSetObserver) {
        }

    @Override
        public int getCount() {
        return dataModel.size();
    }

    @Override
        public Object getItem(int i) {
        return dataModel.get(i);
    }

    @Override
        public long getItemId(int i) {
        return 0;
    }

    @Override
        public boolean hasStableIds() {
        return false;
    }

    @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
        /*Function for creating view to show contacts*/
        android.view.LayoutInflater inflater = (android.view.LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View item = inflater.inflate(R.layout.item, viewGroup, false);
        Contacts cont = (Contacts) getItem(i);
        ImageView imgView = (ImageView) item.findViewById(R.id.imgView);
        TextView txtName = (TextView) item.findViewById(R.id.txtName);
        txtName.setText(cont.getContactName());
        TextView txtPhone = (TextView) item.findViewById(R.id.txtPhone);
        txtPhone.setText(cont.getContactPhone());
        byte[] pic = cont.getContactPicture();
        if (pic != null) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(pic, 0, pic.length);
            imgView.setImageBitmap(bitmap);
        }
        return item;
    }

    @Override
    public int getItemViewType(int i) {
        return 0;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public boolean isEmpty() {
        return getCount() < 1 ? true : false;
    }
    }
