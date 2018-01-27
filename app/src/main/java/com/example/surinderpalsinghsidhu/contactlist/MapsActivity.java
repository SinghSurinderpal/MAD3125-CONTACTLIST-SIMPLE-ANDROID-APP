package com.example.surinderpalsinghsidhu.contactlist;
import android.database.Cursor;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import java.io.IOException;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
/*MapActivity class that inherits properties of FragmentActivity*/
    private GoogleMap mMap; //reference of Googlemap class
    Contacts cont;
    DatabaseHelper dbHelper; //reference variable for DatabaseHelper class
    String contact_Address;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);//linking with xml file
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        dbHelper = new DatabaseHelper(getApplicationContext());
        if (getIntent().getStringExtra("map") != null) {
            Cursor viewData = dbHelper.viewData(dbHelper.getWritableDatabase(), getIntent().getStringExtra("map"));
            viewData.moveToFirst();
            android.widget.TextView text = (android.widget.TextView) findViewById(R.id.textaddress);
            text.setText(viewData.getString(4));
            contact_Address = viewData.getString(4);
        }
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        List<Address> addressList = null;
        if (contact_Address != null || !contact_Address.equals("")) {
            Geocoder geocoder = new Geocoder(this);
            try {
                addressList = geocoder.getFromLocationName(contact_Address, 1);
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                Address address = addressList.get(0);
                LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                mMap.addMarker(new MarkerOptions().position(latLng).title("Marker"));
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(),"Not Found",Toast.LENGTH_LONG).show();
                this.finish();
            }

        }
    }
}
