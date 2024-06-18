//package com.example.trainadmin;
//
//import android.Manifest;
//import android.content.pm.PackageManager;
//import android.location.Location;
//import android.os.Bundle;
//
//import androidx.annotation.NonNull;
//import androidx.core.app.ActivityCompat;
//import androidx.fragment.app.Fragment;
//
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Toast;
//
//import com.example.trainadmin.models.LocationModel;
//import com.google.android.gms.location.FusedLocationProviderClient;
//import com.google.android.gms.location.LocationServices;
//import com.google.android.gms.maps.CameraUpdateFactory;
//import com.google.android.gms.maps.GoogleMap;
//import com.google.android.gms.maps.OnMapReadyCallback;
//import com.google.android.gms.maps.SupportMapFragment;
//import com.google.android.gms.maps.model.LatLng;
//import com.google.android.gms.maps.model.MarkerOptions;
//import com.google.android.gms.tasks.OnFailureListener;
//import com.google.android.gms.tasks.OnSuccessListener;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.FirebaseUser;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//
//public class MapFragment extends Fragment {
//
//    private FirebaseAuth mAuth;
//    private DatabaseReference mDatabase;
//    private FusedLocationProviderClient fusedLocationClient;
//    private GoogleMap mGoogleMap;
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//
//        View view = inflater.inflate(R.layout.fragment_map, container, false);
//        mAuth = FirebaseAuth.getInstance();
//        FirebaseUser currentUser = mAuth.getCurrentUser();
//        mDatabase = FirebaseDatabase.getInstance().getReference("users")
//                .child(currentUser.getUid())
//                .child("location");
//
//        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity());
//
//        // Inflate the layout for this fragment
//        SupportMapFragment supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.google_map);
//
//        supportMapFragment.getMapAsync(new OnMapReadyCallback() {
//            @Override
//            public void onMapReady(@NonNull GoogleMap googleMap) {
//                mGoogleMap = googleMap;
//
//                // Get and update current location
//                getCurrentLocation();
//            }
//        });
//
//        return view;
//    }
//
//    private void getCurrentLocation() {
//        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
//                ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
//            return;
//        }
//
//        fusedLocationClient.getLastLocation()
//                .addOnSuccessListener(requireActivity(), new OnSuccessListener<Location>() {
//                    @Override
//                    public void onSuccess(Location location) {
//                        if (location != null) {
//                            LatLng currentLatLng = new LatLng(location.getLatitude(), location.getLongitude());
//                            mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 15));
//                            mGoogleMap.addMarker(new MarkerOptions().position(currentLatLng).title("Current Location"));
//                            updateLocation(location.getLatitude(), location.getLongitude());
//                        } else {
//                            Toast.makeText(requireContext(), "Location not available", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                })
//                .addOnFailureListener(requireActivity(), new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Toast.makeText(requireContext(), "Failed to get location", Toast.LENGTH_SHORT).show();
//                    }
//                });
//    }
//
//    private void updateLocation(double latitude, double longitude) {
//        LocationModel locationData = new LocationModel(latitude, longitude);
//        mDatabase.setValue(locationData);
//    }
//}


package com.example.trainadmin;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.example.trainadmin.models.LocationModel;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MapFragment extends Fragment {

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private FusedLocationProviderClient fusedLocationClient;
    private LocationCallback locationCallback;
    private GoogleMap mGoogleMap;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_map, container, false);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference("users")
                .child(currentUser.getUid())
                .child("location");

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity());


        // Initialize location callback for continuous updates
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    // Update map with the latest location
                    updateMapLocation(location);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        saveLocationToFirebase(location.getLatitude(), location.getLongitude(), location.getBearingAccuracyDegrees());
                    }
                }
            }
        };

        // Inflate the layout for this fragment
        SupportMapFragment supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.google_map);

        supportMapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull GoogleMap googleMap) {
                mGoogleMap = googleMap;
                // Request continuous location updates
                requestLocationUpdates();
            }
        });

        return view;
    }

    // Request continuous location updates
    private void requestLocationUpdates() {
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(10000); // 10 seconds

        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            return;
        }
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
    }

    // Update map with the latest location
    private void updateMapLocation(Location location) {
        LatLng currentLatLng = new LatLng(location.getLatitude(), location.getLongitude());

        // Clear existing markers
        mGoogleMap.clear();

        // Add marker to the map
        mGoogleMap.addMarker(new MarkerOptions().position(currentLatLng).title("Current Location"));

        // Move camera to current location
        mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 15));
    }

    // Save location data to Firebase
    private void saveLocationToFirebase(double latitude, double longitude, float direction) {
        // Create a LocationModel object with latitude and longitude
        LocationModel locationData = new LocationModel(latitude, longitude, direction);

        // Save the location data to Firebase database
        mDatabase.setValue(locationData)
                .addOnSuccessListener(aVoid -> {
                    // Data successfully saved
                    //Toast.makeText(requireContext(), "Location saved to Firebase", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    // Failed to save data
                    Toast.makeText(requireContext(), "Failed to save location to Firebase", Toast.LENGTH_SHORT).show();
                });
    }

    @Override
    public void onPause() {
        super.onPause();
        // Stop location updates when the fragment is paused
        fusedLocationClient.removeLocationUpdates(locationCallback);
    }
}
