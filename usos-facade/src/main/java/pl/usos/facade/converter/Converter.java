package pl.usos.facade.converter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.apache.commons.lang3.Validate.notNull;

public interface Converter<SOURCE, TARGET> extends org.springframework.core.convert.converter.Converter<SOURCE, TARGET> {

    default List<TARGET> convertAll(Collection<SOURCE> sources) {

        notNull(sources, "Source array cannot be null!");

        List<TARGET> targets = new ArrayList<>();

        for (SOURCE s : sources)
            targets.add(convert(s));

        return targets;
    }
}
