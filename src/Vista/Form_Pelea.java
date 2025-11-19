package Vista;

import Controlador.ActionLog;
import Controlador.Batalla;
import Controlador.Encuentro;
import Controlador.ProgresionMapa;
import Modelo.Heroe;
import Modelo.Villano;
import javax.swing.ImageIcon;
import java.awt.Image;
import java.net.URL;
import javax.swing.SwingUtilities;

/**
 *
 * @author Panky
 */
public class Form_Pelea extends javax.swing.JFrame {

    /**
     * Creates new form Form_Pelea
     */
    private ProgresionMapa pro;
    private Heroe heroeDelJuego; // Para guardar la referencia del héroe
    private ActionLog actionLog;
    private Villano villanoActual;

    // Constructor modificado para recibir el héroe
    public Form_Pelea(Heroe heroe) {
        this.heroeDelJuego = heroe;
        initComponents(); // Inicializa componentes de Swing
        setIconImage(new ImageIcon(getClass().getResource("/Imagenes/Logo.png")).getImage());
        setTitle("Batman Rogue");
        setLocationRelativeTo(null);
        
        actionLog = new ActionLog();
        actionLog.setTextArea(Jtxt_Log);
        Jbtn_Continue.setVisible(false);
        inicializarJuego(); // Nuevo método para configurar la lógica del juego

    }

    //metodo para cargar las imagenes durante la pelea
    private void cargarImagen(Modelo.Personaje personaje, javax.swing.JLabel label) {
        try {
            if (personaje == null || personaje.getImagenPath() == null) {
                label.setIcon(null);
                label.setText("Sin imagen");
                return;
            }
            //ruta
            String ruta = "/ImagenesPersonajes/" + personaje.getImagenPath();
            java.net.URL url = getClass().getResource(ruta);

            if (url == null) {
                System.err.println("Imagen no encontrada en la ruta: " + ruta);
                label.setText("No existe: " + personaje.getImagenPath());
                label.setIcon(null);
                return;
            }
            ImageIcon icono = new ImageIcon(url);

            int w = label.getWidth();
            int h = label.getHeight();

            Image img = icono.getImage();
            img = img.getScaledInstance(w, h, Image.SCALE_SMOOTH);

            label.setText("");
            label.setIcon(new ImageIcon(img));

        } catch (Exception e) {
            label.setText("Error de imagen");
        }
    }

    private void inicializarJuego() {

        Encuentro encuentro = new Encuentro();
        Batalla batalla = new Batalla();

        pro = new ProgresionMapa(1, encuentro, batalla, heroeDelJuego, null, Jbtn_Continue, actionLog);
        pro.setLog(actionLog);
        pro.setFormPelea(this);

        // Inicia el primer encuentro inmediatamente
        pro.iniciarEncuentro(heroeDelJuego);
        villanoActual = pro.getVillanoActual();

        //Inicializando barras de hp
        configurarBarras();

        //Actualizar datos
        actualizarBarrasHP();
        actualizarBarrasHPVillano();
        actualizarEspecialHero();

        cargarImagen(heroeDelJuego, Jlbl_SpriteHero);
        cargarImagen(villanoActual, Jlbl_SpriteVillain);

    }

    private void configurarBarras() {
        // Héroe
        Jpbr_HpHero.setMinimum(0);
        Jpbr_HpHero.setMaximum(heroeDelJuego.getHpMax());
        Jpbr_HpHero.setValue(heroeDelJuego.getHp());

        // Villano
        if (villanoActual != null) {
            Jpbr_HpVillain.setMinimum(0);
            Jpbr_HpVillain.setMaximum(villanoActual.getHpMax());
            Jpbr_HpVillain.setValue(villanoActual.getHp());
        }
    }

    public void actualizarEspecialHero() {
        // 1. Obtener el nuevo valor de HP
        int espec = heroeDelJuego.getEspecial();
        int costo = heroeDelJuego.getCostoEspecial();

        // 2. Formatear y establecer el texto en el JLabel
        Jlbl_Sp.setText("Energia: " + espec + "/Costo " + costo);

    }

    public void actualizarHPLabel() {
        int nuevoHP = heroeDelJuego.getHp();

        // AcJlbl_hptualiza etiqueta
        Jlbl_hp.setText("HP: " + nuevoHP + "/" + heroeDelJuego.getHpMax());

        // Actualiza barra
        Jpbr_HpHero.setValue(nuevoHP);
    }

    public void actualizarHPVillanoLabel() {
        villanoActual = pro.getVillanoActual();

        if (villanoActual != null) {
            int hp = villanoActual.getHp();
            int max = villanoActual.getHpMax();

            Jlbl_hpVillano.setText("HP: " + hp + "/" + max);

            Jpbr_HpVillain.setMaximum(max);
            Jpbr_HpVillain.setValue(hp);
        } else {
            Jlbl_hpVillano.setText("HP: --");
            Jpbr_HpVillain.setValue(0);
        }
    }

