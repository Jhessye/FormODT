package Util;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

/**
 * Filtro que impede digitação de letras ou caracteres especiais em tempo real,
 * permitindo estritamente números (0-9).
 */
public class FiltroSomenteNumeros extends DocumentFilter {

    private final int limiteMaximo;

    public FiltroSomenteNumeros() {
        this(-1);
    }

    public FiltroSomenteNumeros(int limiteMaximo) {
        this.limiteMaximo = limiteMaximo;
    }

    @Override
    public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
        if (string == null) return;
        if (isApenasDigitos(string) && respeitaLimite(fb, string.length())) {
            super.insertString(fb, offset, string, attr);
        }
    }

    @Override
    public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
        if (text == null) return;
        if (isApenasDigitos(text) && respeitaLimite(fb, text.length() - length)) {
            super.replace(fb, offset, length, text, attrs);
        }
    }

    private boolean isApenasDigitos(String text) {
        for (int i = 0; i < text.length(); i++) {
            if (!Character.isDigit(text.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    private boolean respeitaLimite(FilterBypass fb, int delta) {
        if (limiteMaximo <= 0) return true;
        return (fb.getDocument().getLength() + delta) <= limiteMaximo;
    }
}
