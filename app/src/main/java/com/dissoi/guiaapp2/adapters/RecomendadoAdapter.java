package com.dissoi.guiaapp2.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.dissoi.guiaapp2.EmpresaActivity;
import com.dissoi.guiaapp2.R;
import com.dissoi.guiaapp2.db.GuiaAppDatabaseHelper;
import com.dissoi.guiaapp2.models.Empresa;
import com.squareup.picasso.Picasso;

import java.util.List;


public class RecomendadoAdapter extends ArrayAdapter<Empresa>
{
    private final Context context;
    private final List<Empresa> values;

    GuiaAppDatabaseHelper dbHelper;

    public RecomendadoAdapter(Context context, List<Empresa> values)
    {
        super(context, -1, values);
        this.context = context;
        this.values = values;

        dbHelper = new GuiaAppDatabaseHelper(context);
    }

    @Override
    public View getView(final int position, final View convertView, final ViewGroup parent)
    {
        final Empresa empresa = values.get(position);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.listview_row_recomendado, parent, false);
        TextView textViewNombre = (TextView)rowView.findViewById(R.id.textViewRecomendadoNombre);
        ImageView imageViewFoto = (ImageView)rowView.findViewById(R.id.imageViewRecomendadoFoto);
        ImageButton imageButtonTelefono = (ImageButton)rowView.findViewById(R.id.imageButtonRecomendadoTelefono);
        ImageButton imageButtonEmail = (ImageButton)rowView.findViewById(R.id.imageButtonRecomendadoEmail);
        ImageButton imageButtonUbicacion = (ImageButton)rowView.findViewById(R.id.imageButtonRecomendadoUbicacion);
        ImageButton imageButtonWeb = (ImageButton)rowView.findViewById(R.id.imageButtonRecomendadoWeb);

        textViewNombre.setText(values.get(position).nombre);

        imageViewFoto.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                int empresaId = empresa.id;
                Intent intent = new Intent(getContext(), EmpresaActivity.class);
                Bundle b = new Bundle();
                b.putInt("empresa_id", empresaId);
                intent.putExtras(b);
                getContext().startActivity(intent);
            }
        });

        if (empresa.foto != null)
        {
            if (!empresa.foto.equals(""))
            {
                Picasso.with(context).load(empresa.foto).fit().centerCrop().into(imageViewFoto);
            }
        }

        if (empresa.telefonos.size() > 0)
        {
            final String numero = empresa.telefonos.get(0).numero;
            imageButtonTelefono.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:" + numero));
                    getContext().startActivity(intent);
                }
            });
        }
        else {imageButtonTelefono.setVisibility(View.GONE);}

        if (!empresa.email.equals(""))
        {
            final String email = empresa.email;
            imageButtonEmail.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setType("message/rfc822");
                    intent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{email});
                    getContext().startActivity(intent);
                }
            });
        }
        else {imageButtonEmail.setVisibility(View.GONE);}

        if (!empresa.web.equals(""))
        {
            final String web = empresa.web;
            imageButtonWeb.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    Uri uri = Uri.parse(web);
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    getContext().startActivity(intent);
                }
            });
        }
        else {imageButtonWeb.setVisibility(View.GONE);}

        if (!empresa.ubicacion.equals(""))
        {
            final String ubicacion = empresa.ubicacion;
            imageButtonUbicacion.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    Uri uri = Uri.parse("http://maps.google.com/maps?q=loc:" + ubicacion);
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    getContext().startActivity(intent);
                }
            });
        }
        else {imageButtonUbicacion.setVisibility(View.GONE);}

        return rowView;
    }

    @Override
    public boolean isEnabled(int position)
    {
        return true;
    }
}