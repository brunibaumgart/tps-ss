import matplotlib.pyplot as plt
import numpy as np

def read_data_file(file_path):
    particles = []
    with open(file_path, 'r') as file:
        next(file)
        next(file)

        for line in file:
            particle_id, x, y, neighbours = line.strip().split()

            cleaned_str = neighbours.strip('[]').split()
            neighbours_array = list(map(int, cleaned_str))

            x = x.replace(',', '.')
            y = y.replace(',', '.')
            particles.append((int(particle_id), float(x), float(y), neighbours_array))
    return particles


def visualize_particles(particles, l, rc, r, m, selected_particle_id):

    neighbours = particles[selected_particle_id][3]

    fig = plt.figure()
    ax = fig.add_subplot(1, 1, 1)

    major_ticks = np.arange(0, l + 1, l/m)

    ax.set_xticks(major_ticks)
    ax.set_yticks(major_ticks)

    x_values = [p[1] for p in particles]
    y_values = [p[2] for p in particles]
    ids = [p[0] for p in particles]

    ax.set(xlim=(0, l+1), ylim=(0, l+1))

    ax.set_xlabel('x')
    ax.set_ylabel('y')
    ax.set_title('Partículas')

    fig_size = fig.get_size_inches()
    ax_limits = ax.get_xlim()
    ay_limits = ax.get_ylim()

    scale_factor = (fig_size[0] * 72.0 / np.diff(ax_limits)) ** 2

    particle_area = np.pi * (r**2)
    particle_size = particle_area * scale_factor

    rc_area = np.pi * (rc ** 2)
    rc_size = rc_area * scale_factor

    for i, id in enumerate(ids):
        ax.text(x_values[i] +r, y_values[i] +r, str(id), fontsize=9, ha='left')
        if id == selected_particle_id:
            ax.scatter(x_values[i], y_values[i], c="green", s=particle_size)
            ax.scatter(x_values[i], y_values[i], edgecolor='black', facecolor='none', s=rc_size)
            clipped_x = x_values[i]
            clipped_y = y_values[i]
            if x_values[i] + rc > l or x_values[i] -rc < 0:
                clipped_x = np.abs(clipped_x - np.diff(ax_limits))
            if y_values[i] + rc > l or y_values[i] -rc < 0:
                clipped_y = np.abs(clipped_y - np.diff(ay_limits))
            if clipped_x != x_values[i] or clipped_y != y_values[i]:
                ax.scatter(clipped_x, clipped_y, edgecolor='black', facecolor='none', s=rc_size)
        elif id in neighbours:
            ax.scatter(x_values[i], y_values[i], c="red", s=particle_size)
        else:
            ax.scatter(x_values[i], y_values[i], c="blue", s=particle_size)

    ax.grid(True)

    plt.show()

file_path = '../../simulations/src/main/resources/positions.txt'

particles = read_data_file(file_path)

l = 20
rc = 1
r = 0.25
m = 5

visualize_particles(particles, l, rc, r, m, 25)