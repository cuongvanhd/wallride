package org.wallride.core.support;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;
import org.junit.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Created by Cuong on 2015/10/28.
 */
public class PaginationExampleTest extends TestCase {

    Page page;
    Pageable pageable;
    Pagination pagination;
    int interval = 5;
    int pageSize = 10;
    int totalRecord = 72;

    /**
     * create list record for a page
     *
     * @param pageSize
     * @return list record in a page
     */
    public List<String> createListRecordForApage(int pageSize) {
        List<String> stringList = new ArrayList<>();
        int i = 0;
        while (i < pageSize) {
            stringList.add("New String" + (i + 1));
            i++;
        }
        return stringList;
    }

    /**
     * constructor With Page Has Content
     *
     * @param pageTest
     * @param pageSize
     * @param totalRecord
     * @throws Exception
     */
    public void constructorWithPageHasContent(int pageTest, int pageSize, int totalRecord) {
        // set index currentPage. index = 0 mean page 1
        pageable = new PageRequest(pageTest, pageSize);
        // total has 72 record and 8 pages. Page from 1 to 7 has 10 record; page 8 has 2 record
        page = new PageImpl(createListRecordForApage(pageSize), pageable, totalRecord);

        pagination = new Pagination(page);
    }

    /**
     * constructor With Page Has Content
     *
     * @param pageTest
     * @param pageSize
     * @param totalRecord
     * @throws Exception
     */
    public void constructorWithPageHasNotContent(int pageTest, int pageSize, int totalRecord) {
        pageable = new PageRequest(pageTest, pageSize);
        page = new PageImpl(new ArrayList(), pageable, totalRecord);
        pagination = new Pagination(page);
    }

    /**
     * test Get Number Of First Element When Page Has Content
     * <p>Data Test: page 1</p>
     *
     * @throws Exception
     */
    @Test
    public void testGetNumberOfFirstElementWhenPageHasContent() throws Exception {
//        pageable = new PageRequest(0, 10);
//        page = new PageImpl(createListRecordForApage(), pageable, 72);
//        pagination = new Pagination(page);
        constructorWithPageHasContent(0, pageSize, totalRecord);

        long actualIndex = pagination.getNumberOfFirstElement();

        assertThat(new Long(1), is(actualIndex));
    }

    /**
     * test Get Number Of First Element When Page Has Not Content
     * <p>Data Test: page 1</p>
     *
     * @throws Exception
     */
    @Test
    public void testGetNumberOfFirstElementWhenPageHasNotContent() throws Exception {
//        pageable = new PageRequest(0, 10);
//        page = new PageImpl(new ArrayList(), pageable, 72);
//        pagination = new Pagination(page);
        constructorWithPageHasNotContent(0, pageSize, totalRecord);

        long actualIndex = pagination.getNumberOfFirstElement();

        assertThat(new Long(0), is(actualIndex));
    }

    /**
     * test Get Number Of Last Element When Page Has Content
     * <p>Data Test: page 1</p>
     *
     * @throws Exception
     */
    @Test
    public void testGetNumberOfLastElementWhenPageHasContent() throws Exception {
        constructorWithPageHasContent(0, pageSize, totalRecord);

        long actualIndex = pagination.getNumberOfLastElement();

        assertThat(new Long(10), is(actualIndex));
    }

    /**
     * test Get Number Of Last Element When Page Has not Content
     * <p>Data Test: page 1</p>
     *
     * @throws Exception
     */
    @Test
    public void testGetNumberOfLastElementWhenPageHasNotContent() throws Exception {
        constructorWithPageHasNotContent(0, pageSize, totalRecord);

        long actualIndex = pagination.getNumberOfLastElement();

        assertThat(new Long(0), is(actualIndex));
    }

    /**
     * test Get Pagable When Page Is Between Start And End
     * <p>Data Test: Page 4 (page 4 of index is 3)</p>
     *
     * @throws Exception
     */
    @Test
    public void testGetPagableWhenPageIsBetweenStartAndEnd() throws Exception {

        // set current page is 4. (index page 4 is 3.)
        constructorWithPageHasContent(3, pageSize, totalRecord);

        List<Pageable> pageableListActual = pagination.getPageables(pageable, interval);

        assertThat(8, is(pageableListActual.size()));
        assertThat(1, is(pageableListActual.get(0).getPageNumber() + 1));
        assertThat(8, is(pageableListActual.get(7).getPageNumber() + 1));
    }

    /**
     * test Get Pagable When Page Is Start
     * <p>Data Test: Page start: (start page of index is 0)</p>
     *
     * @throws Exception
     */
    @Test
    public void testGetPagableWhenPageIsStart() throws Exception {
        constructorWithPageHasContent(0, pageSize, totalRecord);

        List<Pageable> pageableListActual = pagination.getPageables(pageable, interval);

        assertThat(6, is(pageableListActual.size()));
        assertThat(1, is(pageableListActual.get(0).getPageNumber() + 1));
        assertThat(6, is(pageableListActual.get(5).getPageNumber() + 1));

    }

    /**
     * test Get Pagable When Page Is Start
     * <p>Data Test: Page end: (end page is 7)</p>
     *
     * @throws Exception
     */
    @Test
    public void testGetPagableWhenPageIsEnd() throws Exception {
        constructorWithPageHasContent(7, pageSize, totalRecord);

        List<Pageable> pageableListActual = pagination.getPageables(pageable, interval);

        assertThat(6, is(pageableListActual.size()));
        assertThat(3, is(pageableListActual.get(0).getPageNumber() + 1));
        assertThat(8, is(pageableListActual.get(5).getPageNumber() + 1));

    }

}
