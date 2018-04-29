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
	private byte activa;

	@Column(nullable=false)
	private byte solucion;

	@Lob
	@Column(nullable=false)
	private String texto;

	@Column(name="used_in_exam_question", nullable=false)
	private byte usedInExamQuestion;

	@Column(nullable=false)
	private byte valor;

	//bi-directional many-to-one association to ExtraRespuesta
	@OneToMany(mappedBy="respuestas")
	private List<ExtraRespuesta> extraResps;

	//bi-directional many-to-one association to LogExamen
	@OneToMany(mappedBy="respuestas")
	private List<LogExamen> logExams;

	//bi-directional many-to-one association to Pregunta
	@ManyToOne
	@JoinColumn(name="preg", nullable=false)
	private Pregunta preguntas;

	public Respuesta() {
	}

	public int getIdresp() {
		return this.idresp;
	}

	public void setIdresp(int idresp) {
		this.idresp = idresp;
	}

	public byte getActiva() {
		return this.activa;
	}

	public void setActiva(byte activa) {
		this.activa = activa;
	}

	public byte getSolucion() {
		return this.solucion;
	}

	public void setSolucion(byte solucion) {
		this.solucion = solucion;
	}

	public String getTexto() {
		return this.texto;
	}

	public void setTexto(String texto) {
		this.texto = texto;
	}

	public byte getUsedInExamQuestion() {
		return this.usedInExamQuestion;
	}

	public void setUsedInExamQuestion(byte usedInExamQuestion) {
		this.usedInExamQuestion = usedInExamQuestion;
	}

	public byte getValor() {
		return this.valor;
	}

	public void setValor(byte valor) {
		this.valor = valor;
	}

	public List<ExtraRespuesta> getExtraResps() {
		return this.extraResps;
	}

	public void setExtraResps(List<ExtraRespuesta> extraResps) {
		this.extraResps = extraResps;
	}

	public ExtraRespuesta addExtraResp(ExtraRespuesta extraResp) {
		getExtraResps().add(extraResp);
		extraResp.setRespuestas(this);

		return extraResp;
	}

	public ExtraRespuesta removeExtraResp(ExtraRespuesta extraResp) {
		getExtraResps().remove(extraResp);
		extraResp.setRespuestas(null);

		return extraResp;
	}

	public List<LogExamen> getLogExams() {
		return this.logExams;
	}

	public void setLogExams(List<LogExamen> logExams) {
		this.logExams = logExams;
	}

	public LogExamen addLogExam(LogExamen logExam) {
		getLogExams().add(logExam);
		logExam.setRespuestas(this);

		return logExam;
	}

	public LogExamen removeLogExam(LogExamen logExam) {
		getLogExams().remove(logExam);
		logExam.setRespuestas(null);

		return logExam;
	}

	public Pregunta getPreguntas() {
		return this.preguntas;
	}

	public void setPreguntas(Pregunta preguntas) {
		this.preguntas = preguntas;
	}

}