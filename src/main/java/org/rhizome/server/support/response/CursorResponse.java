package org.rhizome.server.support.response;

import java.util.List;

public class CursorResponse<T, C> {
    private final C next;
    private final List<T> results;

    public CursorResponse(C next, List<T> results) {
        this.next = next;
        this.results = results;
    }
}
