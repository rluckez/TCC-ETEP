package br.com.luckez.tcc.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "unidade")
public class Unidade implements Parcelable{


	@DatabaseField(generatedId=true)
	private Integer id;
	
	@DatabaseField(columnName = "name")
	private String name;
	
	@DatabaseField(columnName = "code")
	private String code;
	
	public Unidade() {
		// TODO Auto-generated constructor stub
	}
	
	public Unidade(Parcel parcel){
		this.id = parcel.readInt();
		this.setName(parcel.readString());
		this.setCode(parcel.readString());
	}
	
	public Unidade(String name, String code){
		this.setName(name);
		this.setCode(code);
	}
	
	@Override
	public String toString(){
		return this.getName() + " (" + this.getCode() + ")";
	}
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(id);
		dest.writeString(name);
		dest.writeString(code);
	}

	public static final Parcelable.Creator<Unidade> CREATOR = new Parcelable.Creator<Unidade>(){

		@Override
		public Unidade createFromParcel(Parcel source) {
			// TODO Auto-generated method stub
			return new Unidade(source);
		}

		@Override
		public Unidade[] newArray(int size) {
			// TODO Auto-generated method stub
			return new Unidade[size];
		}
		
	};
}
