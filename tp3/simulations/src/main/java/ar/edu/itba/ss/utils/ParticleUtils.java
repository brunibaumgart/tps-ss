package ar.edu.itba.ss.utils;

import ar.edu.itba.ss.models.Particle;
import ar.edu.itba.ss.models.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ParticleUtils {
    private ParticleUtils() {
        throw new IllegalStateException("Utility class");
    }

    public static final int BROWNIAN_ID = -1;

    /**
     * Create particles in the space without overlapping
     *
     * @param particles list of particles. not modified
     * @param N         number of particles
     * @param L         length of the box
     * @param r         radius of the particles
     * @param speed     speed of particles
     * @param mass      mass of particles
     * @return a new list of particles containing the old ones and the new ones
     */
    public static List<Particle> createMovingParticles(
            final List<Particle> particles,
            final int N,
            final double L,
            final double r,
            final double speed,
            final double mass
    ) {
        final Random seedGenerator = new Random();
        final long seed = seedGenerator.nextLong();
        final Random random = new Random(seed);
        System.out.println("Seed " + seed);

        final List<Particle> result = new ArrayList<>(particles);

        for (int i = 0; i < N; i++) {
            final double angle = random.nextDouble() * 2 * Math.PI;
            double x = random.nextDouble() * L;
            double y = random.nextDouble() * L;

            Particle p = new Particle(i, r, new Vector(x, y), Vector.fromPolar(speed, angle), mass);
            // check particles do not overlap
            while (collidesWithAny(p, result) || collidesWithWalls(p, L)) {
                x = random.nextDouble() * L;
                y = random.nextDouble() * L;
                p = new Particle(i, r, new Vector(x, y), Vector.fromPolar(speed, angle), mass);
            }

            result.add(p);
        }

        return result;
    }

    private static boolean collidesWithAny(final Particle particle, final List<Particle> particles) {
        return particles.parallelStream().anyMatch(particle::collidesWith);
    }

    private static boolean collidesWithWalls(final Particle particle, final double L) {
        return particle.position().x() - particle.radius() <= 0 ||
                particle.position().x() + particle.radius() >= L ||
                particle.position().y() - particle.radius() <= 0 ||
                particle.position().y() + particle.radius() >= L;
    }
}
