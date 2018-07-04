package ke.co.milleradulu.milleradulu.mazoezigymnasium.gymlocations;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.PlaceDetectionClient;
import com.google.android.gms.location.places.PlaceLikelihood;
import com.google.android.gms.location.places.PlaceLikelihoodBufferResponse;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.List;

import ke.co.milleradulu.milleradulu.mazoezigymnasium.R;
import ke.co.milleradulu.milleradulu.mazoezigymnasium.apihandler.APIHelper;
import ke.co.milleradulu.milleradulu.mazoezigymnasium.apihandler.APIServiceProvider;
import ke.co.milleradulu.milleradulu.mazoezigymnasium.SessionManager;
import ke.co.milleradulu.milleradulu.mazoezigymnasium.apihandler.clients.GymLocationClient;
import ke.co.milleradulu.milleradulu.mazoezigymnasium.apihandler.models.GymLocation;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GymLocationsActivity extends AppCompatActivity implements OnMapReadyCallback {

  private final static String TAG = GymLocationsActivity.class.getSimpleName();
  private final static int DEFAULT_ZOOM = 12;
  private final static int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
  private boolean locationPermissionsGranted;

  private static final String KEY_CAMERA_POSITION = "camera_position";
  private static final String KEY_LOCATION = "location";

  private static final int MAX_ENTRIES = 5;
  private String[] likelyPlaceNames;
  private String[] likelyPlaceAddresses;
  private String[] likelyPlaceAttributions;
  private LatLng[] likelyPlaceLatLngs;
  ProgressBar mapProgress;

  List<GymLocation> gymLocations;

  GoogleMap gymMap;
  Location lastKnownLocation;
  CameraPosition cameraPosition;

  GeoDataClient geoDataClient;
  PlaceDetectionClient placeDetectionClient;
  FusedLocationProviderClient fusedLocationProviderClient;

  SessionManager sessionManager;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    sessionManager = new SessionManager(getApplicationContext());
    sessionManager.checkLogin();

    if(savedInstanceState != null) {
      lastKnownLocation = savedInstanceState.getParcelable(KEY_LOCATION);
      cameraPosition = savedInstanceState.getParcelable(KEY_CAMERA_POSITION);
    }

    setContentView(R.layout.activity_gym_locations);

    mapProgress = findViewById(R.id.progress);
    mapProgress.bringToFront();
    showLoading();


    GymLocationClient gymLocationClient = APIServiceProvider.createService(GymLocationClient.class);
    Call<List<GymLocation>> gymLocationCall = gymLocationClient.gymLocations();

    APIHelper.enqueWithRetry(gymLocationCall, 3, new Callback<List<GymLocation>>() {
      @Override
      public void onResponse(@NonNull Call<List<GymLocation>> call, @NonNull Response<List<GymLocation>> response) {
        gymLocations = response.body();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.locations_map);
        mapFragment.getMapAsync(GymLocationsActivity.this);

        geoDataClient = Places.getGeoDataClient(GymLocationsActivity.this);
        placeDetectionClient = Places.getPlaceDetectionClient(GymLocationsActivity.this);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(GymLocationsActivity.this);
      }

      @Override
      public void onFailure(@NonNull Call<List<GymLocation>> call, @NonNull Throwable t) {
        Log.d(TAG, t.getMessage());
        stopLoading();
        Toast.makeText(
          GymLocationsActivity.this,
          "Unable to load the map at this moment",
          Toast.LENGTH_SHORT
        ).show();
      }
    });

  }

  @Override
  public void onMapReady(GoogleMap googleMap) {
    gymMap = googleMap;

    for (GymLocation gymLocation : gymLocations) {

      gymMap.addMarker(
        new MarkerOptions()
          .position(
            new LatLng(
              Double.parseDouble(gymLocation.getGymLongitude()),
              Double.parseDouble(gymLocation.getGymLatitude())
            )
          )
          .title(
            gymLocation.getGymLocation()
          )
          .snippet(
            "Opening Time: " + gymLocation.getOpeningTime() + "\n" +
              "Closing Time: " + gymLocation.getClosingTime()
          )
          .icon(
            BitmapDescriptorFactory.fromResource(
              R.drawable.gym_dumbbell
            )
          )
      );

    }
    getLocationPermission();
    updateLocationUI();
    getDeviceLocation();
    stopLoading();
  }

  private void getDeviceLocation() {
    try {
      if(locationPermissionsGranted) {
        Task<Location> locationTask = fusedLocationProviderClient.getLastLocation();
        locationTask.addOnCompleteListener(new OnCompleteListener<Location>() {
          @Override
          public void onComplete(@NonNull Task<Location> task) {
            if(task.isSuccessful()) {
              lastKnownLocation = task.getResult();
              gymMap.moveCamera(
                CameraUpdateFactory.newLatLngZoom(
                  new LatLng(
                    lastKnownLocation.getLatitude(),
                    lastKnownLocation.getLongitude()),
                  DEFAULT_ZOOM
                )
              );
            } else {
              Log.d(TAG, "Current location is null. Using defaults.");
              Log.e(TAG, "Exception: %s", task.getException());
              gymMap.getUiSettings().setMyLocationButtonEnabled(false);
            }
          }
        });
      }
    } catch (SecurityException e) {
      e.printStackTrace();
    }
  }
  private void updateLocationUI() {
    if(gymMap == null) {
      return;
    }

    try {
      if(locationPermissionsGranted) {
        gymMap.setMyLocationEnabled(true);
        gymMap.getUiSettings().setMyLocationButtonEnabled(false);
      } else {
        gymMap.setMyLocationEnabled(false);
        gymMap.getUiSettings().setMyLocationButtonEnabled(false);
        lastKnownLocation = null;
        getLocationPermission();
      }
    } catch (SecurityException e) {
      e.printStackTrace();
    }
  }

  private void getLocationPermission() {
    if(ContextCompat.checkSelfPermission(
      this.getApplicationContext(),
      android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
      locationPermissionsGranted = true;
    } else {
      ActivityCompat.requestPermissions(this,
        new String[] {
          Manifest.permission.ACCESS_FINE_LOCATION
        }, PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
    }
  }

  @Override
  public void onSaveInstanceState(Bundle outState) {
    if(gymMap != null) {
      outState.putParcelable(KEY_CAMERA_POSITION, gymMap.getCameraPosition());
      outState.putParcelable(KEY_LOCATION, lastKnownLocation);
      super.onSaveInstanceState(outState);
    }
  }

  public boolean onOptionsItemSelected(MenuItem menuItem) {
    if(menuItem.getItemId() == R.id.option_get_place) {
      showCurrentPlace();
    }
    return true;
  }

  private void showCurrentPlace() {
    if(gymMap == null) {
      return;
    }

    if(locationPermissionsGranted) {
      @SuppressLint("MissingPermission") Task<PlaceLikelihoodBufferResponse> placeResult = placeDetectionClient.getCurrentPlace(null);
      placeResult.addOnCompleteListener(new OnCompleteListener<PlaceLikelihoodBufferResponse>() {
        @Override
        public void onComplete(@NonNull Task<PlaceLikelihoodBufferResponse> task) {
          if(task.isSuccessful() && task.getResult() != null) {

            PlaceLikelihoodBufferResponse likelyPlaces = task.getResult();

            int count;
            if(likelyPlaces.getCount() < MAX_ENTRIES) {
              count = likelyPlaces.getCount();
            } else {
              count = MAX_ENTRIES;
            }

            int i = 0;

            likelyPlaceNames = new String[count];
            likelyPlaceAddresses = new String[count];
            likelyPlaceAttributions = new String[count];
            likelyPlaceLatLngs = new LatLng[count];

            for(PlaceLikelihood placeLikelihood : likelyPlaces) {
              likelyPlaceNames[i] = (String) placeLikelihood.getPlace().getName();
              likelyPlaceAddresses[i] = (String) placeLikelihood.getPlace().getAddress();
              likelyPlaceAttributions[i] = (String) placeLikelihood.getPlace().getAttributions();
              likelyPlaceLatLngs[i] = placeLikelihood.getPlace().getLatLng();

              i++;

              if(i > count--) {
                break;
              }
            }

            likelyPlaces.release();
            openPlacesDialog();

          } else {
            Log.e(TAG, "Exception: %s", task.getException());
          }
        }
      });

    } {
      Log.i(TAG, "The user did not grant location permissions.");
    }
  }

  private void openPlacesDialog() {
    final DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialog, int which) {
        LatLng markerLatLng = likelyPlaceLatLngs[which];
        String markerSnippet = likelyPlaceAddresses[which];

        if (likelyPlaceAttributions[which] != null) {
          markerSnippet = markerSnippet + "\n" + likelyPlaceAttributions[which];
        }

        gymMap.addMarker(
          new MarkerOptions()
            .title(likelyPlaceNames[which])
            .position(markerLatLng)
            .snippet(markerSnippet)
        );

        gymMap.moveCamera(
          CameraUpdateFactory.newLatLngZoom(
            markerLatLng,
            DEFAULT_ZOOM
          )
        );
      }
    };

    AlertDialog alertDialog = new AlertDialog.Builder(GymLocationsActivity.this)
      .setTitle("Pick a place")
      .setItems(likelyPlaceNames, listener)
      .show();
  }

  @Override
  public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
    locationPermissionsGranted = false;

    switch (requestCode) {
      case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION : {
        if(grantResults.length > 0  && grantResults[0] == PackageManager.PERMISSION_DENIED) {
          locationPermissionsGranted = true;
        }
      }
    }
    updateLocationUI();
  }

  void showLoading() {
    mapProgress.setVisibility(View.VISIBLE);
  }

  void stopLoading() {
    mapProgress.setVisibility(View.GONE);
  }
}
