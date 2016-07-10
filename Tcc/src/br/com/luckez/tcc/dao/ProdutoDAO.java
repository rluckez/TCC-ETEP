package br.com.luckez.tcc.dao;

import java.sql.SQLException;

import br.com.luckez.tcc.entity.Produto;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;


public class ProdutoDAO extends BaseDaoImpl<Produto, Integer>{
	
	public ProdutoDAO(ConnectionSource cs) throws SQLException{
		super(Produto.class);
		setConnectionSource(cs);
		initialize();
	}
}
