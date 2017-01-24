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
	@Column(unique=true, nullable=false)
	private int idlogexamsfill;

	@Column(nullable=false)
	private int alu;

	@Column(nullable=false)
	private int exam;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="hora_resp")
	private Date horaResp;

	@Column(name="nivel_confianza", nullable=false)
	private byte nivelConfianza;

	@Column(nullable=false)
	private int preg;

	@Column(precision=10, scale=2)
	private BigDecimal puntos;

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

	public int getAlu() {
		return this.alu;
	}

	public void setAlu(int alu) {
		this.alu = alu;
	}

	public int getExam() {
		return this.exam;
	}

	public void setExam(int exam) {
		this.exam = exam;
	}

	public Date getHoraResp() {
		return this.horaResp;
	}

	public void setHoraResp(Date horaResp) {
		this.horaResp = horaResp;
	}

	public byte getNivelConfianza() {
		return this.nivelConfianza;
	}

	public void setNivelConfianza(byte nivelConfianza) {
		this.nivelConfianza = nivelConfianza;
	}

	public int getPreg() {
		return this.preg;
	}

	public void setPreg(int preg) {
		this.preg = preg;
	}

	public BigDecimal getPuntos() {
		return this.puntos;
	}

	public void setPuntos(BigDecimal puntos) {
		this.puntos = puntos;
	}

	public String getResp() {
		return this.resp;
	}

	public void setResp(String resp) {
		this.resp = resp;
	}

}