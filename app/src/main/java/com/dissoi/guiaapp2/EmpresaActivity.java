package com.dissoi.guiaapp2;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dissoi.guiaapp2.models.Celular;
import com.dissoi.guiaapp2.models.Empresa;
import com.dissoi.guiaapp2.models.Linea;
import com.dissoi.guiaapp2.models.Telefono;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.regex.Pattern;

import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;


public class EmpresaActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = "EmpresaActivity";

    private TextView mTextViewEmpresaDescripcion;
    private TextView mTextViewEmpresaHorario;
    private TextView mTextViewEmpresaTelefono1;
    private TextView mTextViewEmpresaTelefono2;
    private TextView mTextViewEmpresaTelefono3;
    private TextView mTextViewEmpresaTelefono4;
    private TextView mTextViewEmpresaTelefono5;
    private TextView mTextViewEmpresaCelular1;
    private TextView mTextViewEmpresaCelular2;
    private TextView mTextViewEmpresaCelular3;
    private TextView mTextViewEmpresaCelular4;
    private TextView mTextViewEmpresaCelular5;
    private TextView mTextViewEmpresaTiempoDeRespuesta;
    private TextView mTextViewEmpresaEmail;
    private TextView mTextViewEmpresaWeb;
    private TextView mTextViewEmpresaDireccion;
    private TextView mTextViewEmpresaFacebook;
    private TextView mTextViewEmpresaInstagram;
    private TextView mTextViewEmpresaWhatsapp;
    private TextView mTextViewEmpresaLineas;

    private LinearLayout mLinearLayoutEmpresaDescripcion;
    private LinearLayout mLinearLayoutEmpresaHorario;
    private LinearLayout mLinearLayoutEmpresaTelefono1;
    private LinearLayout mLinearLayoutEmpresaTelefono2;
    private LinearLayout mLinearLayoutEmpresaTelefono3;
    private LinearLayout mLinearLayoutEmpresaTelefono4;
    private LinearLayout mLinearLayoutEmpresaTelefono5;
    private LinearLayout mLinearLayoutEmpresaCelular1;
    private LinearLayout mLinearLayoutEmpresaCelular2;
    private LinearLayout mLinearLayoutEmpresaCelular3;
    private LinearLayout mLinearLayoutEmpresaCelular4;
    private LinearLayout mLinearLayoutEmpresaCelular5;
    private LinearLayout mLinearLayoutEmpresaTiempoDeRespuesta;
    private LinearLayout mLinearLayoutEmpresaEmail;
    private LinearLayout mLinearLayoutEmpresaWeb;
    private LinearLayout mLinearLayoutEmpresaDireccion;
    private LinearLayout mLinearLayoutEmpresaFacebook;
    private LinearLayout mLinearLayoutEmpresaInstagram;
    private LinearLayout mLinearLayoutEmpresaWhatsapp;
    private LinearLayout mLinearLayoutEmpresaLineas;

    private ImageView mImageViewEmpresa;
    private FloatingActionButton mFab;
    private ImageButton mImageButtonViewEmpresaUbicacion;
    private ImageButton mImageButtonViewEmpresaTelefono1;
    private ImageButton mImageButtonViewEmpresaTelefono2;
    private ImageButton mImageButtonViewEmpresaTelefono3;
    private ImageButton mImageButtonViewEmpresaTelefono4;
    private ImageButton mImageButtonViewEmpresaTelefono5;
    private ImageButton mImageButtonViewEmpresaCelular1;
    private ImageButton mImageButtonViewEmpresaCelular2;
    private ImageButton mImageButtonViewEmpresaCelular3;
    private ImageButton mImageButtonViewEmpresaCelular4;
    private ImageButton mImageButtonViewEmpresaCelular5;
    private ImageButton mImageButtonViewEmpresaWhatsapp;
    private ImageButton mImageButtonViewEmpresaEmail;
    private ImageButton mImageButtonViewEmpresaWeb;
    private ImageButton mImageButtonViewEmpresaFacebook;

    private List<Empresa> mEmpresas;
    private int mEmpresaId;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle b = getIntent().getExtras();
        mEmpresaId = b.getInt("empresa_id");
        getDataFromAPI(mEmpresaId);

        setContentView(R.layout.activity_empresa);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarEmpresa);
        setSupportActionBar(toolbar);

        NavigationView navigationView = (NavigationView) findViewById(R.id.navigationViewDrawer);
        navigationView.setNavigationItemSelectedListener(this);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawerLayoutEmpresa);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        Pattern emailPattern = Patterns.EMAIL_ADDRESS;
        Account[] accounts = AccountManager.get(this).getAccounts();
        for (Account account : accounts) {
            if (emailPattern.matcher(account.name).matches()) {
                View headerView = navigationView.getHeaderView(0);
                TextView tv = (TextView) headerView.findViewById(R.id.textViewDrawerHeaderNombre);
                tv.setText(account.name);
                break;
            }
        }

        mTextViewEmpresaDescripcion = (TextView) findViewById(R.id.textViewEmpresaDescripcion);
        mTextViewEmpresaHorario = (TextView) findViewById(R.id.textViewEmpresaHorario);
        mTextViewEmpresaTelefono1 = (TextView) findViewById(R.id.textViewEmpresaTelefono1);
        mTextViewEmpresaTelefono2 = (TextView) findViewById(R.id.textViewEmpresaTelefono2);
        mTextViewEmpresaTelefono3 = (TextView) findViewById(R.id.textViewEmpresaTelefono3);
        mTextViewEmpresaTelefono4 = (TextView) findViewById(R.id.textViewEmpresaTelefono4);
        mTextViewEmpresaTelefono5 = (TextView) findViewById(R.id.textViewEmpresaTelefono5);
        mTextViewEmpresaCelular1 = (TextView) findViewById(R.id.textViewEmpresaCelular1);
        mTextViewEmpresaCelular2 = (TextView) findViewById(R.id.textViewEmpresaCelular2);
        mTextViewEmpresaCelular3 = (TextView) findViewById(R.id.textViewEmpresaCelular3);
        mTextViewEmpresaCelular4 = (TextView) findViewById(R.id.textViewEmpresaCelular4);
        mTextViewEmpresaCelular5 = (TextView) findViewById(R.id.textViewEmpresaCelular5);
        mTextViewEmpresaTiempoDeRespuesta = (TextView) findViewById(R.id.textViewEmpresaTiempoDeRespuesta);
        mTextViewEmpresaEmail = (TextView) findViewById(R.id.textViewEmpresaEmail);
        mTextViewEmpresaWeb = (TextView) findViewById(R.id.textViewEmpresaWeb);
        mTextViewEmpresaDireccion = (TextView) findViewById(R.id.textViewEmpresaDireccion);

        mTextViewEmpresaFacebook = (TextView) findViewById(R.id.textViewEmpresaFacebook);
        mTextViewEmpresaInstagram = (TextView) findViewById(R.id.textViewEmpresaInstagram);
        mTextViewEmpresaWhatsapp = (TextView) findViewById(R.id.textViewEmpresaWhatsapp);
        mTextViewEmpresaLineas = (TextView) findViewById(R.id.textViewEmpresaLineas);

        mLinearLayoutEmpresaDescripcion = (LinearLayout) findViewById(R.id.linearLayoutEmpresaDescripcion);
        mLinearLayoutEmpresaHorario = (LinearLayout) findViewById(R.id.linearLayoutEmpresaHorario);
        mLinearLayoutEmpresaTelefono1 = (LinearLayout) findViewById(R.id.linearLayoutEmpresaTelefono1);
        mLinearLayoutEmpresaTelefono2 = (LinearLayout) findViewById(R.id.linearLayoutEmpresaTelefono2);
        mLinearLayoutEmpresaTelefono3 = (LinearLayout) findViewById(R.id.linearLayoutEmpresaTelefono3);
        mLinearLayoutEmpresaTelefono4 = (LinearLayout) findViewById(R.id.linearLayoutEmpresaTelefono4);
        mLinearLayoutEmpresaTelefono5 = (LinearLayout) findViewById(R.id.linearLayoutEmpresaTelefono5);
        mLinearLayoutEmpresaCelular1 = (LinearLayout) findViewById(R.id.linearLayoutEmpresaCelular1);
        mLinearLayoutEmpresaCelular2 = (LinearLayout) findViewById(R.id.linearLayoutEmpresaCelular2);
        mLinearLayoutEmpresaCelular3 = (LinearLayout) findViewById(R.id.linearLayoutEmpresaCelular3);
        mLinearLayoutEmpresaCelular4 = (LinearLayout) findViewById(R.id.linearLayoutEmpresaCelular4);
        mLinearLayoutEmpresaCelular5 = (LinearLayout) findViewById(R.id.linearLayoutEmpresaCelular5);
        mLinearLayoutEmpresaTiempoDeRespuesta = (LinearLayout) findViewById(R.id.linearLayoutEmpresaTiempoDeRespuesta);
        mLinearLayoutEmpresaEmail = (LinearLayout) findViewById(R.id.linearLayoutEmpresaEmail);
        mLinearLayoutEmpresaWeb = (LinearLayout) findViewById(R.id.linearLayoutEmpresaWeb);
        mLinearLayoutEmpresaDireccion = (LinearLayout) findViewById(R.id.linearLayoutEmpresaDireccion);
        mLinearLayoutEmpresaFacebook = (LinearLayout) findViewById(R.id.linearLayoutEmpresaFacebook);
        mLinearLayoutEmpresaInstagram = (LinearLayout) findViewById(R.id.linearLayoutEmpresaInstagram);
        mLinearLayoutEmpresaWhatsapp = (LinearLayout) findViewById(R.id.linearLayoutEmpresaWhatsapp);
        mLinearLayoutEmpresaLineas = (LinearLayout) findViewById(R.id.linearLayoutEmpresaLineas);

        mImageViewEmpresa = (ImageView) findViewById(R.id.imageViewEmpresa);

        mFab = (FloatingActionButton) findViewById(R.id.fabEmpresa);
        mImageButtonViewEmpresaUbicacion = (ImageButton) findViewById(R.id.imageButtonUBicacion);
        mImageButtonViewEmpresaTelefono1 = (ImageButton) findViewById(R.id.imageButtonTelefono1);
        mImageButtonViewEmpresaTelefono2 = (ImageButton) findViewById(R.id.imageButtonTelefono2);
        mImageButtonViewEmpresaTelefono3 = (ImageButton) findViewById(R.id.imageButtonTelefono3);
        mImageButtonViewEmpresaTelefono4 = (ImageButton) findViewById(R.id.imageButtonTelefono4);
        mImageButtonViewEmpresaTelefono5 = (ImageButton) findViewById(R.id.imageButtonTelefono5);
        mImageButtonViewEmpresaCelular1 = (ImageButton) findViewById(R.id.imageButtonCelular1);
        mImageButtonViewEmpresaCelular2 = (ImageButton) findViewById(R.id.imageButtonCelular2);
        mImageButtonViewEmpresaCelular3 = (ImageButton) findViewById(R.id.imageButtonCelular3);
        mImageButtonViewEmpresaCelular4 = (ImageButton) findViewById(R.id.imageButtonCelular4);
        mImageButtonViewEmpresaCelular5 = (ImageButton) findViewById(R.id.imageButtonCelular5);
        mImageButtonViewEmpresaWhatsapp = (ImageButton) findViewById(R.id.imageButtonWhatsapp);
        mImageButtonViewEmpresaEmail = (ImageButton) findViewById(R.id.imageButtonEmail);
        mImageButtonViewEmpresaWeb = (ImageButton) findViewById(R.id.imageButtonWeb);
        mImageButtonViewEmpresaFacebook = (ImageButton) findViewById(R.id.imageButtonFacebook);

        getSupportActionBar().setTitle("");
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client2 = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    void getDataFromAPI(final int empresaId) {
        final Context context = this;

        String baseUrl = getString(R.string.server);

        Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();
        Retrofit client = new Retrofit.Builder().baseUrl(baseUrl).addConverterFactory(GsonConverterFactory.create(gson)).build();

        GuiaService service = client.create(GuiaService.class);
        Call<List<Empresa>> call = service.getEmpresaById(empresaId);

        call.enqueue(new Callback<List<Empresa>>() {
            @Override
            public void onResponse(Response<List<Empresa>> response, Retrofit retrofit) {
                if (response.isSuccess()) {
                    mEmpresas = response.body();

                    if (mEmpresas.size() > 0) {
                        final Empresa empresa = mEmpresas.get(0);

                        ((EmpresaActivity) context).getSupportActionBar().setTitle(empresa.nombre);

                        String descripcion = "";
                        if (!empresa.descripcionCorta.equals("")) {
                            descripcion += empresa.descripcionCorta + "\n\n";
                        }
                        if (!empresa.descripcionLarga.equals("")) {
                            descripcion += empresa.descripcionLarga;
                        }
                        if (descripcion != "") {
                            mTextViewEmpresaDescripcion.setText(descripcion);
                        } else {
                            mLinearLayoutEmpresaDescripcion.setVisibility(View.GONE);
                        }

                        String horario = "";
                        if (empresa.horario != null) {
                            if ((empresa.horario.getLunesString() != null)) {
                                horario += empresa.horario.getLunesString();
                            }
                            if ((empresa.horario.getMartesString() != null)) {
                                horario += empresa.horario.getMartesString();
                            }
                            if ((empresa.horario.getMiercolesString() != null)) {
                                horario += empresa.horario.getMiercolesString();
                            }
                            if ((empresa.horario.getJuevesString() != null)) {
                                horario += empresa.horario.getJuevesString();
                            }
                            if ((empresa.horario.getViernesString() != null)) {
                                horario += empresa.horario.getViernesString();
                            }
                            if ((empresa.horario.getSabadoString() != null)) {
                                horario += empresa.horario.getSabadoString();
                            }
                            if ((empresa.horario.getDomingoString() != null)) {
                                horario += empresa.horario.getDomingoString();
                            }
                            if (horario != "") {
                                mTextViewEmpresaHorario.setText(horario.substring(0, horario.length() - 1));
                            } else {
                                mLinearLayoutEmpresaHorario.setVisibility(View.GONE);
                            }
                        } else {
                            mLinearLayoutEmpresaHorario.setVisibility(View.GONE);
                        }

                        if (!empresa.tiempoDeRespuesta.equals("")) {
                            mTextViewEmpresaTiempoDeRespuesta.setText(empresa.tiempoDeRespuesta);
                        } else {
                            mLinearLayoutEmpresaTiempoDeRespuesta.setVisibility(View.GONE);
                        }

                        if (!empresa.direccion.equals("")) {
                            mTextViewEmpresaDireccion.setText(empresa.direccion);
                        } else {
                            mLinearLayoutEmpresaDireccion.setVisibility(View.GONE);
                        }

                        if (!empresa.instagram.equals("")) {
                            mTextViewEmpresaInstagram.setText(empresa.instagram);
                        } else {
                            mLinearLayoutEmpresaInstagram.setVisibility(View.GONE);
                        }

                        if (empresa.lineas.size() > 0) {
                            String lineas = "";
                            for (Linea linea : empresa.lineas) {
                                lineas += linea.nombre;
                                if (!linea.observacion.equals("")) {
                                    lineas += ": " + linea.observacion;
                                }
                                lineas += "\n";
                            }
                            mTextViewEmpresaLineas.setText(lineas.substring(0, lineas.length() - 1));
                        } else {
                            mLinearLayoutEmpresaLineas.setVisibility(View.GONE);
                        }

                        if (empresa.foto != null) {
                            if (!empresa.foto.equals("")) {
                                Picasso.with(context).load(empresa.foto).fit().centerCrop().into(mImageViewEmpresa);
                            }
                        }

                        if (empresa.telefonos.size() > 0) {
                            final String numero = empresa.telefonos.get(0).numero;
                            mFab.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent = new Intent(Intent.ACTION_DIAL);
                                    intent.setData(Uri.parse("tel:" + numero));
                                    startActivity(intent);
                                }
                            });
                        } else {
                            CoordinatorLayout.LayoutParams p = (CoordinatorLayout.LayoutParams) mFab.getLayoutParams();
                            p.setAnchorId(View.NO_ID);
                            mFab.setLayoutParams(p);
                            mFab.setVisibility(View.GONE);
                        }

                        if (!empresa.ubicacion.equals("")) {
                            final String link = "http://maps.google.com/maps?q=loc:" + empresa.ubicacion;
                            mImageButtonViewEmpresaUbicacion.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent = new Intent(Intent.ACTION_VIEW);
                                    intent.setData(Uri.parse(link));
                                    startActivity(intent);
                                }
                            });
                        } else {
                            mLinearLayoutEmpresaDireccion.setVisibility(View.GONE);
                        }
                        int cantidadTelefonos = empresa.telefonos.size();

                        if (cantidadTelefonos > 0 && !empresa.telefonos.get(0).numero.equals("")) {
                                mImageButtonViewEmpresaTelefono1.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Intent intent = new Intent(Intent.ACTION_DIAL);
                                        intent.setData(Uri.parse("tel:" + empresa.telefonos.get(0).numero));
                                        startActivity(intent);
                                    }
                                });
                                mTextViewEmpresaTelefono1.setText(empresa.telefonos.get(0).numero);
                        }
                        else {
                            mLinearLayoutEmpresaTelefono1.setVisibility(View.GONE);
                        }

                        if (cantidadTelefonos > 1 && !empresa.telefonos.get(1).numero.equals("")) {
                            mImageButtonViewEmpresaTelefono2.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent = new Intent(Intent.ACTION_DIAL);
                                    intent.setData(Uri.parse("tel:" + empresa.telefonos.get(1).numero));
                                    startActivity(intent);
                                }
                            });
                            mTextViewEmpresaTelefono2.setText(empresa.telefonos.get(1).numero);
                        }
                        else {
                            mLinearLayoutEmpresaTelefono2.setVisibility(View.GONE);
                        }

                        if (cantidadTelefonos > 2 && !empresa.telefonos.get(2).numero.equals("")) {
                            mImageButtonViewEmpresaTelefono3.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent = new Intent(Intent.ACTION_DIAL);
                                    intent.setData(Uri.parse("tel:" + empresa.telefonos.get(2).numero));
                                    startActivity(intent);
                                }
                            });
                            mTextViewEmpresaTelefono3.setText(empresa.telefonos.get(2).numero);
                        }
                        else {
                            mLinearLayoutEmpresaTelefono3.setVisibility(View.GONE);
                        }

                        if (cantidadTelefonos > 3 && !empresa.telefonos.get(3).numero.equals("")) {
                            mImageButtonViewEmpresaTelefono4.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent = new Intent(Intent.ACTION_DIAL);
                                    intent.setData(Uri.parse("tel:" + empresa.telefonos.get(3).numero));
                                    startActivity(intent);
                                }
                            });
                            mTextViewEmpresaTelefono4.setText(empresa.telefonos.get(3).numero);
                        }
                        else {
                            mLinearLayoutEmpresaTelefono4.setVisibility(View.GONE);
                        }

                        if (cantidadTelefonos > 4 && !empresa.telefonos.get(4).numero.equals("")) {
                            mImageButtonViewEmpresaTelefono5.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent = new Intent(Intent.ACTION_DIAL);
                                    intent.setData(Uri.parse("tel:" + empresa.telefonos.get(4).numero));
                                    startActivity(intent);
                                }
                            });
                            mTextViewEmpresaTelefono5.setText(empresa.telefonos.get(4).numero);
                        }
                        else {
                            mLinearLayoutEmpresaTelefono5.setVisibility(View.GONE);
                        }

                        int cantidadCelulares = empresa.celulares.size();

                        if (cantidadCelulares > 0 && !empresa.celulares.get(0).numero.equals("")) {
                            mImageButtonViewEmpresaCelular1.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent = new Intent(Intent.ACTION_DIAL);
                                    intent.setData(Uri.parse("tel:" + empresa.celulares.get(0).numero));
                                    startActivity(intent);
                                }
                            });
                            mTextViewEmpresaCelular1.setText(empresa.celulares.get(0).numero);
                        }
                        else {
                            mLinearLayoutEmpresaCelular1.setVisibility(View.GONE);
                        }

                        if (cantidadCelulares > 1 && !empresa.celulares.get(1).numero.equals("")) {
                            mImageButtonViewEmpresaCelular2.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent = new Intent(Intent.ACTION_DIAL);
                                    intent.setData(Uri.parse("tel:" + empresa.celulares.get(1).numero));
                                    startActivity(intent);
                                }
                            });
                            mTextViewEmpresaCelular2.setText(empresa.celulares.get(1).numero);
                        }
                        else {
                            mLinearLayoutEmpresaCelular2.setVisibility(View.GONE);
                        }

                        if (cantidadCelulares > 2 && !empresa.celulares.get(2).numero.equals("")) {
                            mImageButtonViewEmpresaCelular3.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent = new Intent(Intent.ACTION_DIAL);
                                    intent.setData(Uri.parse("tel:" + empresa.celulares.get(2).numero));
                                    startActivity(intent);
                                }
                            });
                            mTextViewEmpresaCelular3.setText(empresa.telefonos.get(2).numero);
                        }
                        else {
                            mLinearLayoutEmpresaCelular3.setVisibility(View.GONE);
                        }

                        if (cantidadCelulares > 3 && !empresa.celulares.get(3).numero.equals("")) {
                            mImageButtonViewEmpresaCelular4.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent = new Intent(Intent.ACTION_DIAL);
                                    intent.setData(Uri.parse("tel:" + empresa.celulares.get(3).numero));
                                    startActivity(intent);
                                }
                            });
                            mTextViewEmpresaCelular4.setText(empresa.celulares.get(3).numero);
                        }
                        else {
                            mLinearLayoutEmpresaCelular4.setVisibility(View.GONE);
                        }

                        if (cantidadCelulares > 4 && !empresa.celulares.get(4).numero.equals("")) {
                            mImageButtonViewEmpresaCelular5.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent = new Intent(Intent.ACTION_DIAL);
                                    intent.setData(Uri.parse("tel:" + empresa.celulares.get(4).numero));
                                    startActivity(intent);
                                }
                            });
                            mTextViewEmpresaCelular5.setText(empresa.celulares.get(4).numero);
                        }
                        else {
                            mLinearLayoutEmpresaCelular5.setVisibility(View.GONE);
                        }

                        if (!empresa.whatsapp.equals("")) {
                            final String whatsapp = empresa.whatsapp;
                            mImageButtonViewEmpresaWhatsapp.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Uri uri = Uri.parse("smsto:" + whatsapp);
                                    Intent i = new Intent(Intent.ACTION_SENDTO, uri);
                                    i.setPackage("com.whatsapp");
                                    startActivity(Intent.createChooser(i, ""));
                                }
                            });
                            mTextViewEmpresaWhatsapp.setText(empresa.whatsapp);
                        } else {
                            mLinearLayoutEmpresaWhatsapp.setVisibility(View.GONE);
                        }

                        if (!empresa.email.equals("")) {
                            final String email = empresa.email;
                            mImageButtonViewEmpresaEmail.setOnClickListener(new View.OnClickListener(){
                                @Override
                                public void onClick(View view){
                                    Intent i = new Intent(Intent.ACTION_SENDTO);
                                    i.setData(Uri.parse("mailto:"));
                                    i.putExtra(Intent.EXTRA_EMAIL, email);
                                    if (i.resolveActivity(getPackageManager()) != null) {
                                        startActivity(i);
                                    }
                                }
                            });
                            mTextViewEmpresaEmail.setText(empresa.email);
                        } else {
                            mLinearLayoutEmpresaEmail.setVisibility(View.GONE);
                        }

                        if (!empresa.web.equals("")) {
                            final Uri web = Uri.parse(empresa.web);
                            mImageButtonViewEmpresaWeb.setOnClickListener(new View.OnClickListener(){
                                @Override
                                public void onClick(View view){
                                    Intent i = new Intent(Intent.ACTION_VIEW, web);
                                    if (i.resolveActivity(getPackageManager()) != null) {
                                        startActivity(i);
                                    }
                                }
                            });
                            mTextViewEmpresaWeb.setText(empresa.web);
                        } else {
                            mLinearLayoutEmpresaWeb.setVisibility(View.GONE);
                        }

                        if (!empresa.facebook.equals("")) {
                            final Uri facebook = Uri.parse("http://" + empresa.facebook);
                            mImageButtonViewEmpresaFacebook.setOnClickListener(new View.OnClickListener(){
                                @Override
                                public void onClick(View view){
                                    Intent i = new Intent(Intent.ACTION_VIEW, facebook);
                                    if (i.resolveActivity(getPackageManager()) != null) {
                                        startActivity(i);
                                    }
                                }
                            });
                            mTextViewEmpresaFacebook.setText(empresa.facebook);
                        } else {
                            mLinearLayoutEmpresaFacebook.setVisibility(View.GONE);
                        }

                    }
                } else {
                    Log.d(TAG, "onResponse: call not successful");
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_inicio) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_favoritos) {
            Intent intent = new Intent(this, FavoritosActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_recomendados) {
            Intent intent = new Intent(this, RecomendadosActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawerLayoutEmpresa);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client2.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Empresa Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.dissoi.guiaapp2/http/host/path")
        );
        AppIndex.AppIndexApi.start(client2, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Empresa Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.dissoi.guiaapp2/http/host/path")
        );
        AppIndex.AppIndexApi.end(client2, viewAction);
        client2.disconnect();
    }
}
