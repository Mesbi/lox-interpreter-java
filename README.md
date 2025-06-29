# 🚀 Interpretador da Linguagem Lox em Java

Este repositório documenta o desenvolvimento de um interpretador para a linguagem de programação Lox, como parte da disciplina de Compiladores. O projeto é inteiramente baseado no livro **"Crafting Interpreters"** de Robert Nystrom.

---

## 📜 Status Atual: Interpretador de Expressões Funcional

O projeto evoluiu para um interpretador funcional capaz de analisar e avaliar expressões complexas, concluindo os capítulos 4, 5, 6 e 7 do livro. As seguintes fases foram implementadas e integradas:

✅ **Analisador Léxico (Scanner)**
* Converte o código-fonte em uma sequência de tokens (números, strings, operadores, palavras-chave, etc.).

✅ **Árvore Sintática Abstrata (AST)**
* Define a estrutura de dados em árvore que representa a hierarquia e a gramática do código.

✅ **Analisador Sintático (Parser)**
* Recebe os tokens e constrói a AST, validando a sintaxe das expressões e respeitando as regras de precedência e associatividade.

✅ **Interpretador (Avaliador de Expressões)**
* Percorre a AST (usando o padrão Visitor) para **executar** o código e calcular os resultados. A implementação atual suporta:
    * **Literais:** Números, strings, `true`, `false` e `nil`.
    * **Operações Aritméticas:** `+`, `-`, `*`, `/`, incluindo a concatenação de strings com `+`.
    * **Operações de Comparação:** `>`, `>=`, `<`, `<=`.
    * **Operações de Igualdade:** `==`, `!=`.
    * **Operações Unárias:** `-` (negação numérica) e `!` (negação lógica).
    * **Lógica de "Truthiness":** `false` e `nil` são avaliados como falsos, e todos os outros valores como verdadeiros.
    * **Tratamento de Erros em Tempo de Execução:** Reporta erros como divisão por zero ou operações com tipos incompatíveis.

---

## 👨‍💻 Desenvolvedor

* **Marcos Eduardo de Sousa Barbosa** - [@Mesbi](https://github.com/Mesbi)

---

## 💻 Tecnologias

* **Linguagem:** Java (JDK 11 ou superior)
* **Controle de Versão:** Git & GitHub



