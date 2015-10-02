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
import org.springframework.data.domain.Pageable;

import java.util.List;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by Cuong on 2015/09/30.
 */
@RunWith(MockitoJUnitRunner.class)
public class PaginationTest extends TestCase {

    @InjectMocks
    Pagination pagination;

    @Mock
    Page page;

    @Mock
    Pageable currentPageable;

    int interval;

    @Before
    public void setUp() throws Exception {
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
    public void testGetNumberOfFirstElement() throws Exception {

        //データベースの中にレコードが72です。
        when(page.hasContent()).thenReturn(true);
        // 最初のページのインデックスは "0"です。
        when(page.getNumber()).thenReturn(0);
        // 最初のページのサイズは "10"です。
        when(page.getSize()).thenReturn(10);

        long firstElementActual = pagination.getNumberOfFirstElement();

        // 以下のメソッドは実行されることを確認
        verify(page).hasContent();
        verify(page).getNumber();
        verify(page).getSize();

        //最初のページの最初の要素の値は１です。
        assertEquals(1, firstElementActual);
    }

    /**
     * 　最後の要素の数を取ることのテスト
     *
     * @throws Exception
     */
    @Test
    public void testGetNumberOfLastElement() throws Exception {

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
     * Pageableクラスのリストのテスト
     *<p>Pageableクラスの目的はサイトのページの数</p>
     * @throws Exception
     */
    @Test
    public void testGetPageables() throws Exception {

        // 例えば；ページのトータル８です。現在のページは０です。
        //それで　getPageables(,)のメソッドの結果は０ページ目から5ページ目まで表示です
        when(page.getNumber()).thenReturn(0);

        // 72レコードがあるし、それに各ページは１０レコードがある。それでページの数は８です
        when(page.getTotalPages()).thenReturn(8);

        // Pageablesジュクトのオブジェクトの作成
        when(currentPageable.next()).thenReturn(currentPageable);

        List<Pageable> pageableListActual = pagination.getPageables(currentPageable, interval);

        // 以下のメソッドは実行されることを確認
        verify(page).getTotalPages();
        verify(currentPageable, times(5)).next();
        verify(page, times(4)).getNumber();

        assertEquals(6, pageableListActual.size());

    }
}
