package com.itest.entity;

import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the temas database table.
 * 
 */
@Entity
@Table(name="temas")
public class Tema  {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique=true, nullable=false)
	private int idtema;

	@Column(nullable=false)
	private int orden;

	@Column(nullable=false, length=60)
	private String tema;

	//bi-directional many-to-one association to Pregunta
	@OneToMany(mappedBy="temas")
	private List<Pregunta> preguntas;

	//bi-directional many-to-one association to Grupo
	@ManyToOne
	@JoinColumn(name="grupo", nullable=false)
	private Grupo grupos;

	//bi-directional many-to-one association to TemaExamen
	@OneToMany(mappedBy="temas")
	private List<TemaExamen> temasExam;

	public Tema() {
	}

	public int getIdtema() {
		return this.idtema;
	}

	public void setIdtema(int idtema) {
		this.idtema = idtema;
	}

	public int getOrden() {
		return this.orden;
	}

	public void setOrden(int orden) {
		this.orden = orden;
	}

	public String getTema() {
		return this.tema;
	}

	public void setTema(String tema) {
		this.tema = tema;
	}

	public List<Pregunta> getPreguntas() {
		return this.preguntas;
	}

	public void setPreguntas(List<Pregunta> preguntas) {
		this.preguntas = preguntas;
	}

	public Pregunta addPregunta(Pregunta pregunta) {
		getPreguntas().add(pregunta);
		pregunta.setTemas(this);

		return pregunta;
	}

	public Pregunta removePregunta(Pregunta pregunta) {
		getPreguntas().remove(pregunta);
		pregunta.setTemas(null);

		return pregunta;
	}

	public Grupo getGrupos() {
		return this.grupos;
	}

	public void setGrupos(Grupo grupos) {
		this.grupos = grupos;
	}

	public List<TemaExamen> getTemasExam() {
		return this.temasExam;
	}

	public void setTemasExam(List<TemaExamen> temasExam) {
		this.temasExam = temasExam;
	}

	public TemaExamen addTemasExam(TemaExamen temasExam) {
		getTemasExam().add(temasExam);
		temasExam.setTemas(this);

		return temasExam;
	}

	public TemaExamen removeTemasExam(TemaExamen temasExam) {
		getTemasExam().remove(temasExam);
		temasExam.setTemas(null);

		return temasExam;
	}

}