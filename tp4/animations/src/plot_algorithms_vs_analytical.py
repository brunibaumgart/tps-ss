import numpy as np
import matplotlib.pyplot as plt

from src.constants import FilePaths


# Function to read data from file
def read_data(file_name):
    times = []
    positions = []
    with open(file_name, 'r') as file:
        for line in file:
            time, position = map(float, line.split())
            times.append(time)
            positions.append(position)
    return times, positions


# Function for the analytical solution
def analytical_solution(t, A, gamma, m, k):
    term1 = A * np.exp(-gamma / (2 * m) * t)
    term2 = np.cos(np.sqrt(k / m - (gamma ** 2 / (4 * m ** 2))) * t)
    return term1 * term2


# Read the data for the Beeman and Verlet algorithms
times_beeman, positions_beeman = read_data(FilePaths.SIMULATIONS_DIR + 'ej1/beeman_0.001000.txt')
times_verlet, positions_verlet = read_data(FilePaths.SIMULATIONS_DIR + 'ej1/verlet_0.001000.txt')
times_gear, positions_gear = read_data(FilePaths.SIMULATIONS_DIR + 'ej1/gear_0.001000.txt')

# Time range for the analytical solution
t_values = np.linspace(0, max(times_beeman), 500)

# Placeholder values for analytical solution parameters (replace as needed)
A = 1.0  # Amplitude
gamma = 100.0  # Damping coefficient
m = 70.0  # Mass
k = 10000.0  # Spring constant

# Compute the analytical solution for the given time values
r_values = analytical_solution(t_values, A, gamma, m, k)

# Plot the Beeman curve
plt.plot(times_beeman, positions_beeman, label='Beeman')

# Plot the Verlet curve
plt.plot(times_verlet, positions_verlet, label='Verlet')

# Plot the Gear curve
plt.plot(times_gear, positions_gear, label='Gear')

# Plot the Analytical Solution curve
plt.plot(t_values, r_values, label='Solución Analítica', linestyle='--')

# Add labels, title, and legend with increased font size
plt.xlabel('Tiempo (s)', fontsize=14)
plt.ylabel('Posición (m)', fontsize=14)
plt.xticks(fontsize=12)  # Increase x-axis tick font size
plt.yticks(fontsize=12)  # Increase y-axis tick font size

# Add grid and legend with larger fonts
plt.grid(True)
plt.legend(fontsize=12)  # Increase legend font size

# Adjust layout and display the plot
plt.tight_layout()
plt.show()
