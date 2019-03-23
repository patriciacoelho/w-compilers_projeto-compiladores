/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package compiler;

import AST.DeclaracaoDeVariavel;

/**
 *
 * @author uesleymelo
 */
public class DeclarationPointer {
    DeclaracaoDeVariavel declaracao;
    int indice;

    public DeclarationPointer(DeclaracaoDeVariavel declaracao, int indice) {
        this.declaracao = declaracao;
        this.indice = indice;
    }
}
