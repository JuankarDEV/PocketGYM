package com.example.pocketgym.Conexion;

import android.os.StrictMode;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionBBDD
{
    private Connection conexion = null  ;

    //private static  final  String DRIVER ="org.postgresql.Driver";
    //private static  final  String URL ="jdbc:postgresql://192.168.1.12:5432/gimnasio";
    //private static  final  String USER ="postgres";
    //private static  final  String PASSWORD ="1234";


    private static final String DRIVER = "org.postgresql.Driver";
    private static final String URL = "jdbc:postgresql://ep-late-union-a2n7fqn1-pooler.eu-central-1.aws.neon.tech/PocketGym?sslmode=require";
    private static final String USER = "neondb_owner";
    private static final String PASSWORD = "npg_kRz6yliK3sAI";

    public Connection conectarBD(){
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            Class.forName(DRIVER);
            conexion = DriverManager.getConnection(URL,USER,PASSWORD);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return  conexion;
    }

    public void cerrarConexion (Connection conexion)throws Exception{
        conexion.close();
    }
}
