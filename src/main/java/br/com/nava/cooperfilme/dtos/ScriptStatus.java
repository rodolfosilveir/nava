package br.com.nava.cooperfilme.dtos;

import br.com.nava.cooperfilme.exceptions.ScriptStatusNotFoundException;
import lombok.Generated;
import lombok.Getter;

public enum ScriptStatus {
    
    AGUARDANDO_ANALISE("aguardando_analise"),
    EM_ANALISE("em_analise"),
    AGUARDANDO_REVISAO("aguardando_revisao"),
    EM_REVISAO("em_revisao"),
    AGUARDANDO_APROVACAO("aguardando_aprovacao"),
    EM_APROVACAO("em_aprovacao"),
    APROVADO("aprovado"),
    RECUSADO("recusado"),
    ERRO("erro");

    @Getter
    private String text;

    ScriptStatus(String scriptStatus){
        this.text = scriptStatus;
    }

    public static ScriptStatus fromText(String text){
        for (ScriptStatus scriptStatus: ScriptStatus.values()) {
            if (scriptStatus.getText().toUpperCase().equals(text.toUpperCase())) {
                return scriptStatus;
            }
        }

        throw new ScriptStatusNotFoundException(text);
    }

    @Generated
    public static String getDescriptions() {
        StringBuilder descriptions = new StringBuilder();
        for (ScriptStatus scriptStatus: ScriptStatus.values()) {
            descriptions.append("'").append(scriptStatus.getText()).append("', ");
        }
        return descriptions.substring(0, descriptions.length() - 2);
    }
}
