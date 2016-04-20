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
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.dissoi.guiaapp2.adapters.EspecialidadAdapter;
import com.dissoi.guiaapp2.adapters.FavoritoAdapter;
import com.dissoi.guiaapp2.db.GuiaAppDatabaseHelper;
import com.dissoi.guiaapp2.models.Empresa;
import com.dissoi.guiaapp2.models.Especialidad;
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


public class FavoritosActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener
{
    private static final String TAG = "FavoritosActivity";

    private ListView mListViewEmpresas;

    private List<Empresa> mEmpresas;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favoritos);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbarFavoritos);
        setSupportActionBar(toolbar);

        NavigationView navigationView = (NavigationView)findViewById(R.id.navigationViewDrawer);
        navigationView.setNavigationItemSelectedListener(this);

        DrawerLayout drawer = (DrawerLayout)findViewById(R.id.drawerLayoutFavoritos);
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

        setTitle("Favoritos");

        mListViewEmpresas = (ListView)findViewById(R.id.listViewFavoritos);
        mListViewEmpresas.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                int empresaId = mEmpresas.get(position).id;
                Intent intent = new Intent(FavoritosActivity.this, EmpresaActivity.class);
                Bundle b = new Bundle();
                b.putInt("empresa_id", empresaId);
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
        mEmpresas = new ArrayList<>();

        GuiaAppDatabaseHelper dbHelper = new GuiaAppDatabaseHelper(this);
        Cursor favoritos = dbHelper.getFavoritos();

        String empresas = "";
        if (favoritos.getCount() > 0)
        {
            while (favoritos.moveToNext())
            {
                empresas += String.valueOf(favoritos.getInt(1)) + ",";
            }
            empresas = empresas.substring(0, empresas.length()-1);
        }

        String baseUrl = getString(R.string.server);
        Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();
        Retrofit client = new Retrofit.Builder().baseUrl(baseUrl).addConverterFactory(GsonConverterFactory.create(gson)).build();

        GuiaService service = client.create(GuiaService.class);
        Call<List<Empresa>> call = service.getEmpresaByIds(empresas);

        final Context context = this;

        call.enqueue(new Callback<List<Empresa>>()
        {
            @Override
            public void onResponse(Response<List<Empresa>> response, Retrofit retrofit)
            {
                if (response.isSuccess())
                {
                    mEmpresas.addAll(response.body());
                    FavoritoAdapter favoritoListAdapter = new FavoritoAdapter(context, mEmpresas);
                    mListViewEmpresas.setAdapter(favoritoListAdapter);
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
        }
        else if (id == R.id.nav_recomendados)
        {
            Intent intent = new Intent(this, RecomendadosActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout)findViewById(R.id.drawerLayoutFavoritos);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}