package dto;

import java.util.List;
import java.util.function.Function;

public class PageData<T> {
    public List<T> items;
    public int page;
    public int size;
    public long totalElements;
    public long totalPages;

    public PageData(List<T> items, int page, int size, long totalElements) {
        this.items = items;
        this.page = page;
        this.size = size;
        this.totalElements = totalElements;
        this.totalPages = totalElements / size + (totalElements % size == 0 ? 0 : 1);
    }

    public <R> PageData<R> map(Function<T, R> mapper) {
        return new PageData<>(items.stream().map(mapper).toList(), page, size, totalElements);
    }
}
