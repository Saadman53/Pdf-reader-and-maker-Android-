
package com.example.nyoreaderpdfmaker;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.nyoreaderpdfmaker.R;

import java.io.File;
import java.util.ArrayList;

public class pdf_adapter extends ArrayAdapter<File> {

    Context context;
    ViewHolder viewHolder;

    ArrayList<File>al_pdf;

    public pdf_adapter(Context context, ArrayList<File>al_pdf) {
        super(context, R.layout.adapter_pdf,al_pdf);

        this.context=context;
        this.al_pdf=al_pdf;
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public int getViewTypeCount() {
        if(al_pdf.size()>0)
            return al_pdf.size();
        else return 1;
    }

    @Override
    public View getView(int position,  View convertView,ViewGroup parent) {
        if(convertView==null){
            convertView= LayoutInflater.from(getContext()).inflate(R.layout.adapter_pdf,parent,false);

            viewHolder=new ViewHolder();
            viewHolder.tv_filename=(TextView)convertView.findViewById(R.id.tv_name);
            convertView.setTag(viewHolder);

        }
        else
        {
            viewHolder=(ViewHolder)convertView.getTag();

        }

        viewHolder.tv_filename.setText(al_pdf.get(position).getName());

        return convertView;
    }

    public class ViewHolder {
        TextView tv_filename;
    }


}
