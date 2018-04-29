package com.itest.entity;

import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the asignaturas database table.
 * 
 */
@Entity
@Table(name="asignaturas")
public class Asignatura  {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique=true, nullable=false)
	private int idasig;

	@Column(nullable=false, length=10)
	private String cod;

	@Column(length=250)
	private String comentarios;

	@Column(nullable=false, length=2)
	private String curso;

	@Column(nullable=false, length=20)
	private String estudios;

	@Column(nullable=false, length=100)
	private String nombre;

	//bi-directional many-to-one association to Grupo
	@OneToMany(mappedBy="asignaturas")
	private List<Grupo> grupos;

	//bi-directional many-to-one association to Topico
	@OneToMany(mappedBy="asignaturas")
	private List<Topico> topics;

	public Asignatura() {
	}

	public int getIdasig() {
		return this.idasig;
	}

	public void setIdasig(int idasig) {
		this.idasig = idasig;
	}

	public String getCod() {
		return this.cod;
	}

	public void setCod(String cod) {
		this.cod = cod;
	}

	public String getComentarios() {
		return this.comentarios;
	}

	public void setComentarios(String comentarios) {
		this.comentarios = comentarios;
	}

	public String getCurso() {
		return this.curso;
	}

	public void setCurso(String curso) {
		this.curso = curso;
	}

	public String getEstudios() {
		return this.estudios;
	}

	public void setEstudios(String estudios) {
		this.estudios = estudios;
	}

	public String getNombre() {
		return this.nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public List<Grupo> getGrupos() {
		return this.grupos;
	}

	public void setGrupos(List<Grupo> grupos) {
		this.grupos = grupos;
	}

	public Grupo addGrupo(Grupo grupo) {
		getGrupos().add(grupo);
		grupo.setAsignaturas(this);

		return grupo;
	}

	public Grupo removeGrupo(Grupo grupo) {
		getGrupos().remove(grupo);
		grupo.setAsignaturas(null);

		return grupo;
	}

	public List<Topico> getTopics() {
		return this.topics;
	}

	public void setTopics(List<Topico> topics) {
		this.topics = topics;
	}

	public Topico addTopic(Topico topic) {
		getTopics().add(topic);
		topic.setAsignaturas(this);

		return topic;
	}

	public Topico removeTopic(Topico topic) {
		getTopics().remove(topic);
		topic.setAsignaturas(null);

		return topic;
	}

}