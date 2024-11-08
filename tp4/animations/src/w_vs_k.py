import matplotlib.pyplot as plt
import os

from src.constants import FilePaths

# Definir los valores de k y los nombres de los archivos
k_values = [100, 1000, 2500, 5000, 10000]
w_max_values = []

# Base del nombre del archivo
base_filename = FilePaths.SIMULATIONS_DIR + 'amplitude_vs_w_k_'

# Leer cada archivo y encontrar el w máximo
for k in k_values:
    filename = f"{base_filename}{k}.txt"
    amplitudes = []
    ws = []

    with open(filename, 'r') as file:
        next(file)  # Omitir el encabezado
        for line in file:
            a, w = map(float, line.split())  # Leer amplitud y w
            amplitudes.append(a)
            ws.append(w)

    # Encontrar el índice de la amplitud máxima
    max_index = amplitudes.index(max(amplitudes))

    # Guardar el w correspondiente a la amplitud máxima
    w_max_values.append(ws[max_index])

# Graficar w_0 en función de k, solo puntos
plt.plot(k_values, w_max_values, '-o')

# Etiquetas de los ejes con unidades
plt.xlabel(r'$k$ ($\mathrm{kg/s^2}$)', fontsize=16)
plt.ylabel(r'$\omega_0$ ($\text{s}^{-1}$)', fontsize=16)

# Configurar los ticks del eje x manualmente para que muestren los valores de k claramente
plt.xticks(k_values, [f"{k}" for k in k_values], fontsize=12)
plt.yticks(w_max_values, [f"{w:.1f}" for w in w_max_values], fontsize=12)

# Configuración del gráfico
plt.grid(True)
plt.tight_layout()
plt.show()
