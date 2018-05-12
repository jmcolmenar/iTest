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
	private Grupo grupo;

	//bi-directional many-to-one association to Usuario
	@ManyToOne
	@JoinColumn(name="alumno", nullable=false)
	private Usuario usuario;

	public Matricula() {
	}

	public int getIdmat() {
		return this.idmat;
	}

	public void setIdmat(int idmat) {
		this.idmat = idmat;
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