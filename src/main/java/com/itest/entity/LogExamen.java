package com.itest.entity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;


/**
 * The persistent class for the log_exams database table.
 * 
 */
@Entity
@Table(name="log_exams")
public class LogExamen  {

	@Id
	@Column(unique=true, nullable=false)
	private int idlogexams;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="hora_resp")
	private Date horaResp;

	@Column(nullable=false)
	private byte marcada;

	@Column(name="nivel_confianza", nullable=false)
	private byte nivelConfianza;

	@Column(precision=10, scale=2)
	private BigDecimal puntos;

	//bi-directional many-to-one association to Examen
	@ManyToOne
	@JoinColumn(name="exam", nullable=false)
	private Examen examenes;

	//bi-directional many-to-one association to Pregunta
	@ManyToOne
	@JoinColumn(name="preg", nullable=false)
	private Pregunta preguntas;

	//bi-directional many-to-one association to Respuesta
	@ManyToOne
	@JoinColumn(name="resp", nullable=false)
	private Respuesta respuestas;

	//bi-directional many-to-one association to Usuario
	@ManyToOne
	@JoinColumn(name="alu", nullable=false)
	private Usuario usuarios;

	public LogExamen() {
	}

	public int getIdlogexams() {
		return this.idlogexams;
	}

	public void setIdlogexams(int idlogexams) {
		this.idlogexams = idlogexams;
	}

	public Date getHoraResp() {
		return this.horaResp;
	}

	public void setHoraResp(Date horaResp) {
		this.horaResp = horaResp;
	}

	public byte getMarcada() {
		return this.marcada;
	}

	public void setMarcada(byte marcada) {
		this.marcada = marcada;
	}

	public byte getNivelConfianza() {
		return this.nivelConfianza;
	}

	public void setNivelConfianza(byte nivelConfianza) {
		this.nivelConfianza = nivelConfianza;
	}

	public BigDecimal getPuntos() {
		return this.puntos;
	}

	public void setPuntos(BigDecimal puntos) {
		this.puntos = puntos;
	}

	public Examen getExamenes() {
		return this.examenes;
	}

	public void setExamenes(Examen examenes) {
		this.examenes = examenes;
	}

	public Pregunta getPreguntas() {
		return this.preguntas;
	}

	public void setPreguntas(Pregunta preguntas) {
		this.preguntas = preguntas;
	}

	public Respuesta getRespuestas() {
		return this.respuestas;
	}

	public void setRespuestas(Respuesta respuestas) {
		this.respuestas = respuestas;
	}

	public Usuario getUsuarios() {
		return this.usuarios;
	}

	public void setUsuarios(Usuario usuarios) {
		this.usuarios = usuarios;
	}

}