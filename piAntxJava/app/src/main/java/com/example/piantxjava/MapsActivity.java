package com.example.piantxjava;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

import static java.lang.Math.incrementExact;
import static java.lang.Math.sqrt;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap;
    private static final int LOCATION_REQUEST = 500;
    FusedLocationProviderClient fusedLocationProviderClient;
    private Button btGerarRota;
    private Polyline polyline = null;
    LatLng me;
    List<LatLng> latLongList = new ArrayList<>();
    List<Marker> makerList = new ArrayList<>();
    Trashes trashes;
    TextView tituloLixeira, descricaoLixeira;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        btGerarRota = findViewById(R.id.bt_gerarRota);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        getLocation();

        Intent intent = getIntent();
        String string = intent.getParcelableExtra("id");
        HTTPService service = new HTTPService(string);
        try {
            trashes = service.execute().get();
        }catch (InterruptedException e){
            e.printStackTrace();
        }catch (ExecutionException e){
            e.printStackTrace();
        }

        btGerarRota.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (polyline != null) polyline.remove();
                PolylineOptions polylineOptions;
                latLongList.add(0, me);
                atualizarLista(latLongList);
                polylineOptions = new PolylineOptions().addAll(latLongList).clickable(true);
                polyline = mMap.addPolyline(polylineOptions);

            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST);
            return;
        }

        mMap.setMyLocationEnabled(true);

        LatLng osasco = new LatLng(-23.5317, -46.789923);

        adicionarMarkers();

        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(osasco, 11));
        mMap.setOnMarkerClickListener(this);
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case LOCATION_REQUEST:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mMap.setMyLocationEnabled(true);
                }
                break;
        }
    }


    public void addMaker(Trashe trashe) {
        LatLng latLong = new LatLng(trashe.getLatitude(), trashe.getLongitude());
        latLongList.add(latLong);
        MarkerOptions markerOptions = new MarkerOptions()
                .position(latLong)
                .title(trashe.getName());
        Marker marker = mMap.addMarker(markerOptions);
        marker.setTag(trashe);
        marker.setIcon(selecionarIcone(trashe.getPercentage()));
        makerList.add(marker);
    }

    public void adicionarMarkers(){
        for(Trashe trashe: trashes.getTrashes()){
            addMaker(trashe);
        }
    }

    public BitmapDescriptor selecionarIcone(double percent){
        //lixeira vermelinha
        if(percent > 85.0) {
            return BitmapDescriptorFactory.fromResource(R.drawable.lixeira);
        }
        //lixeira amarela
        if(percent > 45.0){
            return BitmapDescriptorFactory.fromResource(R.drawable.lixeira);
        }
        //lixeira verde
        if(percent > 10.0) {
            return BitmapDescriptorFactory.fromResource(R.drawable.lixeira);
        }
        //lixeira vazia
        return BitmapDescriptorFactory.fromResource(R.drawable.lixeira);
    }


    public void atualizarLista(List<LatLng> list) {
        List<Double> dist = new ArrayList<>();
        List<LatLng> nList = new ArrayList<>();
        Double temp = 10000.0;
        int t = 0;
        for(LatLng l: list)dist.add(calcDistMe(l));
        for(int i=0;i<dist.size();i++) {
            if(dist.get(i) < temp){
                temp = dist.get(i);
                t = i;
        } }
        nList.add(list.get(t));
        list.remove(t); dist.remove(t);

        int size = list.size();

        for(int i=0;i<size;i++){
            dist = new ArrayList<>();
            for(int j=0;j<list.size();j++) dist.add(calcDist(list.get(j), nList.get(i)));
            temp = 1000.0; t = 0;
            for(int j=0;j<dist.size();j++) {
                if(dist.get(j) < temp){
                    temp = dist.get(j);
                    t = j;
            } }
            nList.add(list.get(t));
            list.remove(t); dist.remove(t);
        }
        latLongList = nList;
    }

    public double calcDistMe(LatLng latLng) {
        return sqrt((me.latitude-latLng.latitude)*(me.latitude-latLng.latitude) + (me.longitude - latLng.longitude)*(me.longitude - latLng.longitude));
    }

    public double calcDist(LatLng latLng, LatLng mk) {
        return sqrt((mk.latitude-latLng.latitude)*(mk.latitude-latLng.latitude) + (mk.longitude - latLng.longitude)*(mk.longitude - latLng.longitude));
    }

    public void getLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST);
            return;
        }
        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                Location location = task.getResult();
                if (location != null) {
                    try {
                        Geocoder geocoder = new Geocoder(MapsActivity.this, Locale.getDefault());
                        List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                        me = new LatLng(addresses.get(0).getLatitude(), addresses.get(0).getLongitude());

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    @Override
    public boolean onMarkerClick(Marker marker) {

        final Dialog dialog = new Dialog(this);

        dialog.setContentView(R.layout.lixeira_dialog);

        dialog.setCanceledOnTouchOutside(true);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = android.R.style.Animation_Dialog;

        Button btRetorno = dialog.findViewById(R.id.bt_retorno);
        tituloLixeira = dialog.findViewById(R.id.title_Lixeira);
        descricaoLixeira = dialog.findViewById(R.id.descricao_Lixeira);

        Trashe trashe = (Trashe) marker.getTag();
        tituloLixeira.setText(trashe.getName());
        descricaoLixeira.setText(montaDescricao(trashe));

        btRetorno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
        return true;
    }

    private String montaDescricao(Trashe trashe) {
        return "Porcentagem: " + trashe.getPercentage()
                +"\nPrioridade: "+ trashe.getPriority()
                +"\nÚltima atualização: \n"+ trashe.getLast_update();
    }
}