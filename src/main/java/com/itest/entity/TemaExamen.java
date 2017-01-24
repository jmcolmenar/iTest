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
	@Column(unique=true, nullable=false)
	private int idtemaexam;

	@Column(name="dificultad_max", nullable=false)
	private byte dificultadMax;

	@Column(name="dificultad_min", nullable=false)
	private byte dificultadMin;

	@Column(name="n_pregs", nullable=false)
	private byte nPregs;

	@Column(name="n_resp_x_preg")
	private byte nRespXPreg;

	@Column(name="tipo_pregs", nullable=false)
	private int tipoPregs;

	//bi-directional many-to-one association to Examen
	@ManyToOne
	@JoinColumn(name="exam", nullable=false)
	private Examen examenes;

	//bi-directional many-to-one association to Tema
	@ManyToOne
	@JoinColumn(name="tema", nullable=false)
	private Tema temas;

	public TemaExamen() {
	}

	public int getIdtemaexam() {
		return this.idtemaexam;
	}

	public void setIdtemaexam(int idtemaexam) {
		this.idtemaexam = idtemaexam;
	}

	public byte getDificultadMax() {
		return this.dificultadMax;
	}

	public void setDificultadMax(byte dificultadMax) {
		this.dificultadMax = dificultadMax;
	}

	public byte getDificultadMin() {
		return this.dificultadMin;
	}

	public void setDificultadMin(byte dificultadMin) {
		this.dificultadMin = dificultadMin;
	}

	public byte getNPregs() {
		return this.nPregs;
	}

	public void setNPregs(byte nPregs) {
		this.nPregs = nPregs;
	}

	public byte getNRespXPreg() {
		return this.nRespXPreg;
	}

	public void setNRespXPreg(byte nRespXPreg) {
		this.nRespXPreg = nRespXPreg;
	}

	public int getTipoPregs() {
		return this.tipoPregs;
	}

	public void setTipoPregs(int tipoPregs) {
		this.tipoPregs = tipoPregs;
	}

	public Examen getExamenes() {
		return this.examenes;
	}

	public void setExamenes(Examen examenes) {
		this.examenes = examenes;
	}

	public Tema getTemas() {
		return this.temas;
	}

	public void setTemas(Tema temas) {
		this.temas = temas;
	}

}