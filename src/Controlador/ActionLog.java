
package Controlador;

/**
 *
 * @author artor
 */


import java.util.LinkedList;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

public class ActionLog {

    private final int MAX_LINEAS = 200;   // límite de mensajes (configurable)
    private final LinkedList<String> mensajes;
    private JTextArea areaTexto;          // TextArea donde se mostrará el log

    public ActionLog() {
        mensajes = new LinkedList<>();
    }

    // Vincula el TextArea de la interfaz
    public void setTextArea(JTextArea areaTexto) {
        this.areaTexto = areaTexto;
    }

    // Agrega un mensaje al log
    public void agregar(String texto) {
        if (texto == null || texto.isBlank()) return;

        mensajes.add(texto);

        // Si supera el máximo, borra los más viejos
        if (mensajes.size() > MAX_LINEAS) {
            mensajes.removeFirst();
        }

        actualizarTextArea();
    }

    // Agrega mensaje con salto de línea automático
    public void agregarLinea(String texto) {
        agregar(texto + "\n");
    }

    // Limpia todo el log
    public void limpiar() {
        mensajes.clear();
        actualizarTextArea();
    }

    // Refresca el contenido del JTextArea en la GUI
    private void actualizarTextArea() {
        if (areaTexto == null) return;

        SwingUtilities.invokeLater(() -> {
            StringBuilder sb = new StringBuilder();
            for (String msg : mensajes) {
                sb.append(msg);
            }
            areaTexto.setText(sb.toString());
            areaTexto.setCaretPosition(areaTexto.getDocument().getLength());
        });
    }
}