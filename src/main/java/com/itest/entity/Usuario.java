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
	@GeneratedValue(strategy = GenerationType.IDENTITY)
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
	@OneToMany(mappedBy= "usuario")
	private List<Calificacion> calificaciones;

	//bi-directional many-to-one association to ExamenIndividual
	@OneToMany(mappedBy= "usuario")
	private List<ExamenIndividual> examenesIndividuales;

	//bi-directional many-to-one association to Imparten
	@OneToMany(mappedBy= "usuario")
	private List<Imparten> imparten;

	//bi-directional many-to-one association to LogExamen
	@OneToMany(mappedBy= "usuario")
	private List<LogExamen> logExamenes;

	//bi-directional many-to-one association to Matricula
	@OneToMany(mappedBy= "usuario")
	private List<Matricula> matriculas;

	//bi-directional many-to-one association to Centro
	@ManyToOne
	@JoinColumn(name="centro", nullable=false)
	private Centro centro;

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

	public List<Calificacion> getCalificaciones() {
		return this.calificaciones;
	}

	public void setCalificaciones(List<Calificacion> califs) {
		this.calificaciones = califs;
	}

	public Calificacion addCalif(Calificacion calif) {
		getCalificaciones().add(calif);
		calif.setUsuario(this);

		return calif;
	}

	public Calificacion removeCalif(Calificacion calif) {
		getCalificaciones().remove(calif);
		calif.setUsuario(null);

		return calif;
	}

	public List<ExamenIndividual> getExamenesIndividuales() {
		return this.examenesIndividuales;
	}

	public void setExamenesIndividuales(List<ExamenIndividual> examIndivids) {
		this.examenesIndividuales = examIndivids;
	}

	public ExamenIndividual addExamIndivid(ExamenIndividual examIndivid) {
		getExamenesIndividuales().add(examIndivid);
		examIndivid.setUsuario(this);

		return examIndivid;
	}

	public ExamenIndividual removeExamIndivid(ExamenIndividual examIndivid) {
		getExamenesIndividuales().remove(examIndivid);
		examIndivid.setUsuario(null);

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
		imparten.setUsuario(this);

		return imparten;
	}

	public Imparten removeImparten(Imparten imparten) {
		getImparten().remove(imparten);
		imparten.setUsuario(null);

		return imparten;
	}

	public List<LogExamen> getLogExamenes() {
		return this.logExamenes;
	}

	public void setLogExamenes(List<LogExamen> logExams) {
		this.logExamenes = logExams;
	}

	public LogExamen addLogExam(LogExamen logExam) {
		getLogExamenes().add(logExam);
		logExam.setUsuario(this);

		return logExam;
	}

	public LogExamen removeLogExam(LogExamen logExam) {
		getLogExamenes().remove(logExam);
		logExam.setUsuario(null);

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
		matricula.setUsuario(this);

		return matricula;
	}

	public Matricula removeMatricula(Matricula matricula) {
		getMatriculas().remove(matricula);
		matricula.setUsuario(null);

		return matricula;
	}

	public Centro getCentro() {
		return this.centro;
	}

	public void setCentro(Centro centros) {
		this.centro = centros;
	}

}