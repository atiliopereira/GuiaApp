package com.dissoi.guiaapp2.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.dissoi.guiaapp2.R;
import com.dissoi.guiaapp2.models.Especialidad;

import java.util.List;


public class EspecialidadAdapter extends ArrayAdapter<Especialidad>
{
    private final Context context;
    private final List<Especialidad> values;

    public EspecialidadAdapter(Context context, List<Especialidad> values)
    {
        super(context, -1, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.listview_row_especialidad, parent, false);
        TextView textViewNombre = (TextView)rowView.findViewById(R.id.textViewEspecialidadRowNombre);
        TextView textViewNEmpresas = (TextView)rowView.findViewById(R.id.textViewEspecialidadRowNEmpresas);
        textViewNombre.setText(values.get(position).nombre);
        textViewNEmpresas.setText(String.valueOf(values.get(position).nEmpresas));

        return rowView;
    }

    @Override
    public boolean isEnabled(int position)
    {
        return true;
    }
}
