package com.itest.entity;

import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the grupos database table.
 * 
 */
@Entity
@Table(name="grupos")
public class Grupo  {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique=true, nullable=false)
	private int idgrupo;

	@Column(nullable=false, length=9)
	private String anio;

	@Column(nullable=false, length=4)
	private String grupo;

	@Column(name="tipo_alumno")
	private int tipoAlumno;

	//bi-directional many-to-one association to Examen
	@OneToMany(mappedBy= "grupo")
	private List<Examen> examenes;

	//bi-directional many-to-one association to Asignatura
	@ManyToOne
	@JoinColumn(name="asig", nullable=false)
	private Asignatura asignatura;

	//bi-directional many-to-one association to Centro
	@ManyToOne
	@JoinColumn(name="centro", nullable=false)
	private Centro centro;

	//bi-directional many-to-one association to Imparten
	@OneToMany(mappedBy= "grupo")
	private List<Imparten> imparten;

	//bi-directional many-to-one association to Matricula
	@OneToMany(mappedBy= "grupo")
	private List<Matricula> matriculas;

	//bi-directional many-to-one association to Pregunta
	@OneToMany(mappedBy= "grupo")
	private List<Pregunta> preguntas;

	//bi-directional many-to-one association to Tema
	@OneToMany(mappedBy= "grupo")
	private List<Tema> temas;

	public Grupo() {
	}

	public int getIdgrupo() {
		return this.idgrupo;
	}

	public void setIdgrupo(int idgrupo) {
		this.idgrupo = idgrupo;
	}

	public String getAnio() {
		return this.anio;
	}

	public void setAnio(String anio) {
		this.anio = anio;
	}

	public String getGrupo() {
		return this.grupo;
	}

	public void setGrupo(String grupo) {
		this.grupo = grupo;
	}

	public int getTipoAlumno() {
		return this.tipoAlumno;
	}

	public void setTipoAlumno(int tipoAlumno) {
		this.tipoAlumno = tipoAlumno;
	}

	public List<Examen> getExamenes() {
		return this.examenes;
	}

	public void setExamenes(List<Examen> examenes) {
		this.examenes = examenes;
	}

	public Examen addExamene(Examen examene) {
		getExamenes().add(examene);
		examene.setGrupo(this);

		return examene;
	}

	public Examen removeExamene(Examen examene) {
		getExamenes().remove(examene);
		examene.setGrupo(null);

		return examene;
	}

	public Asignatura getAsignatura() {
		return this.asignatura;
	}

	public void setAsignatura(Asignatura asignaturas) {
		this.asignatura = asignaturas;
	}

	public Centro getCentro() {
		return this.centro;
	}

	public void setCentro(Centro centros) {
		this.centro = centros;
	}

	public List<Imparten> getImparten() {
		return this.imparten;
	}

	public void setImparten(List<Imparten> imparten) {
		this.imparten = imparten;
	}

	public Imparten addImparten(Imparten imparten) {
		getImparten().add(imparten);
		imparten.setGrupo(this);

		return imparten;
	}

	public Imparten removeImparten(Imparten imparten) {
		getImparten().remove(imparten);
		imparten.setGrupo(null);

		return imparten;
	}

	public List<Matricula> getMatriculas() {
		return this.matriculas;
	}

	public void setMatriculas(List<Matricula> matriculas) {
		this.matriculas = matriculas;
	}

	public Matricula addMatricula(Matricula matricula) {
		getMatriculas().add(matricula);
		matricula.setGrupo(this);

		return matricula;
	}

	public Matricula removeMatricula(Matricula matricula) {
		getMatriculas().remove(matricula);
		matricula.setGrupo(null);

		return matricula;
	}

	public List<Pregunta> getPreguntas() {
		return this.preguntas;
	}

	public void setPreguntas(List<Pregunta> preguntas) {
		this.preguntas = preguntas;
	}

	public Pregunta addPregunta(Pregunta pregunta) {
		getPreguntas().add(pregunta);
		pregunta.setGrupo(this);

		return pregunta;
	}

	public Pregunta removePregunta(Pregunta pregunta) {
		getPreguntas().remove(pregunta);
		pregunta.setGrupo(null);

		return pregunta;
	}

	public List<Tema> getTemas() {
		return this.temas;
	}

	public void setTemas(List<Tema> temas) {
		this.temas = temas;
	}

	public Tema addTema(Tema tema) {
		getTemas().add(tema);
		tema.setGrupo(this);

		return tema;
	}

	public Tema removeTema(Tema tema) {
		getTemas().remove(tema);
		tema.setGrupo(null);

		return tema;
	}

}