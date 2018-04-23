package com.daisa;

import java.util.Objects;

public class Usuario {
    private String username;
    private String password;
    private String tipoLogin;

    public Usuario(String username, String password, String tipoLogin) {
        this.username = username;
        this.password = password;
        this.tipoLogin = tipoLogin;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTipoLogin() {
        return tipoLogin;
    }

    public void setTipoLogin(String tipoLogin) {
        this.tipoLogin = tipoLogin;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Usuario usuario = (Usuario) o;
        return Objects.equals(username, usuario.username);
    }

    @Override
    public int hashCode() {

        return Objects.hash(username);
    }
}
