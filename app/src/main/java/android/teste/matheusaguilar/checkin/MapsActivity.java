package android.teste.matheusaguilar.checkin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private BancoDeDados bd;

    private GoogleMap mMap;
    private LatLng local;

    static final int MAPANORMAL = 1;
    static final int MAPAHIBRIDO = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        bd = BancoDeDados.getInstance();

        Intent it = getIntent();
        Bundle bundle = it.getExtras();

        local = new LatLng(bundle.getDouble("latitude"), bundle.getDouble("longitude"));

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(local, 16));

        adicionarMarcadores();
    }

    public void adicionarMarcadores(){
        Cursor c = bd.buscar("Checkin, Categoria", new String[]{"Local", "qtdVisitas", "nome", "latitude", "longitude"}, "cat = idCategoria", "Local");

        while(c.moveToNext()){

            String titulo = c.getString(0);
            String descricao = "Categoria: " + c.getString(2) + " Visitas: " + c.getInt(1);
            LatLng posicao = new LatLng(c.getDouble(3), c.getDouble(4));

            mMap.addMarker(new MarkerOptions().position(posicao).title(titulo).snippet(descricao));
        }

        c.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_maps, menu);

        SubMenu tipos = menu.addSubMenu("Tipos de Mapa");
        tipos.add(0, MAPANORMAL, 4, "MAPA NORMAL");
        tipos.add(0, MAPAHIBRIDO, 5, "MAPA HIBRIDO");

        super.onCreateOptionsMenu(menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.mapsMenuVoltar:
                finish();
                break;
            case R.id.mapsMenuGestao: {

                Intent it = new Intent(this, GestaoActivity.class);

                finish();
                startActivity(it);

                break;
            }
            case R.id.mapsMenuLugares: {
                Intent it = new Intent(this, RelatorioActivity.class);

                finish();
                startActivity(it);

                break;
            }
            case MAPANORMAL:
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                break;
            case MAPAHIBRIDO:
                mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                break;
        }
        return true;
    }
}