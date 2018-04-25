package com.itest.entity;

import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the usuarios database table.
 * 
 */
@Entity
@Table(name="usuarios")
public class Usuario  {

	@Id
	@Column(unique=true, nullable=false)
	private int idusu;

	@Column(nullable=false, length=50)
	private String apes;

	@Column(nullable=false, length=9)
	private String dni;

	@Column(length=40)
	private String email;

	@Column(nullable=false, length=20)
	private String nombre;

	@Column(length=32)
	private String passw;

	@Column(name="ruta_foto", length=30)
	private String rutaFoto;

	@Column(unique=true, nullable=false, length=20)
	private String usuario;

	@Column(nullable=false)
	private int idioma;

	//bi-directional many-to-one association to Calificacion
	@OneToMany(mappedBy="usuarios")
	private List<Calificacion> califs;

	//bi-directional many-to-one association to ExamenIndividual
	@OneToMany(mappedBy="usuarios")
	private List<ExamenIndividual> examIndivids;

	//bi-directional many-to-one association to Imparten
	@OneToMany(mappedBy="usuarios")
	private List<Imparten> imparten;

	//bi-directional many-to-one association to LogExamen
	@OneToMany(mappedBy="usuarios")
	private List<LogExamen> logExams;

	//bi-directional many-to-one association to Matricula
	@OneToMany(mappedBy="usuarios")
	private List<Matricula> matriculas;

	//bi-directional many-to-one association to Centro
	@ManyToOne
	@JoinColumn(name="centro", nullable=false)
	private Centro centros;

	public Usuario() {
	}

	public int getIdusu() {
		return this.idusu;
	}

	public void setIdusu(int idusu) {
		this.idusu = idusu;
	}

	public String getApes() {
		return this.apes;
	}

	public void setApes(String apes) {
		this.apes = apes;
	}

	public String getDni() {
		return this.dni;
	}

	public void setDni(String dni) {
		this.dni = dni;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getNombre() {
		return this.nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getPassw() {
		return this.passw;
	}

	public void setPassw(String passw) {
		this.passw = passw;
	}

	public String getRutaFoto() {
		return this.rutaFoto;
	}

	public void setRutaFoto(String rutaFoto) {
		this.rutaFoto = rutaFoto;
	}

	public String getUsuario() {
		return this.usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public int getIdioma() {
		return idioma;
	}

	public void setIdioma(int idioma) {
		this.idioma = idioma;
	}

	public List<Calificacion> getCalifs() {
		return this.califs;
	}

	public void setCalifs(List<Calificacion> califs) {
		this.califs = califs;
	}

	public Calificacion addCalif(Calificacion calif) {
		getCalifs().add(calif);
		calif.setUsuarios(this);

		return calif;
	}

	public Calificacion removeCalif(Calificacion calif) {
		getCalifs().remove(calif);
		calif.setUsuarios(null);

		return calif;
	}

	public List<ExamenIndividual> getExamIndivids() {
		return this.examIndivids;
	}

	public void setExamIndivids(List<ExamenIndividual> examIndivids) {
		this.examIndivids = examIndivids;
	}

	public ExamenIndividual addExamIndivid(ExamenIndividual examIndivid) {
		getExamIndivids().add(examIndivid);
		examIndivid.setUsuarios(this);

		return examIndivid;
	}

	public ExamenIndividual removeExamIndivid(ExamenIndividual examIndivid) {
		getExamIndivids().remove(examIndivid);
		examIndivid.setUsuarios(null);

		return examIndivid;
	}

	public List<Imparten> getImparten() {
		return this.imparten;
	}

	public void setImparten(List<Imparten> imparten) {
		this.imparten = imparten;
	}

	public Imparten addImparten(Imparten imparten) {
		getImparten().add(imparten);
		imparten.setUsuarios(this);

		return imparten;
	}

	public Imparten removeImparten(Imparten imparten) {
		getImparten().remove(imparten);
		imparten.setUsuarios(null);

		return imparten;
	}

	public List<LogExamen> getLogExams() {
		return this.logExams;
	}

	public void setLogExams(List<LogExamen> logExams) {
		this.logExams = logExams;
	}

	public LogExamen addLogExam(LogExamen logExam) {
		getLogExams().add(logExam);
		logExam.setUsuarios(this);

		return logExam;
	}

	public LogExamen removeLogExam(LogExamen logExam) {
		getLogExams().remove(logExam);
		logExam.setUsuarios(null);

		return logExam;
	}

	public List<Matricula> getMatriculas() {
		return this.matriculas;
	}

	public void setMatriculas(List<Matricula> matriculas) {
		this.matriculas = matriculas;
	}

	public Matricula addMatricula(Matricula matricula) {
		getMatriculas().add(matricula);
		matricula.setUsuarios(this);

		return matricula;
	}

	public Matricula removeMatricula(Matricula matricula) {
		getMatriculas().remove(matricula);
		matricula.setUsuarios(null);

		return matricula;
	}

	public Centro getCentros() {
		return this.centros;
	}

	public void setCentros(Centro centros) {
		this.centros = centros;
	}

}