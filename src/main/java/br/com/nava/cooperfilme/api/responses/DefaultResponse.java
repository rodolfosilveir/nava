package br.com.nava.cooperfilme.api.responses;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Builder;
import lombok.Getter;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DefaultResponse<T> {
    
    private Integer httpStatus;
    private List<String> erros;
    private T resultData;

    @Builder
    public DefaultResponse(Integer httpStatus, List<String> errors, T resultData) {
        this.httpStatus = httpStatus;
        this.erros = errors;
        this.resultData = resultData;
    }
}
