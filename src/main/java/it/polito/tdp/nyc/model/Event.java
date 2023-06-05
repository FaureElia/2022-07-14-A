package it.polito.tdp.nyc.model;

import java.util.Objects;

public class Event implements Comparable<Event> {
	
	public enum Type{
		pubblicazione,rimozione;
	}
	
	
	
	private int giorno;
	private Type tipo;
	private NTA nta;
	private int durata;
	
	
	public Event(int giorno, Type tipo, NTA nta,int durata) {
		super();
		this.giorno = giorno;
		this.tipo = tipo;
		this.nta=nta;
		this.durata=durata;
	}

	

	public int getDurata() {
		return durata;
	}



	public void setDurata(int durata) {
		this.durata = durata;
	}



	public NTA getNta() {
		return nta;
	}


	public void setNta(NTA nta) {
		this.nta = nta;
	}


	public int getGiorno() {
		return giorno;
	}


	public void setGiorno(int giorno) {
		this.giorno = giorno;
	}


	public Type getTipo() {
		return tipo;
	}


	public void setTipo(Type tipo) {
		this.tipo = tipo;
	}

	
	

	@Override
	public int hashCode() {
		return Objects.hash(giorno, tipo);
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Event other = (Event) obj;
		return giorno == other.giorno && tipo == other.tipo;
	}


	@Override
	public int compareTo(Event o) {
		
		return this.giorno-o.giorno;
	}
	
	
	
	

}
