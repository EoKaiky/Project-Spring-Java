package br.com.estudos.screenmatch.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DadosEpisode(@JsonAlias("Title") String title,
                           @JsonAlias("Episode") Integer numero,
                           @JsonAlias("imdbRating") String rating,
                           @JsonAlias("Released") String dataLancamento) {
}
