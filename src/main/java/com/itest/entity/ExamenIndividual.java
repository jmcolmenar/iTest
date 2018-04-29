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
	private Examen examenes;

	//bi-directional many-to-one association to Usuario
	@ManyToOne
	@JoinColumn(name="alumno", nullable=false)
	private Usuario usuarios;

	public ExamenIndividual() {
	}

	public int getIdexami() {
		return this.idexami;
	}

	public void setIdexami(int idexami) {
		this.idexami = idexami;
	}

	public Examen getExamenes() {
		return this.examenes;
	}

	public void setExamenes(Examen examenes) {
		this.examenes = examenes;
	}

	public Usuario getUsuarios() {
		return this.usuarios;
	}

	public void setUsuarios(Usuario usuarios) {
		this.usuarios = usuarios;
	}

}