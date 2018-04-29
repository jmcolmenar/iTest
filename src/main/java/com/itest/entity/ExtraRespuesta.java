package com.itest.entity;

import javax.persistence.*;


/**
 * The persistent class for the extra_resps database table.
 * 
 */
@Entity
@Table(name="extra_resps")
public class ExtraRespuesta  {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique=true, nullable=false)
	private int idextrar;

	@Column(length=8)
	private String alto;

	@Column(length=8)
	private String ancho;

	@Column(nullable=false, length=40)
	private String nombre;

	private byte orden;

	@Column(nullable=false, length=100)
	private String ruta;

	private byte tipo;

	//bi-directional many-to-one association to Respuesta
	@ManyToOne
	@JoinColumn(name="resp", nullable=false)
	private Respuesta respuestas;

	public ExtraRespuesta() {
	}

	public int getIdextrar() {
		return this.idextrar;
	}

	public void setIdextrar(int idextrar) {
		this.idextrar = idextrar;
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

	public Respuesta getRespuestas() {
		return this.respuestas;
	}

	public void setRespuestas(Respuesta respuestas) {
		this.respuestas = respuestas;
	}

}