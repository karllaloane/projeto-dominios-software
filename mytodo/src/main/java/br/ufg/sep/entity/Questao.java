package br.ufg.sep.entity;

import br.ufg.sep.state.QuestaoState;
import br.ufg.sep.state.stateImpl.Elaboracao;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;

@Entity
public class Questao extends AbstractEntity {

	@OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
	QuestaoState state;
	@ManyToOne()
	@JoinColumn(name="prova_id", nullable = true)
	private Prova prova;
	
	private int idQuestao;
	@Column(length = 2054)
	private String enunciado;
	
	private String conteudoEspecifico;
	
	@Enumerated(EnumType.STRING)
	private NivelDificuldade nivelDificuldade;
	
	//private String descricao;
	
	private String justificativa;
	
	@OneToOne(cascade = CascadeType.ALL)
	private AnexoQuestao anexo;
	
	
	public Questao(){
		this.state = new Elaboracao();
	}




	/************ Métodos STATE********/
	public QuestaoState getState() {
		return state;
	}

	public void setState(QuestaoState state) {
		this.state = state;
	}
	public void enviarParaRevisao(Correcao correcao){
	this.state.enviarParaRevisao(this,correcao);
	}

	public void enviarParaCorrecao(Revisao revisao){
		this.state.enviarParaCorrecao(this,revisao);
	}


	public void concluir(){
		this.state.concluir(this);
	}

	public void descartar(Revisao revisao){
		this.state.descartar(this,revisao);
	}

	public void guardarNoBanco() {
	this.state.guardar(this);
	}


	/************ Métodos STATE********/



	public Questao(int num) {
		this.idQuestao = num;
	}
	public Questao(String s){

	}
	
	public String getEnunciado() {
		return enunciado;
	}

	public void setEnunciado(String enunciado) {
		this.enunciado = enunciado;
	}

	public String getConteudoEspecifico() {
		return conteudoEspecifico;
	}

	public void setConteudoEspecifico(String conteudoEspecifico) {
		this.conteudoEspecifico = conteudoEspecifico;
	}

	public NivelDificuldade getNivelDificuldade() {
		return nivelDificuldade;
	}

	public void setNivelDificuldade(NivelDificuldade nivelDificuldade) {
		this.nivelDificuldade = nivelDificuldade;
	}

//	public String getDescricao() {
//		return descricao;
//	}
//
//	public void setDescricao(String descricao) {
//		this.descricao = descricao;
//	}

	public String getJustificativa() {
		return justificativa;
	}

	public void setJustificativa(String justificativa) {
		this.justificativa = justificativa;
	}

	public AnexoQuestao getAnexo() {
		return anexo;
	}

	public void setAnexo(AnexoQuestao anexo) {
		this.anexo = anexo;
	}
	
	public Prova getProva() {
		return prova;
	}

	public void setProva(Prova prova) {
		this.prova = prova;
	}


	public int getIdQuestao() {
		return idQuestao;
	}

	public void setIdQuestao(int idQuestao) {
		this.idQuestao = idQuestao;
	}
	
	

}
