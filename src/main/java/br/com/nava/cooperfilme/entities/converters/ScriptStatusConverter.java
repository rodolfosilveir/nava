package br.com.nava.cooperfilme.entities.converters;

import br.com.nava.cooperfilme.dtos.ScriptStatus;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = false)
public class ScriptStatusConverter implements AttributeConverter<ScriptStatus, String> {

    @Override
    public String convertToDatabaseColumn(ScriptStatus attribute) {
        return attribute == null ? null : attribute.getText();
    }

    @Override
    public ScriptStatus convertToEntityAttribute(String dbData) {
        return dbData == null ? null : ScriptStatus.fromText(dbData);
    }
}
