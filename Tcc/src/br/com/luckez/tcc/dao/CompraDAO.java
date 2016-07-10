package br.com.luckez.tcc.dao;

import java.sql.SQLException;

import br.com.luckez.tcc.entity.Compra;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;

public class CompraDAO extends BaseDaoImpl<Compra, Integer>{
	
	public CompraDAO(ConnectionSource cs) throws SQLException{
		super(Compra.class);
		setConnectionSource(cs);
		initialize();
	}
}
