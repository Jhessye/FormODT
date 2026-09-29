import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import View.TelaPrincipal;

public class Main {

    public static void main(String[] args) {
        // Define o Look and Feel nativo do sistema operacional (Windows / etc.)
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            // Em caso de falha, mantém o visual padrão Swing
            e.printStackTrace();
        }

        // Inicia a interface gráfica na Event Dispatch Thread do Swing
        SwingUtilities.invokeLater(() -> {
            TelaPrincipal tela = new TelaPrincipal();
            tela.setVisible(true);
        });
    }
}
