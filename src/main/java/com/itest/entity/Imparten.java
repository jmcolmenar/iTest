package com.itest.entity;

import javax.persistence.*;


/**
 * The persistent class for the imparten database table.
 * 
 */
@Entity
@Table(name="imparten")
public class Imparten  {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique=true, nullable=false)
	private int idimp;

	//bi-directional many-to-one association to Grupo
	@ManyToOne
	@JoinColumn(name="grupo", nullable=false)
	private Grupo grupos;

	//bi-directional many-to-one association to Usuario
	@ManyToOne
	@JoinColumn(name="profe", nullable=false)
	private Usuario usuarios;

	public Imparten() {
	}

	public int getIdimp() {
		return this.idimp;
	}

	public void setIdimp(int idimp) {
		this.idimp = idimp;
	}

	public Grupo getGrupos() {
		return this.grupos;
	}

	public void setGrupos(Grupo grupos) {
		this.grupos = grupos;
	}

	public Usuario getUsuarios() {
		return this.usuarios;
	}

	public void setUsuarios(Usuario usuarios) {
		this.usuarios = usuarios;
	}

}