package br.com.luckez.tcc.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "produto")
public class Produto implements Parcelable{

	@DatabaseField(generatedId = true)
	private Integer id;
	
	@DatabaseField
	private Long codigo;
	
	@DatabaseField
	private String nome;
	
	@DatabaseField
	private String descricao;
	
	@DatabaseField(foreign = true, foreignAutoRefresh = true, canBeNull = true)
	private Categoria categoria;
	
	public Produto() {
		// TODO Auto-generated constructor stub
	}
	
	public Produto(Long codigo, String nome){
		this.codigo = codigo;
		this.nome = nome;
	}
	
	public Produto(Long codigo, String nome, Categoria categoria){
		this.codigo = codigo;
		this.nome = nome;
		this.categoria = categoria;
	}
	
	public Produto(Parcel parcel){
		this.id = parcel.readInt();
		this.codigo = parcel.readLong();
		this.nome = parcel.readString();
		this.categoria = (Categoria) parcel.readValue(Categoria.class.getClassLoader());
	}

	@Override
	public String toString(){
		return this.nome;
	}
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public Categoria getCategoria() {
		return categoria;
	}

	public void setCategoria(Categoria categoria) {
		this.categoria = categoria;
	}

	public Long getCodigo() {
		return codigo;
	}

	public void setCodigo(Long codigo) {
		this.codigo = codigo;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(id);
		dest.writeLong(codigo);
		dest.writeString(nome);
		dest.writeValue(categoria);
	}
	
	public static final Parcelable.Creator<Produto> CREATOR = new Parcelable.Creator<Produto>(){

		@Override
		public Produto createFromParcel(Parcel source) {
			// TODO Auto-generated method stub
			return new Produto(source);
		}

		@Override
		public Produto[] newArray(int size) {
			// TODO Auto-generated method stub
			return new Produto[size];
		}
	};
	
}
