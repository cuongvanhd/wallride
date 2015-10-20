package org.wallride.core.support;

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

import java.util.List;

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
//
        long firstElementActual = pagination.getNumberOfFirstElement();
//        // 以下のメソッドは実行されることを確認
        verify(page).hasContent();
        verify(page).getNumber();
        verify(page).getSize();

//        //最初のページの最初の要素の値は１です。
        assertEquals(1, firstElementActual);
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

        //最初のページの最後の要素の値は１０です
        assertEquals(10, lastElementActual);
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

        // 72レコードがあるし、それに各ページは１０レコードがある。それでページの数は８です
        when(page.getTotalPages()).thenReturn(8);

        List<Pageable> pageableListActual = pagination.getPageables(currentPageable, interval);

        // 以下のメソッドは実行されることを確認
        verify(page).getTotalPages();
        verify(page, times(4)).getNumber();

        // 例えば；ページのトータル８です。現在のページは０です。
        //それで　getPageables(,)のメソッドの結果は０ページ目から5ページ目まで表示です
        // リストのサイズを確認する
        assertEquals(6, pageableListActual.size());
        // スタートのページを確認する
        assertEquals(1, pageableListActual.get(0).getPageNumber() + 1);
        // エンドのページを確認する
        assertEquals(6, pageableListActual.get(5).getPageNumber() + 1);
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

        // 例えば；ページのトータル８です。現在のページは０です。
        //それで　getPageables(,)のメソッドの結果は０ページ目から5ページ目まで表示です
        when(page.getNumber()).thenReturn(3);

        // 72レコードがあるし、それに各ページは１０レコードがある。それでページの数は８です
        when(page.getTotalPages()).thenReturn(8);

        List<Pageable> pageableListActual = pagination.getPageables(currentPageable, interval);

        // 以下のメソッドは実行されることを確認
        verify(page, times(2)).getTotalPages();
        verify(page, times(4)).getNumber();

        // リストのサイズを確認する
        assertEquals(8, pageableListActual.size());
        // スタートのページを確認する
        assertEquals(1, pageableListActual.get(0).getPageNumber() + 1);
        // エンドのページを確認する
        assertEquals(8, pageableListActual.get(7).getPageNumber() + 1);
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

        // 例えば；ページのトータル８です。現在のページは０です。
        //それで　getPageables(,)のメソッドの結果は０ページ目から5ページ目まで表示です
        when(page.getNumber()).thenReturn(7);

        // 72レコードがあるし、それに各ページは１０レコードがある。それでページの数は８です
        when(page.getTotalPages()).thenReturn(8);

        List<Pageable> pageableListActual = pagination.getPageables(currentPageable, interval);

        // 以下のメソッドは実行されることを確認
        verify(page, times(2)).getTotalPages();
        verify(page, times(4)).getNumber();

        // リストのサイズを確認する
        assertEquals(6, pageableListActual.size());
        // スタートのページを確認する
        assertEquals(3, pageableListActual.get(0).getPageNumber() + 1);
        // エンドのページを確認する
        assertEquals(8, pageableListActual.get(5).getPageNumber() + 1);
    }
}
