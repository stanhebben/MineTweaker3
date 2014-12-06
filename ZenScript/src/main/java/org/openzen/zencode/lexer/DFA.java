/*
 * This file is part of ZenCode, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 openzen.org <http://zencode.openzen.org>
 */
package org.openzen.zencode.lexer;

import gnu.trove.map.TIntIntMap;
import gnu.trove.map.TIntObjectMap;
import gnu.trove.map.hash.TIntIntHashMap;
import gnu.trove.map.hash.TIntObjectHashMap;
import gnu.trove.set.TIntSet;
import gnu.trove.set.hash.TIntHashSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Implements a DFA.
 *
 * @author Stan Hebben
 */
public class DFA {
    public static final int NOFINAL = Integer.MIN_VALUE;

    private final DFAState initial;

    /**
     * Constructs a new DFA with the specified initial state.
     *
     * @param initial
     */
    public DFA(DFAState initial) {
        this.initial = initial;
    }

    /**
     * Compiles this DFA into a more efficient structure.
     *
     * @return the compiled DFA
     */
    public CompiledDFA compile() {
        ArrayList<DFAState> nodeList = new ArrayList<DFAState>();
        HashMap<DFAState, Integer> nodes = new HashMap<DFAState, Integer>();
        nodes.put(initial, 0);
        nodeList.add(initial);

        /* Find all reachable nodes in the dfs */
        int counter = 1;
        Queue<DFAState> todo = new LinkedList<DFAState>();
        todo.add(initial);

        while (!todo.isEmpty()) {
            DFAState current = todo.poll();

			for (int k : current.transitions.keys()) {
                DFAState next = current.transitions.get(k);
                if (!nodes.containsKey(next)) {
                    todo.add(next);
                    nodes.put(next, counter++);
                    nodeList.add(next);
                }
            }
        }

        /* Compile */
        TIntIntMap[] transitions = new TIntIntHashMap[counter];
        int[] finals2 = new int[counter];

        for (DFAState node : nodeList) {
            int index = nodes.get(node);
            finals2[index] = node.finalCode;

            transitions[index] = new TIntIntHashMap();
			for (int k : node.transitions.keys()) {
                DFAState next = node.transitions.get(k);
                transitions[index].put(k, nodes.get(next));
            }
        }

        return new CompiledDFA(transitions, finals2);
    }

    /**
     * Generates the minimal version of this DFA.
     *
     * @return the minimal DFA
     */
    public DFA optimize() {
        CompiledDFA compiled = compile();
        TIntIntMap[] transitions = compiled.transitions;
        int size = transitions.length;

        /* Collect all edges and determine alphabet */
        TIntSet alphabet = new TIntHashSet();
        for (int i = 0; i < size; i++) {
			alphabet.addAll(transitions[i].keys());
        }

        /* Initialize distinguishing array */
        boolean[][] distinguishable = new boolean[size + 1][size + 1];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                distinguishable[i][j] = compiled.finals[i] != compiled.finals[j];
            }
        }
        for (int i = 0; i < size; i++) {
            distinguishable[i][size] = true;
            distinguishable[size][i] = true;
        }

        /* Minimization algorithm implementation */
        boolean changed;
        do {
            changed = false;
            for (int x : alphabet.toArray()) {
                for (int i = 0; i < size; i++) {
                    int ti = transitions[i].containsKey(x) ? transitions[i].get(x) : size;
                    for (int j = 0; j < size; j++) {
                        if (distinguishable[i][j])
							continue;

                        int tj = transitions[j].containsKey(x) ? transitions[j].get(x) : size;
                        if (distinguishable[ti][tj]) {
                            distinguishable[i][j] = true;
                            changed = true;
                        }
                    }
                }
            }
        } while (changed);

        /* Group nodes */
        TIntObjectMap<DFAState> nodeMap = new TIntObjectHashMap<DFAState>();
        outer: for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (!distinguishable[i][j] && nodeMap.containsKey(j)) {
                    nodeMap.put(i, nodeMap.get(j));
                    if (compiled.finals[i] != NOFINAL) {
                        if (nodeMap.get(j).getFinal() != NOFINAL && nodeMap.get(j).getFinal() != compiled.finals[i]) {
                            throw new RuntimeException("Eh?");
                        }
                    }
                    continue outer;
                }
            }
            DFAState node = new DFAState();
            node.setFinal(compiled.finals[i]);
            nodeMap.put(i, node);
        }

        for (int i = 0; i < compiled.transitions.length; i++) {
            for (int k : transitions[i].keys()) {
                nodeMap.get(i).addTransition(k, nodeMap.get(transitions[i].get(k)));
            }
        }
        
        DFA result = new DFA(nodeMap.get(0));
        return result;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        CompiledDFA dfs = compile();
        for (int i = 0; i < dfs.transitions.length; i++) {
            TIntIntMap map = dfs.transitions[i];
            
            for (int v : map.keys()) {
                result.append("edge(");
                result.append(i);
                result.append(", ");
                result.append(v);
                result.append("): ");
                result.append(map.get(v));
                result.append("\r\n");
            }
        }
        for (int i = 0; i < dfs.finals.length; i++) {
            if (dfs.finals[i] != NOFINAL) {
                result.append("final(");
                result.append(i);
                result.append("): ");
                result.append(dfs.finals[i]);
                result.append("\r\n");
            }
        }
        return result.toString();
    }

    /////////////////////////
    // Public inner classes
    /////////////////////////

    /**
     * Represents a state in a DFA.
     */
    public static class DFAState {
        private TIntObjectMap<DFAState> transitions;
        private int finalCode = NOFINAL;

        /**
         * Creates a new DFA state.
         */
        public DFAState() {
            transitions = new TIntObjectHashMap<DFAState>();
        }

        /**
         * Adds a transition.
         *
         * @param label transition edge label
         * @param next next state
         */
        public void addTransition(int label, DFAState next) {
            transitions.put(label, next);
        }

        /**
         * Sets the final class of this state. Finals can be divided in multiple
         * class, in which case each class gets its own index. Class NOFINAL is
         * used to indicate nonfinals.
         *
         * @param finalCode final index
         */
        public void setFinal(int finalCode) {
            this.finalCode = finalCode;
        }

        /**
         * Gets the final class of this state. Equals NOFINAL if this state is
         * not a final.
         *
         * @return final index
         */
        public int getFinal() {
            return finalCode;
        }
    }
}
