package org.wallride.core.support;

import java.util.List;

import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

/**
 * Created by Cuong on 2015/09/30.
 */
@RunWith(MockitoJUnitRunner.class)
public class PaginationTest extends TestCase
{
    @InjectMocks
    Pagination pagination;

    @Mock
    Page page;

    int interval;

    @Before
    public void setUp() throws Exception
    {
        super.setUp();
        MockitoAnnotations.initMocks(this);
        interval = 5;
    }

    /**
     * 　最初の要素の数を取ることのテスト
     *
     * @throws Exception
     */
    @Test
    public void testGetNumberOfFirstElement() throws Exception
    {
//        List<String> stringList = new ArrayList<>();
//        stringList.add("String1");
//        Pageable pageable = new PageRequest(0, 10);
//        Page page = new PageImpl(stringList,pageable,10);
//        Pagination pagination = new Pagination(page);
//        long number = pagination.getNumberOfFirstElement();
//
//        assertEquals(1, number);

        //データベースの中にレコードが72です。
        when(page.hasContent()).thenReturn(true);
        //最初のページのインデックスは "0"です。

        when(page.getNumber()).thenReturn(0);
        //  最初のページのサイズは "10"です。
        when(page.getSize()).thenReturn(10);

        long firstElementActual = pagination.getNumberOfFirstElement();

        verify(page).hasContent();
        verify(page).getNumber();
        verify(page).getSize();

        //assertEquals(1, firstElementActual);
        assertThat(firstElementActual, is((long) 1));
    }

    /**
     * 　最後の要素の数を取ることのテスト
     *
     * @throws Exception
     */
    @Test
    public void testGetNumberOfLastElement() throws Exception
    {

        // 最初のページの値のテスト
        when(page.hasContent()).thenReturn(true);
        when(page.getNumberOfElements()).thenReturn(10);

        long lastElementActual = pagination.getNumberOfLastElement();

        // 以下のメソッドは実行されることを確認
        verify(page,times(2)).hasContent();
        verify(page).getNumberOfElements();

        //assertEquals(10, lastElementActual);
        assertThat(lastElementActual, is((long) 10));
    }

    /**
     * ページはスタートになって、Pageablesをゲットすることのテスト
     *
     * @throws Exception
     */
    @Test
    public void testGetPageablesWhenPageIsStart() throws Exception
    {
        // pageのインデックスは0から始める.
        Pageable currentPageable = new PageRequest(0, 10);

        when(page.getNumber()).thenReturn(0);

        when(page.getTotalPages()).thenReturn(8);

        List<Pageable> pageableListActual = pagination.getPageables(currentPageable, interval);

        // 以下のメソッドは実行されることを確認
        verify(page).getTotalPages();
        verify(page, times(4)).getNumber();


//        assertEquals(6, pageableListActual.size())
        assertThat(pageableListActual.size(), is(6));
//        assertEquals(1, pageableListActual.get(0).getPageNumber() + 1);
        assertThat(pageableListActual.get(0).getPageNumber() + 1, is(1));
//        assertEquals(6, pageableListActual.get(5).getPageNumber() + 1);
        assertThat(pageableListActual.get(5).getPageNumber() + 1, is(6));
    }

    /**
     * ページは4になって、Pageablesをゲットすることのテスト
     *（０ページから５ページは何もいいです。例えばこっち）
     * @throws Exception
     */
    @Test
    public void testGetPageablesWhenPageIsBetweenStartandEnd() throws Exception
    {
        // pageのインデックスは0から始める。だから4ページのインデックスは3になる。
        Pageable currentPageable = new PageRequest(3, 10);

        when(page.getNumber()).thenReturn(3);

        when(page.getTotalPages()).thenReturn(8);

        List<Pageable> pageableListActual = pagination.getPageables(currentPageable, interval);

        // 以下のメソッドは実行されることを確認
        verify(page, times(2)).getTotalPages();
        verify(page, times(4)).getNumber();

//        assertEquals(8, pageableListActual.size());
        assertThat(pageableListActual.size(), is(8));
//        assertEquals(1, pageableListActual.get(0).getPageNumber() + 1);
        assertThat(pageableListActual.get(0).getPageNumber() + 1, is(1));
//        assertEquals(8, pageableListActual.get(7).getPageNumber() + 1);
        assertThat(pageableListActual.get(7).getPageNumber() + 1, is(8));
    }

    /**
     * ページはエンドになって、Pageablesをゲットすることのテスト
     *
     * @throws Exception
     */
    @Test
    public void testGetPageablesWhenPageIsEnd() throws Exception
    {
        // pageのインデックスは0から始める。だから最後のページのインデックスは７になる。
        Pageable currentPageable = new PageRequest(7, 10);

        when(page.getNumber()).thenReturn(7);

        when(page.getTotalPages()).thenReturn(8);

        List<Pageable> pageableListActual = pagination.getPageables(currentPageable, interval);

        // 以下のメソッドは実行されることを確認
        verify(page, times(2)).getTotalPages();
        verify(page, times(4)).getNumber();

//        assertEquals(6, pageableListActual.size());
        assertThat(pageableListActual.size(), is(6));
//        assertEquals(3, pageableListActual.get(0).getPageNumber() + 1);
        assertThat(pageableListActual.get(0).getPageNumber() + 1, is(3));
//        assertEquals(8, pageableListActual.get(5).getPageNumber() + 1);
        assertThat(pageableListActual.get(5).getPageNumber() + 1, is(8));
    }
}
