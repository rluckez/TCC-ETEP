package br.com.luckez.tcc.dao;

import java.sql.SQLException;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import br.com.luckez.tcc.entity.Categoria;
import br.com.luckez.tcc.entity.Compra;
import br.com.luckez.tcc.entity.Produto;
import br.com.luckez.tcc.entity.ProdutoPorCompra;
import br.com.luckez.tcc.entity.Supermercado;
import br.com.luckez.tcc.entity.Unidade;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

public class DBHelper extends OrmLiteSqliteOpenHelper {

	private static final String DATABASE_NAME = "db_tcc";
	private static final int VERSION = 1;

	public DBHelper(Context context) {
		super(context, DATABASE_NAME, null, VERSION);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase ldb, ConnectionSource cs) {
		try{
		TableUtils.createTable(cs, Categoria.class);
		TableUtils.createTable(cs, Supermercado.class);
		TableUtils.createTable(cs, Compra.class);
		TableUtils.createTable(cs, Produto.class);
		TableUtils.createTable(cs, ProdutoPorCompra.class);
		TableUtils.createTable(cs, Unidade.class);
		
		UnidadeDAO unidadeDAO = new UnidadeDAO(cs);
		
		Unidade unidade1 = new Unidade("Dúzia", "Dz");
		Unidade unidade2 = new Unidade("Unidade", "Un");
		Unidade unidade3 = new Unidade("Quilograma", "Kg");
		
		
		unidadeDAO.create(unidade1);
		unidadeDAO.create(unidade2);
		unidadeDAO.create(unidade3);
		
		}catch(SQLException e){
			//TODO blabla
			e.printStackTrace();
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase ldb, ConnectionSource cs, int oldVersion,
			int newVersion) {
		try {
			TableUtils.dropTable(cs, Categoria.class, true);
			TableUtils.dropTable(cs, Compra.class, true);
			TableUtils.dropTable(cs, Supermercado.class, true);
			TableUtils.dropTable(cs, Produto.class, true);
			TableUtils.dropTable(cs, ProdutoPorCompra.class, true);
			TableUtils.dropTable(cs, Unidade.class, true);
			onCreate(ldb, cs);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public void close(){
		super.close();
	}

}
