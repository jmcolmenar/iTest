package com.itest.entity;

import javax.persistence.*;


/**
 * The persistent class for the matriculas database table.
 * 
 */
@Entity
@Table(name="matriculas")
public class Matricula  {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique=true, nullable=false)
	private int idmat;

	//bi-directional many-to-one association to Grupo
	@ManyToOne
	@JoinColumn(name="grupo", nullable=false)
	private Grupo grupos;

	//bi-directional many-to-one association to Usuario
	@ManyToOne
	@JoinColumn(name="alumno", nullable=false)
	private Usuario usuarios;

	public Matricula() {
	}

	public int getIdmat() {
		return this.idmat;
	}

	public void setIdmat(int idmat) {
		this.idmat = idmat;
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