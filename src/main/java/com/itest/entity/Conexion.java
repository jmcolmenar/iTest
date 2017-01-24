package com.itest.entity;

import javax.persistence.*;
import java.sql.Timestamp;


/**
 * The persistent class for the conexiones database table.
 * 
 */
@Entity
@Table(name="conexiones")
public class Conexion  {

	@Id
	@Column(unique=true, nullable=false)
	private int id;

	@Column(nullable=false)
	private Timestamp fecha;

	@Column(nullable=false)
	private int idusuario;

	@Column(length=15)
	private String ip;

	public Conexion() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Timestamp getFecha() {
		return this.fecha;
	}

	public void setFecha(Timestamp fecha) {
		this.fecha = fecha;
	}

	public int getIdusuario() {
		return this.idusuario;
	}

	public void setIdusuario(int idusuario) {
		this.idusuario = idusuario;
	}

	public String getIp() {
		return this.ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

}