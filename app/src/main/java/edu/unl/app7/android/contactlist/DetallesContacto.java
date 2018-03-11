package edu.unl.app7.android.contactlist;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.squareup.picasso.Picasso;

import edu.unl.app7.android.contactlist.manager.FirebaseContactManager;
import edu.unl.app7.android.contactlist.model.Contact;

public class DetallesContacto extends AppCompatActivity implements View.OnClickListener {

    private ImageButton ibLocation;
    private ImageButton ibTelefono;
    private ImageButton ibEmail;

    private Location myCurrentLocation;

    private final int PERMISSIONS_REQUEST_CALL = 1;
    private final int PERMISSIONS_REQUEST_LOCATION = 2;

    private String EMAIL;
    private String PHONE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalles_contacto);

        Bundle i = getIntent().getExtras();
        if (i != null) {

            ibLocation = findViewById(R.id.ibLocation);
            ibEmail = findViewById(R.id.ibEmail);
            ibTelefono = findViewById(R.id.ibPhone);

            ibEmail.setOnClickListener(this);
            ibTelefono.setOnClickListener(this);
            ibLocation.setOnClickListener(this);

            Contact obj = FirebaseContactManager.getInstance().getContactByObjectId(String.valueOf(i.get(getString(R.string.key_id_contacto))));

            TextView tvName = findViewById(R.id.tvNombre);
            TextView tvDescription = findViewById(R.id.tvdescripcion);
            TextView tvTelefono = findViewById(R.id.tvTelefono);
            TextView tvDireccion = findViewById(R.id.tvDireccion);
            ImageView img = findViewById(R.id.ivFotoDetalle);

            tvName.setText(obj.getName());
            tvDescription.setText(obj.getDescription());
            tvTelefono.setText(obj.getPhone());
            tvDireccion.setText(obj.getCity());

            this.EMAIL = obj.getEmail();
            this.PHONE = obj.getPhone();

            Picasso.get().load(obj.getImageUrl()).into(img);
        }


    }

    @Override
    public void onClick(View v) {
        if (v == ibLocation) {
            loadLocation();
        } else if (v == ibTelefono) {
            makeCall();
        } else if (v == ibEmail) {
            sendEmail();
        }
    }

    @SuppressLint("MissingPermission")
    private void getCoordenadas() {

        LocationManager locManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        assert locManager != null;
        myCurrentLocation = locManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (myCurrentLocation == null)
            myCurrentLocation = locManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        if (myCurrentLocation == null)
            myCurrentLocation = locManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
    }

    private void loadLocation() {
        if (existsPermisoLocation()) {
            getCoordenadas();
            if (myCurrentLocation != null) {
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse("http://maps.google.com/maps?saddr=" + myCurrentLocation.getLatitude() + "," + myCurrentLocation.getLongitude() + "&daddr=" + -4.012146 + "," + -79.204575));
                startActivity(intent);
            } else {
                Toast.makeText(this, "Encienda el GPS", Toast.LENGTH_SHORT).show();
            }
        } else
            getPermisoLocation();
    }

    @SuppressLint("MissingPermission")
    private void makeCall() {
        if (existsPermisoCall()) {
            Intent intent = new Intent(Intent.ACTION_CALL);
            intent.setData(Uri.parse("tel:" + PHONE));
            startActivity(intent);
        } else {
            getPermisoCall();
        }

    }

    private void sendEmail() {
        Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", EMAIL, null));
        intent.putExtra(Intent.EXTRA_SUBJECT, "Mensaje de App ContactList");
        startActivity(Intent.createChooser(intent, "Enviar un email:"));
    }

    private void getPermisoLocation() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_REQUEST_LOCATION);
    }

    private void getPermisoCall() {
        ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CALL_PHONE}, PERMISSIONS_REQUEST_CALL);
    }

    private boolean existsPermisoCall() {
        return ContextCompat.checkSelfPermission(this, android.Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED;
    }

    private boolean existsPermisoLocation() {
        return ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {

            case PERMISSIONS_REQUEST_CALL: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    makeCall();
                }
                return;
            }
            case PERMISSIONS_REQUEST_LOCATION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    loadLocation();
                }
            }
        }
    }
}
