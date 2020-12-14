package android.teste.matheusaguilar.checkin;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class GestaoActivity extends AppCompatActivity {

    private BancoDeDados bd;
    private String localDeletar;

    private final View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            localDeletar = v.getTag().toString();
            deletarRegistro();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gestao);

        LinearLayout lista = (LinearLayout) findViewById(R.id.listaScroll);

        bd = BancoDeDados.getInstance();

        Cursor c = bd.buscar("Checkin", new String[]{"Local"}, "", "Local");

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

            TextView text = new TextView(this);
            ImageButton button = new ImageButton(this);

            text.setText(local);
            text.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
            text.setLayoutParams(leftCenter);


            button.setImageResource(android.R.drawable.ic_delete);
            button.setTag(local);
            button.setScaleType(ImageView.ScaleType.FIT_CENTER);
            button.setOnClickListener(onClickListener);

            item.addView(text, leftCenter);
            item.addView(button, rightCenter);

            lista.addView(item);
        }

        c.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_gestao, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.gestaoMenuVoltar:
                finish();
                break;
        }
        return true;
    }

    private void deletarRegistro(){
        AlertDialog.Builder alerta = new AlertDialog.Builder(this);

        alerta.setTitle("Exclusão");
        alerta.setMessage("Tem certeza que deseja excluir " + localDeletar + "?");

        alerta.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                bd.deletar("Checkin", "Local='" + localDeletar + "'");
                dialog.dismiss();
                recreate();
            }
        });

        alerta.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        alerta.show();
    }
}