package br.com.luckez.tcc.dao;

import java.sql.SQLException;

import br.com.luckez.tcc.entity.Unidade;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;


public class UnidadeDAO extends BaseDaoImpl<Unidade, Integer>{
	
	public UnidadeDAO(ConnectionSource cs) throws SQLException{
		super(Unidade.class);
		setConnectionSource(cs);
		initialize();
	}
}
