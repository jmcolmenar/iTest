package com.itest.entity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;


/**
 * The persistent class for the califs database table.
 * 
 */
@Entity
@Table(name="califs")
public class Calificacion  {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique=true, nullable=false)
	private int idcalif;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="fecha_fin")
	private Date fechaFin;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="fecha_ini")
	private Date fechaIni;

	@Column(length=15)
	private String ip;

	@Column(precision=10, scale=2)
	private BigDecimal nota;

	@Column(nullable=false)
	private int tiempo;

	//bi-directional many-to-one association to Examen
	@ManyToOne
	@JoinColumn(name="exam", nullable=false)
	private Examen examenes;

	//bi-directional many-to-one association to Usuario
	@ManyToOne
	@JoinColumn(name="alu", nullable=false)
	private Usuario usuarios;

	public Calificacion() {
	}

	public int getIdcalif() {
		return this.idcalif;
	}

	public void setIdcalif(int idcalif) {
		this.idcalif = idcalif;
	}

	public Date getFechaFin() {
		return this.fechaFin;
	}

	public void setFechaFin(Date fechaFin) {
		this.fechaFin = fechaFin;
	}

	public Date getFechaIni() {
		return this.fechaIni;
	}

	public void setFechaIni(Date fechaIni) {
		this.fechaIni = fechaIni;
	}

	public String getIp() {
		return this.ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public BigDecimal getNota() {
		return this.nota;
	}

	public void setNota(BigDecimal nota) {
		this.nota = nota;
	}

	public int getTiempo() {
		return this.tiempo;
	}

	public void setTiempo(int tiempo) {
		this.tiempo = tiempo;
	}

	public Examen getExamenes() {
		return this.examenes;
	}

	public void setExamenes(Examen examenes) {
		this.examenes = examenes;
	}

	public Usuario getUsuarios() {
		return this.usuarios;
	}

	public void setUsuarios(Usuario usuarios) {
		this.usuarios = usuarios;
	}

}