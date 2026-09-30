package silly.chemthunder.rinvenium.render.manager.client;

import silly.chemthunder.rinvenium.render.APMDSCBeamRender;

import java.util.concurrent.ConcurrentLinkedQueue;

public class APMDSCBeamManager {
    private final ConcurrentLinkedQueue<APMDSCBeamRender> BEAMS = new ConcurrentLinkedQueue<>();

    public void add(APMDSCBeamRender beam) {
        this.BEAMS.add(beam);
    }

    public void tick() {
        this.BEAMS.removeIf(beamRender -> ++beamRender.age >= beamRender.maxAge);
    }

    public ConcurrentLinkedQueue<APMDSCBeamRender> get() {
        return this.BEAMS;
    }

    public void clear() {
        this.BEAMS.clear();
    }

}
