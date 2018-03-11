package edu.unl.app7.android.contactlist.model;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import edu.unl.app7.android.contactlist.DetallesContacto;
import edu.unl.app7.android.contactlist.R;

public class AdapterContact extends ArrayAdapter<Contact> {
    private List<Contact> lista;
    private Context context;

    public AdapterContact(Context contex, ArrayList<Contact> list) {
        super(contex, R.layout.item_contacto);
        this.context = contex;
        this.lista = list;
    }

    @Override
    public int getCount() {
        return lista.size();
    }

    @Override
    public Contact getItem(int position) {
        return lista.get(position);
    }


    @NonNull
    @Override
    public View getView(final int position, View view, @NonNull ViewGroup parent) {

        final ViewHolder holder;

        if (view == null || view.getTag() == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_contacto, parent, false);
            holder = new ViewHolder();

            holder.tvName = view.findViewById(R.id.tvName_item);
            holder.ibDetalle = view.findViewById(R.id.ibDetalle_item);
            holder.ivFoto = view.findViewById(R.id.ivFoto_item);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        holder.tvName.setText(lista.get(position).getName());
        Picasso.get().load(lista.get(position).getImageUrl()).into(holder.ivFoto);

        holder.ibDetalle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, DetallesContacto.class);
                i.putExtra(context.getString(R.string.key_id_contacto), lista.get(position).getObjectId());
                context.startActivity(i);
            }
        });
        return view;
    }
}


class ViewHolder {
    ImageButton ibDetalle;
    ImageView ivFoto;
    TextView tvName;
}

