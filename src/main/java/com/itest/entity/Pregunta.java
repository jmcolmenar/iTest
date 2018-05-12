package com.itest.entity;

import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the preguntas database table.
 * 
 */
@Entity
@Table(name="preguntas")
public class Pregunta  {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique=true, nullable=false)
	private int idpreg;

	@Column(nullable=false)
	private int activa;

	@Lob
	private String comentario;

	@Column(nullable=false)
	private int dificultad;

	@Lob
	@Column(nullable=false)
	private String enunciado;

	@Column(name="n_resp_correctas", nullable=false)
	private int nRespCorrectas;

	@Column(nullable=false)
	private int tipo;

	@Column(length=60)
	private String titulo;

	@Column(name="used_in_exam", nullable=false)
	private int usedInExam;

	@Column(nullable=false)
	private int visibilidad;

	//bi-directional many-to-one association to ExtraPregunta
	@OneToMany(mappedBy= "pregunta")
	private List<ExtraPregunta> extraPreguntas;

	//bi-directional many-to-one association to ExtraPreguntaComentario
	@OneToMany(mappedBy= "pregunta")
	private List<ExtraPreguntaComentario> extraPreguntaComentarios;

	//bi-directional many-to-one association to LogExamen
	@OneToMany(mappedBy= "pregunta")
	private List<LogExamen> logExamenes;

	//bi-directional many-to-one association to Grupo
	@ManyToOne
	@JoinColumn(name="grupo", nullable=false)
	private Grupo grupo;

	//bi-directional many-to-one association to Tema
	@ManyToOne
	@JoinColumn(name="tema", nullable=false)
	private Tema tema;

	//bi-directional many-to-one association to Topico
	@ManyToOne
	@JoinColumn(name="topic")
	private Topico topico;

	//bi-directional many-to-one association to Respuesta
	@OneToMany(mappedBy= "pregunta")
	private List<Respuesta> respuestas;

	public Pregunta() {
	}

	public int getIdpreg() {
		return this.idpreg;
	}

	public void setIdpreg(int idpreg) {
		this.idpreg = idpreg;
	}

	public int getActiva() {
		return this.activa;
	}

	public void setActiva(int activa) {
		this.activa = activa;
	}

	public String getComentario() {
		return this.comentario;
	}

	public void setComentario(String comentario) {
		this.comentario = comentario;
	}

	public int getDificultad() {
		return this.dificultad;
	}

	public void setDificultad(int dificultad) {
		this.dificultad = dificultad;
	}

	public String getEnunciado() {
		return this.enunciado;
	}

	public void setEnunciado(String enunciado) {
		this.enunciado = enunciado;
	}

	public int getNRespCorrectas() {
		return this.nRespCorrectas;
	}

	public void setNRespCorrectas(int nRespCorrectas) {
		this.nRespCorrectas = nRespCorrectas;
	}

	public int getTipo() {
		return this.tipo;
	}

	public void setTipo(int tipo) {
		this.tipo = tipo;
	}

	public String getTitulo() {
		return this.titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public int getUsedInExam() {
		return this.usedInExam;
	}

	public void setUsedInExam(int usedInExam) {
		this.usedInExam = usedInExam;
	}

	public int getVisibilidad() {
		return this.visibilidad;
	}

	public void setVisibilidad(int visibilidad) {
		this.visibilidad = visibilidad;
	}

	public List<ExtraPregunta> getExtraPreguntas() {
		return this.extraPreguntas;
	}

	public void setExtraPreguntas(List<ExtraPregunta> extraPregs) {
		this.extraPreguntas = extraPregs;
	}

	public ExtraPregunta addExtraPreg(ExtraPregunta extraPreg) {
		getExtraPreguntas().add(extraPreg);
		extraPreg.setPregunta(this);

		return extraPreg;
	}

	public ExtraPregunta removeExtraPreg(ExtraPregunta extraPreg) {
		getExtraPreguntas().remove(extraPreg);
		extraPreg.setPregunta(null);

		return extraPreg;
	}

	public List<ExtraPreguntaComentario> getExtraPreguntaComentarios() {
		return this.extraPreguntaComentarios;
	}

	public void setExtraPreguntaComentarios(List<ExtraPreguntaComentario> extraPregsComentario) {
		this.extraPreguntaComentarios = extraPregsComentario;
	}

	public ExtraPreguntaComentario addExtraPregsComentario(ExtraPreguntaComentario extraPregsComentario) {
		getExtraPreguntaComentarios().add(extraPregsComentario);
		extraPregsComentario.setPregunta(this);

		return extraPregsComentario;
	}

	public ExtraPreguntaComentario removeExtraPregsComentario(ExtraPreguntaComentario extraPregsComentario) {
		getExtraPreguntaComentarios().remove(extraPregsComentario);
		extraPregsComentario.setPregunta(null);

		return extraPregsComentario;
	}

	public List<LogExamen> getLogExamenes() {
		return this.logExamenes;
	}

	public void setLogExamenes(List<LogExamen> logExams) {
		this.logExamenes = logExams;
	}

	public LogExamen addLogExam(LogExamen logExam) {
		getLogExamenes().add(logExam);
		logExam.setPregunta(this);

		return logExam;
	}

	public LogExamen removeLogExam(LogExamen logExam) {
		getLogExamenes().remove(logExam);
		logExam.setPregunta(null);

		return logExam;
	}

	public Grupo getGrupo() {
		return this.grupo;
	}

	public void setGrupo(Grupo grupos) {
		this.grupo = grupos;
	}

	public Tema getTema() {
		return this.tema;
	}

	public void setTema(Tema temas) {
		this.tema = temas;
	}

	public Topico getTopico() {
		return this.topico;
	}

	public void setTopico(Topico topics) {
		this.topico = topics;
	}

	public List<Respuesta> getRespuestas() {
		return this.respuestas;
	}

	public void setRespuestas(List<Respuesta> respuestas) {
		this.respuestas = respuestas;
	}

	public Respuesta addRespuesta(Respuesta respuesta) {
		getRespuestas().add(respuesta);
		respuesta.setPregunta(this);

		return respuesta;
	}

	public Respuesta removeRespuesta(Respuesta respuesta) {
		getRespuestas().remove(respuesta);
		respuesta.setPregunta(null);

		return respuesta;
	}

}