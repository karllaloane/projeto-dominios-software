package br.ufg.sep.views.gerenciarProvas.presenter;

import br.ufg.sep.data.repositories.ProvaRepository;
import br.ufg.sep.data.services.ProvaService;
import br.ufg.sep.view.prova.ProvasView;
import br.ufg.sep.entity.Concurso;
import br.ufg.sep.entity.Prova;
import br.ufg.sep.views.concurso.EditarConcursoView;
import br.ufg.sep.views.gerenciarProvas.GerenciarProvasView;
import br.ufg.sep.views.gerenciarProvas.NovaProvaView;
import org.springframework.data.domain.PageRequest;

import java.util.Optional;
import java.util.Set;

public class GerenciarProvasPresenter {

    private ProvaService provaService;
    private GerenciarProvasView view;

    private Prova provaSelecionada;
    public GerenciarProvasPresenter(ProvaService provaService, GerenciarProvasView view){
        this.view = view;
    this.provaService = provaService;
        //Adicionar um escutador de eventos de seleção de item na grid
        //Ele pega o item selecionado, o guarda em 'provaSelecionada' e habilita os botões.
        view.getProvas().addSelectionListener(selection -> {
            Optional<Prova> optionalProva = selection.getFirstSelectedItem();
            if (optionalProva.isPresent()) {
                Long testeId = optionalProva.get().getId();
                Optional<Prova> talvezProva = provaService.getRepository().findById(testeId);
                if(talvezProva.isPresent()) {
                    provaSelecionada = talvezProva.get();
                    view.habilitarButtons();
                }
            }
        });

        view.getProvas().setItems(query-> provaService.getRepository()
                .findAll(PageRequest.of(query.getPage(), query.getPageSize())).stream());




        configBotoes();
    }

    private void configBotoes(){
        view.getNovo().addClickListener(e->{
            view.getNovo().getUI().ifPresent(ui->{
                ui.navigate(NovaProvaView.class, view.getConcurso().getId());
            });
        });

    }

}
