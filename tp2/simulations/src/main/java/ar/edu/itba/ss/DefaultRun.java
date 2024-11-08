package ar.edu.itba.ss;


import ar.edu.itba.ss.constants.FilePaths;
import ar.edu.itba.ss.methods.CellIndexMethod;
import ar.edu.itba.ss.methods.OffLaticeMethod;
import ar.edu.itba.ss.models.MovingParticle;
import ar.edu.itba.ss.models.parameters.CimParameters;
import ar.edu.itba.ss.models.parameters.Parameters;
import ar.edu.itba.ss.models.parameters.VideoParameters;
import ar.edu.itba.ss.utils.OutputUtils;
import ar.edu.itba.ss.utils.ParticleUtils;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Collections;
import java.util.List;


public class DefaultRun {
    public static void run(Parameters parameters) throws IOException {
        final CimParameters cimParameters = parameters.getCim();
        final VideoParameters videoParameters = parameters.getPlots().getVideo();
        final FileWriter videoWriter = new FileWriter(FilePaths.OUTPUT_DIR + "video_parameters.txt");
        OutputUtils.printVideoParameters(videoWriter, videoParameters.getEtha(), videoParameters.getIterations());

        final List<MovingParticle> particles = ParticleUtils.createMovingParticles(cimParameters.getN(), cimParameters.getL(), cimParameters.getR(), parameters.getSpeed());

        final OffLaticeMethod ofm = new OffLaticeMethod(cimParameters.getRc(), parameters.getSpeed(), videoParameters.getEtha());
        CellIndexMethod cim = new CellIndexMethod(cimParameters.getM(), cimParameters.getL(), true, particles);
        List<MovingParticle> updatedParticles = Collections.unmodifiableList(particles);

        for (int i = 0; i < videoParameters.getIterations(); i++) {
            final FileWriter writer = new FileWriter(FilePaths.OUTPUT_DIR + "video_frames/frame_" + i + ".txt");
            OutputUtils.printParticleDataHeader(writer);

            updatedParticles = ofm.runIteration(cim, parameters.getDt(), updatedParticles, writer);

            cim = new CellIndexMethod(parameters.getCim().getM(), parameters.getCim().getL(), true, updatedParticles);
        }
    }
}
