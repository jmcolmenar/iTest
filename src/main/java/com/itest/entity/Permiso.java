package com.itest.entity;

import javax.persistence.*;


/**
 * The persistent class for the permisos database table.
 * 
 */
@Entity
@Table(name="permisos")
public class Permiso  {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique=true, nullable=false)
	private int idper;

	@Column(nullable=false, length=16)
	private String permiso;

	@Column(nullable=false, length=20)
	private String usuario;

	public Permiso() {
	}

	public int getIdper() {
		return this.idper;
	}

	public void setIdper(int idper) {
		this.idper = idper;
	}

	public String getPermiso() {
		return this.permiso;
	}

	public void setPermiso(String permiso) {
		this.permiso = permiso;
	}

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}
}