    public void actualizarBarrasHP() {
        Jpbr_HpHero.setValue(heroeDelJuego.getHp());
    }

    public void actualizarBarrasHPVillano() {
        if (villanoActual != null) {
            Jpbr_HpVillain.setValue(villanoActual.getHp());
        }
    }

    public void actualizarVillano() {
        this.villanoActual = pro.getVillanoActual();

        if (villanoActual != null) {
            actualizarHPVillanoLabel();
            cargarImagen(villanoActual, Jlbl_SpriteVillain);
        } else {
            Jlbl_hpVillano.setText("HP: --");
            Jlbl_SpriteVillain.setIcon(null);
            Jlbl_SpriteVillain.setText("Sin villano");
        }
    }

    public void deshabilitarBotonesAccion() {
        // Se establece la propiedad setEnabled en false para cada botón.
        Jbtn_Ataque.setEnabled(false);
        Jbtn_Defensa.setEnabled(false);
        Jbtn_SpAtaque.setEnabled(false);
        // ... incluye aquí cualquier otro botón de acción que deba bloquearse
    }

    /**
     * Habilita los botones de acción del jugador.
     */
    public void habilitarBotonesAccion() {
        // Se establece la propiedad setEnabled en true para cada botón.
        Jbtn_Ataque.setEnabled(true);
        Jbtn_Defensa.setEnabled(true);
        Jbtn_SpAtaque.setEnabled(true);
        // ... incluye aquí cualquier otro botón de acción que deba desbloquearse
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        Jlbl_Sp = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        Jlbl_hpVillano = new javax.swing.JLabel();
        Jlbl_hp = new javax.swing.JLabel();
        Jbtn_Continue = new javax.swing.JButton();
        Jpbr_HpHero = new javax.swing.JProgressBar();
        Jpbr_HpVillain = new javax.swing.JProgressBar();
        Jpbr_Sp = new javax.swing.JProgressBar();
        Jlbl_SpriteHero = new javax.swing.JLabel();
        Jlbl_SpriteVillain = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        Jbtn_Ataque = new javax.swing.JButton();
        Jbtn_SpAtaque = new javax.swing.JButton();
        Jbtn_Defensa = new javax.swing.JButton();
        Jbtn_Salir = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        Jtxt_Log = new javax.swing.JTextArea();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel4.setFont(new java.awt.Font("Comic Sans MS", 3, 24)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 0, 0));
        jLabel4.setText("ATTACK");
        getContentPane().add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(720, 640, -1, -1));

        jLabel5.setFont(new java.awt.Font("Comic Sans MS", 3, 24)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 0, 0));
        jLabel5.setText("SP ATTACK");
        getContentPane().add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(900, 640, 170, -1));

        Jlbl_Sp.setFont(new java.awt.Font("Segoe UI", 3, 24)); // NOI18N
        Jlbl_Sp.setText("ENERGIA: /");
        getContentPane().add(Jlbl_Sp, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 530, -1, -1));

        jLabel7.setFont(new java.awt.Font("Comic Sans MS", 3, 24)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 0, 0));
        jLabel7.setText("EXIT");
        getContentPane().add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(930, 720, -1, -1));

        jLabel6.setFont(new java.awt.Font("Comic Sans MS", 3, 24)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 0, 0));
        jLabel6.setText("GUARD");
        getContentPane().add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(730, 720, -1, -1));

        Jlbl_hpVillano.setFont(new java.awt.Font("Segoe UI", 3, 24)); // NOI18N
        Jlbl_hpVillano.setText("HP: /");
        getContentPane().add(Jlbl_hpVillano, new org.netbeans.lib.awtextra.AbsoluteConstraints(820, 480, -1, -1));

        Jlbl_hp.setFont(new java.awt.Font("Segoe UI", 3, 24)); // NOI18N
        Jlbl_hp.setText("HP: /");
        getContentPane().add(Jlbl_hp, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 480, -1, -1));

        Jbtn_Continue.setBackground(new java.awt.Color(0, 0, 0));
        Jbtn_Continue.setFont(new java.awt.Font("Comic Sans MS", 3, 18)); // NOI18N
        Jbtn_Continue.setForeground(new java.awt.Color(255, 255, 102));
        Jbtn_Continue.setText("Continue");
        Jbtn_Continue.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Jbtn_ContinueActionPerformed(evt);
            }
        });
        getContentPane().add(Jbtn_Continue, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 540, -1, -1));
        getContentPane().add(Jpbr_HpHero, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 480, 370, 30));
        getContentPane().add(Jpbr_HpVillain, new org.netbeans.lib.awtextra.AbsoluteConstraints(666, 480, 380, 30));
        getContentPane().add(Jpbr_Sp, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 530, 370, 30));

        Jlbl_SpriteHero.setText("Sprite Hero");
        getContentPane().add(Jlbl_SpriteHero, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 20, 370, 440));

        Jlbl_SpriteVillain.setText("Sprite Villain");
        getContentPane().add(Jlbl_SpriteVillain, new org.netbeans.lib.awtextra.AbsoluteConstraints(670, 30, 370, 440));

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/Gotham (1).jpg"))); // NOI18N
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/Texto.png"))); // NOI18N
        getContentPane().add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 600, -1, -1));

        Jbtn_Ataque.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/boton.png"))); // NOI18N
        Jbtn_Ataque.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Jbtn_AtaqueActionPerformed(evt);
            }
        });
        getContentPane().add(Jbtn_Ataque, new org.netbeans.lib.awtextra.AbsoluteConstraints(700, 630, 150, 60));

        Jbtn_SpAtaque.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/boton.png"))); // NOI18N
        Jbtn_SpAtaque.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Jbtn_SpAtaqueActionPerformed(evt);
            }
        });
        getContentPane().add(Jbtn_SpAtaque, new org.netbeans.lib.awtextra.AbsoluteConstraints(890, 630, 150, 60));

        Jbtn_Defensa.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/boton.png"))); // NOI18N
        Jbtn_Defensa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Jbtn_DefensaActionPerformed(evt);
            }
        });
        getContentPane().add(Jbtn_Defensa, new org.netbeans.lib.awtextra.AbsoluteConstraints(700, 710, 150, 60));

        Jbtn_Salir.setForeground(new java.awt.Color(255, 0, 0));
        Jbtn_Salir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/boton.png"))); // NOI18N
        Jbtn_Salir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Jbtn_SalirActionPerformed(evt);
            }
        });
        getContentPane().add(Jbtn_Salir, new org.netbeans.lib.awtextra.AbsoluteConstraints(890, 710, 150, 60));

        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/Botones.png"))); // NOI18N
        getContentPane().add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(660, 590, -1, -1));

        Jtxt_Log.setColumns(20);
        Jtxt_Log.setRows(5);
        jScrollPane1.setViewportView(Jtxt_Log);

        getContentPane().add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 620, 600, 170));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void Jbtn_AtaqueActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Jbtn_AtaqueActionPerformed
        pro.jugadorAccion(1);
        actualizarHPLabel();
        actualizarHPVillanoLabel();
    }//GEN-LAST:event_Jbtn_AtaqueActionPerformed

    private void Jbtn_SpAtaqueActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Jbtn_SpAtaqueActionPerformed
        pro.jugadorAccion(2);
        actualizarHPLabel();
        actualizarHPVillanoLabel();
        actualizarEspecialHero();
    }//GEN-LAST:event_Jbtn_SpAtaqueActionPerformed

    private void Jbtn_DefensaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Jbtn_DefensaActionPerformed
        pro.jugadorAccion(3);
        actualizarHPLabel();
    }//GEN-LAST:event_Jbtn_DefensaActionPerformed

    private void Jbtn_SalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Jbtn_SalirActionPerformed
        MenuPrincipal m = new MenuPrincipal();
        m.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_Jbtn_SalirActionPerformed

    private void Jbtn_ContinueActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Jbtn_ContinueActionPerformed
        pro.avanzarDePiso();
        actualizarHPVillanoLabel();
    }//GEN-LAST:event_Jbtn_ContinueActionPerformed

