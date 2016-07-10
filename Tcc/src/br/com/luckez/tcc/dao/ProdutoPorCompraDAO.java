package br.com.luckez.tcc.dao;

import java.sql.SQLException;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import br.com.luckez.tcc.entity.ProdutoPorCompra;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;


public class ProdutoPorCompraDAO extends BaseDaoImpl<ProdutoPorCompra, Integer>{
	
	public ProdutoPorCompraDAO(ConnectionSource cs) throws SQLException{
		super(ProdutoPorCompra.class);
		setConnectionSource(cs);
		initialize();
	}
	
	public Double selectMaxPreco(DBHelper dbHelper, Integer idProduto){
		SQLiteDatabase db = dbHelper.getWritableDatabase(); 
	    final SQLiteStatement stmt = db.compileStatement("SELECT MAX(preco_produto) FROM produto_por_compra WHERE produto_id = " + idProduto);
	    String maxValue = (String) stmt.simpleQueryForString();
	    if(maxValue != null){
	    	return Double.parseDouble(maxValue);
	    }else{
	    	return null;
	    }
	}
	
	public Double selectMinPreco(DBHelper dbHelper, Integer idProduto){
		SQLiteDatabase db = dbHelper.getWritableDatabase(); 
	    final SQLiteStatement stmt = db.compileStatement("SELECT MIN(preco_produto) FROM produto_por_compra WHERE produto_id = " + idProduto);
	    String minValue = (String) stmt.simpleQueryForString();
	    if(minValue != null){
	    	return Double.parseDouble(minValue);
	    }else{
	    	return null;
	    }
	}
	
	public Integer countProdutoCompras(DBHelper dbHelper, Integer idProduto){
		SQLiteDatabase db = dbHelper.getWritableDatabase(); 
	    final SQLiteStatement stmt = db.compileStatement("SELECT COUNT(*) FROM produto_por_compra WHERE produto_id = " + idProduto);
	    String count = (String) stmt.simpleQueryForString();
	    if(count != null){
	    	return Integer.parseInt(count);
	    }else{
	    	return null;
	    }
	}
	
	public Double sumtTotalCompradoPorProduto(DBHelper dbHelper, Integer idProduto){
		SQLiteDatabase db = dbHelper.getWritableDatabase(); 
	    final SQLiteStatement stmt = db.compileStatement("SELECT SUM(preco_produto * quantidade) FROM produto_por_compra WHERE produto_id = " + idProduto);
	    String sum = (String) stmt.simpleQueryForString();
	    if(sum != null){
	    	return Double.parseDouble(sum);
	    }else{
	    	return null;
	    }
	}
	
}
