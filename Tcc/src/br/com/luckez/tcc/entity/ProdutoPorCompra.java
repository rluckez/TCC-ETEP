package br.com.luckez.tcc.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "produto_por_compra")
public class ProdutoPorCompra implements Parcelable{

	@DatabaseField(generatedId = true)
	private Integer id;
	
	@DatabaseField(foreign = true, foreignAutoRefresh = true)
	private Compra compra;
	
	@DatabaseField(foreign = true, foreignAutoRefresh = true)
	private Produto produto;
	
	@DatabaseField(foreign = true, foreignAutoRefresh = true)
	private Unidade unidade;
	
	@DatabaseField
	private Double quantidade;
	
	@DatabaseField(columnName = "preco_produto")
	private Double precoProduto;	
	
	public ProdutoPorCompra() {
		// TODO Auto-generated constructor stub
	}

	public ProdutoPorCompra(Compra compra, Produto produto, Double quantidade, Double precoProduto){
		this.compra = compra;
		this.produto = produto;
		this.quantidade = quantidade;
		this.precoProduto = precoProduto;
	}
	
	public ProdutoPorCompra(Parcel parcel){
		this.id = parcel.readInt();
		this.compra = (Compra) parcel.readValue(Compra.class.getClassLoader());
		this.produto = (Produto) parcel.readValue(Produto.class.getClassLoader());
		this.quantidade = parcel.readDouble();
		this.precoProduto = parcel.readDouble();
	}
	
	@Override
	public String toString(){
		return "Id: " + this.id + ", Compra: " + this.compra.getId() + " Produto: " + this.produto.getNome();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Compra getCompra() {
		return compra;
	}

	public void setCompra(Compra compra) {
		this.compra = compra;
	}

	public Produto getProduto() {
		return produto;
	}

	public void setProduto(Produto produto) {
		this.produto = produto;
	}

	public Double getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(Double quantidade) {
		this.quantidade = quantidade;
	}

	public Double getPrecoProduto() {
		return precoProduto;
	}

	public void setPrecoProduto(Double precoProduto) {
		this.precoProduto = precoProduto;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(id);
		dest.writeValue(compra);
		dest.writeValue(produto);
		dest.writeDouble(quantidade);
		dest.writeDouble(precoProduto);
	}
	
	public Unidade getUnidade() {
		return unidade;
	}

	public void setUnidade(Unidade unidade) {
		this.unidade = unidade;
	}

	public static final Parcelable.Creator<ProdutoPorCompra> CREATOR = new Parcelable.Creator<ProdutoPorCompra>(){

		@Override
		public ProdutoPorCompra createFromParcel(Parcel source) {
			// TODO Auto-generated method stub
			return new ProdutoPorCompra(source);
		}

		@Override
		public ProdutoPorCompra[] newArray(int size) {
			// TODO Auto-generated method stub
			return new ProdutoPorCompra[size];
		}
		
	};
}
