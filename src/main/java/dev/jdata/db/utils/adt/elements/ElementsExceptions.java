package dev.jdata.db.utils.adt.elements;

import java.util.NoSuchElementException;

public class ElementsExceptions {

    public static NoSuchElementException emptyException() {

        return new NoSuchElementException();
    }

    public static NoSuchElementException lessThanOneFoundException() {

        return new NoSuchElementException();
    }

    public static IllegalStateException moreThanOneFoundException() {

        return new IllegalStateException();
    }

    public static AlreadyAddedExeption alreadyAddedException() {

        return new AlreadyAddedExeption();
    }
}
