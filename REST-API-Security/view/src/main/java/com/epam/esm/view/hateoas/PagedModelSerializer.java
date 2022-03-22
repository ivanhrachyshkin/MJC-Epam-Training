package com.epam.esm.view.hateoas;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.springframework.hateoas.PagedModel;

import java.io.IOException;

public class PagedModelSerializer extends StdSerializer<PagedModel> {

    public PagedModelSerializer() {
        super(PagedModel.class);
    }

    @Override
    public void serialize(PagedModel value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeStartObject();
        gen.writeFieldName("content");
        provider.defaultSerializeValue(value.getContent(), gen);
        gen.writeFieldName("nextLink");
        provider.defaultSerializeValue(value.getNextLink(), gen); //todo
        gen.writeFieldName("previousLink");
        provider.defaultSerializeValue(value.getPreviousLink(), gen);
        gen.writeEndObject();
    }
}
