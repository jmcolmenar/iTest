package com.itest.entity;

import javax.persistence.*;


/**
 * The persistent class for the extra_pregs database table.
 * 
 */
@Entity
@Table(name="extra_pregs")
public class ExtraPregunta  {

	@Id
	@Column(unique=true, nullable=false)
	private int idextrap;

	@Column(length=8)
	private String alto;

	@Column(length=8)
	private String ancho;

	@Column(name="geogebra_type", nullable=false)
	private byte geogebraType;

	@Column(nullable=false, length=40)
	private String nombre;

	private byte orden;

	@Column(nullable=false, length=100)
	private String ruta;

	private byte tipo;

	//bi-directional many-to-one association to Pregunta
	@ManyToOne
	@JoinColumn(name="preg", nullable=false)
	private Pregunta preguntas;

	public ExtraPregunta() {
	}

	public int getIdextrap() {
		return this.idextrap;
	}

	public void setIdextrap(int idextrap) {
		this.idextrap = idextrap;
	}

	public String getAlto() {
		return this.alto;
	}

	public void setAlto(String alto) {
		this.alto = alto;
	}

	public String getAncho() {
		return this.ancho;
	}

	public void setAncho(String ancho) {
		this.ancho = ancho;
	}

	public byte getGeogebraType() {
		return this.geogebraType;
	}

	public void setGeogebraType(byte geogebraType) {
		this.geogebraType = geogebraType;
	}

	public String getNombre() {
		return this.nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public byte getOrden() {
		return this.orden;
	}

	public void setOrden(byte orden) {
		this.orden = orden;
	}

	public String getRuta() {
		return this.ruta;
	}

	public void setRuta(String ruta) {
		this.ruta = ruta;
	}

	public byte getTipo() {
		return this.tipo;
	}

	public void setTipo(byte tipo) {
		this.tipo = tipo;
	}

	public Pregunta getPreguntas() {
		return this.preguntas;
	}

	public void setPreguntas(Pregunta preguntas) {
		this.preguntas = preguntas;
	}

}