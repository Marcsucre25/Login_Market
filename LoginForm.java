import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class LoginForm extends JDialog{
    private JTextField emailTF;
    private JPasswordField passwordTF;
    private JButton OKButton;
    private JButton cancelButton;
    private JPanel loginPanel;
    //ATRIBUTOS AUTENTICACION
    public User user;

    public LoginForm(JFrame parent){
        super(parent);
        setTitle("LOGIN");
        setContentPane(loginPanel);
        setMinimumSize(new Dimension(480,300));
        setModal(true);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setDefaultLookAndFeelDecorated(true);
        //setVisible(true);



        //BOTON OK FUNCIONALIDAD
        OKButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String email=emailTF.getText();
                String password =String.valueOf(passwordTF.getPassword()); //Asi obtenemos el password sin saber cual es (CASTEO)
                System.out.println("Boton Ok");
                user = getAuthenticationUser(email,password);

                if(user!= null){
                    dispose();
                }
                else {
                    JOptionPane.showMessageDialog(LoginForm.this,"email o password incorrectos",
                            "Intente nuevamente",JOptionPane.ERROR_MESSAGE);

                }

            }
        });
        //BOTON CANCEL
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Boton Cancel");
                dispose();


            }
        });
        setVisible(true);
    }
    ///////
    //CREAR AUTENTICACION (elementos)
    private User getAuthenticationUser(String email, String password){
        User user = null;
        final String DB_URL= "jdbc:mysql://localhost/mi_tienda?serverTimezone=UTC";
        final String USERNAME = "PaulinaMales";
        final String PASSWORD="Males2001";

        try {
            Connection conn = DriverManager.getConnection(DB_URL,USERNAME,PASSWORD);
            Statement stmt = conn.createStatement();
            String sql= "SELECT * FROM users WHERE email =? AND password =?";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1,email);
            preparedStatement.setString(2,password);
            System.out.println("Conexion OK");
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()){
                user = new User();
                user.nombre= resultSet.getString("nombre");
                user.email = resultSet.getString("email");
                user.celular = resultSet.getString("celular");
                user.direccion = resultSet.getString("direccion");
                user.password= resultSet.getString("password");

            }

            stmt.close();
            conn.close();


        } catch (Exception e){

            System.out.println("Error de .....");
            e.printStackTrace();
        }

        return user;
    }

    ///////////////////////

    public static void main(String[] args) {
        LoginForm loginForm = new LoginForm(null);
        User user = loginForm.user;

        if(user!= null){

            JOptionPane.showMessageDialog(null,"BIENVENIDO\n"+user.nombre+"\nEmail: "+user.email+"\nCelular: "
                    +user.celular+"\nDireccion: "+user.direccion);

/*            System.out.println("email: "+user.email);
            System.out.println("celular"+user.celular);
            System.out.println("direccion"+user.direccion);
            System.out.println("Clave"+user.password);*/

        }
        else {
            System.out.println("Autenticacion fallida");
        }
    }

}
