package br.com.luckez.tcc.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "supermercado")
public class Supermercado implements Parcelable{

	@DatabaseField(generatedId=true)
	private Integer id;
	
	@DatabaseField
	private String nome;
		
	public Supermercado() {
		// TODO Auto-generated constructor stub
	}
	
	public Supermercado(String nome){
		this.nome = nome;
	}
	
	public Supermercado(Parcel parcel){
		this.id = parcel.readInt();
		this.nome = parcel.readString();
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
	
	public static final Parcelable.Creator<Supermercado> CREATOR = new Parcelable.Creator<Supermercado>(){

		@Override
		public Supermercado createFromParcel(Parcel source) {
			// TODO Auto-generated method stub
			return new Supermercado(source);
		}

		@Override
		public Supermercado[] newArray(int size) {
			// TODO Auto-generated method stub
			return new Supermercado[size];
		}
		
	};
}
