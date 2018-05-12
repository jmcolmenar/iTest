package com.itest.entity;

import javax.persistence.*;


/**
 * The persistent class for the temas_exam database table.
 * 
 */
@Entity
@Table(name="temas_exam")
public class TemaExamen  {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique=true, nullable=false)
	private int idtemaexam;

	@Column(name="dificultad_max", nullable=false)
	private int dificultadMax;

	@Column(name="dificultad_min", nullable=false)
	private int dificultadMin;

	@Column(name="n_pregs", nullable=false)
	private int nPregs;

	@Column(name="n_resp_x_preg")
	private int nRespXPreg;

	@Column(name="tipo_pregs", nullable=false)
	private int tipoPregs;

	//bi-directional many-to-one association to Examen
	@ManyToOne
	@JoinColumn(name="exam", nullable=false)
	private Examen examen;

	//bi-directional many-to-one association to Tema
	@ManyToOne
	@JoinColumn(name="tema", nullable=false)
	private Tema tema;

	public TemaExamen() {
	}

	public int getIdtemaexam() {
		return this.idtemaexam;
	}

	public void setIdtemaexam(int idtemaexam) {
		this.idtemaexam = idtemaexam;
	}

	public int getDificultadMax() {
		return this.dificultadMax;
	}

	public void setDificultadMax(int dificultadMax) {
		this.dificultadMax = dificultadMax;
	}

	public int getDificultadMin() {
		return this.dificultadMin;
	}

	public void setDificultadMin(int dificultadMin) {
		this.dificultadMin = dificultadMin;
	}

	public int getNPregs() {
		return this.nPregs;
	}

	public void setNPregs(int nPregs) {
		this.nPregs = nPregs;
	}

	public int getNRespXPreg() {
		return this.nRespXPreg;
	}

	public void setNRespXPreg(int nRespXPreg) {
		this.nRespXPreg = nRespXPreg;
	}

	public int getTipoPregs() {
		return this.tipoPregs;
	}

	public void setTipoPregs(int tipoPregs) {
		this.tipoPregs = tipoPregs;
	}

	public Examen getExamen() {
		return this.examen;
	}

	public void setExamen(Examen examenes) {
		this.examen = examenes;
	}

	public Tema getTema() {
		return this.tema;
	}

	public void setTema(Tema temas) {
		this.tema = temas;
	}

}