package br.com.estudos.screenmatch.principal;

import br.com.estudos.screenmatch.model.DadosEpisode;
import br.com.estudos.screenmatch.model.DadosSerie;
import br.com.estudos.screenmatch.model.DadosTemporada;
import br.com.estudos.screenmatch.model.Episodios;
import br.com.estudos.screenmatch.service.ConsumoApi;
import br.com.estudos.screenmatch.service.ConverteDados;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Principal {

    private Scanner leitura = new Scanner(System.in);
    private ConsumoApi consumo = new ConsumoApi();
    private ConverteDados conversor = new ConverteDados();
    private final String ENDERECO = "https://www.omdbapi.com/?t=";
    private final String API_KEY = "&apikey=b370b0c";

    public void exibeMenu(){
        System.out.println("Digite o nome da série para buscar: ");
        var nomeSerie = leitura.nextLine();
        var json = consumo.obterDados(ENDERECO + nomeSerie.replace(" ", "+") + API_KEY);
        DadosSerie dados = conversor.ObterDados(json, DadosSerie.class);
        System.out.println(dados);

        List<DadosTemporada> temporadas = new ArrayList<>();


		for (int i = 1; i<=dados.totalSeasons(); i++) {
			json = consumo.obterDados(ENDERECO + nomeSerie.replace(" ", "+") +"&season=" + i + API_KEY);
			DadosTemporada dadosTemporada = conversor.ObterDados(json, DadosTemporada.class);
			temporadas.add(dadosTemporada);

		}
		temporadas.forEach(System.out::println);

//        for (int i = 0; i < dados.totalSeasons(); i++ ){
//            List<DadosEpisode> episodiosTemporada = temporadas.get(i).episodes();
//            for (int j = 0; j < episodiosTemporada.size(); j++){
//                System.out.println(episodiosTemporada.get(j).title());
//            }
//        }

        temporadas.forEach(t -> t.episodes().forEach(e -> System.out.println(e.title())));
        List<DadosEpisode> dadosEpisodes = temporadas.stream()
                .flatMap(t -> t.episodes().stream())
                .collect(Collectors.toList());

        System.out.println("\nTop 5 episódios");
        dadosEpisodes.stream()
                .filter(e -> !e.rating().equalsIgnoreCase("N/A"))
                .sorted(Comparator.comparing(DadosEpisode::rating).reversed())
                .limit(5)
                .forEach(System.out::println);

        List<Episodios> episodios = temporadas.stream()
                .flatMap(t -> t.episodes().stream()
                        .map(d -> new Episodios(t. numero(), d))
                ).collect(Collectors.toList());

        episodios.forEach(System.out::println);
    }

}
