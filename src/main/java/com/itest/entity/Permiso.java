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
	@Column(unique=true, nullable=false)
	private int idper;

	@Column(nullable=false, length=16)
	private String permiso;

	//bi-directional many-to-one association to Usuario
	@ManyToOne
	@JoinColumn(name="usuario", nullable=false)
	private Usuario usuarios;

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

	public Usuario getUsuarios() {
		return this.usuarios;
	}

	public void setUsuarios(Usuario usuarios) {
		this.usuarios = usuarios;
	}

}