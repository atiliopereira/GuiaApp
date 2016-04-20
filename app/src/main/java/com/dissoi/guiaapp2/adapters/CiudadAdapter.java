package com.dissoi.guiaapp2.adapters;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.dissoi.guiaapp2.R;
import com.dissoi.guiaapp2.db.GuiaAppDatabaseHelper;
import com.dissoi.guiaapp2.models.Ciudad;

import java.util.List;


public class CiudadAdapter extends ArrayAdapter<Ciudad>
{
    private final Context context;
    private final List<Ciudad> values;

    GuiaAppDatabaseHelper dbHelper;

    public CiudadAdapter(Context context, List<Ciudad> values)
    {
        super(context, -1, values);
        this.context = context;
        this.values = values;

        dbHelper = new GuiaAppDatabaseHelper(context);
    }

    @Override
    public View getView(final int position, final View convertView, final ViewGroup parent)
    {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.listview_row_ciudad, parent, false);
        TextView textViewNombre = (TextView) rowView.findViewById(R.id.textViewCiudadRowNombre);

        textViewNombre.setText(values.get(position).nombre);

        return rowView;
    }

    @Override
    public boolean isEnabled(int position)
    {
        return true;
    }
}