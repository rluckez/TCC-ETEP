package br.com.luckez.tcc.dao;

import java.sql.SQLException;

import br.com.luckez.tcc.entity.Categoria;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;


public class CategoriaDAO extends BaseDaoImpl<Categoria, Integer>{
	
	public CategoriaDAO(ConnectionSource cs) throws SQLException{
		super(Categoria.class);
		setConnectionSource(cs);
		initialize();
	}
	
	
	
}
