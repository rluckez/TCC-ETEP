package br.com.luckez.tcc.entity;

import java.util.Calendar;
import java.util.Date;

import android.os.Parcel;
import android.os.Parcelable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "compra")
public class Compra implements Parcelable{

	@DatabaseField(generatedId = true)
	private Integer id;
	
	@DatabaseField
	private Date dataHora;
	
	@DatabaseField(foreign = true, foreignAutoRefresh = true, canBeNull = false)
	private Supermercado supermercado;
	
	@DatabaseField
	private Integer numeroDeProdutos;
	
	@DatabaseField
	private Double valor;
	
	public Compra() {
		// TODO Auto-generated constructor stub
	}
	
	public Compra(Parcel parcel){
		this.id = parcel.readInt();
		this.dataHora = new Date(parcel.readLong());
		this.supermercado = (Supermercado) parcel.readValue(Supermercado.class.getClassLoader());
		this.numeroDeProdutos = parcel.readInt(); 
		this.valor = parcel.readDouble();
	}
	
	public Compra(Supermercado supermercado){
		this.setDataHora(Calendar.getInstance().getTime());
		this.supermercado = supermercado;
	}
	
	public Compra(Supermercado supermercado, Integer numerodeProdutos, Double valor){
		this.setDataHora(Calendar.getInstance().getTime());
		this.supermercado = supermercado;
		this.numeroDeProdutos = numerodeProdutos;
		this.valor = valor;
	}
	
	@Override
	public String toString(){
		return "Compra em: " + this.supermercado.getNome() + " em " + this.dataHora.getTime();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Date getDataHora() {
		return dataHora;
	}

	public void setDataHora(Date dataHora) {
		this.dataHora = dataHora;
	}

	public Supermercado getSupermercado() {
		return supermercado;
	}

	public void setSupermercado(Supermercado supermercado) {
		this.supermercado = supermercado;
	}
	
	public Integer getNumeroDeProdutos() {
		return numeroDeProdutos;
	}

	public void setNumeroDeProdutos(Integer numeroDeProdutos) {
		this.numeroDeProdutos = numeroDeProdutos;
	}

	public Double getValor() {
		return valor;
	}

	public void setValor(Double valor) {
		this.valor = valor;
	}
	
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(id);
		dest.writeLong(dataHora.getTime());
		dest.writeValue(supermercado);
		dest.writeInt(numeroDeProdutos);
		dest.writeDouble(valor);
	}
	
	public static final Parcelable.Creator<Compra> CREATOR = new Parcelable.Creator<Compra>(){

		@Override
		public Compra createFromParcel(Parcel source) {
			// TODO Auto-generated method stub
			return new Compra(source);
		}

		@Override
		public Compra[] newArray(int size) {
			// TODO Auto-generated method stub
			return new Compra[size];
		}
		
	};
	
}
