package com.diego.api.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class SenhaUtils {

    /**
     * Gera um hash utilizando o Bcrypt.
     *
     * @param senha
     * @return
     */
    public static String gerarBcrypt(String senha){
        if (senha == null){
            return senha;
        }
        BCryptPasswordEncoder bCryptEncoder = new BCryptPasswordEncoder();
        return bCryptEncoder.encode(senha);
    }

    /**
     *
     * Verifica se a senha é valida.
     *
     * @param senha
     * @param senhaEncoded
     * @return
     */
    public static boolean senhaValida(String senha, String senhaEncoded){
        BCryptPasswordEncoder bCryptEncoder = new BCryptPasswordEncoder();
        return bCryptEncoder.matches(senha, senhaEncoded);
    }

}