import java.util.ArrayList;
import java.util.List;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/* 

Classe principal, ele instancia as classes 

- AnalisadorLexicoSimples: Separa em tokens
- AnalisadorSintaticoSimples: AnalisadorSintaticoSimples usa a classe AnalisadorSemanticoSimples

*/

public class Compilador {

    public static void main(String[] args) {
        String codigoParaAnalisar = "";
	

	// se não foi passado nada na linha de comando, informa erro 

        if (args.length == 0) {
            System.out.println("Uso: java Compilador <caminho_do_arquivo.macslang>");
            System.out.println("Nenhum arquivo de código MACSLang foi fornecido.");
            return;
        }
	
	// arquivo é a primeira opção passada java Compilador arquivo.txt	
        
	String caminhoArquivo = args[0];

	// Tenta abir o arquivo, se der problema informa o erro 

        try {
            codigoParaAnalisar = new String(Files.readAllBytes(Paths.get(caminhoArquivo)));
            System.out.println("Arquivo '" + caminhoArquivo + "' lido com sucesso.");
        } catch (IOException e) {
            System.err.println("Erro ao ler o arquivo '" + caminhoArquivo + "': " + e.getMessage());
            e.printStackTrace();
            return;
        }

        // Analise lexica - separa em tokens

        System.out.println("\n--- Iniciando Análise Léxica ---");
        AnalisadorLexicoSimples lexer = new AnalisadorLexicoSimples(codigoParaAnalisar);
        List<String[]> tokens = lexer.analisar();
	
	// imprime os tokens

        System.out.println("\n--- Tokens encontrados (Lexema, Tipo) ---");
        for (String[] token : tokens) {
            System.out.println("Lexema: \"" + token[0] + "\", Tipo: \"" + token[1] + "\"");
        }



        //  Cria o objeto de Analisador Semantico AnalisadorSemanticoSimples

        System.out.println("\n--- Iniciando Análise Semântica ---");
        AnalisadorSemanticoSimples analisadorSemantico = new AnalisadorSemanticoSimples(); // Instancia o analisador semântico

        // Análise Sintática com a semantica embutida nos metodos
	// se der problema, informar o ele 
 
        try {
            AnalisadorSintaticoSimples parser = new AnalisadorSintaticoSimples(tokens, analisadorSemantico);
            parser.analisarPrograma(); // Inicia a análise pelo símbolo inicial da gramática
            System.out.println("\nAnálise Completa: SUCESSO! O código MACSLang é sintaticamente e semanticamente válido.");
        } catch (AnalisadorSintaticoSimples.SintaxeException e) {
            System.err.println("\nAnálise Completa: ERRO DE SINTAXE! " + e.getMessage());
            // e.printStackTrace(); // Descomente para ver o stack trace completo
        } catch (SemanticaException e) { // Captura erros semânticos
            System.err.println("\nAnálise Completa: ERRO SEMÂNTICO! " + e.getMessage());
            // e.printStackTrace(); // Descomente para ver o stack trace completo
        } catch (Exception e) { // Captura outras exceções inesperadas
            System.err.println("\nAnálise Completa: ERRO INESPERADO! " + e.getMessage());
            e.printStackTrace();
        }
        
        System.out.println("\n--- Fim do Processo de Compilação (Fases de Análise) ---");
    }
}