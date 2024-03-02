package it.unibo.ares.core.api;

import java.util.List;

public interface SimulationControlApi {

    /**
     * remove the simulation from the current session.
     *
     * @param id The identifier of the simulation.
     */
    void removeSimulation(String id);

    /**
     * pause the simulation of the given id.
     *
     * @param id The identifier of the simulation.
     */
    void pauseSimulation(String id);

    /**
     * start the simulation of the given id.
     *
     * @param id The identifier of the simulation.
     */
    void startSimulation(String id);

    /**
     * Returns a list of running models.
     *
     * @return a list of running models
     */
    List<String> getRunningSimulations();

}