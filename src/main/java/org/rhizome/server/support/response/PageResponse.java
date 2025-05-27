package org.rhizome.server.support.response;

import java.util.List;

public class PageResponse<T> {
    private final Paging paging;
    private final List<T> results;

    public PageResponse(Paging paging, List<T> results) {
        this.paging = paging;
        this.results = results;
    }

    public static class Paging {
        private final int pageNumber;
        private final int pageSize;
        private final boolean hasNext;
        private final long totalCount;

        public Paging(int pageNumber, int pageSize, boolean hasNext, long totalCount) {
            this.pageNumber = pageNumber;
            this.pageSize = pageSize;
            this.hasNext = hasNext;
            this.totalCount = totalCount;
        }

        public int getPageNumber() {
            return pageNumber;
        }

        public int getPageSize() {
            return pageSize;
        }

        public boolean isHasNext() {
            return hasNext;
        }

        public long getTotalCount() {
            return totalCount;
        }
    }
}
