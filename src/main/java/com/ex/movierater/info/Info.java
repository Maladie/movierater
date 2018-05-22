package com.ex.movierater.info;

import java.util.UUID;

public class Info {

    private String key;

    private long httpStatusCode;

    private InfoCode infoCode;

    private String description;

    private Object object;

    public Info() {
        key = UUID.randomUUID().toString();
        httpStatusCode = 200L;
        infoCode = InfoCode.OK;
    }

    public String getKey() {
        return key;
    }

    public void setInfoCode(InfoCode infoCode) {
        this.infoCode = infoCode;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public InfoCode getInfoCode() {
        return infoCode;
    }

    public long getHttpStatusCode() {
        return httpStatusCode;
    }

    public void setHttpStatusCode(long httpStatusCode) {
        this.httpStatusCode = httpStatusCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    @Override
    public String toString() {
        return "Info [httpStatusCode=" + httpStatusCode + ", description=" + description + ", object=" + object + "]";
    }


    public static Info succesfulInfo(String description, InfoCode infoCode, Object object) {
        Info.InfoBuilder builder = new Info.InfoBuilder();
        return builder.setDescription(description).setInfoCode(infoCode).setHttpStatusCode(200L).setObject(object).build();
    }

    public static Info succesfulyCreatedInfo(String description, InfoCode infoCode, Object object) {
        Info.InfoBuilder builder = new Info.InfoBuilder();
        return builder.setDescription(description).setInfoCode(infoCode).setHttpStatusCode(201L).setObject(object).build();
    }

    public static Info unsuccesfulInfo(String description, InfoCode infoCode, Object object) {
        Info.InfoBuilder builder = new Info.InfoBuilder();
        return builder.setDescription(description).setInfoCode(infoCode).setHttpStatusCode(400L).setObject(object).build();
    }

    public static Info succesfulyPatchedInfo(String description, InfoCode infoCode, Object object) {
        Info.InfoBuilder builder = new Info.InfoBuilder();
        return builder.setDescription(description).setInfoCode(infoCode).setHttpStatusCode(200L).setObject(object).build();
    }

    public static Info notFound(String description, InfoCode infoCode, Object object) {
        Info.InfoBuilder builder = new Info.InfoBuilder();
        return builder.setDescription(description).setInfoCode(infoCode).setHttpStatusCode(404L).setObject(object).build();
    }

    public static class InfoBuilder {
        private Info info;

        public InfoBuilder() {
            info = new Info();
        }

        public InfoBuilder setHttpStatusCode(long code) {
            info.setHttpStatusCode(code);
            return this;
        }

        public InfoBuilder setDescription(String desc) {
            info.setDescription(desc);
            return this;
        }

        public InfoBuilder setInfoCode(InfoCode infoCode) {
            info.setInfoCode(infoCode);
            return this;
        }

        public InfoBuilder setObject(Object object) {
            info.setObject(object);
            return this;
        }

        public Info build() {
            info.setKey(UUID.randomUUID().toString());
            return info;
        }
    }
}