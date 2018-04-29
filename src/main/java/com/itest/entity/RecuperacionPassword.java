package com.itest.entity;

import javax.persistence.*;
import java.util.Date;


/**
 * The persistent class for the recupera_pass database table.
 * 
 */
@Entity
@Table(name="recupera_pass")
public class RecuperacionPassword  {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique=true, nullable=false)
	private int idrec;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="fecha_caducidad", nullable=false)
	private Date fechaCaducidad;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="fecha_cambio")
	private Date fechaCambio;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="fecha_insert", nullable=false)
	private Date fechaInsert;

	@Column(nullable=false)
	private int idusu;

	@Column(nullable=false, length=32)
	private String token;

	public RecuperacionPassword() {
	}

	public int getIdrec() {
		return this.idrec;
	}

	public void setIdrec(int idrec) {
		this.idrec = idrec;
	}

	public Date getFechaCaducidad() {
		return this.fechaCaducidad;
	}

	public void setFechaCaducidad(Date fechaCaducidad) {
		this.fechaCaducidad = fechaCaducidad;
	}

	public Date getFechaCambio() {
		return this.fechaCambio;
	}

	public void setFechaCambio(Date fechaCambio) {
		this.fechaCambio = fechaCambio;
	}

	public Date getFechaInsert() {
		return this.fechaInsert;
	}

	public void setFechaInsert(Date fechaInsert) {
		this.fechaInsert = fechaInsert;
	}

	public int getIdusu() {
		return this.idusu;
	}

	public void setIdusu(int idusu) {
		this.idusu = idusu;
	}

	public String getToken() {
		return this.token;
	}

	public void setToken(String token) {
		this.token = token;
	}

}