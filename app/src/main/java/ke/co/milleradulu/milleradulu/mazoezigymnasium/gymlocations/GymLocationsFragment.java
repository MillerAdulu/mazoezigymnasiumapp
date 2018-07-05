package ke.co.milleradulu.milleradulu.mazoezigymnasium.gymlocations;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.PlaceDetectionClient;
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
import ke.co.milleradulu.milleradulu.mazoezigymnasium.SessionManager;
import ke.co.milleradulu.milleradulu.mazoezigymnasium.apihandler.APIHelper;
import ke.co.milleradulu.milleradulu.mazoezigymnasium.apihandler.APIServiceProvider;
import ke.co.milleradulu.milleradulu.mazoezigymnasium.apihandler.clients.GymLocationClient;
import ke.co.milleradulu.milleradulu.mazoezigymnasium.apihandler.models.GymLocation;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GymLocationsFragment extends Fragment implements OnMapReadyCallback {

  private OnFragmentInteractionListener mListener;

  private final static String TAG = GymLocationsFragment.class.getSimpleName();
  private final static int DEFAULT_ZOOM = 12;
  private final static int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
  private boolean locationPermissionsGranted;

  private static final String KEY_CAMERA_POSITION = "camera_position";
  private static final String KEY_LOCATION = "location";

  ProgressBar mapProgress;

  List<GymLocation> gymLocations;

  GoogleMap gymMap;
  Location lastKnownLocation;
  CameraPosition cameraPosition;

  GeoDataClient geoDataClient;
  PlaceDetectionClient placeDetectionClient;
  FusedLocationProviderClient fusedLocationProviderClient;

  SessionManager sessionManager;

  public GymLocationsFragment() {
    // Required empty public constructor
  }

  public static GymLocationsFragment newInstance() {
    return new GymLocationsFragment();
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

  }


  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_gym_locations, container, false);
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    sessionManager = new SessionManager(getContext());
    sessionManager.checkLogin();

    if(savedInstanceState != null) {
      lastKnownLocation = savedInstanceState.getParcelable(KEY_LOCATION);
      cameraPosition = savedInstanceState.getParcelable(KEY_CAMERA_POSITION);
    }

    mapProgress = view.findViewById(R.id.progress);
    mapProgress.bringToFront();
    showLoading();
    fetchLocations();

  }

  // TODO: Rename method, update argument and hook method into UI event
  public void onButtonPressed(Uri uri) {
    if (mListener != null) {
      mListener.onFragmentInteraction(uri);
    }
  }

  @Override
  public void onAttach(Context context) {
    super.onAttach(context);
    if (context instanceof OnFragmentInteractionListener) {
      mListener = (OnFragmentInteractionListener) context;
    } else {
      throw new RuntimeException(context.toString()
        + " must implement OnFragmentInteractionListener");
    }
  }

  @Override
  public void onDetach() {
    super.onDetach();
    mListener = null;
  }

  public interface OnFragmentInteractionListener {
    // TODO: Update argument type and name
    void onFragmentInteraction(Uri uri);
  }

  public void fetchLocations() {
    GymLocationClient gymLocationClient = APIServiceProvider.createService(GymLocationClient.class);
    Call<List<GymLocation>> gymLocationCall = gymLocationClient.gymLocations();

    APIHelper.enqueWithRetry(gymLocationCall, 3, new Callback<List<GymLocation>>() {
      @Override
      public void onResponse(@NonNull Call<List<GymLocation>> call, @NonNull Response<List<GymLocation>> response) {
        gymLocations = response.body();
        initializeMap();
      }

      @Override
      public void onFailure(@NonNull Call<List<GymLocation>> call, @NonNull Throwable t) {
        Log.d(TAG, t.getMessage());
        stopLoading();
        Toast.makeText(
          getContext(),
          "Unable to load the map at this moment",
          Toast.LENGTH_SHORT
        ).show();
      }
    });
  }

  public void initializeMap() {
    SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.locations_map);
    mapFragment.getMapAsync(GymLocationsFragment.this);

    geoDataClient = Places.getGeoDataClient(getContext());
    placeDetectionClient = Places.getPlaceDetectionClient(getContext());
    fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getContext());
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
      getContext(),
      android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
      locationPermissionsGranted = true;
    } else {
      ActivityCompat.requestPermissions(getActivity(),
        new String[] {
          Manifest.permission.ACCESS_FINE_LOCATION
        }, PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
    }
  }

  @Override
  public void onSaveInstanceState(@NonNull Bundle outState) {
    if(gymMap != null) {
      outState.putParcelable(KEY_CAMERA_POSITION, gymMap.getCameraPosition());
      outState.putParcelable(KEY_LOCATION, lastKnownLocation);
      super.onSaveInstanceState(outState);
    }
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
