package eu.ase.ro.licenta;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import eu.ase.ro.licenta.databinding.ActivityClientMapBinding;

public class ClientMapActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityClientMapBinding binding;

    private Button btn_inapoi;
    private Marker marker;
    private Marker courierMarker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityClientMapBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        initializare_variabile();

        btn_inapoi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ClientMainActivity.class);
                startActivity(intent);
            }
        });
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LatLng latLng = new LatLng(44.419618110257005, 26.109286073162817);
        MarkerOptions markerOptions = new MarkerOptions().position(latLng).title("Client location");
        MarkerOptions markerOptionsCourier = new MarkerOptions().position(latLng).title("Courier location").icon(BitmapFromVector(getApplicationContext(), R.drawable.courier_icon));
        marker = mMap.addMarker(markerOptions);
        courierMarker = mMap.addMarker(markerOptionsCourier);

        getClientLocation();
        getCourier();
    }

    private void initializare_variabile(){
        btn_inapoi = findViewById(R.id.btn_inapoi_client);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void checkLocationPermission() {
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        1234);
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void getClientLocation(){
        checkLocationPermission();
        LocationCallback locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                super.onLocationResult(locationResult);
                if(locationResult != null && locationResult.getLastLocation() != null) {
                    double latitude = locationResult.getLastLocation().getLatitude();
                    double longitude = locationResult.getLastLocation().getLongitude();
                    LatLng my_position = new LatLng(latitude, longitude);
                    marker.setPosition(my_position);
                    Log.d("LOCATION_UPDATE", latitude + " - " + longitude);
                }
            }
        };
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(2000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationServices.getFusedLocationProviderClient(this)
                .requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
    }

    public void getCourier(){
        Intent intent = getIntent();
        String idCurier = intent.getStringExtra("idCurier");
        if(idCurier.length() < 25){
            Toast.makeText(ClientMapActivity.this,
                    "ID curier incorect", Toast.LENGTH_SHORT).show();
        } else {
            DatabaseReference courierLocation = FirebaseDatabase.getInstance().getReference()
                    .child("CurieriDisponibili").child(idCurier).child("l");
            courierLocation.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        List<Object> map = (List<Object>) snapshot.getValue();
                        double latitude = 0;
                        double longitude = 0;
                        if (map.get(0) != null) {
                            latitude = Double.parseDouble(map.get(0).toString());
                        }
                        if (map.get(1) != null) {
                            longitude = Double.parseDouble(map.get(1).toString());
                        }
                        LatLng courierLocation = new LatLng(latitude, longitude);
                        /*mMap.clear();
                        mMap.addMarker(new MarkerOptions().position(courierLocation).title("Courier position")
                                .icon(BitmapFromVector(getApplicationContext(), R.drawable.courier_icon)));
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(courierLocation));*/
                        courierMarker.setPosition(courierLocation);
                    } else {
                        Toast.makeText(ClientMapActivity.this, "ID curier incorect", Toast.LENGTH_SHORT).show();
                    }
                }


                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }

    private BitmapDescriptor BitmapFromVector(Context context, int vectorResId) {
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);

        vectorDrawable.setBounds(0, 0, 50, 50);

        Bitmap bitmap = Bitmap.createBitmap(50, 50, Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmap);

        vectorDrawable.draw(canvas);

        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }
}