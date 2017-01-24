package com.itest.entity;

import javax.persistence.*;


/**
 * The persistent class for the centros_estudios database table.
 * 
 */
@Entity
@Table(name="centros_estudios")
public class CentroEstudio  {

	@Id
	@Column(unique=true, nullable=false)
	private int idcen;

	@Column(nullable=false)
	private int centro;

	@Column(nullable=false, length=40)
	private String estudio;

	public CentroEstudio() {
	}

	public int getIdcen() {
		return this.idcen;
	}

	public void setIdcen(int idcen) {
		this.idcen = idcen;
	}

	public int getCentro() {
		return this.centro;
	}

	public void setCentro(int centro) {
		this.centro = centro;
	}

	public String getEstudio() {
		return this.estudio;
	}

	public void setEstudio(String estudio) {
		this.estudio = estudio;
	}

}