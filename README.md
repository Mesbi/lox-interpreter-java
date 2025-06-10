

# 🚀 Interpretador da Linguagem Lox em Java

Este repositório documenta o desenvolvimento de um interpretador para a linguagem de programação Lox, como parte da disciplina de Compiladores. O projeto é inteiramente baseado no livro **"Crafting Interpreters"** de Robert Nystrom.



## 📜 Status Atual: Analisador Léxico (Scanner) Concluído

A implementação atual cobre 100% do **Capítulo 4: Scanning** do livro. O Scanner é capaz de processar um código-fonte `.lox` e convertê-lo em uma sequência de tokens, reconhecendo:

* **Operadores** de um ou dois caracteres (`+`, `!`, `!=`, `==`, etc.).
* **Literais** de String (`"Olá, mundo!"`) e Números (`123`, `45.67`).
* **Palavras-chave** da linguagem (`var`, `if`, `print`, `while`, etc.).
* **Identificadores** (nomes de variáveis e funções).
* **Comentários** de linha (`// ...`) e espaços em branco.
* **Token `EOF`** para marcar o fim do arquivo.

## Integrante

* **Nome Completo:** Marcos Eduardo de Sousa Barbosa
* **Usuário GitHub:** @Mesbi
