package org.wallride.core.support;

import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.data.domain.Page;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

/**
 * Created by Cuong on 2015/10/28.
 */
@RunWith(MockitoJUnitRunner.class)
public class PaginationExampleTest extends TestCase {

    @InjectMocks
    PaginationExample example;

    @Mock
    Page page;

    @Test
    public void testGetNumberOfFirstElement() {

        when(page.getNumber()).thenReturn(0);
        when(page.hasContent()).thenReturn(true);
        when(page.getSize()).thenReturn(10);

        int actualIndex = example.getNumberOfFirstElement();

        verify(page).hasContent();
        verify(page).getNumber();
        verify(page).getSize();

        assertThat(1, is(actualIndex));
    }

}
