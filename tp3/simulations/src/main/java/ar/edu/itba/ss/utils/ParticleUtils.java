package ar.edu.itba.ss.utils;

import ar.edu.itba.ss.models.Particle;
import ar.edu.itba.ss.models.Vector;

import javax.sound.midi.Soundbank;
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

    public static List<Particle> createMovingParticlesCircular(final List<Particle> particles,
                                                               final int N,
                                                               final double L,
                                                               final double r,
                                                               final double speed,
                                                               final double mass) {
        final Random seedGenerator = new Random();
        final long seed = seedGenerator.nextLong();
        final Random random = new Random(seed);
        System.out.println("Seed " + seed);

        final List<Particle> result = new ArrayList<>(particles);

        final double R = L / 2;

        for (int i = 0; i < N; i++) {
            double theta = random.nextDouble() * 2 * Math.PI;
            double radius = Math.sqrt(random.nextDouble()) * (R - r); // Adjust radius to account for particle size

            double x = radius * Math.cos(theta);
            double y = radius * Math.sin(theta);

            // Adjust for the center of the circle being at (L/2, L/2)
            x += R;
            y += R;

            Particle p = new Particle(i, r, new Vector(x, y), Vector.fromPolar(speed, theta), mass);

            // Check particles do not overlap and are within the circular boundary
            while (collidesWithAny(p, result) || collidesWithCircularWall(p, L)) {
                theta = random.nextDouble() * 2 * Math.PI;
                radius = Math.sqrt(random.nextDouble()) * (R - r);
                x = radius * Math.cos(theta);
                y = radius * Math.sin(theta);

                // Adjust for the center of the circle being at (L/2, L/2)
                x += R;
                y += R;

                p = new Particle(i, r, new Vector(x, y), Vector.fromPolar(speed, theta), mass);
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

    private static boolean collidesWithCircularWall(final Particle particle, final double L){
        final double distanceCenter = particle.position().distanceTo(new Vector(L/2, L/2));
        return distanceCenter + particle.radius() >= L/2;
    }
}
