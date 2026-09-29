package Util;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;
import javax.swing.text.JTextComponent;

public class ComponentUtils {

    private static final Color COLOR_DISABLED_BG = new Color(240, 240, 240);
    private static final Color COLOR_ENABLED_BG = Color.WHITE;
    private static final Color COLOR_TITLE_ACTIVE = new Color(20, 60, 120);
    private static final Color COLOR_TITLE_INACTIVE = new Color(150, 150, 150);

    /**
     * Habilita ou desabilita recursivamente um componente e todos os seus filhos.
     * Ajusta cores de fundo e rótulos para evidenciar o estado visual (cinza/ativo).
     */
    public static void setEnabledRecursive(Component component, boolean enabled) {
        component.setEnabled(enabled);

        if (component instanceof JTextComponent) {
            JTextComponent textComp = (JTextComponent) component;
            textComp.setEditable(enabled);
            textComp.setBackground(enabled ? COLOR_ENABLED_BG : COLOR_DISABLED_BG);
        } else if (component instanceof JLabel) {
            JLabel label = (JLabel) component;
            label.setForeground(enabled ? new Color(30, 30, 30) : Color.GRAY);
        } else if (component instanceof JPanel) {
            JPanel panel = (JPanel) component;
            if (panel.getBorder() instanceof TitledBorder) {
                TitledBorder border = (TitledBorder) panel.getBorder();
                border.setTitleColor(enabled ? COLOR_TITLE_ACTIVE : COLOR_TITLE_INACTIVE);
                panel.repaint();
            }
        }

        if (component instanceof Container) {
            Container container = (Container) component;
            for (Component child : container.getComponents()) {
                setEnabledRecursive(child, enabled);
            }
        }
    }
}
