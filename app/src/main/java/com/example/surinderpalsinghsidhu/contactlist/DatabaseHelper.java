package com.example.surinderpalsinghsidhu.contactlist;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.Cursor;
import java.util.ArrayList;

/*DatabaseHelper class extending properties of SQLitwOpenHelper*/
public class DatabaseHelper extends SQLiteOpenHelper {

    private SQLiteDatabase sqLiteDatabase;  //creating reference of SQLiteDatabase Class
    static final String DB_Name = "MyContactsList";  //Constant variable for database name
    static final String Table_Name = "Contact"; //variable for 1st table of database
    static final int DB_Version = 1;
    static final String Table2_name="Log";  //variable for 2nd table
    static final String Table3_name="DeletedContacts"; //variable for 3rd table

    public DatabaseHelper (Context context)
     /*Method that creates database*/ 
    {
        super(context, DB_Name, null, DB_Version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase)
      /*Function to create tables using sql queries*/ 
        {
        String sql = "CREATE TABLE " + Table_Name + " (" + Contacts.DbColumns.COLUMN_1 + " integer primary key autoincrement, ";
        sql += Contacts.DbColumns.COLUMN_2 + " TEXT NULL, " + Contacts.DbColumns.COLUMN_3 + " TEXT NULL,"+ Contacts.DbColumns.COLUMN_4 + " TEXT NULL,"
                +Contacts.DbColumns.COLUMN_5 + " TEXT NULL,"+  Contacts.DbColumns.COLUMN_6 + " BLOB NULL)";

        sqLiteDatabase.execSQL(sql);
        String sql2= "CREATE TABLE "+ Table2_name + " (" + Contacts.DbColumns.COL1 + " INTEGER PRIMARY KEY NOT NULL, " + Contacts.DbColumns.COL2 + " TEXT NULL , " + Contacts.DbColumns.COL3 + " TEXT NULL , " + Contacts.DbColumns.COL4 + " TIMESTAMP)";
        String sql3= "CREATE TABLE "+ Table3_name + " (" + Contacts.DbColumns.COL1 + " INTEGER  PRIMARY KEY NOT NULL , " + Contacts.DbColumns.COL2 + " TEXT NULL , " + Contacts.DbColumns.COL3 + " TEXT NULL , " + Contacts.DbColumns.COL4 + " TIMESTAMP)";
        sqLiteDatabase.execSQL(insertRecordTrigger());
        sqLiteDatabase.execSQL(deleteRecordTrigger());
        sqLiteDatabase.execSQL(updateRecordTrigger());
        sqLiteDatabase.execSQL(sql2);
        sqLiteDatabase.execSQL(sql3);
        }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        String sql = "DROP TABLE " + Table_Name;
        sqLiteDatabase.execSQL(sql);
        String sql2="DROP TABLE IF EXISTS "+Table2_name;
        sqLiteDatabase.execSQL(sql2);
        sqLiteDatabase.execSQL("DROP trigger insert_contact ");
        sqLiteDatabase.execSQL("DROP trigger delete_contact ");
        onCreate(sqLiteDatabase);
    }
    public String insertRecordTrigger()
/*Method for implementing Insert Trigger*/
{       String insertRecord = "CREATE TRIGGER if not exists insert_contact "
                + " AFTER INSERT "
                + " ON[Contact] "
                + " for each row "
                + " BEGIN "
                + " insert into Log(dId,Name,Status,Date) values (NEW.Id,NEW.Name,'inserted',datetime('now'));"
                + " END;";
        return insertRecord;
    }
    public String updateRecordTrigger()
    /*Method to implement update trigger query*/
    {
        String updateRecord =
                "CREATE TRIGGER if not exists update_contact "
                        + " AFTER UPDATE "
                        + " ON[Contact] "
                        + " for each row "
                        + " BEGIN "
                        + " UPDATE Log SET Name = new.Name , Status='updated' , Date=datetime('now') WHERE dId = old.Id;"
                        + " END;";
        return updateRecord;
    }
    public String deleteRecordTrigger()
    /*Function to implement delete trigger query*/
    {
        String deleteRecord = "CREATE TRIGGER if not exists delete_contact "
                + " AFTER DELETE "
                + " ON[Contact] "
                + " for each row "
                + " BEGIN "
                + " insert into DeletedContacts(dId,Name,Status,Date) values (OLD.Id,OLD.Name,'deleted',datetime('now'));"
                + " END;";
        return deleteRecord;
    }

