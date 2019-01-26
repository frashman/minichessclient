package de.pki.minichess.game.util;

import de.pki.minichess.game.Color;
import de.pki.minichess.game.utils.PieceUtil;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class PieceUtilTest {
    @Test
    public void shouldReturnWhiteForUpperCase() {
        Color expectedColor = Color.WHITE;
        Color resultColor = PieceUtil.getColorForPiece('Q');
        assertThat(resultColor, is(expectedColor));
    }

    @Test
    public void shouldReturnBlackForLowerCase() {
        Color expectedColor = Color.BLACK;
        Color resultColor = PieceUtil.getColorForPiece('k');
        assertThat(resultColor, is(expectedColor));
    }

    @Test
    public void shouldReturnEmptyForDot() {
        Color expectedColor = Color.EMPTY;
        Color resultColor = PieceUtil.getColorForPiece('.');
        assertThat(resultColor, is(expectedColor));
    }
}
