package edu.unl.app7.android.contactlist;

import android.annotation.SuppressLint;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;


import edu.unl.app7.android.contactlist.manager.FirebaseContactManager;

import edu.unl.app7.android.contactlist.model.AdapterContact;
import edu.unl.app7.android.contactlist.model.Contact;


public class MainActivity extends AppCompatActivity {

    FirebaseContactManager firebaseContactManager;
    AdapterContact adapterLista;

    ArrayList<Contact> lista = new ArrayList<>();

    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ListView listViewContactos = findViewById(R.id.listaContactos);

        firebaseContactManager = FirebaseContactManager.getInstance();
        lista.addAll(firebaseContactManager.getAllContacts());

        adapterLista = new AdapterContact(this, lista);
        listViewContactos.setAdapter(adapterLista);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }
}
