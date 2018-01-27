package com.example.surinderpalsinghsidhu.contactlist;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;
import android.graphics.drawable.BitmapDrawable;
import java.io.ByteArrayOutputStream;
public class Add_Edit_Contact extends AppCompatActivity {

    DatabaseHelper dbHelper = new DatabaseHelper(this);  //object of DatabaseHelper class
    android.widget.EditText txtId;
    android.widget.EditText txtName;
    android.widget.EditText txtPhone;
    android.widget.EditText txtEmail;
    android.widget.EditText txtAddress;
    android.widget.ImageView imgPicture;

    private static final int PICK_IMAGE_ID = 234;

    Contacts conts;  //object refernce of Contact class

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add__edit__contact);

        dbHelper = new DatabaseHelper(getApplicationContext());

        txtId = (android.widget.EditText) findViewById(R.id.txtId);
        txtName = (android.widget.EditText) findViewById(R.id.txtName);
        txtPhone = (android.widget.EditText) findViewById(R.id.txtPhone);
        txtEmail = (android.widget.EditText) findViewById(R.id.txtEmail);
        txtAddress = (android.widget.EditText) findViewById(R.id.txtAddress);
        imgPicture = (android.widget.ImageView) findViewById(R.id.imgPicture);
        if (getIntent().getSerializableExtra("conts") != null) {
           String ID = getIntent().getStringExtra("conts");  //Gets ID from Intent
            Cursor viewData = dbHelper.viewData(dbHelper.getWritableDatabase(),ID);
            viewData.moveToFirst();
            txtId.setText(viewData.getString(0));
            txtName.setText(viewData.getString(1));
            txtPhone.setText(viewData.getString(2));
            txtEmail.setText(viewData.getString(3));
            txtAddress.setText(viewData.getString(4));
            imgPicture.setImageBitmap(BitmapFactory.decodeByteArray(viewData.getBlob(5), 0, viewData.getBlob(5).length));
        }
    }

    public void saveContact(android.view.View view) //method to save Contact and edit contact in database
    {

        Bitmap bitmap = ((BitmapDrawable) imgPicture.getDrawable()).getBitmap();
        ByteArrayOutputStream boas = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, boas);

        byte[] pic = boas.toByteArray();   //conversion of JPEG into Bytes
        //  String conts=getIntent().getStringExtra("cont");

        if(getIntent().getSerializableExtra("conts") == null) {
            conts = new Contacts(txtId.getText().toString(), txtName.getText().toString(),
                    txtPhone.getText().toString(),txtEmail.getText().toString(), txtAddress.getText().toString(), pic);

            Toast.makeText(this, dbHelper.addContact(conts) != -1 ? "Contact ID: " + conts.getContactId() + " - " +
                    conts.getContactName() + " is successfully added" : "Failed to add Contact " + conts.getContactId() + " - " +
                    conts.getContactName(), Toast.LENGTH_SHORT).show();
        }
        else {
            conts = new Contacts(txtId.getText().toString(), txtName.getText().toString(),
                    txtPhone.getText().toString(),txtEmail.getText().toString(), txtAddress.getText().toString(), pic);

            Toast.makeText(this, dbHelper.updateContact(conts) != -1 ? "Gallery ID: " + conts.getContactId() + " - " +
                    conts.getContactName() + " is successfully updated" : "Failed to update Gallery " + conts.getContactId() + " - " +
                    conts.getContactName(), Toast.LENGTH_SHORT).show();
        }
        android.content.Intent intent = new android.content.Intent(this, MainActivity.class);
        startActivity(intent);
        }

        public void cancel(View view) {
        finish();
    }// method to finish process

    @Override
        protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case PICK_IMAGE_ID:
                try {
                    Bitmap bitmap = ImagePicker.getImageFromResult(this, resultCode, data);
                    bitmap = Bitmap.createScaledBitmap(bitmap, 500, 500, true);//// Scale the image to fill the 500x500 image size
                    imgPicture.setImageBitmap(bitmap);
        // TODO use bitmap
                    break;
                }
                catch(Exception e) {
                    break;
                }
        }
    }
    public void pickupPicture(View view) 
    /*Method that is invoked on pressing "choose image" Button*/
    {
        Intent chooseImageIntent = ImagePicker.getPickImageIntent(this);
        startActivityForResult(chooseImageIntent, PICK_IMAGE_ID);
    }
    }


