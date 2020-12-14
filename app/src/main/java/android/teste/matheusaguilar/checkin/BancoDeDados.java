package android.teste.matheusaguilar.checkin;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public final class BancoDeDados {

    protected SQLiteDatabase db;
    private static final String NOME_BANCO = "sistema_checkin";
    private static BancoDeDados INSTANCE;

    private final String[] SCRIPT_DATABASE_CREATE = new String[]{

            "CREATE TABLE Checkin (Local TEXT PRIMARY KEY, qtdVisitas INTEGER " +
            "NOT NULL, cat INTEGER NOT NULL, latitude TEXT NOT NULL, " +
            "longitude TEXT NOT NULL, CONSTRAINT fkey0 FOREIGN KEY (cat) " +
            "REFERENCES Categoria (idCategoria));",

            "CREATE TABLE Categoria (idCategoria INTEGER PRIMARY KEY " +
            "        AUTOINCREMENT, nome TEXT NOT NULL);",

            "INSERT INTO Categoria (nome) VALUES ('Restaurante');",
            "INSERT INTO Categoria (nome) VALUES ('Bar');",
            "INSERT INTO Categoria (nome) VALUES ('Cinema');",
            "INSERT INTO Categoria (nome) VALUES ('Universidade');",
            "INSERT INTO Categoria (nome) VALUES ('Estádio');",
            "INSERT INTO Categoria (nome) VALUES ('Parque');",
            "INSERT INTO Categoria (nome) VALUES ('Outros');"
    };

    private BancoDeDados(){
        Context ctx = MyApp.getContext();

        if(ctx == null){
            Log.e("NULL", "NOME");
        }

        db = ctx.openOrCreateDatabase(NOME_BANCO, Context.MODE_PRIVATE, null);

        Cursor c = buscar("sqlite_master", null, "type = 'table'", "");

        if(c.getCount() == 1){
            for(int i = 0; i < SCRIPT_DATABASE_CREATE.length; i++){
                db.execSQL(SCRIPT_DATABASE_CREATE[i]);
            }
        }

        c.close();
    }

    public static BancoDeDados getInstance(){
        if(INSTANCE == null){
            INSTANCE = new BancoDeDados();
        }
        return INSTANCE;
    }

    public long inserir(String tabela, ContentValues valores){
        long id = db.insert(tabela, null, valores);
        return id;
    }

    public int atualizar(String tabela, ContentValues valores, String where){
        int cnt = db.update(tabela, valores, where, null);
        return cnt;
    }

    public int deletar(String tabela, String where){
        int cnt = db.delete(tabela, where, null);
        return cnt;
    }

    public Cursor buscar(String tabela, String colunas[], String where, String orderBy){
        Cursor c;

        if(where.equals("")){
            c = db.query(tabela, colunas, null, null, null, null, orderBy);
        }
        else{
            c = db.query(tabela, colunas, where, null, null, null, orderBy);
        }

        return c;
    }

    public void abrir(){
        Context ctx = MyApp.getContext();
        db = ctx.openOrCreateDatabase(NOME_BANCO, Context.MODE_PRIVATE, null);
    }

    public void fechar(){
        if(db != null){
            db.close();
        }
    }
}