    public long addContact(Contacts cnt) 
/*Method for inserting Contact attributes to database*/
{
        sqLiteDatabase = this.getWritableDatabase();

        android.content.ContentValues contentValues = new android.content.ContentValues();

        //  contentValues.put(Contacts.DbColumns.COLUMN_1, cnt.getContactId());
        contentValues.put(Contacts.DbColumns.COLUMN_2, cnt.getContactName());
        contentValues.put(Contacts.DbColumns.COLUMN_3, cnt.getContactPhone());
        contentValues.put(Contacts.DbColumns.COLUMN_4, cnt.getContactEmail());
        contentValues.put(Contacts.DbColumns.COLUMN_5, cnt.getContactAddress());
        contentValues.put(Contacts.DbColumns.COLUMN_6, cnt.getContactPicture());

        return sqLiteDatabase.insert(Table_Name, null, contentValues);

/*
        String sql = "INSERT INTO " + Table_Name + " VALUES ('" + msg + "')";
        sqLiteDatabase.execSQL(sql);
        return 0;*/
    }

    public long updateContact(Contacts cnt) //method to update information of contact
{
 //SQLiteDatabase sqLiteDatabase, ArtGallery art) {

        sqLiteDatabase = this.getWritableDatabase();
        android.content.ContentValues contentValues = new android.content.ContentValues();

        contentValues.put(Contacts.DbColumns.COLUMN_2, cnt.getContactName());
        contentValues.put(Contacts.DbColumns.COLUMN_3, cnt.getContactPhone());
        contentValues.put(Contacts.DbColumns.COLUMN_4, cnt.getContactEmail());
        contentValues.put(Contacts.DbColumns.COLUMN_5, cnt.getContactAddress());
        contentValues.put(Contacts.DbColumns.COLUMN_6, cnt.getContactPicture());

        return sqLiteDatabase.update(Table_Name, contentValues, Contacts.DbColumns.COLUMN_1 + " = '" + cnt.getContactId() + "'", null);
    }

    public long deleteContact(String cID) //method for delete contact
{ //SQLiteDatabase sqLiteDatabase, String gID) { //ArtGallery art) {

        sqLiteDatabase = this.getWritableDatabase();
        return sqLiteDatabase.delete(Table_Name, Contacts.DbColumns.COLUMN_1 + " = '" + cID + "'", null);   //art.getArtGalID() + "'", null);

    }
    Integer deleteContact(SQLiteDatabase sq, String ID) {
        return sq.delete("Contact","Id = ?",new String[] {ID});
    }
    Cursor viewData(SQLiteDatabase sqLiteDatabase, String ID) {

        String[] projection = {Contacts.DbColumns.COLUMN_1,Contacts.DbColumns.COLUMN_2,Contacts.DbColumns.COLUMN_3,Contacts.DbColumns.COLUMN_4,Contacts.DbColumns.COLUMN_5,Contacts.DbColumns.COLUMN_6};
        //Return Database cursor
        return sqLiteDatabase.query(Table_Name,projection,"Id = ?",new String[] {ID},null,null,null);
    }
    public java.util.ArrayList<Contacts> pullContacts() {    //SQLiteDatabase sqLiteDatabase) {

        sqLiteDatabase = this.getReadableDatabase();
        java.util.ArrayList<Contacts> retList = new java.util.ArrayList<Contacts> ();

        String[] projection = { Contacts.DbColumns.COLUMN_1, Contacts.DbColumns.COLUMN_2, Contacts.DbColumns.COLUMN_3,
                Contacts.DbColumns.COLUMN_4, Contacts.DbColumns.COLUMN_5, Contacts.DbColumns.COLUMN_6 };
        String sort =  Contacts.DbColumns.COLUMN_2;

        Cursor cursor = sqLiteDatabase.query(
                Table_Name,                     // The table to query
                projection,                     // The columns to return
                null,                           // The columns for the WHERE clause
                null,                           // The values for the WHERE clause
                null,                           // don't group the rows
                null,                           // don't filter by row groups
                sort                            // The sort order
        );

        if (cursor != null) {
            while (cursor.moveToNext()) {
                retList.add(new Contacts(cursor.getString(0), cursor.getString(1), cursor.getString(2),
                        cursor.getString(3), cursor.getString(4), cursor.getBlob(5)));
            }

            cursor.close();
        }
        return retList;
    }
    ArrayList<Contacts>searchData(SQLiteDatabase db, String search) {

        Cursor cursor;              //Cursor object
        ArrayList<Contacts> retList = new ArrayList<>(); //ArrayList ContactModel Type

        //SQL Query to Perform Search in database
        String selectQuery = "SELECT * FROM " + Table_Name + " WHERE Name LIKE '" + search + "%' ORDER BY NAME ASC";

        //executes sql query
        cursor = db.rawQuery(selectQuery, null);
        Contacts contact;              //contactModel Object

        //Loop Adds to data ArrayList
        while (cursor.moveToNext()) {
            contact = new Contacts();
            contact.setContactId(cursor.getString(0));
            contact.setContactName(cursor.getString(1));
            contact.setContactPhone(cursor.getString(2));
            contact.setContactEmail(cursor.getString(3));
            contact.setContactAddress(cursor.getString(4));
            contact.setContactPicture(cursor.getBlob(5));
            retList.add(contact);
        }

        cursor.close(); //closes cursor

        return retList;
    }
}
