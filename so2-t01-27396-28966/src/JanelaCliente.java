package so2trabalho;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class JanelaCliente
        implements ActionListener {

    private static String address = "localhost";
    private static int sPort = 9000;

    String DEV_EMAIL = "l27396@alunos.uevora.pt";
    String APP_NAME = "Novo Aeroporto de Evora";

    protected static String ENVIAR = "ENVIAR";
    protected static String SAIR = "SAIR";

    private JTextArea qresult, tRef1, tRef2, tRef3, lnomes2, tDescr1, tUnid2, tDescr4;
    private static final Font fontLabel = new Font("Arial", Font.PLAIN, 11);
    private JEditorPane outPane = null;

    private JFrame frame;
    JRadioButton rbt1 = new JRadioButton("pesquisa por destino");
    JRadioButton rbt2 = new JRadioButton("compra N lugares");
    JRadioButton rbt3 = new JRadioButton("consultar lista de passageiros");
    JRadioButton rbt4 = new JRadioButton("consultar voos numa data");
    ButtonGroup bgroup;

    int LARGURA = 665;
    int ALTURA = 600;

    public JanelaCliente() {
        bgroup = new ButtonGroup();
        bgroup.add(rbt1);
        bgroup.add(rbt2);
        bgroup.add(rbt3);
        bgroup.add(rbt4);
    }

    public static void main(String[] args)
            throws Exception {
        JanelaCliente e = new JanelaCliente();

        e.initX();
    }

    public void initX() {
        frame = new JFrame(APP_NAME);
        frame.setSize(new Dimension(LARGURA, ALTURA));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        tRef1 = new JTextArea(1, 8);

        tDescr1 = new JTextArea(1, 20);

        tRef2 = new JTextArea(1, 8);
        tUnid2 = new JTextArea(1, 4);
        lnomes2 = new JTextArea(1, 8);

        tRef3 = new JTextArea(1, 8);

        tDescr4 = new JTextArea(1, 12);

        // campo para mostrar informacao, nao editavel
        qresult = new JTextArea(8, 70);
        qresult.setEditable(false);

        // mostrar
        updateFrame();
    }

    // actualizar o frame de acordo com o estado
    private void updateFrame() {

        try {
            if (outPane != null) {
                frame.setSize(new Dimension(LARGURA, ALTURA));
            }

            JPanel jp = new JPanel(new BorderLayout());

            Component centro = desenhaCentro();
            jp.add(centro, BorderLayout.CENTER);

            frame.setContentPane(jp);
        } catch (Exception e) {
            System.err.println("ERROR while updating frame: " + e);
            e.printStackTrace();
            System.exit(1);
        }
        // actualizar ==========
        if (outPane == null) {
            frame.pack();
        }
        frame.setVisible(true);
        // ---------------------
        //System.out.println("          == PAINEL ACTUALIZADO");
    }

    private Component desenhaCentro()
            throws Exception {
        if (outPane != null) {    // done, falta so' mostrar
            JScrollPane scrollPane = new JScrollPane(outPane);
            return scrollPane;
        } else {    //
            JPanel buttonP = new JPanel(new BorderLayout());

            JPanel p = new JPanel(new BorderLayout());
            //p.setBackground(Color.yellow);

            JPanel p1 = new JPanel(new BorderLayout());
            JPanel p12 = new JPanel(new BorderLayout());
            JPanel p3 = new JPanel(new BorderLayout());
            JPanel p4 = new JPanel(new BorderLayout());
            JPanel p2 = new JPanel(new GridLayout(1, 8));

            // *************************
            // campos das operacoes
            JPanel pbase = new JPanel(new GridLayout(1, 5, 2, 3));
            pbase.setBackground(Color.yellow);

            JPanel pbase1 = new JPanel(new GridLayout(3, 1));
            pbase1.add(rbt1);
            pbase1.add(new JLabel("destino"));
            pbase1.add(tRef1);
            pbase.add(pbase1);

            JPanel pbase2 = new JPanel(new GridLayout(7, 1));
            pbase2.add(rbt2);
            pbase2.add(new JLabel("id do voo"));
            pbase2.add(tRef2);
            pbase2.add(new JLabel("unidades"));
            pbase2.add(tUnid2);
            pbase2.add(new JLabel("nome(s) - usar ; se tiver mais de um nome"));
            pbase2.add(lnomes2);

            pbase.add(pbase2);

            JPanel pbase3 = new JPanel(new GridLayout(5, 1));
            pbase3.add(rbt3);
            pbase3.add(new JLabel("id do voo"));
            pbase3.add(tRef3);
            pbase.add(pbase3);

            JPanel pbase4 = new JPanel(new GridLayout(5, 1));
            pbase4.add(rbt4);
            pbase4.add(new JLabel("data"));
            pbase4.add(tDescr4);
            pbase.add(pbase4);

            p2.setAlignmentX(JPanel.CENTER_ALIGNMENT);
            // *************************

            p12.add(pbase, BorderLayout.WEST);
            p12.add(p1, BorderLayout.CENTER);

            // ****************
            JLabel lch = new JLabel("resultado");
            lch.setFont(fontLabel);
            p2.add(qresult);
            p2.setAlignmentX(JPanel.CENTER_ALIGNMENT);

            p3.add(lch, BorderLayout.NORTH);
            p3.add(p2, BorderLayout.CENTER);

            JButton button = new JButton(" enviar ");
            button.addActionListener(this);
            button.setActionCommand(ENVIAR);

            buttonP.add(button, BorderLayout.CENTER);

            p4.add(p3, BorderLayout.NORTH);
            p4.add(buttonP, BorderLayout.CENTER);
            // ****************

            p.add(new JLabel("Operações para o Serviço de Peças"), BorderLayout.NORTH);
            p.add(p12, BorderLayout.WEST);
            p.add(p4, BorderLayout.SOUTH); // resultado e botao enviar
            return p;
        }
    }

    // EVENTO SOBRE O FRAME "ACTION"
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        //System.out.println("COMMAND: "+command);
        // ********************************************

            


        try {
            Voos obj = (Voos) java.rmi.Naming.lookup("rmi://" + address + ":" + sPort + "/voos");
            if (command.equals(SAIR)) {
                frame.dispose();
                System.exit(0);
            }

            if (command.equals(ENVIAR)) {
                try {
                    
                    String s = "EXEMPLO PARA: ";

                    // que operacao esta seleccionada?
                    if (rbt1.isSelected()) {  // pesquisa voos por destino
                        // debug
                        s += "op1:  destino: " + tRef1.getText();


                        // invocacao de metodos remotos
                        s = obj.pesquisar_destinos(tRef1.getText());
                        
                        

                    } else if (rbt2.isSelected()) { // compra N lugares
                        String nomeStr = "";
                        String[] arrayNomes = lnomes2.getText().split(";");
                        ArrayList<String> nomes = new ArrayList<String>();
                        for (String nome : arrayNomes) {
                            nomes.add(nome.trim());
                            nomeStr+=nome+" ";
                        }
                        int id = Integer.parseInt(tUnid2.getText());
                       boolean reservou =  obj.comprar(tRef2.getText(),id, nomes);
                        // debug:
                        if(reservou)
                            s = "reservados: " + tUnid2.getText()+"para os passageiros:"+nomeStr +"com o id voo: "+ tRef2.getText();
                        else
                            s = "Impossivel reservar voos";

                    } else if (rbt3.isSelected()) { // consultar lista passageiros por voo
                        s += "op3:  voo: " + tRef3.getText();
                        
                        s = obj.consultar_passageiros(tRef3.getText());
                        

                    } else if (rbt4.isSelected()) { // procura voos por data
                        s += "op4:  data: " + tDescr4.getText();

                        s = obj.consultar_voos(tDescr4.getText());
                    }

                    System.err.println("DEBUG: " + s);
                    if (s != null) {
                        qresult.setText(s);

                    }
                } catch (Exception eNum) {
                    qresult.setText("problemas: " + eNum.getMessage());
                    eNum.printStackTrace();
                }
                //System.err.println("------------------------------------");
                updateFrame();
            }

             //resetFocusCheck();
        } catch (Exception e1) {
            System.err.println(e1);
            try {
                Thread.sleep(1000);
            } catch (Exception e2) {
            }
            System.exit(0);
        }
    }

}
