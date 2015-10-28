package org.wallride.core.support;


import org.springframework.data.domain.Page;

/**
 * Created by Cuong on 2015/10/28.
 */
public class PaginationExample {

    private Page page;

    public PaginationExample(Page page) {
        this.page = page;
    }

    /**
     * getNumber of first element
     *
     * @return index first element
     */
    public int getNumberOfFirstElement() {
        int index = 0;
        if (page.hasContent()) {
            index = page.getNumber() * page.getSize() + 1;
        }
        return index;
    }
}
