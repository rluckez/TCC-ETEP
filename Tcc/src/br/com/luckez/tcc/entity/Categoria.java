package br.com.luckez.tcc.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "categoria")
public class Categoria implements Parcelable{


	@DatabaseField(generatedId=true)
	private Integer id;
	
	@DatabaseField
	private String nome;
	
	public Categoria() {
		// TODO Auto-generated constructor stub
	}
	
	public Categoria(Parcel parcel){
		this.id = parcel.readInt();
		this.nome = parcel.readString();
	}
	
	public Categoria(String nome){
		this.nome = nome;
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

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(id);
		dest.writeString(nome);
	}
	
	public static final Parcelable.Creator<Categoria> CREATOR = new Parcelable.Creator<Categoria>(){

		@Override
		public Categoria createFromParcel(Parcel source) {
			// TODO Auto-generated method stub
			return new Categoria(source);
		}

		@Override
		public Categoria[] newArray(int size) {
			// TODO Auto-generated method stub
			return new Categoria[size];
		}
		
	};
}
