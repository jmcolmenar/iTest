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
	private byte corrParcial;

	@Column(name="cota_calif_preg", nullable=false)
	private double cotaCalifPreg;

	@Column(name="distrib_pregs", nullable=false)
	private byte distribPregs;

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
	private byte muestraNumCorr;

	@Column(name = "nivelconfianza", nullable=false)
	private byte nivelConfianza;

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

	private byte personalizado;

	@Column(name="peso_exam")
	private byte pesoExam;

	@Column(nullable=false)
	private byte publicado;

	@Column(name="r_nivel_confianza", nullable=false)
	private double rNivelConfianza;

	@Column(name="rev_activa", nullable=false)
	private byte revActiva;

	@Column(length=60)
	private String titulo;

	@Column(nullable=false)
	private byte visibilidad;

	//bi-directional many-to-one association to Calificacion
	@OneToMany(mappedBy="examenes")
	private List<Calificacion> califs;

	//bi-directional many-to-one association to ExamenIndividual
	@OneToMany(mappedBy="examenes")
	private List<ExamenIndividual> examIndivid;

	//bi-directional many-to-one association to Grupo
	@ManyToOne
	@JoinColumn(name="grupo", nullable=false)
	private Grupo grupos;

	//bi-directional many-to-one association to LogExamen
	@OneToMany(mappedBy="examenes")
	private List<LogExamen> logExams;

	//bi-directional many-to-one association to TemaExamen
	@OneToMany(mappedBy="examenes")
	private List<TemaExamen> temasExam;

	public Examen() {
	}

	public int getIdexam() {
		return this.idexam;
	}

	public void setIdexam(int idexam) {
		this.idexam = idexam;
	}

	public byte getCorrParcial() {
		return this.corrParcial;
	}

	public void setCorrParcial(byte corrParcial) {
		this.corrParcial = corrParcial;
	}

	public double getCotaCalifPreg() {
		return this.cotaCalifPreg;
	}

	public void setCotaCalifPreg(double cotaCalifPreg) {
		this.cotaCalifPreg = cotaCalifPreg;
	}

	public byte getDistribPregs() {
		return this.distribPregs;
	}

	public void setDistribPregs(byte distribPregs) {
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

	public byte getMuestraNumCorr() {
		return this.muestraNumCorr;
	}

	public void setMuestraNumCorr(byte muestraNumCorr) {
		this.muestraNumCorr = muestraNumCorr;
	}

	public byte getNivelConfianza() {
		return this.nivelConfianza;
	}

	public void setNivelConfianza(byte nivelConfianza) {
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

	public byte getPersonalizado() {
		return this.personalizado;
	}

	public void setPersonalizado(byte personalizado) {
		this.personalizado = personalizado;
	}

	public byte getPesoExam() {
		return this.pesoExam;
	}

	public void setPesoExam(byte pesoExam) {
		this.pesoExam = pesoExam;
	}

	public byte getPublicado() {
		return this.publicado;
	}

	public void setPublicado(byte publicado) {
		this.publicado = publicado;
	}

	public double getRNivelConfianza() {
		return this.rNivelConfianza;
	}

	public void setRNivelConfianza(double rNivelConfianza) {
		this.rNivelConfianza = rNivelConfianza;
	}

	public byte getRevActiva() {
		return this.revActiva;
	}

	public void setRevActiva(byte revActiva) {
		this.revActiva = revActiva;
	}

	public String getTitulo() {
		return this.titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public byte getVisibilidad() {
		return this.visibilidad;
	}

	public void setVisibilidad(byte visibilidad) {
		this.visibilidad = visibilidad;
	}

	public List<Calificacion> getCalifs() {
		return this.califs;
	}

	public void setCalifs(List<Calificacion> califs) {
		this.califs = califs;
	}

	public Calificacion addCalif(Calificacion calif) {
		getCalifs().add(calif);
		calif.setExamenes(this);

		return calif;
	}

	public Calificacion removeCalif(Calificacion calif) {
		getCalifs().remove(calif);
		calif.setExamenes(null);

		return calif;
	}

	public List<ExamenIndividual> getExamIndivid() {
		return this.examIndivid;
	}

	public void setExamIndivid(List<ExamenIndividual> examIndivid) {
		this.examIndivid = examIndivid;
	}

	public ExamenIndividual addExamIndivid(ExamenIndividual examIndivid) {
		getExamIndivid().add(examIndivid);
		examIndivid.setExamenes(this);

		return examIndivid;
	}

	public ExamenIndividual removeExamIndivid(ExamenIndividual examIndivid) {
		getExamIndivid().remove(examIndivid);
		examIndivid.setExamenes(null);

		return examIndivid;
	}

	public Grupo getGrupos() {
		return this.grupos;
	}

	public void setGrupos(Grupo grupos) {
		this.grupos = grupos;
	}

	public List<LogExamen> getLogExams() {
		return this.logExams;
	}

	public void setLogExams(List<LogExamen> logExams) {
		this.logExams = logExams;
	}

	public LogExamen addLogExam(LogExamen logExam) {
		getLogExams().add(logExam);
		logExam.setExamenes(this);

		return logExam;
	}

	public LogExamen removeLogExam(LogExamen logExam) {
		getLogExams().remove(logExam);
		logExam.setExamenes(null);

		return logExam;
	}

	public List<TemaExamen> getTemasExam() {
		return this.temasExam;
	}

	public void setTemasExam(List<TemaExamen> temasExam) {
		this.temasExam = temasExam;
	}

	public TemaExamen addTemasExam(TemaExamen temasExam) {
		getTemasExam().add(temasExam);
		temasExam.setExamenes(this);

		return temasExam;
	}

	public TemaExamen removeTemasExam(TemaExamen temasExam) {
		getTemasExam().remove(temasExam);
		temasExam.setExamenes(null);

		return temasExam;
	}

}