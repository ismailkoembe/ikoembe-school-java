package com.ikoembe.school.models;

public enum Majors {

        MATH("MATH"),
        PHYSICS("PHYSICS"),
        RELIGION("RELIGION"),
        BIOLOGY("BIOLOGY"),
        ENGLISH("ENGLISH"),
        GERMAN("GERMAN"),
        ART("ART"),
        MUSIC("MUSIC");

        public final String name;

        Majors(String name) {
                this.name = name;
        }

}
