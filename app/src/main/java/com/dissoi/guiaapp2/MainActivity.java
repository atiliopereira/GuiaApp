package com.dissoi.guiaapp2;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.dissoi.guiaapp2.adapters.EspecialidadAdapter;
import com.dissoi.guiaapp2.models.Especialidad;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;
import java.util.regex.Pattern;

import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener
{
    private static final String TAG = "MainActivity";

    private ListView mListViewEspecialidad;

    private List<Especialidad> mEspecialidades;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbarMain);
        setSupportActionBar(toolbar);

        NavigationView navigationView = (NavigationView)findViewById(R.id.navigationViewDrawer);
        navigationView.setNavigationItemSelectedListener(this);

        DrawerLayout drawer = (DrawerLayout)findViewById(R.id.drawerLayoutMain);
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

        setTitle("Guía de empresas");

        mListViewEspecialidad = (ListView)findViewById(R.id.listViewEspecialidad);
        mListViewEspecialidad.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                int especialidadId = mEspecialidades.get(position).id;
                String especialidadNombre = mEspecialidades.get(position).nombre;
                Intent intent = new Intent(MainActivity.this, EmpresaListActivity.class);
                Bundle b = new Bundle();
                b.putInt("especialidad_id", especialidadId);
                b.putString("especialidad_nombre", especialidadNombre);
                intent.putExtras(b);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume()
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
        Call<List<Especialidad>> call = service.getAllEspecialidades();

        final Context context = this;

        call.enqueue(new Callback<List<Especialidad>>()
        {
            @Override
            public void onResponse(Response<List<Especialidad>> response, Retrofit retrofit)
            {
                if (response.isSuccess())
                {
                    mEspecialidades = response.body();
                    EspecialidadAdapter especialidadListAdapter = new EspecialidadAdapter(context, mEspecialidades);
                    mListViewEspecialidad.setAdapter(especialidadListAdapter);
                    //Log.d(TAG, "onResponse: Especialidades: " + mEspecialidades.toString());
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item)
    {
        int id = item.getItemId();

        if (id == R.id.nav_inicio)
        {
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

        DrawerLayout drawer = (DrawerLayout)findViewById(R.id.drawerLayoutMain);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}