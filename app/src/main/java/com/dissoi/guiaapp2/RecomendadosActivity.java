package com.dissoi.guiaapp2;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.dissoi.guiaapp2.adapters.EmpresaAdapter;
import com.dissoi.guiaapp2.adapters.FavoritoAdapter;
import com.dissoi.guiaapp2.adapters.RecomendadoAdapter;
import com.dissoi.guiaapp2.db.GuiaAppDatabaseHelper;
import com.dissoi.guiaapp2.models.Empresa;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;


public class RecomendadosActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener
{
    private static final String TAG = "RecomendadosActivity";

    private List<Empresa> mEmpresas;
    private ListView mListViewEmpresas;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recomendados);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbarRecomendados);
        setSupportActionBar(toolbar);

        NavigationView navigationView = (NavigationView)findViewById(R.id.navigationViewDrawer);
        navigationView.setNavigationItemSelectedListener(this);

        DrawerLayout drawer = (DrawerLayout)findViewById(R.id.drawerLayoutRecomendados);
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

        mListViewEmpresas = (ListView)findViewById(R.id.listViewRecomendados);

        setTitle("Recomendados");
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        getDataFromAPI();
    }

    void getDataFromAPI()
    {
        final Context context = this;

        String baseUrl = getString(R.string.server);
        Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();
        Retrofit client = new Retrofit.Builder().baseUrl(baseUrl).addConverterFactory(GsonConverterFactory.create(gson)).build();

        GuiaService service = client.create(GuiaService.class);

        Call<List<Empresa>> call;
        call = service.getEmpresasRecomendadas();

        call.enqueue(new Callback<List<Empresa>>()
        {
            @Override
            public void onResponse(Response<List<Empresa>> response, Retrofit retrofit)
            {
                if (response.isSuccess())
                {
                    mEmpresas = response.body();
                    RecomendadoAdapter empresaListAdapter = new RecomendadoAdapter(context, mEmpresas);
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
        }

        DrawerLayout drawer = (DrawerLayout)findViewById(R.id.drawerLayoutRecomendados);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
