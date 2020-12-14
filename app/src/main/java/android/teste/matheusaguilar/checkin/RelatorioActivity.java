package android.teste.matheusaguilar.checkin;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class RelatorioActivity extends AppCompatActivity {

    private BancoDeDados bd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_relatorio);

        LinearLayout lista = (LinearLayout) findViewById(R.id.listaScroll);

        bd = BancoDeDados.getInstance();

        Cursor c = bd.buscar("Checkin", new String[]{"Local", "qtdVisitas"}, "", "qtdVisitas DESC, Local");

        while(c.moveToNext()){

            RelativeLayout item = new RelativeLayout(this);

            RelativeLayout.LayoutParams leftCenter = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            leftCenter.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            leftCenter.addRule(RelativeLayout.CENTER_VERTICAL);


            RelativeLayout.LayoutParams rightCenter = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            rightCenter.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            rightCenter.addRule(RelativeLayout.CENTER_VERTICAL);

            String local = c.getString(0);
            Integer visitas = c.getInt(1);

            TextView localText = new TextView(this);

            localText.setText(local);
            localText.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
            localText.setLayoutParams(leftCenter);

            TextView visitasText = new TextView(this);

            visitasText.setText(Integer.toString(visitas));
            visitasText.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
            visitasText.setLayoutParams(rightCenter);

            item.addView(localText, leftCenter);
            item.addView(visitasText, rightCenter);

            lista.addView(item);
        }

        c.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_relatorio, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.relatorioMenuVoltar:
                finish();
                break;
        }
        return true;
    }
}