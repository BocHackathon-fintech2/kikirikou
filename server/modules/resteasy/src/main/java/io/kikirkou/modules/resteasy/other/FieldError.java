package io.kikirkou.modules.resteasy.other;

import org.apache.tapestry5.ioc.internal.util.CollectionFactory;

import java.util.List;

public class FieldError {
    private final String field;
    private final List<String> messages;

    private FieldError(Builder builder) {
        this.field = builder.field;
        this.messages = CollectionFactory.newList(builder.messages);
    }

    public String getField() {
        return field;
    }

    public List<String> getMessages() {
        return messages;
    }

    public static class Builder {
        private String field;
        private List<String> messages = CollectionFactory.newList();

        public Builder field(String field) {
            this.field = field;
            return this;
        }

        public Builder message(String message) {
            this.messages.add(message);
            return this;
        }

        public FieldError build() {
            return new FieldError(this);
        }
    }
}
