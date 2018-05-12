package com.itest.entity;

import javax.persistence.*;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the examenes database table.
 * 
 */
@Entity
@Table(name="examenes")
public class Examen  {

	@Id
	@Column(unique=true, nullable=false)
	private int idexam;

	@Column(name="corr_parcial", nullable=false)
	private int corrParcial;

	@Column(name="cota_calif_preg", nullable=false)
	private double cotaCalifPreg;

	@Column(name="distrib_pregs", nullable=false)
	private int distribPregs;

	@Column(nullable=false)
	private int duracion;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="fecha_fin")
	private Date fechaFin;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="fecha_fin_rev", nullable=false)
	private Date fechaFinRev;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="fecha_ini")
	private Date fechaIni;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="fecha_ini_rev", nullable=false)
	private Date fechaIniRev;

	@Column(name="muestra_num_corr", nullable=false)
	private int muestraNumCorr;

	@Column(name = "nivelconfianza", nullable=false)
	private int nivelConfianza;

	@Column(name="nota_max", nullable=false)
	private float notaMax;

	@Column(name="p_nivel_confianza", nullable=false)
	private double pNivelConfianza;

	@Column(name="p_preg_fallada", nullable=false)
	private double pPregFallada;

	@Column(name="p_preg_no_resp", nullable=false)
	private double pPregNoResp;

	@Column(name="p_resp_fallada", nullable=false)
	private double pRespFallada;

	private int personalizado;

	@Column(name="peso_exam")
	private int pesoExam;

	@Column(nullable=false)
	private int publicado;

	@Column(name="r_nivel_confianza", nullable=false)
	private double rNivelConfianza;

	@Column(name="rev_activa", nullable=false)
	private int revActiva;

	@Column(length=60)
	private String titulo;

	@Column(nullable=false)
	private int visibilidad;

	//bi-directional many-to-one association to Calificacion
	@OneToMany(mappedBy= "examen")
	private List<Calificacion> calificaciones;

	//bi-directional many-to-one association to ExamenIndividual
	@OneToMany(mappedBy= "examen")
	private List<ExamenIndividual> examenesIndividuales;

	//bi-directional many-to-one association to Grupo
	@ManyToOne
	@JoinColumn(name="grupo", nullable=false)
	private Grupo grupo;

	//bi-directional many-to-one association to LogExamen
	@OneToMany(mappedBy= "examen")
	private List<LogExamen> logExamenes;

	//bi-directional many-to-one association to TemaExamen
	@OneToMany(mappedBy= "examen")
	private List<TemaExamen> temasExamenes;

	public Examen() {
	}

	public int getIdexam() {
		return this.idexam;
	}

	public void setIdexam(int idexam) {
		this.idexam = idexam;
	}

	public int getCorrParcial() {
		return this.corrParcial;
	}

	public void setCorrParcial(int corrParcial) {
		this.corrParcial = corrParcial;
	}

	public double getCotaCalifPreg() {
		return this.cotaCalifPreg;
	}

	public void setCotaCalifPreg(double cotaCalifPreg) {
		this.cotaCalifPreg = cotaCalifPreg;
	}

	public int getDistribPregs() {
		return this.distribPregs;
	}

	public void setDistribPregs(int distribPregs) {
		this.distribPregs = distribPregs;
	}

	public int getDuracion() {
		return this.duracion;
	}

	public void setDuracion(int duracion) {
		this.duracion = duracion;
	}

	public Date getFechaFin() {
		return this.fechaFin;
	}

	public void setFechaFin(Date fechaFin) {
		this.fechaFin = fechaFin;
	}

	public Date getFechaFinRev() {
		return this.fechaFinRev;
	}

	public void setFechaFinRev(Date fechaFinRev) {
		this.fechaFinRev = fechaFinRev;
	}

	public Date getFechaIni() {
		return this.fechaIni;
	}

	public void setFechaIni(Date fechaIni) {
		this.fechaIni = fechaIni;
	}

	public Date getFechaIniRev() {
		return this.fechaIniRev;
	}

	public void setFechaIniRev(Date fechaIniRev) {
		this.fechaIniRev = fechaIniRev;
	}

	public int getMuestraNumCorr() {
		return this.muestraNumCorr;
	}

	public void setMuestraNumCorr(int muestraNumCorr) {
		this.muestraNumCorr = muestraNumCorr;
	}

	public int getNivelConfianza() {
		return this.nivelConfianza;
	}

	public void setNivelConfianza(int nivelConfianza) {
		this.nivelConfianza = nivelConfianza;
	}

	public float getNotaMax() {
		return this.notaMax;
	}

	public void setNotaMax(float notaMax) {
		this.notaMax = notaMax;
	}

	public double getPNivelConfianza() {
		return this.pNivelConfianza;
	}

	public void setPNivelConfianza(double pNivelConfianza) {
		this.pNivelConfianza = pNivelConfianza;
	}

	public double getPPregFallada() {
		return this.pPregFallada;
	}

	public void setPPregFallada(double pPregFallada) {
		this.pPregFallada = pPregFallada;
	}

	public double getPPregNoResp() {
		return this.pPregNoResp;
	}

	public void setPPregNoResp(double pPregNoResp) {
		this.pPregNoResp = pPregNoResp;
	}

	public double getPRespFallada() {
		return this.pRespFallada;
	}

	public void setPRespFallada(double pRespFallada) {
		this.pRespFallada = pRespFallada;
	}

	public int getPersonalizado() {
		return this.personalizado;
	}

	public void setPersonalizado(int personalizado) {
		this.personalizado = personalizado;
	}

	public int getPesoExam() {
		return this.pesoExam;
	}

	public void setPesoExam(int pesoExam) {
		this.pesoExam = pesoExam;
	}

	public int getPublicado() {
		return this.publicado;
	}

	public void setPublicado(int publicado) {
		this.publicado = publicado;
	}

	public double getRNivelConfianza() {
		return this.rNivelConfianza;
	}

	public void setRNivelConfianza(double rNivelConfianza) {
		this.rNivelConfianza = rNivelConfianza;
	}

	public int getRevActiva() {
		return this.revActiva;
	}

	public void setRevActiva(int revActiva) {
		this.revActiva = revActiva;
	}

	public String getTitulo() {
		return this.titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public int getVisibilidad() {
		return this.visibilidad;
	}

	public void setVisibilidad(int visibilidad) {
		this.visibilidad = visibilidad;
	}

	public List<Calificacion> getCalificaciones() {
		return this.calificaciones;
	}

	public void setCalificaciones(List<Calificacion> califs) {
		this.calificaciones = califs;
	}

	public Calificacion addCalif(Calificacion calif) {
		getCalificaciones().add(calif);
		calif.setExamen(this);

		return calif;
	}

	public Calificacion removeCalif(Calificacion calif) {
		getCalificaciones().remove(calif);
		calif.setExamen(null);

		return calif;
	}

	public List<ExamenIndividual> getExamenesIndividuales() {
		return this.examenesIndividuales;
	}

	public void setExamenesIndividuales(List<ExamenIndividual> examIndivid) {
		this.examenesIndividuales = examIndivid;
	}

	public ExamenIndividual addExamIndivid(ExamenIndividual examIndivid) {
		getExamenesIndividuales().add(examIndivid);
		examIndivid.setExamen(this);

		return examIndivid;
	}

	public ExamenIndividual removeExamIndivid(ExamenIndividual examIndivid) {
		getExamenesIndividuales().remove(examIndivid);
		examIndivid.setExamen(null);

		return examIndivid;
	}

	public Grupo getGrupo() {
		return this.grupo;
	}

	public void setGrupo(Grupo grupos) {
		this.grupo = grupos;
	}

	public List<LogExamen> getLogExamenes() {
		return this.logExamenes;
	}

	public void setLogExamenes(List<LogExamen> logExams) {
		this.logExamenes = logExams;
	}

	public LogExamen addLogExam(LogExamen logExam) {
		getLogExamenes().add(logExam);
		logExam.setExamen(this);

		return logExam;
	}

	public LogExamen removeLogExam(LogExamen logExam) {
		getLogExamenes().remove(logExam);
		logExam.setExamen(null);

		return logExam;
	}

	public List<TemaExamen> getTemasExamenes() {
		return this.temasExamenes;
	}

	public void setTemasExamenes(List<TemaExamen> temasExam) {
		this.temasExamenes = temasExam;
	}

	public TemaExamen addTemasExam(TemaExamen temasExam) {
		getTemasExamenes().add(temasExam);
		temasExam.setExamen(this);

		return temasExam;
	}

	public TemaExamen removeTemasExam(TemaExamen temasExam) {
		getTemasExamenes().remove(temasExam);
		temasExam.setExamen(null);

		return temasExam;
	}

}