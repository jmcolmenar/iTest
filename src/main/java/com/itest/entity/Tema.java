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
	@OneToMany(mappedBy= "tema")
	private List<Pregunta> preguntas;

	//bi-directional many-to-one association to Grupo
	@ManyToOne
	@JoinColumn(name="grupo", nullable=false)
	private Grupo grupo;

	//bi-directional many-to-one association to TemaExamen
	@OneToMany(mappedBy= "tema")
	private List<TemaExamen> temasExamenes;

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
		pregunta.setTema(this);

		return pregunta;
	}

	public Pregunta removePregunta(Pregunta pregunta) {
		getPreguntas().remove(pregunta);
		pregunta.setTema(null);

		return pregunta;
	}

	public Grupo getGrupo() {
		return this.grupo;
	}

	public void setGrupo(Grupo grupos) {
		this.grupo = grupos;
	}

	public List<TemaExamen> getTemasExamenes() {
		return this.temasExamenes;
	}

	public void setTemasExamenes(List<TemaExamen> temasExam) {
		this.temasExamenes = temasExam;
	}

	public TemaExamen addTemasExam(TemaExamen temasExam) {
		getTemasExamenes().add(temasExam);
		temasExam.setTema(this);

		return temasExam;
	}

	public TemaExamen removeTemasExam(TemaExamen temasExam) {
		getTemasExamenes().remove(temasExam);
		temasExam.setTema(null);

		return temasExam;
	}

}