package com.example.surinderpalsinghsidhu.contactlist;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.MenuItem;
public class Contact_View extends AppCompatActivity {

    DatabaseHelper helper=new DatabaseHelper(this);  //helper Object
    Cursor viewData;        //Cursor object
    ImageView imageView;    //ImageView Object
    String ID;              // ID String to store sql ID
    TextView Name,Email,Phone,Address,Fax;  //Objects of TextView Class
    AlertDialog.Builder myAlertDialog;    //Object if AlertDialog
    String Longitude;           //Longitude string to store Longitude of location
    String Latitude;            //Latitude string to store Latitude of location
    String contactAddress;      //contactAddress string  to store Address of contact
    java.util.ArrayList<Contacts> dataModel;

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact__view);

        if( getSupportActionBar() != null ) {
            //To Display back button on actionBar
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        imageView = (ImageView)findViewById(R.id.imageView3);  //Assigning View ID To imgPicture Object
        Name = (TextView)findViewById(R.id.vname);          //Assigning View ID To Name Object
        Email = (TextView)findViewById(R.id.vemail);        //Assigning View ID To Email Object
        Phone = (TextView)findViewById(R.id.vphone);        //Assigning View ID To Phone Object
        Fax = (TextView)findViewById(R.id.vfax);            //Assigning View ID To Fax Object
        Address = (TextView)findViewById(R.id.vaddress);    //Assigning View ID To Address Object
        helper = new DatabaseHelper(this);//Allocating to dbHelper Object
        if(getIntent().getStringExtra("ID") != null){
            setViewData();                               //If intent is not null call setViewData Method
        }
    }

    @Override
    protected void onResume() {    //InBuilt Method Called When ViewActivity Comes to the foreground
        setViewData();
        super.onResume();
    }

    public void setViewData()
    /*method to display information of contact*/{

        ID = getIntent().getStringExtra("ID");  //Gets ID from Intent
        viewData = helper.viewData(helper.getWritableDatabase(),ID);   //Calls viewData Method
        viewData.moveToFirst();                                        //Moves Cursor to First Index
        byte[] imgBytes = viewData.getBlob(5);                          //gets Byte array from Cursor
        Bitmap bitmap = BitmapFactory.decodeByteArray(imgBytes, 0, imgBytes.length);    //decodes byte array and sets to bitmap object
        bitmap = Bitmap.createScaledBitmap(bitmap, 500, 500, true);         //scales image to 500x500 pixels
        imageView.setImageBitmap(bitmap);                               //set Image on view
        Name.setText(viewData.getString(1));                            //Sets Name on View
        Email.setText(viewData.getString(3));                           //Sets Email on View
        Address.setText(viewData.getString(4));                         //Sets Address on View
        String[] contactNumbers = viewData.getString(2).split(",");     //Splits phone numbers to array which were concatenated by comma
        if(contactNumbers.length == 2){                              //check if length is of array to two
            Phone.setText(contactNumbers[0]);                       //sets Phone on view
            Fax.setText(contactNumbers[1]);                          //sets fax on view
        }}
        public void editContact(android.view.View view)
        /*method for edit contact*/ {
            String id = getIntent().getStringExtra("ID");
            android.content.Intent intent = new android.content.Intent(this, Add_Edit_Contact.class);
            intent.putExtra("conts", id);
            startActivity(intent);
        }

    public void deleteContact(android.view.View view) {
          /*method for deleting contact*/
        String Id = getIntent().getStringExtra("ID");
        helper.deleteContact(helper.getWritableDatabase(), Id);
        this.finish();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void gotomaps(View view)
    /*method called on click of map button*/
    {
        String id = getIntent().getStringExtra("ID");

        android.content.Intent intent = new android.content.Intent(this, MapsActivity.class);
        intent.putExtra("map", id);

        startActivity(intent);

    }
}
