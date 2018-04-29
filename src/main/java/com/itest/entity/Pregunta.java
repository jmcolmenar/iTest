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
	private byte activa;

	@Lob
	private String comentario;

	@Column(nullable=false)
	private byte dificultad;

	@Lob
	@Column(nullable=false)
	private String enunciado;

	@Column(name="n_resp_correctas", nullable=false)
	private byte nRespCorrectas;

	@Column(nullable=false)
	private byte tipo;

	@Column(length=60)
	private String titulo;

	@Column(name="used_in_exam", nullable=false)
	private byte usedInExam;

	@Column(nullable=false)
	private byte visibilidad;

	//bi-directional many-to-one association to ExtraPregunta
	@OneToMany(mappedBy="preguntas")
	private List<ExtraPregunta> extraPregs;

	//bi-directional many-to-one association to ExtraPreguntaComentario
	@OneToMany(mappedBy="preguntas")
	private List<ExtraPreguntaComentario> extraPregsComentario;

	//bi-directional many-to-one association to LogExamen
	@OneToMany(mappedBy="preguntas")
	private List<LogExamen> logExams;

	//bi-directional many-to-one association to Grupo
	@ManyToOne
	@JoinColumn(name="grupo", nullable=false)
	private Grupo grupos;

	//bi-directional many-to-one association to Tema
	@ManyToOne
	@JoinColumn(name="tema", nullable=false)
	private Tema temas;

	//bi-directional many-to-one association to Topico
	@ManyToOne
	@JoinColumn(name="topic")
	private Topico topics;

	//bi-directional many-to-one association to Respuesta
	@OneToMany(mappedBy="preguntas")
	private List<Respuesta> respuestas;

	public Pregunta() {
	}

	public int getIdpreg() {
		return this.idpreg;
	}

	public void setIdpreg(int idpreg) {
		this.idpreg = idpreg;
	}

	public byte getActiva() {
		return this.activa;
	}

	public void setActiva(byte activa) {
		this.activa = activa;
	}

	public String getComentario() {
		return this.comentario;
	}

	public void setComentario(String comentario) {
		this.comentario = comentario;
	}

	public byte getDificultad() {
		return this.dificultad;
	}

	public void setDificultad(byte dificultad) {
		this.dificultad = dificultad;
	}

	public String getEnunciado() {
		return this.enunciado;
	}

	public void setEnunciado(String enunciado) {
		this.enunciado = enunciado;
	}

	public byte getNRespCorrectas() {
		return this.nRespCorrectas;
	}

	public void setNRespCorrectas(byte nRespCorrectas) {
		this.nRespCorrectas = nRespCorrectas;
	}

	public byte getTipo() {
		return this.tipo;
	}

	public void setTipo(byte tipo) {
		this.tipo = tipo;
	}

	public String getTitulo() {
		return this.titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public byte getUsedInExam() {
		return this.usedInExam;
	}

	public void setUsedInExam(byte usedInExam) {
		this.usedInExam = usedInExam;
	}

	public byte getVisibilidad() {
		return this.visibilidad;
	}

	public void setVisibilidad(byte visibilidad) {
		this.visibilidad = visibilidad;
	}

	public List<ExtraPregunta> getExtraPregs() {
		return this.extraPregs;
	}

	public void setExtraPregs(List<ExtraPregunta> extraPregs) {
		this.extraPregs = extraPregs;
	}

	public ExtraPregunta addExtraPreg(ExtraPregunta extraPreg) {
		getExtraPregs().add(extraPreg);
		extraPreg.setPreguntas(this);

		return extraPreg;
	}

	public ExtraPregunta removeExtraPreg(ExtraPregunta extraPreg) {
		getExtraPregs().remove(extraPreg);
		extraPreg.setPreguntas(null);

		return extraPreg;
	}

	public List<ExtraPreguntaComentario> getExtraPregsComentario() {
		return this.extraPregsComentario;
	}

	public void setExtraPregsComentario(List<ExtraPreguntaComentario> extraPregsComentario) {
		this.extraPregsComentario = extraPregsComentario;
	}

	public ExtraPreguntaComentario addExtraPregsComentario(ExtraPreguntaComentario extraPregsComentario) {
		getExtraPregsComentario().add(extraPregsComentario);
		extraPregsComentario.setPreguntas(this);

		return extraPregsComentario;
	}

	public ExtraPreguntaComentario removeExtraPregsComentario(ExtraPreguntaComentario extraPregsComentario) {
		getExtraPregsComentario().remove(extraPregsComentario);
		extraPregsComentario.setPreguntas(null);

		return extraPregsComentario;
	}

	public List<LogExamen> getLogExams() {
		return this.logExams;
	}

	public void setLogExams(List<LogExamen> logExams) {
		this.logExams = logExams;
	}

	public LogExamen addLogExam(LogExamen logExam) {
		getLogExams().add(logExam);
		logExam.setPreguntas(this);

		return logExam;
	}

	public LogExamen removeLogExam(LogExamen logExam) {
		getLogExams().remove(logExam);
		logExam.setPreguntas(null);

		return logExam;
	}

	public Grupo getGrupos() {
		return this.grupos;
	}

	public void setGrupos(Grupo grupos) {
		this.grupos = grupos;
	}

	public Tema getTemas() {
		return this.temas;
	}

	public void setTemas(Tema temas) {
		this.temas = temas;
	}

	public Topico getTopics() {
		return this.topics;
	}

	public void setTopics(Topico topics) {
		this.topics = topics;
	}

	public List<Respuesta> getRespuestas() {
		return this.respuestas;
	}

	public void setRespuestas(List<Respuesta> respuestas) {
		this.respuestas = respuestas;
	}

	public Respuesta addRespuesta(Respuesta respuesta) {
		getRespuestas().add(respuesta);
		respuesta.setPreguntas(this);

		return respuesta;
	}

	public Respuesta removeRespuesta(Respuesta respuesta) {
		getRespuestas().remove(respuesta);
		respuesta.setPreguntas(null);

		return respuesta;
	}

}