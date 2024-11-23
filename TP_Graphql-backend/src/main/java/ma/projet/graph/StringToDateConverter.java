package ma.projet.graph;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Component // Ajoutez cette annotation pour enregistrer le convertisseur
public class StringToDateConverter implements Converter<String, Date> {

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    public Date convert(String source) {
        try {
            return dateFormat.parse(source);
        } catch (ParseException e) {
            throw new RuntimeException("Invalid date format. Please use yyyy-MM-dd");
        }
    }
}
