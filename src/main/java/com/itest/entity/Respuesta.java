package com.itest.entity;

import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the respuestas database table.
 * 
 */
@Entity
@Table(name="respuestas")
public class Respuesta  {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique=true, nullable=false)
	private int idresp;

	@Column(nullable=false)
	private int activa;

	@Column(nullable=false)
	private int solucion;

	@Lob
	@Column(nullable=false)
	private String texto;

	@Column(name="used_in_exam_question", nullable=false)
	private int usedInExamQuestion;

	@Column(nullable=false)
	private int valor;

	//bi-directional many-to-one association to ExtraRespuesta
	@OneToMany(mappedBy= "respuesta")
	private List<ExtraRespuesta> extraRespuestas;

	//bi-directional many-to-one association to LogExamen
	@OneToMany(mappedBy= "respuesta")
	private List<LogExamen> logExamenes;

	//bi-directional many-to-one association to Pregunta
	@ManyToOne
	@JoinColumn(name="preg", nullable=false)
	private Pregunta pregunta;

	public Respuesta() {
	}

	public int getIdresp() {
		return this.idresp;
	}

	public void setIdresp(int idresp) {
		this.idresp = idresp;
	}

	public int getActiva() {
		return this.activa;
	}

	public void setActiva(int activa) {
		this.activa = activa;
	}

	public int getSolucion() {
		return this.solucion;
	}

	public void setSolucion(int solucion) {
		this.solucion = solucion;
	}

	public String getTexto() {
		return this.texto;
	}

	public void setTexto(String texto) {
		this.texto = texto;
	}

	public int getUsedInExamQuestion() {
		return this.usedInExamQuestion;
	}

	public void setUsedInExamQuestion(int usedInExamQuestion) {
		this.usedInExamQuestion = usedInExamQuestion;
	}

	public int getValor() {
		return this.valor;
	}

	public void setValor(int valor) {
		this.valor = valor;
	}

	public List<ExtraRespuesta> getExtraRespuestas() {
		return this.extraRespuestas;
	}

	public void setExtraRespuestas(List<ExtraRespuesta> extraResps) {
		this.extraRespuestas = extraResps;
	}

	public ExtraRespuesta addExtraResp(ExtraRespuesta extraResp) {
		getExtraRespuestas().add(extraResp);
		extraResp.setRespuesta(this);

		return extraResp;
	}

	public ExtraRespuesta removeExtraResp(ExtraRespuesta extraResp) {
		getExtraRespuestas().remove(extraResp);
		extraResp.setRespuesta(null);

		return extraResp;
	}

	public List<LogExamen> getLogExamenes() {
		return this.logExamenes;
	}

	public void setLogExamenes(List<LogExamen> logExams) {
		this.logExamenes = logExams;
	}

	public LogExamen addLogExam(LogExamen logExam) {
		getLogExamenes().add(logExam);
		logExam.setRespuesta(this);

		return logExam;
	}

	public LogExamen removeLogExam(LogExamen logExam) {
		getLogExamenes().remove(logExam);
		logExam.setRespuesta(null);

		return logExam;
	}

	public Pregunta getPregunta() {
		return this.pregunta;
	}

	public void setPregunta(Pregunta preguntas) {
		this.pregunta = preguntas;
	}

}