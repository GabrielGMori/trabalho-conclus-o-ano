package com.tca.model;

public class Administrador {
    private String cpf;
    private String nome;
    private String senha;

    public Administrador(String cpf, String nome, String senha) {
        this.cpf = cpf;
        this.nome = nome;
        this.senha = senha;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public Boolean validarDados(String cpf, String nome, String senha) {
        if ((cpf == null || nome == null || senha == null)
            || (cpf.isEmpty() || nome.isEmpty() || senha.isEmpty())
            || (cpf.length() != 11)) {
            return false;
        }
        return true;
    }
}
