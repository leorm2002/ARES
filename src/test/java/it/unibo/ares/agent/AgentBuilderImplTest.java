package it.unibo.ares.agent;

import static org.junit.jupiter.api.Assertions.assertThrows;


import java.util.function.BiFunction;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import it.unibo.ares.utils.parameters.ParameterImpl;
import it.unibo.ares.utils.pos.Pos;
import it.unibo.ares.utils.state.State;

/**
 * Unit test for {@link AgentBuilderImpl}.
 */
public final class AgentBuilderImplTest {
    private AgentBuilderImpl agentBuilder;

    /**
     * Istantiate a new AgentBuilder before each test.
     */
    @BeforeEach
    public void setUp() {
        agentBuilder = new AgentBuilderImpl();
    }

    /**
     * Should throw an IllegalStateException if we try to build an agent without a strategy.
     */
    @Test
    void testBuildWithNullStrategy() {
        assertThrows(IllegalStateException.class, () -> {
            agentBuilder.build();
        });
    }

    /**
     * Should create an agent with a simple strategy.
     */
    @Test
    void testBuild() {
        BiFunction<State, Pos, State> strategy = (state, pos) -> state;
        agentBuilder.addStrategy(strategy)
                    .addParameter(new ParameterImpl<>("testKey", 10))
                    .addParameter(new ParameterImpl<>("testKey2", "testValue"))
                    .addParameter(new ParameterImpl<>("testKey3", true));
    }
}
