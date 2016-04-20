package com.dissoi.guiaapp2.models;


public class Horario
{
    public String lunesStart;
    public String lunesEnd;
    public String martesStart;
    public String martesEnd;
    public String miercolesStart;
    public String miercolesEnd;
    public String juevesStart;
    public String juevesEnd;
    public String viernesStart;
    public String viernesEnd;
    public String sabadoStart;
    public String sabadoEnd;
    public String domingoStart;
    public String domingoEnd;

    public String getLunesString()
    {
        if ((lunesStart != null) && (lunesEnd != null))
        {
            if ((!lunesStart.equals("")) && (!lunesEnd.equals("")))
            {
                return "Lun " + lunesStart.substring(0, lunesStart.length() - 3) + " a " + lunesEnd.substring(0, lunesEnd.length() - 3) + "\n";
            }
        }
        return null;
    }

    public String getMartesString()
    {
        if ((martesStart != null) && (martesEnd != null))
        {
            if ((!martesStart.equals("")) && (!martesEnd.equals("")))
            {
                return "Mar " + martesStart.substring(0, martesStart.length() - 3) + " a " + martesEnd.substring(0, martesEnd.length() - 3) + "\n";
            }
        }
        return null;
    }

    public String getMiercolesString()
    {
        if ((miercolesStart != null) && (miercolesEnd != null))
        {
            if ((!miercolesStart.equals("")) && (!miercolesEnd.equals("")))
            {
                return "Mie " + miercolesStart.substring(0, miercolesStart.length() - 3) + " a " + miercolesEnd.substring(0, miercolesEnd.length() - 3) + "\n";
            }
        }
        return null;
    }

    public String getJuevesString()
    {
        if ((juevesStart != null) && (juevesEnd != null))
        {
            if ((!juevesStart.equals("")) && (!juevesEnd.equals("")))
            {
                return "Jue " + juevesStart.substring(0, juevesStart.length() - 3) + " a " + juevesEnd.substring(0, juevesEnd.length() - 3) + "\n";
            }
        }
        return null;
    }

    public String getViernesString()
    {
        if ((viernesStart != null) && (viernesEnd != null))
        {
            if ((!viernesStart.equals("")) && (!viernesEnd.equals("")))
            {
                return "Vie " + viernesStart.substring(0, viernesStart.length() - 3) + " a " + viernesEnd.substring(0, viernesEnd.length() - 3) + "\n";
            }
        }
        return null;
    }

    public String getSabadoString()
    {
        if ((sabadoStart != null) && (sabadoEnd != null))
        {
            if ((!sabadoStart.equals("")) && (!sabadoEnd.equals("")))
            {
                return "Sab " + sabadoStart.substring(0, sabadoStart.length() - 3) + " a " + sabadoEnd.substring(0, sabadoEnd.length() - 3) + "\n";
            }
        }
        return null;
    }

    public String getDomingoString()
    {
        if ((domingoStart != null) && (domingoEnd != null))
        {
            if ((!domingoStart.equals("")) && (!domingoEnd.equals("")))
            {
                return "Dom " + domingoStart.substring(0, domingoStart.length() - 3) + " a " + domingoEnd.substring(0, domingoEnd.length() - 3) + "\n";
            }
        }
        return null;
    }
}