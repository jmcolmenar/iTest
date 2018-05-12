package com.itest.entity;

import javax.persistence.*;


/**
 * The persistent class for the exam_individ database table.
 * 
 */
@Entity
@Table(name="exam_individ")
public class ExamenIndividual  {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique=true, nullable=false)
	private int idexami;

	//bi-directional many-to-one association to Examen
	@ManyToOne
	@JoinColumn(name="examen", nullable=false)
	private Examen examen;

	//bi-directional many-to-one association to Usuario
	@ManyToOne
	@JoinColumn(name="alumno", nullable=false)
	private Usuario usuario;

	public ExamenIndividual() {
	}

	public int getIdexami() {
		return this.idexami;
	}

	public void setIdexami(int idexami) {
		this.idexami = idexami;
	}

	public Examen getExamen() {
		return this.examen;
	}

	public void setExamen(Examen examenes) {
		this.examen = examenes;
	}

	public Usuario getUsuario() {
		return this.usuario;
	}

	public void setUsuario(Usuario usuarios) {
		this.usuario = usuarios;
	}

}