package br.com.luckez.tcc.webservice;

import java.io.IOException;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpResponseException;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import br.com.luckez.tcc.entity.Produto;

public class ProdutoWS {

	public ProdutoWS() {
		
	}
	
	private static final String URL = "http://192.168.1.101:8080/ComprasWS/services/WebService?wsdl";
	private static final String NAMESPACE = "http://ws.tcc.luckez.com.br";
	
//	private static final String INSERIR = "insereProduto";
	private static final String CHECAR_AND_INSERIR = "checkAndInsertProduto";
	private static final String REPORTAR_PRODUTO = "reportarProduto";
//	private static final String EXCLUIR = "deleteProduto";
//	private static final String ATUALIZAR = "updateProduto";
//	private static final String LISTAR = "listAll";
	private static final String BUSCAR_POR_ID = "searchProdutoById";
	private static final String BUSCAR_POR_CODIGO = "searchProdutoByCode";
	
	
	public boolean insereProduto(Produto produto){
		
		SoapObject insereProduto = new SoapObject(NAMESPACE, CHECAR_AND_INSERIR);
		
		SoapObject produtoParaInserir = new SoapObject(NAMESPACE, "produto");
		
		produtoParaInserir.addProperty("codigo", produto.getCodigo());
		produtoParaInserir.addProperty("id", produto.getId());
		produtoParaInserir.addProperty("nome", produto.getNome());
	
		insereProduto.addSoapObject(produtoParaInserir);
		
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		
		envelope.setOutputSoapObject(insereProduto);
		
		envelope.implicitTypes = true;
		
		HttpTransportSE http = new HttpTransportSE(URL);
		
		try {
			http.call("urn:" + CHECAR_AND_INSERIR, envelope);
			
			SoapPrimitive resposta = (SoapPrimitive) envelope.getResponse();
			
			return Boolean.parseBoolean(resposta.toString());
			
		} catch (HttpResponseException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		} catch (XmlPullParserException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public boolean reportarProduto(Long barCode){
		
		SoapObject reportarProduto = new SoapObject(NAMESPACE, REPORTAR_PRODUTO);
		
		reportarProduto.addProperty("code", barCode);
	
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		
		envelope.setOutputSoapObject(reportarProduto);
		
		envelope.implicitTypes = true;
		
		HttpTransportSE http = new HttpTransportSE(URL);
		
		try {
			http.call("urn:" + REPORTAR_PRODUTO, envelope);
			
			SoapPrimitive resposta = (SoapPrimitive) envelope.getResponse();
			
			return Boolean.parseBoolean(resposta.toString());
			
		} catch (HttpResponseException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		} catch (XmlPullParserException e) {
			e.printStackTrace();
			return false;
		}
	}
	
//	public boolean updateProduto(Produto produto){
//		
//		return true;
//	}
//	
//	public boolean deleteProduto(Produto produto){
//		
//		return true;
//	}
	
//	public ArrayList<Produto> listAll(){
//		ArrayList<Produto> lista = new ArrayList<Produto>();
//		
//		SoapObject listaProdutos = new SoapObject(NAMESPACE, LISTAR);
//		
//		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
//		
//		envelope.setOutputSoapObject(listaProdutos);
//		
//		envelope.implicitTypes = true;
//		
//		HttpTransportSE http = new HttpTransportSE(URL);
//		
//		try {
//			http.call("urn:" + LISTAR, envelope);
//			
//			@SuppressWarnings("unchecked")
//			Vector<SoapObject> resposta = (Vector<SoapObject>) envelope.getResponse();
//
//			for (SoapObject soapObject : resposta) {
//				Produto produto = new Produto();
//				produto.setId(Integer.parseInt(soapObject.getProperty("id").toString()));
//				produto.setCodigo(Long.parseLong(soapObject.getProperty("codigo").toString()));
//				produto.setNome((soapObject.getProperty("nome").toString()));
//				
//				lista.add(produto);
//			}
//			
//		} catch (HttpResponseException e) {
//			e.printStackTrace();
//			return null;
//		} catch (IOException e) {
//			e.printStackTrace();
//			return null;
//		} catch (XmlPullParserException e) {
//			e.printStackTrace();
//			return null;
//		}
//		
//		return lista;
//	}
	
	public Produto searchProdutoById(Integer produtoId){
		
		SoapObject searchByCode = new SoapObject(NAMESPACE, BUSCAR_POR_ID);
		searchByCode.addProperty("produtoId", produtoId);
		
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		
		envelope.setOutputSoapObject(searchByCode);
		
		envelope.implicitTypes = true;
		
		HttpTransportSE http = new HttpTransportSE(URL);
		
		Produto produto = null;
		
		try {
			http.call("urn:" + BUSCAR_POR_ID, envelope);
			
			SoapObject resposta = (SoapObject) envelope.getResponse();

			produto = new Produto();
			produto.setId(Integer.parseInt(resposta.getProperty("id").toString()));
			produto.setCodigo(Long.parseLong(resposta.getProperty("codigo").toString()));
			produto.setNome((resposta.getProperty("nome").toString()));
				
			
		} catch (HttpResponseException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} catch (XmlPullParserException e) {
			e.printStackTrace();
			return null;
		}
		
		return produto;
	}
	
	public Produto searchProdutoByCode(Long barCode){
		
		SoapObject searchByCode = new SoapObject(NAMESPACE, BUSCAR_POR_CODIGO);
		
		/**
		 * Essa property deve possuir o mesmo nome do parâmetro recebido no método pelo webservice
		 * Neste caso no web service o método searchByCode recebe um parâmetro Long com nome de "code", por isso o property é code
		 */
		searchByCode.addProperty("code", barCode);
		
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		
		envelope.setOutputSoapObject(searchByCode);
		
		envelope.implicitTypes = true;
		
		HttpTransportSE http = new HttpTransportSE(URL);
		
		Produto produto = null;
		
		try {
			http.call("urn:" + BUSCAR_POR_CODIGO, envelope);
			
			SoapObject resposta = (SoapObject) envelope.getResponse();
			
			/**
			 * Verifica se o produto existe no servidor, senão existir retorna nulo
			 */
			if(resposta == null){
				return null;
			}

			produto = new Produto();
			produto.setId(Integer.parseInt(resposta.getProperty("id").toString()));
			produto.setCodigo(Long.parseLong(resposta.getProperty("codigo").toString()));
			produto.setNome((resposta.getProperty("nome").toString()));
			
		} catch (HttpResponseException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
		} catch (XmlPullParserException e) {
			e.printStackTrace();
			return null;
		}
		
		return produto;
	}
	
	public Produto searchProdutoByCode(Long barCode, int timeToExpire) throws IOException{
		
		SoapObject searchByCode = new SoapObject(NAMESPACE, BUSCAR_POR_CODIGO);
		
		/**
		 * Essa property deve possuir o mesmo nome do parâmetro recebido no método pelo webservice
		 * Neste caso no web service o método searchByCode recebe um parâmetro Long com nome de "code", por isso o property é code
		 */
		searchByCode.addProperty("code", barCode);
		
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		
		envelope.setOutputSoapObject(searchByCode);
		
		envelope.implicitTypes = true;
		
		HttpTransportSE http = new HttpTransportSE(URL, timeToExpire);
		
		Produto produto = null;
		
		try {
			http.call("urn:" + BUSCAR_POR_CODIGO, envelope);
			
			SoapPrimitive resposta = (SoapPrimitive) envelope.getResponse();
			/**
			 * Verifica se o produto existe no servidor, se não existir retorna nulo
			 */
			if(resposta == null){
				return null;
			}

			String codeAndName = (String) resposta.getValue();
			if(codeAndName.contains(";")){
				produto = new Produto(Long.valueOf(codeAndName.split(";")[0]), codeAndName.split(";")[1]);
			}else{
				return null;
			}
			
		} catch (HttpResponseException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		} catch (XmlPullParserException e) {
			e.printStackTrace();
			return null;
		}
		
		return produto;
	}
	
//	public boolean deleteProduto(Integer produtoId){
//		return deleteProduto(new Produto(new Long(0), ""));
//	}
	

}
