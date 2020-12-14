package android.teste.matheusaguilar.checkin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements LocationListener, AdapterView.OnItemSelectedListener {

    BancoDeDados bd;

    private int categoriaId;
    private ArrayList<Integer> categoriaIds;

    private LocationManager lm;
    private Criteria criteria;
    private String provider;
    private int REQ_TIME_LATLONG = 5000;
    private int DIST_MIN = 0;

    public final int LOCATION_PERMISSION = 1;

    private LatLng local;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*Localização*/

        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        criteria = new Criteria();

        PackageManager pm = getPackageManager();
        boolean hasGPS = pm.hasSystemFeature(PackageManager.FEATURE_LOCATION_GPS);

        if(hasGPS){
            criteria.setAccuracy(Criteria.ACCURACY_FINE);
        }
        else{
            criteria.setAccuracy(Criteria.ACCURACY_COARSE);
        }

        /*Fim Localização*/

        /*Banco de Dados */

        bd = BancoDeDados.getInstance();

        /*Autocomplete*/

        Cursor cursorLocais = bd.buscar("Checkin", new String[]{"Local"}, "", "Local");
        ArrayList<String> locais = new ArrayList<String>();

        while(cursorLocais.moveToNext()){
            locais.add(cursorLocais.getString(0));
        }

        locais.add("Teste");
        locais.add("AAAA");

        ArrayAdapter<String> adapterLocal = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, locais);

        AutoCompleteTextView autoComplete = (AutoCompleteTextView) findViewById(R.id.autoCompleteNome);
        autoComplete.setAdapter(adapterLocal);

        /*Fim AutoComplete*/

        /*Spinner*/

        Spinner spinner = (Spinner) findViewById(R.id.spinnerCategoria);
        spinner.setOnItemSelectedListener(this);

        Cursor cursorCategorias = bd.buscar("Categoria", new String[]{"nome, categoriaId"}, "", "idCategoria");
        ArrayList<String> categorias = new ArrayList<String>();
        categoriaIds = new ArrayList<Integer>();

        while(cursorCategorias.moveToNext()){
            categorias.add(cursorCategorias.getString(0));
            categoriaIds.add(cursorCategorias.getInt(1));
        }

        ArrayAdapter<String> adapterCategoria = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categorias);
        adapterCategoria.setDropDownViewResource(android.R.layout.simple_spinner_item);

        spinner.setAdapter(adapterCategoria);

        /*Fim Spinner*/
    }

    @Override
    protected void onStart(){
        super.onStart();

        provider = lm.getBestProvider(criteria, true);

        if(provider == null){
            Toast.makeText(this, "Provedor não encontrado", Toast.LENGTH_LONG).show();
        }
        else{
            if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED)
                lm.requestLocationUpdates(provider, REQ_TIME_LATLONG, DIST_MIN, this);
        }

        requestLocationPermission();
    }

    @Override
    protected void onDestroy(){
        lm.removeUpdates(this);
        super.onDestroy();
    }

    @Override
    public void onLocationChanged(Location location){
        if(location != null){
            local = new LatLng(location.getLatitude(), location.getLongitude());

            TextView latitudeText = (TextView) findViewById(R.id.valorLatitude);
            latitudeText.setText(Double.toString(local.latitude));

            TextView longitudeText = (TextView) findViewById(R.id.valorLongitude);
            longitudeText.setText(Double.toString(local.longitude));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void requestLocationPermission(){
        if(ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){

            if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)){
                Toast.makeText(this,"Permita o uso da localização para acessar seu local", Toast.LENGTH_LONG).show();
            }

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int code, String permission[], int[] grantResults){
        switch(code){
            case LOCATION_PERMISSION:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(this, "Permissão Concedida", Toast.LENGTH_LONG).show();
                }
                else{
                    Toast.makeText(this, "Permissão Negada: não é possível buscar a localização", Toast.LENGTH_LONG);
                }
                break;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        categoriaId = categoriaIds.get(position).intValue();
    }

    public void fazCheckIn(View v){

    }
}