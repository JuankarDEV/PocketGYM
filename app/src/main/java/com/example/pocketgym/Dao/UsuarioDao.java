package com.example.pocketgym.Dao;

import com.example.pocketgym.Conexion.ConexionBBDD;
import com.example.pocketgym.Models.Usuario;

import java.sql.Array;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.stream.Stream;

public class UsuarioDao{

    private ConexionBBDD conexionDB;


    public UsuarioDao(ConexionBBDD conexionDB) {
        this.conexionDB = conexionDB;
    }

    public Usuario obtenerUsuarioporid(int id) {
        Usuario usuario = null;
        String sql = "SELECT * FROM usuarios WHERE id = ?";

        try (Connection conexion = conexionDB.conectarBD();
             PreparedStatement pstmt = conexion.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                usuario = new Usuario(
                        rs.getString("nombre"),
                        rs.getString("codigo_nfc"),
                        rs.getInt("nivel"),
                        rs.getBoolean("suscripcion_activa"),
                        rs.getDate("fecha_vencimiento")

                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return usuario;
    }

    public boolean insertarUsuario(Usuario usuario) {
        String sql = "INSERT INTO usuarios (nombre, codigo_nfc, nivel, suscripcion_activa) VALUES (?, ?, ?, ?)";

        try (Connection conexion = conexionDB.conectarBD();
             PreparedStatement pstmt = conexion.prepareStatement(sql)) {

            pstmt.setString(1, usuario.getNombre());
            pstmt.setString(2, usuario.getCodigoNfc());
            pstmt.setInt(3, usuario.getNivel());
            pstmt.setBoolean(4, usuario.isSuscripcionActiva());

            int filasInsertadas = pstmt.executeUpdate();
            return filasInsertadas > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    public   ArrayList<String> obtenerEjercicios(int id_usuario) {
        ArrayList<String>listaEjercicios = new ArrayList<>();
        String sql = "select e.nombre from ejercicios e inner join usuarios u on u.id = ? inner join niveles_ejercicios ne on u.nivel = ne.nivel where e.tabla_id = ne.id ";

        try (Connection conexion = conexionDB.conectarBD();
             PreparedStatement pstmt = conexion.prepareStatement(sql)) {

            pstmt.setInt(1, id_usuario);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                listaEjercicios.add(rs.getString("nombre"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return listaEjercicios;
    }

    public   ArrayList<String> obtenerEjerciciosdia(int nivel_usuario,int dia) {
        ArrayList<String>listaEjercicios = new ArrayList<>();
        String sql = "SELECT e.nombre FROM ejercicios e \n" +
                "INNER JOIN usuarios u ON u.id = ? \n" +
                "INNER JOIN niveles_ejercicios ne ON u.nivel = ne.nivel \n" +
                "INNER JOIN ejercicio_dia ed ON ed.ejercicio_id = e.id\n" +
                "WHERE e.tabla_id = ne.id and ed.dia_id  = ?";

        try (Connection conexion = conexionDB.conectarBD();
             PreparedStatement pstmt = conexion.prepareStatement(sql)) {

            pstmt.setInt(1, nivel_usuario);
            pstmt.setInt(2,dia);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                listaEjercicios.add(rs.getString("nombre"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return listaEjercicios;
    }

    public String obtenerImagenTabla(int nivel_usuario) {
        String tabla = null;
        String sql = "select imagen_url from progreso_usuarios where usuario_id = ? ";

        try (Connection conexion = conexionDB.conectarBD();
             PreparedStatement pstmt = conexion.prepareStatement(sql)) {

            pstmt.setInt(1, nivel_usuario);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                tabla = rs.getString("imagen_url");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return tabla;
    }

    public String obtenerVideoEjercicio(String nombre) {
        String video = null;
        String sql = "select video_url from ejercicios where nombre like ?;";

        try (Connection conexion = conexionDB.conectarBD();
             PreparedStatement pstmt = conexion.prepareStatement(sql)) {

            pstmt.setString(1, nombre);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                video = rs.getString("video_url");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return video;
    }

    public Usuario obtenerUsuarioPorNFC(String codigoNfc) {
        Usuario usuario = null;
        String sql = "SELECT * FROM usuarios WHERE codigo_nfc = ?";

        try (Connection conexion = conexionDB.conectarBD();
             PreparedStatement pstmt = conexion.prepareStatement(sql)) {

            pstmt.setString(1, codigoNfc);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                usuario = new Usuario(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("codigo_nfc"),
                        rs.getInt("nivel"),
                        rs.getBoolean("suscripcion_activa"),
                        rs.getDate("fecha_registro")

                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return usuario;
    }

    public int ObtenerEjerciciosCompletados(int id) {
        int ejerciciosCompletados = 0;
        String sql = "select ejercicios_completados from progreso_usuarios where id = ?;";

        try (Connection conexion = conexionDB.conectarBD();
             PreparedStatement pstmt = conexion.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                ejerciciosCompletados = rs.getInt("ejercicios_completados");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return ejerciciosCompletados;
    }

    public int ObtenerSeriesEjercicios(String nombre) {
        int series = 0;
        String sql = "select series from ejercicios where nombre like ?;";

        try (Connection conexion = conexionDB.conectarBD();
             PreparedStatement pstmt = conexion.prepareStatement(sql)) {

            pstmt.setString(1, nombre);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                series = rs.getInt("series");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return series;
    }

    public boolean insertarSeries(int usuario_id,int ejercicio_id,int repeticiones,int peso,int serie,int dia) {
        String sql = "INSERT INTO registros_entrenamiento (usuario_id, ejercicio_id, repeticiones, peso, serie,dia) VALUES (?, ?, ?, ?, ?,?)";

        try (Connection conexion = conexionDB.conectarBD();
             PreparedStatement pstmt = conexion.prepareStatement(sql)) {

            pstmt.setInt(1,usuario_id);
            pstmt.setInt(2,ejercicio_id);
            pstmt.setInt(3,repeticiones);
            pstmt.setDouble(4,peso);
            pstmt.setInt(5,serie);
            pstmt.setInt(6,dia);


            int filasInsertadas = pstmt.executeUpdate();
            return filasInsertadas > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public int obteneridEjercicio(String nombre) {
        int id_ejercicio = 0;
        String sql = "select id from ejercicios where nombre like ?;";

        try (Connection conexion = conexionDB.conectarBD();
             PreparedStatement pstmt = conexion.prepareStatement(sql)) {

            pstmt.setString(1, nombre);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                id_ejercicio = rs.getInt("id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return id_ejercicio;
    }

    public ArrayList<Integer> obtenerpesoyrepes(int usuario_id, int ejercicio_id,int serie,int dia) {
        ArrayList<Integer>pesoyrespes =  new ArrayList<>() ;
        String sql = "select peso,repeticiones from registros_entrenamiento where usuario_id = ? and ejercicio_id = ? and serie = ? and dia = ?;";

        try (Connection conexion = conexionDB.conectarBD();
             PreparedStatement pstmt = conexion.prepareStatement(sql)) {

            pstmt.setInt(1, usuario_id);
            pstmt.setInt(2, ejercicio_id);
            pstmt.setInt(3, serie);
            pstmt.setInt(4,dia);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                int peso = rs.getInt(1);
                int repeticiones = rs.getInt(2);
                pesoyrespes.add(peso);
                pesoyrespes.add(repeticiones);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return pesoyrespes;
    }
    public boolean UpdateEjercicios(int id) {
        String sql = "update progreso_usuarios set ejercicios_completados = ejercicios_completados +  1 where usuario_id = ?;";

        try (Connection conexion = conexionDB.conectarBD();
             PreparedStatement pstmt = conexion.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return  true ;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }
}
