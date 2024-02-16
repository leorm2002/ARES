
package it.unibo.ares.core.model;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import it.unibo.ares.core.agent.Agent;
import it.unibo.ares.core.agent.SchellingsAgentFactory;
import it.unibo.ares.core.utils.parameters.ParameterImpl;
import it.unibo.ares.core.utils.parameters.Parameters;
import it.unibo.ares.core.utils.pos.Pos;
import it.unibo.ares.core.utils.pos.PosImpl;
import it.unibo.ares.core.utils.state.State;
import it.unibo.ares.core.utils.state.StateImpl;
import it.unibo.ares.core.utils.uniquepositiongetter.UniquePositionGetter;
import javafx.beans.binding.StringBinding;

/**
 * Generate an instance of a schelling segregation model. It permits the
 * paramtrization of:
 * the number of agents (two types)
 */
public class SchellingModelFactories {
        private static final String MODEL_ID = "Schelling";

        public static String getModelId() {
                return MODEL_ID;
        }

        private static String getAgentType(final int na, final int index) {
                return index < na ? "A" : "B";
        }

        private static State schellingInitializer(final Parameters parameters) throws IllegalAccessException {
                int size = parameters.getParameter("size", Integer.class)
                                .orElseThrow(IllegalAccessException::new).getValue();
                int na = parameters.getParameter("numeroAgentiTipoA", Integer.class)
                                .orElseThrow(IllegalAccessException::new).getValue();
                int nb = parameters.getParameter("numeroAgentiTipoB", Integer.class)
                                .orElseThrow(IllegalAccessException::new).getValue();
                int total = na + nb;
                State state = new StateImpl(size, size);
                if (size * size < total) {
                        throw new IllegalArgumentException("The number of agents is greater than the size of the grid");
                }
                List<Pos> validPositions = IntStream.range(0, size).boxed()
                                .flatMap(i -> IntStream.range(0, size).mapToObj(j -> new PosImpl(i, j)))
                                .map(Pos.class::cast)
                                .toList();

                UniquePositionGetter getter = new UniquePositionGetter(validPositions);

                List<Agent> agents = Stream
                                .generate(SchellingsAgentFactory::getSchellingSegregationModelAgent)
                                .limit(total)
                                .collect(Collectors.toList());
                IntStream.range(0, total)
                                .forEach(i -> agents.get(i).setType(getAgentType(na, i)));
                IntStream.range(0, total)
                                .forEach(i -> state.addAgent(getter.get(), agents.get(i)));

                return state;
        }

        /**
         * Returna a schelling model, before calling initialize you should set:
         * numeroAgentiTipoA (integer)
         * numeroAgentiTipoB (integer)
         * size (integer)
         * 
         * @return
         */
        public static Model getModel() {
                ModelBuilder builder = new ModelBuilderImpl();
                // We need only one agent supplier since all agents are equal and only differs
                // in the type
                return builder
                                .addParameter(new ParameterImpl<>("numeroAgentiTipoA", Integer.class))
                                .addParameter(new ParameterImpl<>("numeroAgentiTipoB", Integer.class))
                                .addParameter(new ParameterImpl<>("size", Integer.class))
                                .addExitFunction((o, n) -> o.getAgents().stream()
                                                .allMatch(p -> n.getAgents().stream().anyMatch(p::equals)))
                                .addInitFunction(t -> {
                                        try {
                                                return schellingInitializer(t);
                                        } catch (IllegalAccessException e) {
                                                throw new IllegalArgumentException(
                                                                "Missing parameters for the model initialization");
                                        }
                                })
                                .build();

        }
}
