package com.dissoi.guiaapp2;

import android.Manifest;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Location;
import android.os.Build;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.Patterns;
import android.view.Gravity;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.dissoi.guiaapp2.adapters.CiudadAdapter;
import com.dissoi.guiaapp2.adapters.EmpresaAdapter;
import com.dissoi.guiaapp2.models.Ciudad;
import com.dissoi.guiaapp2.models.Empresa;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;


public class EmpresaListActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener
{
    private static final String TAG = "EmpresaListActivity";
    private static final int NUM_ITEMS = 4;

    private ViewPager mPager;
    private EmpresaPagerAdapter mAdapter;

    private int mEspecialidadId;
    private String mEspecialidadNombre;

    private static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empresa_list);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbarEmpresaList);
        setSupportActionBar(toolbar);

        NavigationView navigationView = (NavigationView)findViewById(R.id.navigationViewDrawer);
        navigationView.setNavigationItemSelectedListener(this);

        DrawerLayout drawer = (DrawerLayout)findViewById(R.id.drawerLayoutEmpresaList);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        Pattern emailPattern = Patterns.EMAIL_ADDRESS;
        Account[] accounts = AccountManager.get(this).getAccounts();
        for (Account account : accounts)
        {
            if (emailPattern.matcher(account.name).matches())
            {
                View headerView = navigationView.getHeaderView(0);
                TextView tv = (TextView)headerView.findViewById(R.id.textViewDrawerHeaderNombre);
                tv.setText(account.name);
                break;
            }
        }

        Bundle b = getIntent().getExtras();
        mEspecialidadId = b.getInt("especialidad_id");
        mEspecialidadNombre = b.getString("especialidad_nombre");

        mAdapter = new EmpresaPagerAdapter(getSupportFragmentManager(), mEspecialidadId);

        mPager = (ViewPager)findViewById(R.id.pagerEmpresaList);
        mPager.setAdapter(mAdapter);

        TabLayout tabLayout = (TabLayout)findViewById(R.id.tabsEmpresaList);
        tabLayout.setupWithViewPager(mPager);

        setTitle(mEspecialidadNombre);
    }


    public class EmpresaPagerAdapter extends FragmentPagerAdapter
    {
        private String tabTitles[] = new String[] { "Todas", "Recomendadas", "Cerca mio", "Por ciudad"};

        private int mEspecialidadId;

        public EmpresaPagerAdapter(FragmentManager fm, int especialidadId)
        {
            super(fm);
            mEspecialidadId = especialidadId;
        }

        @Override
        public int getCount()
        {
            return NUM_ITEMS;
        }

        @Override
        public Fragment getItem(int position)
        {
            if (position == 0) {return EmpresaListFragment.newInstance(mEspecialidadId, false);}
            if (position == 1) {return EmpresaListFragment.newInstance(mEspecialidadId, true);}
            if (position == 2) {return CercaMioMapFragment.newInstance(mEspecialidadId);}
            if (position == 3) {return PorCiudadFragment.newInstance(mEspecialidadId, mEspecialidadNombre);}

            //Default (should never show up):
            return EmpresaListFragment.newInstance(mEspecialidadId, false);
        }

        @Override
        public CharSequence getPageTitle(int position)
        {
            return tabTitles[position];
        }
    }


    public static class EmpresaListFragment extends Fragment
    {
        private ListView mListViewEmpresas;

        private List<Empresa> mEmpresas;
        private int mEspecialidadId;
        private Boolean mRecomendadas;

        public static EmpresaListFragment newInstance(int especialidadId, Boolean recomendadas)
        {
            EmpresaListFragment fragment = new EmpresaListFragment();
            Bundle args = new Bundle();
            args.putInt("especialidad_id", especialidadId);
            args.putBoolean("recomendadas", recomendadas);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
        {
            super.onCreateView(inflater, container, savedInstanceState);

            View rootView = inflater.inflate(R.layout.fragment_empresa_list, container, false);

            mEspecialidadId = getArguments().getInt("especialidad_id");
            mRecomendadas = getArguments().getBoolean("recomendadas");

            mListViewEmpresas = (ListView)rootView.findViewById(R.id.listViewEmpresas);
            mListViewEmpresas.setOnItemClickListener(new AdapterView.OnItemClickListener()
            {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id)
                {
                    int empresaId = mEmpresas.get(position).id;
                    Intent intent = new Intent(getContext(), EmpresaActivity.class);
                    Bundle b = new Bundle();
                    b.putInt("empresa_id", empresaId);
                    intent.putExtras(b);
                    startActivity(intent);
                }
            });

            //getDataFromAPI(mEspecialidadId);

            return rootView;
        }

        @Override
        public void onResume()
        {
            super.onResume();
            getDataFromAPI(mEspecialidadId);
        }

        void getDataFromAPI(int especialidadId)
        {
            //Log.d(TAG, "getDataFromAPI: especialidad " + String.valueOf(especialidadId));

            String baseUrl = getString(R.string.server);
            Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();
            Retrofit client = new Retrofit.Builder().baseUrl(baseUrl).addConverterFactory(GsonConverterFactory.create(gson)).build();

            GuiaService service = client.create(GuiaService.class);

            Call<List<Empresa>> call;
            if (mRecomendadas) {call = service.getEmpresasRecomendadasByEspecialidad(especialidadId);}
            else {call = service.getEmpresasByEspecialidad(especialidadId);}

            call.enqueue(new Callback<List<Empresa>>()
            {
                @Override
                public void onResponse(Response<List<Empresa>> response, Retrofit retrofit)
                {
                    if (response.isSuccess())
                    {
                        mEmpresas = response.body();
                        EmpresaAdapter empresaListAdapter = new EmpresaAdapter(getContext(), mEmpresas, mRecomendadas);
                        mListViewEmpresas.setAdapter(empresaListAdapter);
                    } else
                    {
                        Log.d(TAG, "onResponse: call not successful");
                    }
                }

                @Override
                public void onFailure(Throwable t)
                {
                    Log.d(TAG, "onFailure: " + t.getMessage());
                }
            });
        }
    }


    public static class CercaMioMapFragment extends Fragment implements
            OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
            GoogleApiClient.OnConnectionFailedListener, LocationListener
    {
        private List<Empresa> mEmpresas;
        private int mEspecialidadId;
        private boolean mMapReady;
        private Map<Marker, Empresa> mMarkersEmpresas;

        private MapView mMapView;

        private GoogleMap mMap;
        private GoogleApiClient mGoogleApiClient;
        private Location mCurrentLocation;
        private LocationRequest mLocationRequest;
        private boolean mRequestingLocationUpdates;
        private String mLastUpdateTime;
        private boolean mHasPermissionAccessFineLocation;

        private static final String REQUESTING_LOCATION_UPDATES_KEY = "REQUESTING_LOCATION_UPDATES_KEY";
        private static final String LOCATION_KEY = "LOCATION_KEY";
        private static final String LAST_UPDATED_TIME_STRING_KEY = "LAST_UPDATED_TIME_STRING_KEY";


        public static CercaMioMapFragment newInstance(int especialidadId)
        {
            CercaMioMapFragment fragment = new CercaMioMapFragment();
            Bundle args = new Bundle();
            args.putInt("especialidad_id", especialidadId);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
        {
            super.onCreateView(inflater, container, savedInstanceState);

            View rootView = inflater.inflate(R.layout.fragment_cerca_mio_map, container, false);

            mEspecialidadId = getArguments().getInt("especialidad_id");
            mMapReady = false;
            mMarkersEmpresas = new HashMap<>();

            mMapView = (MapView)rootView.findViewById(R.id.map_cerca_mio);
            mMapView.onCreate(savedInstanceState);
            mMapView.getMapAsync(this);

            //Create an instance of GoogleAPIClient:
            if (mGoogleApiClient == null)
            {
                mGoogleApiClient = new GoogleApiClient.Builder(getContext())
                        .addConnectionCallbacks(this)
                        .addOnConnectionFailedListener(this)
                        .addApi(LocationServices.API)
                        .build();
            }

            createLocationRequest();

            updateValuesFromBundle(savedInstanceState);

            return rootView;
        }

        @Override
        public void onSaveInstanceState(Bundle savedInstanceState)
        {
            savedInstanceState.putBoolean(REQUESTING_LOCATION_UPDATES_KEY, mRequestingLocationUpdates);
            savedInstanceState.putParcelable(LOCATION_KEY, mCurrentLocation);
            savedInstanceState.putString(LAST_UPDATED_TIME_STRING_KEY, mLastUpdateTime);
            super.onSaveInstanceState(savedInstanceState);

            mMapView.onSaveInstanceState(savedInstanceState);
        }

        private void updateValuesFromBundle(Bundle savedInstanceState)
        {
            if (savedInstanceState != null)
            {
                if (savedInstanceState.keySet().contains(REQUESTING_LOCATION_UPDATES_KEY))
                {
                    mRequestingLocationUpdates = savedInstanceState.getBoolean(REQUESTING_LOCATION_UPDATES_KEY);
                }

                if (savedInstanceState.keySet().contains(LOCATION_KEY))
                {
                    mCurrentLocation = savedInstanceState.getParcelable(LOCATION_KEY);
                }

                if (savedInstanceState.keySet().contains(LAST_UPDATED_TIME_STRING_KEY))
                {
                    mLastUpdateTime = savedInstanceState.getString(LAST_UPDATED_TIME_STRING_KEY);
                }
            }
        }

        @Override
        public void onLowMemory()
        {
            super.onLowMemory();

            mMapView.onLowMemory();
        }

        @Override
        public void onCreate(Bundle bundle)
        {
            super.onCreate(bundle);

            if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
            }
            else {mHasPermissionAccessFineLocation = true;}

            int currentapiVersion = android.os.Build.VERSION.SDK_INT;
            if (currentapiVersion <= Build.VERSION_CODES.LOLLIPOP_MR1) {mHasPermissionAccessFineLocation = true;}
        }

        @Override
        public void onStart()
        {
            mGoogleApiClient.connect();
            super.onStart();
        }

        @Override
        public void onStop()
        {
            mGoogleApiClient.disconnect();
            super.onStop();
        }

        @Override
        public void onPause()
        {
            super.onPause();
            stopLocationUpdates();

            mMapView.onPause();
        }

        @Override
        public void onResume()
        {
            super.onResume();
            if (mGoogleApiClient.isConnected() && !mRequestingLocationUpdates)
            {
                startLocationUpdates();
            }

            mMapView.onResume();
        }

        @Override
        public void onDestroy()
        {
            super.onDestroy();

            mMapView.onDestroy();
        }

        @Override
        public void onConnected(Bundle connectionHint)
        {
            if (mHasPermissionAccessFineLocation)
            {

                if (mRequestingLocationUpdates)
                {
                    startLocationUpdates();
                }

                try
                {
                    Location currentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
                    LatLng currentLatLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 16));
                }
                catch (NullPointerException npe)
                {
                    Log.d(TAG, "onConnected: ignoring NullPointerException for currentLocation");
                }
            }
        }

        @Override
        public void onConnectionSuspended(int i)
        {
        }

        @Override
        public void onConnectionFailed(ConnectionResult result)
        {
        }

        @Override
        public void onMapReady(GoogleMap googleMap)
        {
            if (mHasPermissionAccessFineLocation)
            {
                mMap = googleMap;
                mMap.setMyLocationEnabled(true);

                mMapReady = true;

                getDataFromAPI(mEspecialidadId);

                mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter()
                {
                    @Override
                    public View getInfoWindow(Marker arg0)
                    {
                        return null;
                    }

                    @Override
                    public View getInfoContents(Marker marker)
                    {
                        Context context = getContext();

                        LinearLayout info = new LinearLayout(context);
                        info.setOrientation(LinearLayout.VERTICAL);

                        TextView title = new TextView(context);
                        title.setTextColor(Color.BLACK);
                        title.setGravity(Gravity.CENTER);
                        title.setTypeface(null, Typeface.BOLD);
                        title.setText(marker.getTitle());

                        TextView snippet = new TextView(context);
                        snippet.setTextColor(Color.GRAY);
                        snippet.setText(marker.getSnippet());

                        info.addView(title);
                        info.addView(snippet);

                        return info;
                    }
                });

                mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener()
                {
                    @Override
                    public void onInfoWindowClick(Marker marker)
                    {
                        int empresaId = mMarkersEmpresas.get(marker).id;
                        Intent intent = new Intent(getContext(), EmpresaActivity.class);
                        Bundle b = new Bundle();
                        b.putInt("empresa_id", empresaId);
                        intent.putExtras(b);
                        getContext().startActivity(intent);
                    }
                });
            }
        }

        @Override
        public void onLocationChanged(Location location)
        {
            mCurrentLocation = location;
            mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());

            Log.d(TAG, "onLocationChanged: " + mLastUpdateTime);
        }

        protected void createLocationRequest()
        {
            LocationRequest mLocationRequest = new LocationRequest();
            mLocationRequest.setInterval(10000);
            mLocationRequest.setFastestInterval(5000);
            mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        }

        protected void startLocationUpdates()
        {
            if (mHasPermissionAccessFineLocation)
            {
                LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);

                mRequestingLocationUpdates = true;
            }
        }

        protected void stopLocationUpdates()
        {
            if (mHasPermissionAccessFineLocation)
            {
                LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);

                mRequestingLocationUpdates = false;
            }
        }

        void getDataFromAPI(int especialidadId)
        {
            //Log.d(TAG, "getDataFromAPI: especialidad " + String.valueOf(especialidadId));

            String baseUrl = getString(R.string.server);
            Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();
            Retrofit client = new Retrofit.Builder().baseUrl(baseUrl).addConverterFactory(GsonConverterFactory.create(gson)).build();

            GuiaService service = client.create(GuiaService.class);

            Call<List<Empresa>> call = service.getEmpresasByEspecialidad(especialidadId);

            call.enqueue(new Callback<List<Empresa>>()
            {
                @Override
                public void onResponse(Response<List<Empresa>> response, Retrofit retrofit)
                {
                    if (response.isSuccess())
                    {
                        mEmpresas = response.body();

                        if (mMapReady)
                        {
                            for (Empresa empresa : mEmpresas)
                            {
                                if (!empresa.ubicacion.equals(""))
                                {
                                    String snippet = empresa.descripcionCorta;
                                    if (empresa.telefonos.get(0) != null)
                                    {
                                        snippet += "\n" + empresa.telefonos.get(0).numero;
                                    }

                                    String[] latlong = empresa.ubicacion.split(",");
                                    double latitude = Double.parseDouble(latlong[0]);
                                    double longitude = Double.parseDouble(latlong[1]);

                                    mMarkersEmpresas.put(
                                            mMap.addMarker(new MarkerOptions()
                                                    .title(empresa.nombre)
                                                        .snippet(snippet)
                                                        .position(new LatLng(latitude, longitude))),
                                                    empresa);
                                }
                            }
                        }
                    } else
                    {
                        Log.d(TAG, "onResponse: call not successful");
                    }
                }

                @Override
                public void onFailure(Throwable t)
                {
                    Log.d(TAG, "onFailure: " + t.getMessage());
                }
            });
        }

        @Override
        public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults)
        {
            switch (requestCode)
            {
                case MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION:
                {
                    if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    {
                        Log.d(TAG, "onRequestPermissionsResult: TRUE");
                        mHasPermissionAccessFineLocation = true;
                    }
                    else
                    {
                        Log.d(TAG, "onRequestPermissionsResult: FALSE");
                        mHasPermissionAccessFineLocation = false;
                    }
                    return;
                }
            }
        }
    }

    public static class PorCiudadFragment extends Fragment
    {
        private ListView mListViewCiudades;

        private List<Ciudad> mCiudades;
        private int mEspecialidadId;
        private String mEspecialidadNombre;

        public static PorCiudadFragment newInstance(int especialidadId, String especialidadNombre)
        {
            PorCiudadFragment fragment = new PorCiudadFragment();
            Bundle args = new Bundle();
            args.putInt("especialidad_id", especialidadId);
            args.putString("especialidad_nombre", especialidadNombre);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
        {
            super.onCreateView(inflater, container, savedInstanceState);

            View rootView = inflater.inflate(R.layout.fragment_por_ciudad, container, false);

            mEspecialidadId = getArguments().getInt("especialidad_id");
            mEspecialidadNombre = getArguments().getString("especialidad_nombre");

            mListViewCiudades = (ListView)rootView.findViewById(R.id.listViewCiudades);

            mListViewCiudades.setOnItemClickListener(new AdapterView.OnItemClickListener()
            {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id)
                {
                    int ciudadId = mCiudades.get(position).id;
                    String ciudadNombre = mCiudades.get(position).nombre;
                    Intent intent = new Intent(getContext(), PorCiudadActivity.class);
                    Bundle b = new Bundle();
                    b.putInt("especialidad_id", mEspecialidadId);
                    b.putString("especialidad_nombre", mEspecialidadNombre);
                    b.putInt("ciudad_id", ciudadId);
                    b.putString("ciudad_nombre", ciudadNombre);
                    intent.putExtras(b);
                    startActivity(intent);
                }
            });

            return rootView;
        }

        @Override
        public void onResume()
        {
            super.onResume();
            getDataFromAPI();
        }

        void getDataFromAPI()
        {
            String baseUrl = getString(R.string.server);
            Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();
            Retrofit client = new Retrofit.Builder().baseUrl(baseUrl).addConverterFactory(GsonConverterFactory.create(gson)).build();

            GuiaService service = client.create(GuiaService.class);

            Call<List<Ciudad>> call = service.getAllCiudades();

            call.enqueue(new Callback<List<Ciudad>>()
            {
                @Override
                public void onResponse(Response<List<Ciudad>> response, Retrofit retrofit)
                {
                    if (response.isSuccess())
                    {
                        mCiudades = response.body();
                        CiudadAdapter ciudadListAdapter = new CiudadAdapter(getContext(), mCiudades);
                        mListViewCiudades.setAdapter(ciudadListAdapter);
                    }
                    else
                    {
                        Log.d(TAG, "onResponse: call not successful");
                    }
                }

                @Override
                public void onFailure(Throwable t)
                {
                    Log.d(TAG, "onFailure: " + t.getMessage());
                }
            });
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item)
    {
        int id = item.getItemId();

        if (id == R.id.nav_inicio)
        {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
        else if (id == R.id.nav_favoritos)
        {
            Intent intent = new Intent(this, FavoritosActivity.class);
            startActivity(intent);
        }
        else if (id == R.id.nav_recomendados)
        {
            Intent intent = new Intent(this, RecomendadosActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout)findViewById(R.id.drawerLayoutEmpresaList);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}