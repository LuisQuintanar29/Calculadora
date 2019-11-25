/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import modelo.*;

/**
 *
 * @authors Cortés Antonio,
 *          Pérez Villoria Yoali,
 *          Quintanar Luis.
 */
public class Vista {
    
    public static void main(String args[]) {
        Vista calculadora = new Vista();
        calculadora.LaunchFrame();
    }

    /*
        El estado se va a modificar en cuando el actor
        presione algún boton con el cual se realizará
        alguna operacion(suma,resta,division,multiplicacion)
     */
    private enum Estado {
        SUMA,
        RESTA,
        DIV,
        MULT;
    }
    /*
         En el frame se colocarán todos los componentes:
        botones, el campo de texto
         En el panel se colocarán todos los botones
         El atributo text será utilizado para mostrar los
        numeros que el usuario ingrese
         En el atributo result, cuando el usuario indique que
        va a realizar alguna operacion, se le asignará el valor
        dentro del JTextField, luego se modificará estado según
        el usuario señale, y cuando se presione el botón igual
        se realizará la operacion correspondiente, y el valor será
        guardado en result, para después mostrarlo en la pantalla
         El atributo txt, se utiliza para ir almacenando los numeros
        o puntos que el usuario vaya presionando, para después 
        mostrarlo en el JTextField
     */
    private final JFrame frame;
    private final JPanel panel;
    private final JButton botones[];
    private JTextField text;
    private Estado estado;
    private double result;
    private String txt = "";

    public Vista() {
        frame = new JFrame("Calculadora");
        panel = new JPanel(new GridLayout(4, 4));
        text = new JTextField(50);
        botones = new JButton[16];
        /* Inicializa los botones con los numeros
        correspondientes en su texto */
        for (int i = 0; i < 10; i++) {
            botones[i] = new JButton(Integer.toString(i));
        }
        botones[10] = new JButton(".");
        botones[11] = new JButton("+");
        botones[12] = new JButton("-");
        botones[13] = new JButton("*");
        botones[14] = new JButton("/");
        botones[15] = new JButton("=");

        /* Añade el color amarillo a los botones que debe,
        y agrega los eventos de esos    */
        for (int i = 0; i < 10; i++) {
            String t = botones[i].getText();
            botones[i].setBackground(Color.YELLOW);
            /*
                Los manejadores de eventos los tratamos como
                clases internas anónimas
            */
            botones[i].addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    setText(t);
                    /* /< Coloca en el textField los
                    numeros o el punto
                     */
                }
            });
        }
        
        botones[10].addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                /* Obtiene lo que hay en el JTextField */
                String n = text.getText();
                /* Si ya contiene un punto, no puedes agregar otro */
                if( !n.contains(".") ){
                    setText(".");
                }
            }
        });
        botones[10].setBackground(Color.YELLOW);
        
        /*
            COMIENZA SEGMENTO PARA MODIFICAR LOS BOTONES CON
            OPERACIONES (suma,resta,multiplicacion,division)
         */

        botones[11].addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                estado = Estado.SUMA; //< Modifica estado de la calculadora
                /*
                    Agrega a result el valor en el campo
                    de texto
                 */
                result = Double.parseDouble(text.getText());
                text.setText("");//<Limpia el textField
                txt = ""; //<Reinicia para poder añadir un nuevo numero
            }
        });
        botones[12].addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                estado = Estado.RESTA;
                result = Double.parseDouble(text.getText());
                text.setText("");
                txt = "";
            }
        });
        botones[13].addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                estado = Estado.MULT;
                result = Double.parseDouble(text.getText());
                text.setText("");
                txt = "";
            }
        });
        botones[14].addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                estado = Estado.DIV;
                result = Double.parseDouble(text.getText());
                text.setText("");
                txt = "";
            }
        });
        /* Para el botón de igual */
        botones[15].addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Operation op = new Operation();
                /* 
                    Checa que el JTextField no esté vacío, si se encuentra vacío
                    no realiza nada. Si no se hiciera, puede suceder un error
                    durante la ejecución
                    Además checa que dentro del JTextField no solo se encuentre
                    un punto ".", porque no es posible operar solo con el.
                    Si no lo checara, ocurre un error durante la ejecución
                */
                if( !text.getText().isEmpty() && !text.getText().equalsIgnoreCase(".")){
                /*
                    Segun el estado de la calculadora realizara la operacion
                    utilizando la clase Operation en el paquete modelo
                    cada una de las operaciones recibe como argumentos
                    result, el cual para ese entonces ya tiene un valor
                    asignado, y además lo que se encuentra en el JTextField
                    y regresa el valor cuya operacion ya fue realizada
                */
                    switch (estado) {
                        case SUMA:
                            result = op.suma(result, Double.parseDouble(text.getText()));
                            break;
                        case RESTA:
                            result = op.resta(result, Double.parseDouble(text.getText()));
                            break;
                        case MULT:
                            result = op.mult(result, Double.parseDouble(text.getText()));
                            break;
                        case DIV:
                            result = op.div(result, Double.parseDouble(text.getText()));
                            break;
                    }
                    /*
                        Coloca el resultado en el textField
                     */
                    text.setText(Double.toString(result));
                    txt = ""; //<Espera por si agregas un nuevo numero
                    result = 0; //<Reinicia el resultado
                }
            }
        });
        /* Coloca el color verde a los botones correspondientes */
        for (int i = 11; i < 16; i++) {
            botones[i].setBackground(Color.GREEN);
        }

        /*
        TERMINA SEGMENTO DE MODIFICACION DE BOTONES
         */
        text.setBackground(new Color(0, 239, 239));
        text.setEditable(false);
    }

    public void LaunchFrame() {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        estado = Estado.SUMA; //<Coloca suma por defecto
        /* 
            Se coloca un BoxLayout para que el tamaño del JTextFiel no
            sea del mismo tamaño que todos los botones juntos, además
            de esta manera podemos colocar primero el JTextField y
            después todos los botones
         */
        frame.setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));
        for (int i = 0; i < 16; i++) {
            panel.add(botones[i]);//<Agrega los botones al panel
        }
        frame.add(text);//<Agrega el JTextFiel al frame
        frame.add(panel);//<Ahora los botones
        frame.pack();//<Ajustamos el tamaño del frame
        frame.setVisible(true);//<Lo mostramos en pantalla
    }

    private void setText(String t) {
        /*
            Metodo necesario para modificar lo que hay dentro del
            textField, ya que dentro de los manejadores de eventos
            no se puede modificar
         */
        txt += t;//<Si presionas un nuevo numero, lo concatena a lo anterior
        text.setText(txt);//<Coloca el nuevo numero en el textField
    }
}
