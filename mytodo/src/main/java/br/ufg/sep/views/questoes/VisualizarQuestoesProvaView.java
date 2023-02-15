package br.ufg.sep.views.questoes;

import java.util.Optional;

import javax.annotation.security.PermitAll;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.details.Details;
import com.vaadin.flow.component.details.DetailsVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import br.ufg.sep.data.services.ProvaService;
import br.ufg.sep.data.services.QuestaoService;
import br.ufg.sep.entity.NivelProva;
import br.ufg.sep.entity.Prova;
import br.ufg.sep.entity.Questao;
import br.ufg.sep.entity.TipoProva;
import br.ufg.sep.views.MainLayout;
import br.ufg.sep.views.questoes.presenter.VisualizarQuestoesProvaPresenter;

@Route(value="visualizar_questoes_prova", layout = MainLayout.class)
@PageTitle("Questões")
@PermitAll
public class VisualizarQuestoesProvaView extends VerticalLayout implements HasUrlParameter<Long> {
	
	private ProvaService provaService;
	private QuestaoService questaoService;

	private Prova prova;
	private Long provaId;
	private Grid<Questao> questoesGrid;
	private VisualizarQuestoesProvaPresenter presenter;
	
	private HorizontalLayout layoutButton;
	private HorizontalLayout infoProvaLayout;
	private Details details;
	
	/* TF em referencia ao componente textfield */
	private TextField concursoTF;
	private TextField areaConhecimentoTF;
	private TextField nivelTF;
	private TextArea descricaoTF;
	private TextField numQuestoesFeitasTF;
	private TextField numQuestoesTotalTF;
	
	private Button novaQuestaoButton;
	private Button visualizarButton;
	private Button downloadButton;
	

	public VisualizarQuestoesProvaView(ProvaService provaService, QuestaoService questaoService) {
		this.provaService = provaService;
		this.questaoService = questaoService;
		
		iniciaGrid();
		criarButtons();
		criarLayoutInfoProva(); //cria o layout com informacoes da prova
		
		layoutButton.add(novaQuestaoButton, visualizarButton);
		
		add(details, layoutButton, questoesGrid);
	}

	//metodo para iniciar o grid
	private void iniciaGrid() {
		questoesGrid = new Grid<>(Questao.class,false);
		
		questoesGrid.addColumn("enunciado").setHeader("Enunciado");
		questoesGrid.addColumn("conteudoEspecifico").setHeader("Subárea");
		questoesGrid.addColumn("nivelDificuldade").setHeader("Nível");
		//provas.addColumn("atividade").setHeader("Atividade");
		questoesGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
	}
	
	//metodo para criacao dos botoes
	private void criarButtons() {
		layoutButton = new HorizontalLayout();
		novaQuestaoButton = new Button("Cadastrar Questão", new Icon(VaadinIcon.PLUS));
		novaQuestaoButton.addThemeVariants(ButtonVariant.LUMO_CONTRAST);
		
		visualizarButton = new Button("Visualizar", new Icon(VaadinIcon.EYE));
		visualizarButton.setEnabled(false);
		
		downloadButton = new Button("Baixar arquivo", new Icon(VaadinIcon.DOWNLOAD));
	}

	//metodo para criar o layout superior com informacoes da prova
	private void criarLayoutInfoProva() {
		
		//layouts para organizacao dos componentes
		HorizontalLayout h = new HorizontalLayout();
		VerticalLayout infosForm = new VerticalLayout();
		VerticalLayout numQuestaoform = new VerticalLayout();
		
		
		HorizontalLayout summary = new HorizontalLayout();
		summary.setSpacing(false);
		summary.add(new Text("Prova - Informações Gerais"));
		
		infoProvaLayout = new HorizontalLayout();

		//labels
		concursoTF = new TextField("Concurso", "", "");
		areaConhecimentoTF = new TextField("Area do conhecimento", "", "");
		nivelTF = new TextField("Nível", "", "");
		descricaoTF = new TextArea("Descrição", "", "");
		numQuestoesFeitasTF = new TextField("Questões Elaboradas", "", "");
		numQuestoesTotalTF = new TextField("Questões Solicitadas", "", "");
		
		//tamanho dos componentes
		h.setWidthFull();
		infosForm.setWidth("1700px");
		concursoTF.setWidthFull();
		areaConhecimentoTF.setWidthFull();
		descricaoTF.setWidthFull();
		descricaoTF.setHeight("133px");
		
		//espacamento dos componentes
		infosForm.setSpacing(false);
		numQuestaoform.setSpacing(false);
		
		//adicionando componentes aos formularios
		numQuestaoform.add(nivelTF, numQuestoesFeitasTF, numQuestoesTotalTF);
		h.add(concursoTF, areaConhecimentoTF);
		infosForm.add(h, descricaoTF, downloadButton);
		
		/** Seta apenas leitura **/
		numQuestaoform.getChildren().forEach(txtField->{
        	((TextField)txtField).setReadOnly(true);
        });
		h.getChildren().forEach(txtField->{
        	((TextField)txtField).setReadOnly(true);
        });
		descricaoTF.setReadOnly(true);
		
		//organizando os componentes
		infoProvaLayout.add(infosForm, numQuestaoform);
		infoProvaLayout.setAlignItems(Alignment.BASELINE);
		infoProvaLayout.setWidthFull();

		details = new Details(summary, infoProvaLayout);
		details.addThemeVariants(DetailsVariant.FILLED);
		details.setWidthFull();
		details.setMinWidth("700px");
		details.setOpened(true);
	}
	
	@Override
	public void setParameter(BeforeEvent event, Long parameter) {
		// TODO Auto-generated method stub
		
		Optional<Prova> optionalQuestao = provaService.getRepository().findById(parameter);
		if (optionalQuestao.isPresent()) {
			prova = optionalQuestao.get();
			this.provaId = prova.getId();
			
			setInfoProva(); //setar as informacoes sobre a prova
			
			this.presenter = new VisualizarQuestoesProvaPresenter(provaService, questaoService,this); //iniciar o presenter
		}
	}
	
	public void habilitarButtons() {
		this.visualizarButton.setEnabled(true);
	}

	public void setInfoProva(){
		this.areaConhecimentoTF.setValue(prova.getAreaConhecimento());
		this.descricaoTF.setValue(prova.getDescricao());
		this.numQuestoesTotalTF.setValue("" + prova.getNumeroQuestoes());
		this.nivelTF.setValue("" + prova.getNivel().toString());
	}
	// --------------- Getter e Setters
	
	public Button getNovaQuestaoButton() {
		return novaQuestaoButton;
	}
	
	public Grid<Questao> getQuestoesGrid() {
		return questoesGrid;
	}

	public void setQuestoesGrid(Grid<Questao> questoes) {
		this.questoesGrid = questoes;
	}
	
	public QuestaoService getQuestaoService() {
		return questaoService;
	}

	public Long getProvaId() {
		return provaId;
	}
	
	public Button getVisualizarButton() {
		return visualizarButton;
	}

}
