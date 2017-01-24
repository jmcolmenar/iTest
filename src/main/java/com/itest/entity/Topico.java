package com.itest.entity;

import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the topics database table.
 * 
 */
@Entity
@Table(name="topics")
public class Topico  {

	@Id
	@Column(unique=true, nullable=false)
	private int idtopic;

	@Column(nullable=false)
	private byte orden;

	@Column(nullable=false, length=60)
	private String topic;

	//bi-directional many-to-one association to Pregunta
	@OneToMany(mappedBy="topics")
	private List<Pregunta> preguntas;

	//bi-directional many-to-one association to Asignatura
	@ManyToOne
	@JoinColumn(name="asig", nullable=false)
	private Asignatura asignaturas;

	public Topico() {
	}

	public int getIdtopic() {
		return this.idtopic;
	}

	public void setIdtopic(int idtopic) {
		this.idtopic = idtopic;
	}

	public byte getOrden() {
		return this.orden;
	}

	public void setOrden(byte orden) {
		this.orden = orden;
	}

	public String getTopic() {
		return this.topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	public List<Pregunta> getPreguntas() {
		return this.preguntas;
	}

	public void setPreguntas(List<Pregunta> preguntas) {
		this.preguntas = preguntas;
	}

	public Pregunta addPregunta(Pregunta pregunta) {
		getPreguntas().add(pregunta);
		pregunta.setTopics(this);

		return pregunta;
	}

	public Pregunta removePregunta(Pregunta pregunta) {
		getPreguntas().remove(pregunta);
		pregunta.setTopics(null);

		return pregunta;
	}

	public Asignatura getAsignaturas() {
		return this.asignaturas;
	}

	public void setAsignaturas(Asignatura asignaturas) {
		this.asignaturas = asignaturas;
	}

}