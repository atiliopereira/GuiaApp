package com.dissoi.guiaapp2.adapters;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.dissoi.guiaapp2.EmpresaListActivity;
import com.dissoi.guiaapp2.R;
import com.dissoi.guiaapp2.db.GuiaAppDatabaseHelper;
import com.dissoi.guiaapp2.models.Empresa;

import java.util.List;


public class EmpresaAdapter extends ArrayAdapter<Empresa>
{
    private final Context context;
    private final List<Empresa> values;

    private boolean mRecomendadas;

    GuiaAppDatabaseHelper dbHelper;

    public EmpresaAdapter(Context context, List<Empresa> values, boolean recomendadas)
    {
        super(context, -1, values);
        this.context = context;
        this.values = values;

        mRecomendadas = recomendadas;

        dbHelper = new GuiaAppDatabaseHelper(context);
    }

    @Override
    public View getView(final int position, final View convertView, final ViewGroup parent)
    {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.listview_row_empresa, parent, false);
        TextView textViewNombre = (TextView) rowView.findViewById(R.id.textViewEmpresaRowNombre);
        TextView textViewTelefono = (TextView) rowView.findViewById(R.id.textViewEmpresaRowTelefono);
        CheckBox checkBoxFavorito = (CheckBox) rowView.findViewById(R.id.checkBoxEmpresaRowFavorito);

        textViewNombre.setText(values.get(position).nombre);

        String telefono;
        try
        {
            telefono = values.get(position).telefonos.get(0).numero;
        } catch (IndexOutOfBoundsException exception)
        {
            telefono = "";
        }
        textViewTelefono.setText(telefono);

        Cursor favoritos = dbHelper.getFavorito(values.get(position).id);

        if (favoritos.getCount() > 0)
        {
            checkBoxFavorito.setChecked(true);
        }
        else
        {
            checkBoxFavorito.setChecked(false);
        }

        checkBoxFavorito.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                CheckBox c = (CheckBox)v;

                if (c.isChecked())
                {
                    dbHelper.addFavorito(values.get(position).id);
                }
                else
                {
                    dbHelper.removeFavorito(values.get(position).id);
                }
                //notifyDataSetChanged();
            }
        });

        if (mRecomendadas) {checkBoxFavorito.setVisibility(View.GONE);}

        return rowView;
    }

    @Override
    public boolean isEnabled(int position)
    {
        return true;
    }
}