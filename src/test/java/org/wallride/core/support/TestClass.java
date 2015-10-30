package org.wallride.core.support;

import org.junit.Test;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Created by Cuong on 2015/10/29.
 */
public class TestClass extends PaginationExampleTest {

    int interval = 7;
    int totalRecord = 92;
    int pageSize = 20;

    @Test
    public void testGetFirstElement() throws Exception {
        constructorWithPageHasContent(0, pageSize, totalRecord);
        long actualIndex = pagination.getNumberOfFirstElement();

        assertThat(new Long(1), is(actualIndex));
    }

    @Test
    public void testGetLastElement() throws Exception {
        constructorWithPageHasContent(0, pageSize, totalRecord);
        long actualIndex = pagination.getNumberOfLastElement();
        assertThat(new Long(20), is(actualIndex));
    }

    @Test
    public void testGetPagealeWithPageIsStart() {
        constructorWithPageHasContent(0,  pageSize, totalRecord);
        List<Pageable> pageableListActual = pagination.getPageables(pageable, interval);
        assertThat(5, is(pageableListActual.size()));
        assertThat(1, is(pageableListActual.get(0).getPageNumber() + 1));
        assertThat(5, is(pageableListActual.get(4).getPageNumber() + 1));
    }

}
