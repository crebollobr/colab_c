import os
import tempfile
import subprocess
from IPython.core.magic import register_cell_magic

@register_cell_magic
def c(line, cell):
    """
    Magic personalizada para executar código C no Google Colab.
    """
    try:
        # Criar um arquivo temporário para o código-fonte C
        with tempfile.NamedTemporaryFile(suffix=".c", delete=False) as source_file:
            source_file.write(cell.encode('utf-8'))
            source_file.flush()
            source_path = source_file.name

        # Criar um arquivo temporário para o executável
        binary_path = source_path[:-2]  # Remover ".c" para criar o executável

        # Compilar o código C
        #compile_cmd = f"gcc {source_path} -o {binary_path} -std=c11 -fPIC -shared -rdynamic 2>&1"
        compile_cmd = f"gcc {source_path} -o {binary_path}  2>&1"
        compile_process = subprocess.run(compile_cmd, shell=True, capture_output=True, text=True)

        if compile_process.returncode != 0:
            print("[Erro na compilação]\n", compile_process.stdout,  compile_process.stderr)
            return

        # Executar o código compilado
        run_cmd = f"{binary_path}"
        run_process = subprocess.run(run_cmd, shell=True, capture_output=True, text=True)
        #run_process = subprocess.run(run_cmd, shell=True, stdout=subprocess.PIPE, stderr=subprocess.PIPE, text=True)
        #print(run_process.stdout)  # Força a exibição da saída
        #print(run_process.stderr)  # Caso haja erro
        # Exibir saída da execução
        print(run_process.stdout)

    finally:
        # Remover arquivos temporários
        if os.path.exists(source_path):
            os.remove(source_path)
        if os.path.exists(binary_path):
            os.remove(binary_path)
