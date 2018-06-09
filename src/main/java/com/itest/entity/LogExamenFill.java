package com.itest.entity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;


/**
 * The persistent class for the log_exams_fill database table.
 * 
 */
@Entity
@Table(name="log_exams_fill")
public class LogExamenFill  {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique=true, nullable=false)
	private int idlogexamsfill;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="hora_resp")
	private Date horaResp;

	@Column(name="nivel_confianza", nullable=false)
	private int nivelConfianza;

	@Column(precision=10, scale=2)
	private BigDecimal puntos;

	//bi-directional many-to-one association to Examen
	@ManyToOne
	@JoinColumn(name="exam", nullable=false)
	private Examen examen;

	//bi-directional many-to-one association to Pregunta
	@ManyToOne
	@JoinColumn(name="preg", nullable=false)
	private Pregunta pregunta;

	//bi-directional many-to-one association to Usuario
	@ManyToOne
	@JoinColumn(name="alu", nullable=false)
	private Usuario usuario;

	@Lob
	private String resp;

	public LogExamenFill() {
	}

	public int getIdlogexamsfill() {
		return this.idlogexamsfill;
	}

	public void setIdlogexamsfill(int idlogexamsfill) {
		this.idlogexamsfill = idlogexamsfill;
	}

	public Date getHoraResp() {
		return this.horaResp;
	}

	public void setHoraResp(Date horaResp) {
		this.horaResp = horaResp;
	}

	public int getNivelConfianza() {
		return this.nivelConfianza;
	}

	public void setNivelConfianza(int nivelConfianza) {
		this.nivelConfianza = nivelConfianza;
	}

	public BigDecimal getPuntos() {
		return this.puntos;
	}

	public void setPuntos(BigDecimal puntos) {
		this.puntos = puntos;
	}

	public Examen getExamen() {
		return this.examen;
	}

	public void setExamen(Examen examenes) {
		this.examen = examenes;
	}

	public Pregunta getPregunta() {
		return this.pregunta;
	}

	public void setPregunta(Pregunta preguntas) {
		this.pregunta = preguntas;
	}

	public Usuario getUsuario() {
		return this.usuario;
	}

	public void setUsuario(Usuario usuarios) {
		this.usuario = usuarios;
	}

	public String getResp() {
		return this.resp;
	}

	public void setResp(String resp) {
		this.resp = resp;
	}

}