//    /**
//     * @param args the command line arguments
//     */
//    public static void main(String args[]) {
//        /* Set the Nimbus look and feel */
//        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
//        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
//         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
//         */
//        try {
//            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
//                if ("Nimbus".equals(info.getName())) {
//                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
//                    break;
//                }
//            }
//        } catch (ClassNotFoundException ex) {
//            java.util.logging.Logger.getLogger(Form_Pelea.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (InstantiationException ex) {
//            java.util.logging.Logger.getLogger(Form_Pelea.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (IllegalAccessException ex) {
//            java.util.logging.Logger.getLogger(Form_Pelea.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
//            java.util.logging.Logger.getLogger(Form_Pelea.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        }
//        //</editor-fold>
//
//        /* Create and display the form */
//        java.awt.EventQueue.invokeLater(new Runnable() {
//            public void run() {
//                new Form_Pelea().setVisible(true);
//            }
//        });
//    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton Jbtn_Ataque;
    private javax.swing.JButton Jbtn_Continue;
    private javax.swing.JButton Jbtn_Defensa;
    private javax.swing.JButton Jbtn_Salir;
    private javax.swing.JButton Jbtn_SpAtaque;
    private javax.swing.JLabel Jlbl_Sp;
    private javax.swing.JLabel Jlbl_SpriteHero;
    private javax.swing.JLabel Jlbl_SpriteVillain;
    private javax.swing.JLabel Jlbl_hp;
    private javax.swing.JLabel Jlbl_hpVillano;
    private javax.swing.JProgressBar Jpbr_HpHero;
    private javax.swing.JProgressBar Jpbr_HpVillain;
    private javax.swing.JProgressBar Jpbr_Sp;
    private javax.swing.JTextArea Jtxt_Log;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables
}
