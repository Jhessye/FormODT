package Util;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

/**
 * Filtro que impede exceder o número máximo de caracteres permitidos.
 */
public class FiltroLimiteCaracteres extends DocumentFilter {

    private final int maxCaracteres;

    public FiltroLimiteCaracteres(int maxCaracteres) {
        this.maxCaracteres = maxCaracteres;
    }

    @Override
    public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
        if (string == null) return;
        if ((fb.getDocument().getLength() + string.length()) <= maxCaracteres) {
            super.insertString(fb, offset, string, attr);
        }
    }

    @Override
    public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
        if (text == null) return;
        int currentLength = fb.getDocument().getLength();
        int newLength = currentLength - length + text.length();

        if (newLength <= maxCaracteres) {
            super.replace(fb, offset, length, text, attrs);
        } else {
            // Se exceder (ex: colar texto longo), trunca para caber exatamente o limite
            int permitido = maxCaracteres - (currentLength - length);
            if (permitido > 0) {
                String sub = text.substring(0, permitido);
                super.replace(fb, offset, length, sub, attrs);
            }
        }
    }
}
