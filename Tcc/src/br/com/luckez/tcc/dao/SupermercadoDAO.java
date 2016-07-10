package br.com.luckez.tcc.dao;

import java.sql.SQLException;

import br.com.luckez.tcc.entity.Supermercado;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;


public class SupermercadoDAO extends BaseDaoImpl<Supermercado, Integer>{
	
	public SupermercadoDAO(ConnectionSource cs) throws SQLException{
		super(Supermercado.class);
		setConnectionSource(cs);
		initialize();
	}
}
