package eu.ase.ro.licenta;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.google.android.gms.location.FusedLocationProviderClient;
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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;

import eu.ase.ro.licenta.databinding.ActivityCurierMapBinding;

public class CurierMapActivity extends FragmentActivity implements OnMapReadyCallback {

    private ActivityCurierMapBinding binding;

    private GoogleMap mMap;

    private Button btn_logout;
    private EditText etPin;
    private ArrayList<ClientPackage> clientPackages;
    private HashMap<Integer, Marker> markerHashMap;
    private Marker marker;
    private boolean isLoggingOut = false;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityCurierMapBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        clientPackages = new ArrayList<>();
        markerHashMap = new HashMap<>();
        etPin = findViewById(R.id.editTextCurierPin);

        btn_logout = findViewById(R.id.btn_logout_curier);
        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isLoggingOut = true;
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(CurierMapActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LatLng latLng = new LatLng(44.419618110257005, 26.109286073162817);
        MarkerOptions markerOptions = new MarkerOptions().position(latLng).title("Courier location");
        marker = mMap.addMarker(markerOptions);

        getLocationUpdates();
        addClientLocations();
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
    public void getLocationUpdates(){
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

                    if(!isLoggingOut) {
                        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("CurieriDisponibili");

                        GeoFire geoFire = new GeoFire(reference);
                        geoFire.setLocation(userId, new GeoLocation(latitude, longitude));
                    }
                }
            }
        };
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(4000);
        locationRequest.setFastestInterval(2000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationServices.getFusedLocationProviderClient(this)
                .requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
    }

    private void addClientLocations(){
        ClientPackage clientPackage1 = new ClientPackage("y74JQtvbHAOI8KZhektehe7tL472", "Colet Emag", 1700.30, 44.419618110257005, 26.109286073162817, 1234, false);
        ClientPackage clientPackage2 = new ClientPackage("y74JQtvbHAOI8KZhektehe7tL472", "Colet PcGarage", 3200.50, 44.44593407951203, 26.068914698639418, 2345, false);
        ClientPackage clientPackage3 = new ClientPackage("y74JQtvbHAOI8KZhektehe7tL472", "Colet PcGarage", 3200.50, 44.44781353004108, 26.099068040782097, 7890, false);
        ClientPackage clientPackage4 = new ClientPackage("y74JQtvbHAOI8KZhektehe7tL472", "Colet PcGarage", 3200.50, 44.428070920384116, 26.090843652424113, 1235, false);
        ClientPackage clientPackage5 = new ClientPackage("y74JQtvbHAOI8KZhektehe7tL472", "Colet PcGarage", 3200.50, 44.420249587406545, 26.12895034603028, 1236, false);
        ClientPackage clientPackage6 = new ClientPackage("y74JQtvbHAOI8KZhektehe7tL472", "Colet PcGarage", 3200.50, 44.48211802267675, 26.09356050062495, 1237, false);
        clientPackages.add(clientPackage1);
        clientPackages.add(clientPackage2);
        clientPackages.add(clientPackage3);
        clientPackages.add(clientPackage4);
        clientPackages.add(clientPackage5);
        clientPackages.add(clientPackage6);
        LatLng position1 = new LatLng(clientPackage1.getLatitude(), clientPackage1.getLongitude());
        LatLng position2 = new LatLng(clientPackage2.getLatitude(), clientPackage2.getLongitude());
        LatLng position3 = new LatLng(clientPackage3.getLatitude(), clientPackage3.getLongitude());
        LatLng position4 = new LatLng(clientPackage4.getLatitude(), clientPackage4.getLongitude());
        LatLng position5 = new LatLng(clientPackage5.getLatitude(), clientPackage5.getLongitude());
        LatLng position6 = new LatLng(clientPackage6.getLatitude(), clientPackage6.getLongitude());
        Marker marker1 = mMap.addMarker(new MarkerOptions().position(position1).title("Client position").icon(BitmapFromVector(getApplicationContext(), R.drawable.ic_baseline_flag_24)));
        Marker marker2 = mMap.addMarker(new MarkerOptions().position(position2).title("Client position").icon(BitmapFromVector(getApplicationContext(), R.drawable.ic_baseline_flag_24)));
        Marker marker3 = mMap.addMarker(new MarkerOptions().position(position3).title("Client position").icon(BitmapFromVector(getApplicationContext(), R.drawable.ic_baseline_flag_24)));
        Marker marker4 = mMap.addMarker(new MarkerOptions().position(position4).title("Client position").icon(BitmapFromVector(getApplicationContext(), R.drawable.ic_baseline_flag_24)));
        Marker marker5 = mMap.addMarker(new MarkerOptions().position(position5).title("Client position").icon(BitmapFromVector(getApplicationContext(), R.drawable.ic_baseline_flag_24)));
        Marker marker6 = mMap.addMarker(new MarkerOptions().position(position6).title("Client position").icon(BitmapFromVector(getApplicationContext(), R.drawable.ic_baseline_flag_24)));
        markerHashMap.put(clientPackage1.getPin(), marker1);
        markerHashMap.put(clientPackage2.getPin(), marker2);
        markerHashMap.put(clientPackage3.getPin(), marker3);
        markerHashMap.put(clientPackage4.getPin(), marker4);
        markerHashMap.put(clientPackage5.getPin(), marker5);
        markerHashMap.put(clientPackage6.getPin(), marker6);
    }

    private BitmapDescriptor BitmapFromVector(Context context, int vectorResId) {
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);

        vectorDrawable.setBounds(0, 0, 50, 50);

        Bitmap bitmap = Bitmap.createBitmap(50, 50, Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmap);

        vectorDrawable.draw(canvas);

        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    public void verificaPin(View view) {
        if(etPin.getText() != null){
            int pin = Integer.parseInt(etPin.getText().toString());
            Marker marker = markerHashMap.get(pin);
            marker.remove();
            markerHashMap.remove(pin);
    }
    }
}