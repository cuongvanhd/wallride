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

    @Before
    public void setUp() throws Exception {
        super.setUp();
        MockitoAnnotations.initMocks(this);
    }

    /**
     * 　最初の要素の数を取ることのテスト
     *
     * @throws Exception
     */
    @Test
    public void testGetNumberOfFirstElement() throws Exception {

        // 最初のページの値のテスト
        when(page.hasContent()).thenReturn(true);
        when(page.getNumber()).thenReturn(0);
        when(page.getSize()).thenReturn(10);

        long firstElementActual = pagination.getNumberOfFirstElement();

        verify(page).hasContent();
        verify(page).getNumber();
        verify(page).getSize();

        // 以下のメソッドの実行されることを確認
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

        // 以下のメソッドの実行されることを確認
        verify(page).hasContent();
        verify(page).getNumberOfElements();

        assertEquals(10, lastElementActual);
    }

    /**
     * Pageableクラスのリストのテスト
     *
     * @throws Exception
     */
    @Test
    public void testGetPageables1() throws Exception {

    }
}