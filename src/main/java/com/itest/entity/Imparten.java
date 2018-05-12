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
	private Grupo grupo;

	//bi-directional many-to-one association to Usuario
	@ManyToOne
	@JoinColumn(name="profe", nullable=false)
	private Usuario usuario;

	public Imparten() {
	}

	public int getIdimp() {
		return this.idimp;
	}

	public void setIdimp(int idimp) {
		this.idimp = idimp;
	}

	public Grupo getGrupo() {
		return this.grupo;
	}

	public void setGrupo(Grupo grupos) {
		this.grupo = grupos;
	}

	public Usuario getUsuario() {
		return this.usuario;
	}

	public void setUsuario(Usuario usuarios) {
		this.usuario = usuarios;
	}

}